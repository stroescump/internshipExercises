package com.example.internshipexercises;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public final class ServiceProvider {

    public static OkHttpClient okHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    public ServiceProvider() {
    }
}

