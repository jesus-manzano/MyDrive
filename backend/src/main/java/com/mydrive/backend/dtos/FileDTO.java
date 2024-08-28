package com.mydrive.backend.dtos;

/**
 * Representa un archivo en el sistema de almacenamiento en la nube.
 *
 * <p>Esta clase es un Data Transfer Object (DTO) que encapsula la información sobre un archivo,
 * incluyendo su identificador, nombre, enlace a la miniatura, última vez visto, tamaño y estado de cifrado.</p>
 *
 * <p>Se proporciona un constructor sin argumentos para la creación de instancias vacías y un constructor
 * con argumentos para inicializar todos los atributos de la clase. También se incluyen métodos getter y setter
 * para cada atributo.</p>
 */
public class FileDTO {

    /**
     * Identificador único del archivo en el sistema de almacenamiento en la nube.
     */
    private String id;

    /**
     * Nombre del archivo.
     */
    private String name;

    /**
     * Enlace a la miniatura del archivo. Este atributo puede ser utilizado para mostrar una vista previa
     * del archivo en interfaces de usuario.
     */
    private String thumbnailLink;

    /**
     * Fecha y hora en la que el archivo fue visto por última vez, en formato ISO 8601.
     */
    private String lastTimeViewed;

    /**
     * Tamaño del archivo en bytes.
     */
    private Long size;

    /**
     * Indica si el archivo está cifrado o no. Por defecto, se supone que el archivo no está cifrado.
     */
    private Boolean encrypted = false;

    /**
     * Constructor sin argumentos para la creación de instancias vacías de {@code FileDTO}.
     */
    public FileDTO() {
    }

    /**
     * Constructor con argumentos para inicializar todos los atributos de {@code FileDTO}.
     *
     * @param id El identificador único del archivo.
     * @param name El nombre del archivo.
     * @param thumbnailLink El enlace a la miniatura del archivo.
     * @param lastTimeViewed La fecha y hora en la que el archivo fue visto por última vez.
     * @param size El tamaño del archivo en bytes.
     * @param encrypted Indica si el archivo está cifrado o no.
     */
    public FileDTO(String id, String name, String thumbnailLink, String lastTimeViewed, Long size, Boolean encrypted) {
        this.id = id;
        this.name = name;
        this.thumbnailLink = thumbnailLink;
        this.lastTimeViewed = lastTimeViewed;
        this.size = size;
        this.encrypted = encrypted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getLastTimeViewed() {
        return lastTimeViewed;
    }

    public void setLastTimeViewed(String lastTimeViewed) {
        this.lastTimeViewed = lastTimeViewed;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }
}
