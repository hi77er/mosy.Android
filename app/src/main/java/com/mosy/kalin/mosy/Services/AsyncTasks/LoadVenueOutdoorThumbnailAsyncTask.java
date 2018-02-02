package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Services.AzureBlobService;

public class LoadVenueOutdoorThumbnailAsyncTask
    extends AsyncTask<DownloadBlobModel, String, byte[]>
{
    private final AsyncTaskListener<byte[]> asyncTaskListener;

    public LoadVenueOutdoorThumbnailAsyncTask(AsyncTaskListener<byte[]> listener) {
        asyncTaskListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(DownloadBlobModel... models) {
        byte[] result = null;

        try {
            DownloadBlobModel model = models[0];
            result = new AzureBlobService().GetBlob(model.BlobId, model.StorageContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(final byte[] result) {
        asyncTaskListener.onPostExecute(result);
    }

}
