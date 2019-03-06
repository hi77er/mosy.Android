package com.mosy.kalin.mosy.DAL.Http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mosy.kalin.mosy.DAL.Http.Deserializers.ByteArrayToBase64Deserializer;
import com.mosy.kalin.mosy.DAL.Http.Deserializers.JsonDateDeserializer;
import com.mosy.kalin.mosy.DAL.Http.Deserializers.JsonTimeDeserializer;
import com.mosy.kalin.mosy.DAL.Http.Deserializers.TimeOnlyDateDeserializer;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;

import java.sql.Time;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClientFactory {

        private static Retrofit retrofit = null;

        public static Retrofit getClient(String baseUrl) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .addInterceptor(interceptor).build();

            GsonBuilder builder  = new GsonBuilder();

            builder.registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64Deserializer());

            builder.registerTypeAdapter(Date.class, new JsonDateDeserializer());
            builder.registerTypeAdapter(Date.class, new TimeOnlyDateDeserializer());
            builder.registerTypeAdapter(Time.class, new JsonTimeDeserializer());
            Gson gson = builder.create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit;
        }


    }