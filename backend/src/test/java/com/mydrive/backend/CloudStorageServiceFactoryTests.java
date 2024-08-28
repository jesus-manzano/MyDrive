package com.mydrive.backend;

import com.mydrive.backend.services.CloudStorageService;
import com.mydrive.backend.services.CloudStorageServiceFactory;
import com.mydrive.backend.services.DropboxService;
import com.mydrive.backend.services.GoogleDriveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para la clase {@link CloudStorageServiceFactory}.
 *
 * <p>Esta clase prueba la correcta creación y recuperación de servicios de almacenamiento en la nube desde la factoría
 * {@link CloudStorageServiceFactory}.</p>
 */
@SpringBootTest
public class CloudStorageServiceFactoryTests {

    /**
     * Mock de la interfaz {@link BeanFactory} utilizada para obtener instancias de servicios.
     *
     * <p>Simula el comportamiento del contenedor de beans para la inyección de dependencias.</p>
     */
    @Mock
    private BeanFactory beanFactory;

    /**
     * Mock de la clase {@link GoogleDriveService}, que representa un servicio de almacenamiento en Google Drive.
     *
     * <p>Simula la implementación del servicio para pruebas.</p>
     */
    @Mock
    private GoogleDriveService googleDriveService;

    /**
     * Mock de la clase {@link DropboxService}, que representa un servicio de almacenamiento en Dropbox.
     *
     * <p>Simula la implementación del servicio para pruebas.</p>
     */
    @Mock
    private DropboxService dropboxService;

    /**
     * Instancia de {@link CloudStorageServiceFactory} que se está probando.
     *
     * <p>La factoría que crea y gestiona los servicios de almacenamiento en la nube.</p>
     */
    private CloudStorageServiceFactory factory;

    /**
     * Configura los mocks y la instancia de {@link CloudStorageServiceFactory} antes de cada prueba.
     *
     * <p>Inicializa los mocks y configura la factoría para que use los servicios mockeados.</p>
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new CloudStorageServiceFactory(beanFactory);

        when(beanFactory.getBean("google-drive", CloudStorageService.class)).thenReturn(googleDriveService);
        when(beanFactory.getBean("dropbox", CloudStorageService.class)).thenReturn(dropboxService);
    }

    /**
     * Prueba unitaria para el método {@link CloudStorageServiceFactory#getCloudService(String)} con el
     * identificador "google-drive".
     *
     * <p>Verifica que la factoría devuelve el servicio de Google Drive esperado cuando se solicita.</p>
     */
    @Test
    void testGetGoogleDriveService() {
        CloudStorageService service = factory.getCloudService("google-drive");
        assertNotNull(service, "El servicio de Google Drive no debería ser nulo");
        assertEquals(googleDriveService, service);
    }

    /**
     * Prueba unitaria para el método {@link CloudStorageServiceFactory#getCloudService(String)} con el
     * identificador "dropbox".
     *
     * <p>Verifica que la factoría devuelve el servicio de Dropbox esperado cuando se solicita.</p>
     */
    @Test
    void testGetDropboxService() {
        CloudStorageService service = factory.getCloudService("dropbox");
        assertNotNull(service, "El servicio de Dropbox no debería ser nulo");
        assertEquals(dropboxService, service);
    }
}
