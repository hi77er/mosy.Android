package com.mosy.kalin.mosy.Services;

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;

import org.androidannotations.annotations.EBean;

@EBean
public class AzureBlobService {

    private static final String thumbnail100x100VenueBlobStorageContainerPath = "userimages\\fboalbums\\100x100";
    private static final String thumbnail100x100MLIBlobStorageContainerPath = "userimages\\requestablealbums\\100x100";
    private static final String thumbnail200x200VenueBlobStorageContainerPath = "userimages\\fboalbums\\200x200";
    private static final String thumbnail200x200MLIBlobStorageContainerPath = "userimages\\requestablealbums\\200x200";

    public void downloadVenueThumbnail(String imageId, ImageResolution imageResolution, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            String storagePath = imageResolution == ImageResolution.Format100x100
                    ? thumbnail100x100VenueBlobStorageContainerPath
                    : thumbnail200x200VenueBlobStorageContainerPath;

            DownloadBlobModel model = new DownloadBlobModel(imageId, storagePath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }

    public void downloadMenuListItemThumbnail(String imageId, ImageResolution imageResolution, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            String storagePath = imageResolution == ImageResolution.Format100x100
                    ? thumbnail100x100MLIBlobStorageContainerPath
                    : thumbnail200x200MLIBlobStorageContainerPath;

            DownloadBlobModel model = new DownloadBlobModel(imageId, storagePath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }

}