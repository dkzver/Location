package com.simple.location;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class LocationReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.simple.location.BackgroundLocationService";
    private final MainActivity activity;

    public LocationReceiver(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("LocationReceiver");
        System.out.println(intent);
        activity.updateAdapter();
        Bundle bundle = intent.getExtras();
        System.out.println(bundle);
        System.out.println(bundle.getString("address"));
        Toast.makeText(context, "Обнаружено сообщение: " +
                        intent.getAction(),
                Toast.LENGTH_SHORT).show();

        ActivityManager am = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo
                    rsi = rs.get(i);
            System.out.println("Service " + rsi.process);
            System.out.println("Service " + rsi.service.getClassName());
        }
    }
}