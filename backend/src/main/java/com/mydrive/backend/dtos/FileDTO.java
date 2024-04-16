package com.mydrive.backend.dtos;

import com.google.api.services.drive.model.File;

public class FileDTO {

    private String id;
    private String name;
    private String thumbnailLink;
    private String lastTimeViewed;
    private Long size;

    public FileDTO() {}

    public FileDTO(File file) {
        this.id = file.getId();
        this.name = file.getName();
        this.thumbnailLink = file.getThumbnailLink();
        this.lastTimeViewed = mostRecentLastTimeViewed(file);
        this.size = file.getSize();
    }

    private String mostRecentLastTimeViewed(File file) {
        if (file.getViewedByMeTime() != null)
            return file.getViewedByMeTime().toString();
        if (file.getModifiedByMeTime() != null)
            file.getModifiedByMeTime().toString();
        return file.getCreatedTime().toString();
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
