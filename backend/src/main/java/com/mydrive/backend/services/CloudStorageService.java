package com.mydrive.backend.services;

import com.mydrive.backend.dtos.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * Interfaz que define los métodos de operaciones comunes para los servicios de almacenamiento en la nube.
 * Implementaciones de esta interfaz proporcionan las funcionalidades específicas de cada proveedor de almacenamiento en la nube.
 */
public interface CloudStorageService {

    /**
     * Obtiene la URL de autorización para la autenticación del usuario con el proveedor de nube.
     *
     * @return La URL a la que el usuario debe ser redirigido para autorizar la aplicación.
     * @throws Exception Si ocurre un error al generar la URL de autorización.
     */
    String redirectToAuthorization() throws Exception;

    /**
     * Autentica al usuario utilizando el código recibido del proveedor de nube.
     *
     * @param code El código de autorización proporcionado por el proveedor de nube.
     * @throws Exception Si ocurre un error durante el proceso de autenticación.
     */
    void authenticateUser(String code) throws Exception;

    /**
     * Verifica si el usuario está autenticado en el servicio de almacenamiento en la nube.
     *
     * @return `true` si el usuario está autenticado, `false` en caso contrario.
     * @throws Exception Si ocurre un error durante la verificación de autenticación.
     */
    boolean checkAuthentication() throws Exception;

    /**
     * Cierra la sesión del usuario en el servicio de almacenamiento en la nube.
     *
     * @throws Exception Si ocurre un error durante el proceso de cierre de sesión.
     */
    void logout() throws Exception;

    /**
     * Obtiene la URL de la foto de perfil del usuario autenticado.
     *
     * @return La URL de la foto de perfil del usuario.
     * @throws Exception Si ocurre un error al obtener la foto de perfil.
     */
    String getProfilePhoto() throws Exception;

    /**
     * Obtiene el nombre del usuario autenticado en el servicio de almacenamiento en la nube.
     *
     * @return El nombre del usuario.
     * @throws Exception Si ocurre un error al obtener el nombre del usuario.
     */
    String getUserName() throws Exception;

    /**
     * Obtiene la ruta completa de carpetas hasta la carpeta especificada por su ID.
     *
     * @param folderId El ID de la carpeta.
     * @return Una lista de objetos {@link FileDTO} representando la ruta completa.
     * @throws Exception Si ocurre un error al obtener la ruta de la carpeta.
     */
    List<FileDTO> getPathFolder(String folderId) throws Exception;

    /**
     * Obtiene las subcarpetas dentro de la carpeta especificada.
     *
     * @param folderId El ID de la carpeta padre.
     * @param folderName Un término de búsqueda opcional para filtrar las carpetas.
     * @return Una lista de objetos {@link FileDTO} representando las carpetas encontradas.
     * @throws Exception Si ocurre un error al obtener las carpetas.
     */
    List<FileDTO> getFoldersInFolder(String folderId, String folderName) throws Exception;

    /**
     * Obtiene los archivos dentro de la carpeta especificada.
     *
     * @param folderId El ID de la carpeta.
     * @param fileName Un término de búsqueda opcional para filtrar los archivos.
     * @return Una lista de objetos {@link FileDTO} representando los archivos encontrados.
     * @throws Exception Si ocurre un error al obtener los archivos.
     */
    List<FileDTO> getFilesInFolder(String folderId, String fileName) throws Exception;

    /**
     * Obtiene los archivos recientes en el servicio de almacenamiento en la nube.
     *
     * @param maxDate  La fecha máxima para filtrar los archivos recientes.
     * @param fileName Un término de búsqueda opcional para filtrar los archivos.
     * @return Una lista de objetos {@link FileDTO} representando los archivos recientes.
     * @throws Exception Si ocurre un error al obtener los archivos recientes.
     */
    List<FileDTO> getRecentFiles(String maxDate, String fileName) throws Exception;

    /**
     * Obtiene los archivos que se encuentran en la papelera de reciclaje.
     *
     * @param fileName Un término de búsqueda opcional para filtrar los archivos.
     * @return Una lista de objetos {@link FileDTO} representando los archivos en la papelera.
     * @throws Exception Si ocurre un error al obtener los archivos de la papelera.
     */
    List<FileDTO> getFilesInBin(String fileName) throws Exception;

    /**
     * Busca carpetas por nombre en el servicio de almacenamiento en la nube.
     *
     * @param folderName El nombre de la carpeta a buscar.
     * @return Una lista de objetos {@link FileDTO} representando las carpetas encontradas.
     * @throws Exception Si ocurre un error durante la búsqueda de carpetas.
     */
    List<FileDTO> searchFolders(String folderName) throws Exception;

