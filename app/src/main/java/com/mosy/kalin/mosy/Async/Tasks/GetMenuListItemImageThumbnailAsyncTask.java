package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageThumbnailBindingModel;

public class GetMenuListItemImageThumbnailAsyncTask extends AsyncTask<GetMenuListItemImageThumbnailBindingModel, String, MenuListItemImage> {

    @Override
    protected MenuListItemImage doInBackground(GetMenuListItemImageThumbnailBindingModel... models) {
        GetMenuListItemImageThumbnailBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("MenuListItemFileMetadata/ThumbnailByMenuListItemId");
        MenuListItemImage imageResult;

        try {
            ContentValues params = new ContentValues();
            params.put("menuListItemId", model.MenuListItemId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            imageResult = jsonHttpClient.Get(endpoint, params, new TypeToken<MenuListItemImage>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            MenuListItemImage errResult = new MenuListItemImage();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return imageResult;
    }

    @Override
    protected void onPostExecute(final MenuListItemImage result) {
        super.onPostExecute(result);
    }
}
