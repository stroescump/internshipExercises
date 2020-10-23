package com.example.internshipexercises;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class DownloaderUtil {
  public static final DownloaderUtil INSTANCE = new DownloaderUtil();

  private static final String TAG = "DownloadUtil";

  public final Bitmap downloadImage() {
    String url =
      "https://media-cdn.tripadvisor.com/media/photo-l/06/c6/e4/7e/the-clink-restaurant.jpg";
    OkHttpClient client = ServiceProvider.okHttpClient();
    Request request = (new Request.Builder()).url(url).build();
    Response response = null;
    Bitmap bitmap = null;

    try {
      // Invokes the request immediately, and blocks until
      // the response can be processed or is in error.
      response = client.newCall(request).execute();
    } catch (IOException e) {
      Log.e(TAG, "error trying to download image "+e.getMessage());
    }

    if (response != null) {
      if (response.isSuccessful()) {
        try {
          ResponseBody responseBody = response.body();
          bitmap =
            BitmapFactory.decodeStream(responseBody != null ? responseBody.byteStream() : null);
        } catch (Exception e) {
          Log.e(TAG, "error trying to decode stream "+e.getMessage());
        }
      }
      response.close();
    }

    return bitmap;
  }

  private DownloaderUtil() {
  }
}
