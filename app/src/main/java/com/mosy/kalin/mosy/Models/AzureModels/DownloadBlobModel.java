package com.mosy.kalin.mosy.Models.AzureModels;

public class DownloadBlobModel {

    public String BlobId;
    public String StorageContainer;

    public DownloadBlobModel() { }

    public DownloadBlobModel(String blobId, String storageContainer) {
        this.BlobId = blobId;
        this.StorageContainer = storageContainer;
    }

}
