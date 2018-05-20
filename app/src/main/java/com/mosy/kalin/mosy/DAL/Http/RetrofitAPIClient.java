package com.mosy.kalin.mosy.DAL.Http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;

import java.sql.Time;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClient {

        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            GsonBuilder builder  = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDateDeserializer());
            builder.registerTypeAdapter(Date.class, new JsonDateDeserializer2());
            builder.registerTypeAdapter(Time.class, new JsonTimeDeserializer());
            Gson gson = builder.create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ServiceEndpointFactory.apiEndpoint)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit;
        }

    }