package com.paperless.repository;

import com.paperless.model.DocumentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name ="document")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false)
    private Instant uploadDate;

    @Column(nullable = false)
    private DocumentStatus status;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private Long sizeInBytes;

    @Column(nullable = false)
    private int accessCounter;
}
