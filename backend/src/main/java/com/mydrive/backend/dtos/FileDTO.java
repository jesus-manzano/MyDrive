package com.mydrive.backend.dtos;

public class FileDTO {

    private String id;
    private String name;
    private String thumbnailLink;
    private String lastTimeViewed;
    private Long size;

    public FileDTO() {
    }

    public FileDTO(String id, String name, String thumbnailLink, String lastTimeViewed, Long size) {
        this.id = id;
        this.name = name;
        this.thumbnailLink = thumbnailLink;
        this.lastTimeViewed = lastTimeViewed;
        this.size = size;
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
}
