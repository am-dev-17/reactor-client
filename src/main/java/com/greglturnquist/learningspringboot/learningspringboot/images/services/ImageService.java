package com.greglturnquist.learningspringboot.learningspringboot.images.services;

import com.greglturnquist.learningspringboot.learningspringboot.images.domain.Image;
import com.greglturnquist.learningspringboot.learningspringboot.images.repositories.ImageRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private static final String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;
    private final ImageRepository repository;
    private final MeterRegistry meterRegistry;

    public ImageService(ResourceLoader resourceLoader, ImageRepository repository, MeterRegistry meterRegistry) {
        this.resourceLoader = resourceLoader;
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Pre-load some test images
     *
     * @return Spring Boot {@link CommandLineRunner} automatically
     *         run after app context is loaded.
     */
    @Bean
    CommandLineRunner setUp() throws IOException {
        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file",
                    new FileWriter(UPLOAD_ROOT +
                            "/learning-spring-boot-cover.jpg"));

            FileCopyUtils.copy("Test file2",
                    new FileWriter(UPLOAD_ROOT +
                            "/learning-spring-boot-2nd-edition-cover.jpg"));

            FileCopyUtils.copy("Test file3",
                    new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
        };
    }

    public Flux<Image> findAllImages() {
//        try {
//            return Flux.fromIterable(
//                            Files.newDirectoryStream(Paths.get(UPLOAD_ROOT)))
//                    .map(path ->
//                            new Image(String.valueOf(path.hashCode()),
//                                    path.getFileName().toString()));
//        } catch (IOException e) {
//            return Flux.empty();
//        }
        return repository.findAll()
                .log();
    }

    public Mono<Resource> findOneImage(String filename){
        return Mono.fromSupplier(
                () -> resourceLoader.getResource(
                        "file:" + UPLOAD_ROOT + "/" + filename
                )
        );
    }

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files
                .flatMap(file -> {
                    Mono<Image> saveDatabaseImage = repository.save(
                            new Image(
                                    UUID.randomUUID().toString(),
                                    file.filename()));

                    Mono<Void> copyFile = Mono.just(
                                    Paths.get(UPLOAD_ROOT, file.filename())
                                            .toFile())
                            .log("createImage-picktarget")
                            .map(destFile -> {
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-newfile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");


                    Mono<Void> countFile = Mono.fromRunnable(
                            () -> {
                                meterRegistry
                                        .summary("files.uploaded.bytes")
                                        .record(Paths.get(UPLOAD_ROOT, file.filename()).toFile().length());
                            }
                    );

                    return Mono.when(saveDatabaseImage, copyFile, countFile);
                })
                .then();
    }

    public Mono<Void> deleteImage(String filename) {
        Mono<Void> deleteDatabaseImage = repository
                .findByName(filename)
                .flatMap(im -> repository.delete(im))
                .log("deleteImage=Record");

        Mono<Object> deleteFile = Mono.fromRunnable(
                () -> {
                    try{
                        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
                    } catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
        )
                .log("deleteImage-file");

        return Mono.when(deleteDatabaseImage, deleteFile)
                .log("deleteImage-when")
                .then()
                .log("deleteImage-done");
    }


}
