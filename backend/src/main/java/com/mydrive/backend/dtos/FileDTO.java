package com.mydrive.backend.dtos;

public class FileDTO {

    private String id;
    private String name;
    private String thumbnailLink;
    private String lastTimeViewed;
    private Long size;
    private Boolean encrypted = false; // Por defecto suponemos que el archivo no está cifrado

    public FileDTO() {
    }

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
