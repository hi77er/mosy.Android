package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;

import org.androidannotations.annotations.EBean;

@EBean
public class AzureBlobService {

    public void downloadVenueThumbnail(Context context, String imageId, ImageResolution imageResolution, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            String storagePath = StringHelper.empty();

            switch (imageResolution){
                case Format100x100:
                    storagePath = context.getString(R.string.azureBlobContainerPath_fboalbums_100x100);
                    break;
                case Format200x200:
                    storagePath = context.getString(R.string.azureBlobContainerPath_fboAlbums_200x200);
                    break;
                case Format300x300:
                    storagePath = context.getString(R.string.azureBlobContainerPath_fboAlbums_300x300);
                    break;
                case FormatOriginal:
                    storagePath = context.getString(R.string.azureBlobContainerPath_fboAlbums_original);
                    break;
            }

            DownloadBlobModel model = new DownloadBlobModel(imageId, storagePath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }

    public void downloadMenuListItemThumbnail(Context context, String imageId, ImageResolution imageResolution, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            String storagePath = StringHelper.empty();

            switch (imageResolution){
                case Format100x100:
                    storagePath = context.getString(R.string.azureBlobContainerPath_requestableAlbums_100x100);
                    break;
                case Format200x200:
                    storagePath = context.getString(R.string.azureBlobContainerPath_requestablesAlbums_200x200);
                    break;
                case Format300x300:
                    storagePath = context.getString(R.string.azureBlobContainerPath_requestablesAlbums_300x300);
                    break;
                case FormatOriginal:
                    storagePath = context.getString(R.string.azureBlobContainerPath_requestablesAlbums_original);
                    break;
            }

            DownloadBlobModel model = new DownloadBlobModel(imageId, storagePath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }

    public void downloadUserProfileThumbnail(Context context, String imageId, ImageResolution imageResolution, AsyncTaskListener<byte[]> imageResultListener) {
        if (StringHelper.isNotNullOrEmpty(imageId)) {
            String storagePath = StringHelper.empty();

            switch (imageResolution){
                case Format100x100:
                    storagePath = context.getString(R.string.azureBlobContainerPath_profile_100x100);
                    break;
                case Format200x200:
                    storagePath = context.getString(R.string.azureBlobContainerPath_profile_200x200);
                    break;
                case Format300x300:
                    storagePath = context.getString(R.string.azureBlobContainerPath_profile_300x300);
                    break;
                case FormatOriginal:
                    storagePath = context.getString(R.string.azureBlobContainerPath_profile_original);
                    break;
            }

            DownloadBlobModel model = new DownloadBlobModel(imageId, storagePath);
            new LoadAzureBlobAsyncTask(imageResultListener).execute(model);
        }
    }
}