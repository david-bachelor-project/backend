package com.paperless.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperless.model.Document;
import com.paperless.config.KafkaProducer;
import com.paperless.model.DocumentStatus;
import com.paperless.repository.DocumentEntity;
import com.paperless.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final KafkaProducer kafkaProducer;
    private final StorageService storageService;
    private final ObjectMapper objectMapper; 

    public Document saveDocument(MultipartFile file, String title) {
        try {
            log.info("Create document request: {}", title);

            // Generate UUID storage path
            String storagePath = UUID.randomUUID().toString();

            // Save file in MinIO
            storageService.savePdf(file, storagePath);

            // Build Document metadata
            Document doc = Document.builder()
                    .title(title)
                    .originalFileName(file.getOriginalFilename())
                    .storagePath(storagePath)
                    .uploadDate(Instant.now())
                    .mimeType(file.getContentType())
                    .sizeInBytes(file.getSize())
                    .status(DocumentStatus.UPLOADED)
                    .build();

            // Persist in DB
            var entity = documentMapper.toEntity(doc);
            var savedEntity = documentRepository.save(entity);
            Document savedDoc = documentMapper.toDto(savedEntity);

            String payload = objectMapper.writeValueAsString(savedDoc);
            kafkaProducer.sendToOcrQueue(payload);

            return savedDoc;
        } catch (Exception e) {
            log.error("Error saving document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save document", e);
        }
    }

    public Document getDocumentById(Long id) {
        try {
            log.info("Get document by ID request: {}", id);
            return documentRepository.findById(id)
                    .map(entity -> {
                        try {
                            log.info("Get document by ID: {} - File: {}", entity.getId(), entity.getOriginalFileName());
                            return documentMapper.toDto(entity);
                        } catch (Exception e) {
                            log.error("Error mapping document entity to DTO: {}", e.getMessage());
                            throw new RuntimeException("Mapping error", e);
                        }
                    })
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error retrieving document: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve document", e);
        }
    }

    public List<Document> getAllDocuments(){
        try{
            log.info("Get all documents request");
            List<Document> list = documentRepository.findAll()
                    .stream()
                    .map(entity -> {
                        try {
                            log.info("Get document by ID: {} - File: {}", entity.getId(), entity.getOriginalFileName());
                            return documentMapper.toDto(entity);
                        } catch (Exception e) {
                            log.error("Error mapping document entity to DTO: {}", e.getMessage());
                            throw new RuntimeException("Mapping error", e);
                        }
                    })
                    .toList();
            return list;
        }catch (Exception e){
            log.error("Error retrieving documents: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve documents", e);
        }
    }
}
