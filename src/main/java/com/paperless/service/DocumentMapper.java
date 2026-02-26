package com.paperless.service;

import com.paperless.model.Document;
import com.paperless.repository.DocumentEntity;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DocumentMapper extends AbstractMapper<DocumentEntity, Document> {

    @Override
    public Document toDto(DocumentEntity entity) throws SQLException {
        if (entity == null) {
            return null;
        }
        return Document.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .originalFileName(entity.getOriginalFileName())
                .storagePath(entity.getStoragePath())
                .uploadDate(entity.getUploadDate())
                .status(entity.getStatus())
                .mimeType(entity.getMimeType())
                .sizeInBytes(entity.getSizeInBytes())
                .accessCounter(entity.getAccessCounter())
                .build();
    }

    @Override
    public DocumentEntity toEntity(Document dto) throws SQLException {
        if (dto == null) {
            return null;
        }
        return DocumentEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .originalFileName(dto.getOriginalFileName())
                .storagePath(dto.getStoragePath())
                .uploadDate(dto.getUploadDate())
                .status(dto.getStatus())
                .mimeType(dto.getMimeType())
                .sizeInBytes(dto.getSizeInBytes())
                .accessCounter(dto.getAccessCounter())
                .build();
    }
}
