package com.chatop.back.rental.infrastructure.storage;

import com.chatop.back.rental.domain.service.PictureStorage;
import com.chatop.back.rental.domain.vo.PictureUpload;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Component
class MinioPictureStorage implements PictureStorage {

    private final MinioClient client;
    private final String bucket;
    private final String publicUrl;

    MinioPictureStorage(MinioClient client,
                        @Value("${minio.bucket}") String bucket,
                        @Value("${minio.public-url}") String publicUrl) {
        this.client = client;
        this.bucket = bucket;
        this.publicUrl = publicUrl.replaceAll("/+$", "");
    }

    @Override
    public String store(PictureUpload upload) {
        String objectKey = UUID.randomUUID() + extension(upload.originalFilename());
        try (ByteArrayInputStream stream = new ByteArrayInputStream(upload.bytes())) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(stream, upload.bytes().length, -1)
                    .contentType(upload.contentType())
                    .build());
        } catch (Exception e) {
            throw new PictureStorageException("Failed to upload picture to MinIO", e);
        }
        return "%s/%s/%s".formatted(publicUrl, bucket, objectKey);
    }

    private String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }
}