    /**
     * Busca archivos por nombre en el servicio de almacenamiento en la nube.
     *
     * @param fileName El nombre del archivo a buscar.
     * @return Una lista de objetos {@link FileDTO} representando los archivos encontrados.
     * @throws Exception Si ocurre un error durante la búsqueda de archivos.
     */
    List<FileDTO> searchFiles(String fileName) throws Exception;

    /**
     * Crea una nueva carpeta en el servicio de almacenamiento en la nube.
     *
     * @param folderId El ID de la carpeta padre donde se creará la nueva carpeta.
     * @param folderName El nombre de la nueva carpeta.
     * @throws Exception Si ocurre un error durante la creación de la carpeta.
     */
    void createFolder(String folderId, String folderName) throws Exception;

    /**
     * Sube un archivo al servicio de almacenamiento en la nube.
     *
     * @param file El archivo a subir.
     * @param folderId El ID de la carpeta destino.
     * @throws Exception Si ocurre un error durante la subida del archivo.
     */
    void uploadFile(MultipartFile file, String folderId) throws Exception;

    /**
     * Sube un archivo al servicio de almacenamiento en la nube utilizando un InputStream.
     *
     * @param inputStream El InputStream del archivo a subir.
     * @param fileName El nombre del archivo.
     * @param folderId El ID de la carpeta destino.
     * @throws Exception Si ocurre un error durante la subida del archivo.
     */
    void uploadFile(InputStream inputStream, String fileName, String folderId) throws Exception;

    /**
     * Sube un archivo cifrado al servicio de almacenamiento en la nube.
     *
     * @param file El archivo a subir.
     * @param password La contraseña utilizada para cifrar el archivo.
     * @param folderId El ID de la carpeta destino.
     * @throws Exception Si ocurre un error durante la subida del archivo cifrado.
     */
    void uploadEncryptedFile(MultipartFile file, String password, String folderId) throws Exception;

    /**
     * Obtiene el enlace de vista previa para un archivo en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo.
     * @return El enlace de vista previa del archivo.
     * @throws Exception Si ocurre un error al obtener el enlace de vista previa.
     */
    String getPreviewLink(String fileId) throws Exception;

    /**
     * Obtiene los detalles de un archivo específico.
     *
     * @param fileId El ID del archivo.
     * @return Un objeto {@link FileDTO} que contiene los detalles del archivo.
     * @throws Exception Si ocurre un error al obtener los detalles del archivo.
     */
    FileDTO getFileDetails(String fileId) throws Exception;

    /**
     * Descarga un archivo desde el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a descargar.
     * @return Un arreglo de bytes que representa el contenido del archivo.
     * @throws Exception Si ocurre un error durante la descarga del archivo.
     */
    byte[] downloadFile(String fileId) throws Exception;

    /**
     * Descarga un archivo cifrado desde el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a descargar.
     * @param password La contraseña utilizada para descifrar el archivo.
     * @return Un arreglo de bytes que representa el contenido del archivo cifrado.
     * @throws Exception Si ocurre un error durante la descarga del archivo cifrado.
     */
    byte[] downloadEncryptedFile(String fileId, String password) throws Exception;

    /**
     * Mueve un archivo a una carpeta diferente en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a mover.
     * @param targetFolderId El ID de la carpeta destino.
     * @throws Exception Si ocurre un error durante el movimiento del archivo.
     */
    void moveFile(String fileId, String targetFolderId) throws Exception;

    /**
     * Renombra un archivo en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a renombrar.
     * @param newName El nuevo nombre para el archivo.
     * @throws Exception Si ocurre un error durante el renombramiento del archivo.
     */
    void renameFile(String fileId, String newName) throws Exception;

    /**
     * Mueve un archivo a la papelera de reciclaje en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a mover a la papelera.
     * @throws Exception Si ocurre un error durante el proceso de mover el archivo a la papelera.
     */
    void throwAwayFile(String fileId) throws Exception;

    /**
     * Restaura un archivo desde la papelera de reciclaje en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a restaurar.
     * @throws Exception Si ocurre un error durante el proceso de restauración del archivo.
     */
    void restoreFile(String fileId) throws Exception;

    /**
     * Elimina permanentemente un archivo en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a eliminar.
     * @throws Exception Si ocurre un error durante el proceso de eliminación.
     */
    void deleteFile(String fileId) throws Exception;
}
