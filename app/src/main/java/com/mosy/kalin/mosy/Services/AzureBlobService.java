package com.mosy.kalin.mosy.Services;

import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;

import org.androidannotations.annotations.EBean;

@EBean
public class AzureBlobService {

    private static final String thumbnailVenueBlobStorageContainerPath = "userimages\\fboalbums\\100x100";
    private static final String thumbnailMLIBlobStorageContainerPath = "userimages\\requestablealbums\\100x100";


    public void downloadVenue100x100Thumbnail(String imageId, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            DownloadBlobModel model = new DownloadBlobModel(imageId, thumbnailVenueBlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }

    public void downloadMenuListItem100x100Thumbnail(String imageId, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            DownloadBlobModel model = new DownloadBlobModel(imageId, thumbnailMLIBlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }

}