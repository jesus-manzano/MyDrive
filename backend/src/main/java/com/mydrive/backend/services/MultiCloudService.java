package com.mydrive.backend.services;

import com.mydrive.backend.dtos.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

/**
 * Servicio para operaciones entre múltiples servicios de almacenamiento en la nube.
 *
 * <p>Esta clase proporciona funcionalidades para realizar operaciones que involucren
 * múltiples servicios de almacenamiento en la nube. Utiliza instancias de servicios de almacenamiento
 * en la nube proporcionadas por la {@link CloudStorageServiceFactory} para llevar a cabo estas operaciones.</p>
 *
 * <p>La clase está anotada con {@link Service}, lo que indica que es un componente de servicio de
 * Spring y puede ser inyectada en otros componentes como una dependencia.</p>
 *
 * @see CloudStorageServiceFactory
 * @see CloudStorageService
 */
@Service
public class MultiCloudService {

    private final CloudStorageServiceFactory cloudStorageServiceFactory;

    /**
     * Logger para registrar mensajes de información y errores.
     */
    private static final Logger logger = LoggerFactory.getLogger(MultiCloudService.class);

    /**
     * Crea una nueva instancia de {@link MultiCloudService}.
     *
     * <p>Inyecta la fábrica de servicios de almacenamiento en la nube que se utiliza para obtener las
     * instancias de los servicios de almacenamiento necesarios para las operaciones entre nubes.</p>
     *
     * @param cloudStorageServiceFactory La fábrica de servicios de almacenamiento en la nube.
     */
    @Autowired
    public MultiCloudService(CloudStorageServiceFactory cloudStorageServiceFactory) {
        this.cloudStorageServiceFactory = cloudStorageServiceFactory;
    }

    /**
     * Copia un archivo de un servicio de almacenamiento en la nube a otro.
     *
     * <p>Utiliza los servicios de almacenamiento obtenidos a través de la {@link CloudStorageServiceFactory}
     * para copiar un archivo desde una nube de origen a una nube de destino.</p>
     *
     * @param sourceProvider El nombre del servicio de almacenamiento en la nube de origen.
     * @param sourceFileId El identificador del archivo a copiar.
     * @param destinationProvider El nombre del servicio de almacenamiento en la nube de destino.
     * @param destinationFolderId El identificador de la carpeta destino.
     * @throws Exception Si ocurre un error al copiar el archivo entre nubes.
     */
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

