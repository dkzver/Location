package com.simple.location;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class App extends Application {

    private BackgroundLocationService backgroundLocationService;
    public static AppDatabase Database;

    @Override
    public void onCreate() {
        super.onCreate();

        Database = AppDatabase.getInstance(getApplicationContext());
    }
}
