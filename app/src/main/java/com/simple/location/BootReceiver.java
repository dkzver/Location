package com.simple.location;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BootReceiver extends BroadcastReceiver {
    Context mContext;
    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
    private BackgroundLocationService backgroundLocationService;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            Intent activivtyIntent = new Intent(context, MainActivity.class);
            activivtyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activivtyIntent);

//            final Intent intentBackgroundLocationService = new Intent(context, BackgroundLocationService.class);
//            context.startService(intentBackgroundLocationService);
//            context.bindService(intentBackgroundLocationService, new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName className, IBinder service) {
//                    String name = className.getClassName();
//                    if (name.endsWith("BackgroundLocationService")) {
//                        backgroundLocationService = ((BackgroundLocationService.LocationServiceBinder) service).getService();
//                        boolean access = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//                        if (access) {
//                            backgroundLocationService.startTracking();
//                        } else {
//                            Intent activivtyIntent = new Intent(context, MainActivity.class);
//                            activivtyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(activivtyIntent);
//                        }
//                    }
//                }
//
//                public void onServiceDisconnected(ComponentName className) {
//                    if (className.getClassName().equals("BackgroundLocationService") && backgroundLocationService != null) {
//                        backgroundLocationService = null;
//                    }
//                }
//            }, Context.BIND_AUTO_CREATE);
        }
    }
}
