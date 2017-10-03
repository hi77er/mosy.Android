package com.mosy.kalin.mosy.Async.Tasks;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageThumbnailBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class GetMenuListItemImageAsyncTask extends AsyncTask<GetMenuListItemImageBindingModel, String, MenuListItemImage> {

    @Override
    protected MenuListItemImage doInBackground(GetMenuListItemImageBindingModel... models) {
        GetMenuListItemImageBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("MenuListItemFileMetadata/ByMenuListItemId");
        MenuListItemImage imageResult;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            NameValuePair param1 = new BasicNameValuePair("menuListItemId", model.MenuListItemId);
            params.add(param1);
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
