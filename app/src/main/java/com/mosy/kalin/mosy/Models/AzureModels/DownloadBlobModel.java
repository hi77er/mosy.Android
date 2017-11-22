package com.mosy.kalin.mosy.Models.AzureModels;

public class DownloadBlobModel {

    public String BlobId;
    public String getBlobId() { return BlobId; }
    public void setBlobId(String blobId) { this.BlobId = blobId; }

    public String StorageContainer;
    public String getStorageContainer() { return StorageContainer; }
    public void setStorageContainer(String storageContainer) { this.StorageContainer = storageContainer; }

    public DownloadBlobModel() { }

    public DownloadBlobModel(String blobId, String storageContainer) {
        this.BlobId = blobId;
        this.StorageContainer = storageContainer;
    }

}
