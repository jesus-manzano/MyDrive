package com.mydrive.backend.services;

import com.mydrive.backend.dtos.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class MultiCloudService {

    private final CloudStorageServiceFactory cloudStorageServiceFactory;

    private static final Logger logger = LoggerFactory.getLogger(MultiCloudService.class);

    @Autowired
    public MultiCloudService(CloudStorageServiceFactory cloudStorageServiceFactory) {
        this.cloudStorageServiceFactory = cloudStorageServiceFactory;
    }

    public void moveFileBetweenClouds(String sourceProvider, String sourceFileId, String destinationProvider, String destinationFolderId) throws Exception {
        CloudStorageService sourceService = cloudStorageServiceFactory.getCloudService(sourceProvider);
        CloudStorageService destinationService = cloudStorageServiceFactory.getCloudService(destinationProvider);

        logger.info("Comienza el proceso de mover entre nubes");

        // Obtener los detalles completos del archivo desde la nube origen
        FileDTO sourceFileDetails = sourceService.getFileDetails(sourceFileId);

        // Descargar el archivo desde la nube origen
        byte[] fileData = sourceService.downloadFile(sourceFileId);

        // Crear un InputStream a partir del byte array
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);

        // Subir el archivo a la nube destino usando el nombre original
        destinationService.uploadFile(inputStream, sourceFileDetails.getName(), destinationFolderId);

        logger.info("Archivo subido con éxito a " + destinationProvider);
    }
}

