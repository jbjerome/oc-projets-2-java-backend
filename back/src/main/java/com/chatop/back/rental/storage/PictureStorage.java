package com.chatop.back.rental.storage;

import com.chatop.back.rental.exception.InvalidPictureUploadException;
import com.chatop.back.rental.exception.PictureStorageException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class PictureStorage {

    private final MinioClient client;
    private final String bucket;
    private final String publicUrl;

    public PictureStorage(MinioClient client,
                          @Value("${minio.bucket}") String bucket,
                          @Value("${minio.public-url}") String publicUrl) {
        this.client = client;
        this.bucket = bucket;
        this.publicUrl = publicUrl.replaceAll("/+$", "");
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidPictureUploadException("Picture payload is empty");
        }
        String objectKey = UUID.randomUUID() + extension(file.getOriginalFilename());
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .build());
        } catch (Exception e) {
            throw new PictureStorageException("Failed to upload picture to MinIO", e);
        }
        return "%s/%s/%s".formatted(publicUrl, bucket, objectKey);
    }

    private String extension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }
}
