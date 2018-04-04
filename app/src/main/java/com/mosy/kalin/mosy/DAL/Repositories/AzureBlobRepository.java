package com.mosy.kalin.mosy.DAL.Repositories;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;

public class AzureBlobRepository {

    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=mosystorage;AccountKey=qhoQk3U2VUbobmyxpasPM8rqt78+YtkD20ulZRkT+lbpXTtVc1Njo9P4ep2HDl9oMSJ2ZsL6uNal3YMIfadgng==;EndpointSuffix=core.windows.net";

    public byte[] blobDownload(DownloadBlobModel model) {
        byte[] blobBytes;
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(model.StorageContainer);
            CloudBlob blob = container.getBlockBlobReference(model.BlobId);

            int blobSize = blob.getStreamWriteSizeInBytes();
            blobBytes = new byte[blobSize];
            blob.downloadToByteArray(blobBytes,0);
            return blobBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
