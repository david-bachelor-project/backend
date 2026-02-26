package com.paperless.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Document {
    @Id
    private Long id;

    private String title;

    private String originalFileName;

    private String storagePath;

    private Instant uploadDate;

    private DocumentStatus status;

    private String mimeType;

    private Long sizeInBytes;

    private int accessCounter;
}

