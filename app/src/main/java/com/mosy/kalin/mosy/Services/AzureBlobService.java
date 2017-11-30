package com.mosy.kalin.mosy.Services;

import com.mosy.kalin.mosy.Async.Tasks.AzureBlobDownloadAsyncTask;
import com.mosy.kalin.mosy.BuildConfig;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;

import org.androidannotations.annotations.EBean;

@EBean
public class AzureBlobService {

    public byte[] GetBlob(String blobId, String containerName) {
        byte[] bufferArr = null;
        long execStart = 0;
        long elapsed = 0;

        try {
            if (BuildConfig.DEBUG) execStart = System.currentTimeMillis();

            AzureBlobDownloadAsyncTask task = new AzureBlobDownloadAsyncTask();
            DownloadBlobModel model = new DownloadBlobModel(blobId, containerName);
            bufferArr = task.execute(model).get();

            if (BuildConfig.DEBUG) elapsed = System.currentTimeMillis() - execStart;
            if (BuildConfig.DEBUG) System.out.println("MOSYLOGS : AZURE BLOB DOWNLOAD" + containerName + " - " + blobId + " TOOK: " + elapsed + "ms;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferArr;
    }

}
