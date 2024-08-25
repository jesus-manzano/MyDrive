package com.mydrive.backend;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.exceptions.CloudLimitationException;
import com.mydrive.backend.services.DropboxService;
import com.mydrive.backend.services.utils.FileEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DropboxServiceTests {

    @Mock
    private DbxClientV2 client;

    @Mock
    private DbxUserFilesRequests files;

    @InjectMocks
    private DropboxService dropboxService;

    @Test
    void testGetFoldersInFolderSuccess() throws Exception {
        // Datos simulados
        FolderMetadata mockFolder1 = mock(FolderMetadata.class);
        when(mockFolder1.getName()).thenReturn("folder1");
        FolderMetadata mockFolder2 = mock(FolderMetadata.class);
        when(mockFolder2.getName()).thenReturn("folder2");

        List<Metadata> entries = new ArrayList<>();
        entries.add(mockFolder1);
        entries.add(mockFolder2);

        ListFolderResult listFolderResult = mock(ListFolderResult.class);
        when(listFolderResult.getEntries()).thenReturn(entries);
        when(listFolderResult.getHasMore()).thenReturn(false);

        when(client.files()).thenReturn(files);
        when(files.listFolder(anyString())).thenReturn(listFolderResult);

        // Ejecutar el método
        List<FileDTO> result = dropboxService.getFoldersInFolder("root", "");

        // Verificaciones
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("folder1", result.get(0).getName());
        assertEquals("folder2", result.get(1).getName());
    }

    @Test
    void testGetFoldersInFolderFailure() throws Exception {
        // Configurar el mock para que lance una excepción
        when(client.files()).thenReturn(files);
        when(files.listFolder(anyString())).thenThrow(new RuntimeException("API Error"));

        // Verifica que el método maneje la excepción correctamente
        Exception exception = assertThrows(RuntimeException.class, () ->
                dropboxService.getFoldersInFolder("root", ""));
        assertEquals("API Error", exception.getMessage());
    }

    @Test
    public void testGetRecentFiles() throws Exception {
        // Configurar mocks para los componentes necesarios
        DbxUserListFolderBuilder listFolderBuilder = mock(DbxUserListFolderBuilder.class);
        ListFolderResult listFolderResult = mock(ListFolderResult.class);

        // Crear metadatos de archivos simulados
        String maxDate = "2024-08-20T10:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsedDate = formatter.parse(maxDate);

        FileMetadata fileMetadata1 = mock(FileMetadata.class);
        when(fileMetadata1.getName()).thenReturn("recentFile1");
        when(fileMetadata1.getServerModified()).thenReturn(new Date(parsedDate.getTime() + 1000));

        FileMetadata fileMetadata2 = mock(FileMetadata.class);
        when(fileMetadata2.getName()).thenReturn("recentFile2");
        when(fileMetadata2.getServerModified()).thenReturn(new Date(parsedDate.getTime() - 2000));

        List<Metadata> entries = Arrays.asList(fileMetadata1, fileMetadata2);

        // Configurar el comportamiento de los mocks
        when(client.files()).thenReturn(files);
        when(files.listFolderBuilder("")).thenReturn(listFolderBuilder);
        when(listFolderBuilder.withIncludeDeleted(false)).thenReturn(listFolderBuilder);
        when(listFolderBuilder.withRecursive(true)).thenReturn(listFolderBuilder);
        when(listFolderBuilder.start()).thenReturn(listFolderResult);
        when(listFolderResult.getEntries()).thenReturn(entries);
        when(listFolderResult.getHasMore()).thenReturn(false);

        // Mockear isFileEncrypted para que siempre devuelva false
        DropboxService dropboxServiceSpy = spy(dropboxService);
        doReturn(false).when(dropboxServiceSpy).isFileEncrypted(any(FileMetadata.class));

        // Ejecutar el método
        List<FileDTO> result = dropboxServiceSpy.getRecentFiles(maxDate, "");

        // Verificaciones
        assertNotNull(result);
        assertEquals(1, result.size());  // Solo 1 archivo es reciente
        assertEquals("recentFile1", result.get(0).getName());
    }

    @Test
    public void testSearchFiles() throws Exception {
        // Configurar mocks para los componentes necesarios
        DbxUserListFolderBuilder listFolderBuilder = mock(DbxUserListFolderBuilder.class);
        ListFolderResult listFolderResult = mock(ListFolderResult.class);

        // Crear metadatos de archivos simulados
        String date = "2024-08-20T10:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsedDate = formatter.parse(date);

        FileMetadata fileMetadata1 = mock(FileMetadata.class);
        when(fileMetadata1.getName()).thenReturn("searchFile1");
        when(fileMetadata1.getServerModified()).thenReturn(new Date(parsedDate.getTime() - 2000));
        when(fileMetadata1.getPathLower()).thenReturn("/path/to/searchFile1");

        FileMetadata fileMetadata2 = mock(FileMetadata.class);
        when(fileMetadata2.getName()).thenReturn("searchFile2");
        when(fileMetadata2.getServerModified()).thenReturn(new Date(parsedDate.getTime() + 2000));
        when(fileMetadata2.getPathLower()).thenReturn("/path/to/searchFile2");

        List<Metadata> entries = Arrays.asList(fileMetadata1, fileMetadata2);

        // Configurar el comportamiento de los mocks
        when(client.files()).thenReturn(files);
        when(files.listFolderBuilder("")).thenReturn(listFolderBuilder);
        when(listFolderBuilder.withRecursive(true)).thenReturn(listFolderBuilder);
        when(listFolderBuilder.start()).thenReturn(listFolderResult);
        when(listFolderResult.getEntries()).thenReturn(entries);
        when(listFolderResult.getHasMore()).thenReturn(false);

        // Mockear isFileEncrypted para que siempre devuelva false
        DropboxService dropboxServiceSpy = spy(dropboxService);
        doReturn(false).when(dropboxServiceSpy).isFileEncrypted(any(FileMetadata.class));

        // Ejecutar el método
        List<FileDTO> result = dropboxServiceSpy.searchFiles("searchFile");

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("searchFile1", result.get(0).getName());
        assertEquals("searchFile2", result.get(1).getName());
    }

    @Test
    public void testDownloadEncryptedFile() throws Exception {
        String fileId = "testFileId";
        String password = "testPassword";
        byte[] fileContent = "testContent".getBytes();  // Contenido esperado

        // Mock el método de descarga
        DbxDownloader<FileMetadata> downloader = mock(DbxDownloader.class);
        when(client.files()).thenReturn(files);
        when(files.download(fileId)).thenReturn(downloader);
        doAnswer(invocation -> {
            OutputStream outputStream = invocation.getArgument(0);
            outputStream.write(fileContent);  // Escribir contenido simulado
            return null;
        }).when(downloader).download(any(OutputStream.class));

        // Mock la utilidad de descifrado utilizando mockStatic
        try (MockedStatic<FileEncryptionUtil> mockedEncryptionUtil = mockStatic(FileEncryptionUtil.class)) {
            mockedEncryptionUtil.when(() -> FileEncryptionUtil.decryptFile(any(java.io.File.class),
                            any(java.io.File.class), eq(password)))
                    .thenAnswer(invocation -> {
                        java.io.File inputFile = invocation.getArgument(0);  // Archivo cifrado (temporal)
                        java.io.File outputFile = invocation.getArgument(1);  // Archivo descifrado (temporal)

                        // Simular descifrado copiando contenido del archivo de entrada al de salida
                        try (InputStream fis = new FileInputStream(inputFile);
                             OutputStream fos = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }
                        return null;
                    });

            // Ejecutar el método
            byte[] result = dropboxService.downloadEncryptedFile(fileId, password);

            // Verificaciones
            assertNotNull(result);
            assertArrayEquals(fileContent, result);
        }
    }

    @Test
    void testDeleteFileThrowsCloudLimitationException() {
        // Ejecutar el método y verificar que lanza la excepción esperada
        CloudLimitationException exception = assertThrows(
                CloudLimitationException.class,
                () -> dropboxService.deleteFile("some-file-id")
        );

        // Verificar el mensaje de la excepción
        assertEquals("No se puede eliminar el archivo debido a limitaciones en la nube", exception.getMessage());
    }
}
