package com.mosy.kalin.mosy.Cloud;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.mosy.kalin.mosy.Base.AzureIntegrationTest;

import org.junit.Test;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class AzureBlobStorageIntegrationTests extends AzureIntegrationTest {

    @Test
    public void blobContainer_referenceCreated() throws URISyntaxException, InvalidKeyException, StorageException {
        // Retrieve storage account from connection-string.
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Get a reference to a container.
        // The container Name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference("userimages");

        assertNotNull("Didn't make it to get the Azure Blob Storage Container", container);
    }

    @Test
    public void blob_referenceCreated() throws URISyntaxException, InvalidKeyException, StorageException {
        // Retrieve storage account from connection-string.
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        // Retrieve reference to a previously created container.
        CloudBlobContainer container = blobClient.getContainerReference("userimages\\profile\\100x100");
        // Retrieve reference to a blob.
        CloudBlob blob = container.getBlockBlobReference("276b76c5-4464-406a-8fcb-9f8c54fcf0c4");

        assertNotNull("Didn't manage to get Blob reference", blob);
    }

    @Test
    public void blob_contentDownloaded() throws Exception {
         // Retrieve storage account from connection-string.
         CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
         // Create the blob client.
         CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
         // Retrieve reference to a previously created container.
         CloudBlobContainer container = blobClient.getContainerReference("userimages\\profile\\100x100");
         // Retrieve reference to a blob.
         CloudBlob blob = container.getBlockBlobReference("276b76c5-4464-406a-8fcb-9f8c54fcf0c4");

         int blobSize = blob.getStreamWriteSizeInBytes();
         byte[] bufferArr = new byte[blobSize];
         int downloadedSize = blob.downloadToByteArray(bufferArr,0);

         assertTrue("Didn't manage to get bytes!", bufferArr.length > 0);
    }

}