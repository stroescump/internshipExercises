package com.example.internshipexercises;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.internshipexercises.models.Post;
import com.example.internshipexercises.server.ServerProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView img;
    private Toolbar toolbar;

    public static final String TAG = MainActivity.class.getSimpleName();
    private int incrementValue = 0;
    private TextView labelCounter;
    private final Handler handler = new Handler();
    private final Executor executor = command -> new Thread(command).start();
    private MapView map_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        if (savedInstanceState != null) {
            incrementValue = savedInstanceState.getInt("value");
        } else incrementValue = 0;
        map_view = findViewById(R.id.map_view);

        initViews();
        map_view.onCreate(savedInstanceState);
        map_view.onResume();
//        getPostSynchronously();
//        getPostsAsync();
        getImageUsingThread();
//        getImageUsingExecutor();
    }

    private void getImageUsingExecutor() {
        executor.execute(() -> {
            Bitmap bitmap = DownloaderUtil.INSTANCE.downloadImage();
            handler.post(() -> {
                img.setImageBitmap(bitmap);
            });
        });

    }

    private void getImageUsingThread() {
        new Thread(() -> {
            Bitmap bitmap = DownloaderUtil.INSTANCE.downloadImage();
            handler.post(() -> {
                img.setImageBitmap(bitmap);
            });
        }).start();
    }


    private void getPostsAsync() {
        ServerProvider.createPostService().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> posts = response.body();
                if (posts != null) {
                    Log.d(TAG, "Posts: " + posts.size());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e(TAG, "Err trying to get posts... " + t.getLocalizedMessage());
            }
        });
    }

    private void getPostSynchronously() {
        try {
            Response<List<Post>> res = ServerProvider.createPostService().getPosts().execute();
            List<Post> posts = res.body();
            if (posts != null) {
                Log.d(TAG, "Posts: " + posts.size());
            }
        } catch (IOException e) {
            Log.e(TAG, "Err trying to get posts... ");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("value", incrementValue);
    }


    //    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putInt("value",incrementValue);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_favorites:
                Toast.makeText(this, "Heart", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        map_view = findViewById(R.id.map_view);
        toolbar = findViewById(R.id.toolbar);
        img = findViewById(R.id.img_async);
        labelCounter = findViewById(R.id.labelCounter);
        Button incrementBtn = findViewById(R.id.incrementBtn);
        labelCounter.setText(String.valueOf(incrementValue));
        map_view.getMapAsync(this);
        setToolbarOptions();
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue++;
                labelCounter.setText(String.valueOf(incrementValue));
            }
        });


    }

    private void setToolbarOptions() {
        toolbar.setTitle(R.string.restaurant_title);
        toolbar.setSubtitle(R.string.restaurant_description);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart : The activity exists but cannot be used by USER");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume : Prepping final UI changes");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coords = new LatLng(46.7676919, 23.5709693);
        Marker clujArena = googleMap.addMarker(new MarkerOptions()
                .position(coords)
                .title("Cluj Arena"));
        clujArena.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 15));
    }
}
