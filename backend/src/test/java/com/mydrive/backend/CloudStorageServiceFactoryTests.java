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

@SpringBootTest
public class CloudStorageServiceFactoryTests {

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private GoogleDriveService googleDriveService;

    @Mock
    private DropboxService dropboxService;

    private CloudStorageServiceFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new CloudStorageServiceFactory(beanFactory);

        when(beanFactory.getBean("google-drive", CloudStorageService.class)).thenReturn(googleDriveService);
        when(beanFactory.getBean("dropbox", CloudStorageService.class)).thenReturn(dropboxService);
    }

    @Test
    void testGetGoogleDriveService() {
        CloudStorageService service = factory.getCloudService("google-drive");
        assertNotNull(service, "El servicio de Google Drive no debería ser nulo");
        assertEquals(googleDriveService, service);
    }

    @Test
    void testGetDropboxService() {
        CloudStorageService service = factory.getCloudService("dropbox");
        assertNotNull(service, "El servicio de Dropbox no debería ser nulo");
        assertEquals(dropboxService, service);
    }
}
