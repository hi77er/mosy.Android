package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.AzureBlobRepository;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;


public class LoadAzureBlobAsyncTask
    extends AsyncTask<DownloadBlobModel, String, byte[]>
{
    private final AsyncTaskListener<byte[]> asyncTaskListener;

    public LoadAzureBlobAsyncTask(AsyncTaskListener<byte[]> listener) {
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
            result = new AzureBlobRepository().blobDownload(model);
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
