package com.mosy.kalin.mosy.Listeners;

/**
 * Created by kkras on 1/31/2018.
 */

public abstract class AsyncTaskListener<TResult> {

    public abstract void onPreExecute();
    public abstract void onPostExecute(TResult result);

    public AsyncTaskListener() {
    }


}
