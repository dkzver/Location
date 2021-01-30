package com.simple.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;

public class MainActivity extends FragmentActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private RecyclerView recycler_view_item;
    private Adapter adapter;
    private Button button_get_access;
    private Button button_run_service;
    private BackgroundLocationService backgroundLocationService;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity activity = this;
        boolean fine_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean background_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        button_get_access = (Button) findViewById(R.id.button_get_access);
        button_run_service = (Button) findViewById(R.id.button_run_service);

        button_get_access.setEnabled(!fine_location && !background_location);
        button_run_service.setEnabled(false);
        if (fine_location && background_location) {
            getLocation();
        }
        button_get_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        });

        button_run_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backgroundLocationService != null) {
                    backgroundLocationService.startTracking();
                }
            }
        });


        broadcastReceiver = new LocationReceiver(this);

        IntentFilter intentFilter = new IntentFilter(LocationReceiver.ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter);

        recycler_view_item = (RecyclerView) findViewById(R.id.recycler_view_item);
        recycler_view_item.setHasFixedSize(true);
        recycler_view_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter();
        recycler_view_item.setAdapter(adapter);

        updateAdapter();
    }

    public void updateAdapter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Item> itemList = App.Database.daoApp().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.update(itemList);
                        recycler_view_item.scrollToPosition(itemList.size() - 1);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    private void getLocation() {
        final Intent intent = new Intent(this.getApplication(), BackgroundLocationService.class);
        startService(intent);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                String name = className.getClassName();
                if (name.endsWith("BackgroundLocationService")) {
                    backgroundLocationService = ((BackgroundLocationService.LocationServiceBinder) service).getService();
                    boolean fine_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                    boolean background_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
                    button_run_service.setEnabled(fine_location && background_location);
                }
            }

            public void onServiceDisconnected(ComponentName className) {
                if (className.getClassName().equals("BackgroundLocationService")) {
                    backgroundLocationService = null;
                }
            }
        }, Context.BIND_AUTO_CREATE);
    }

    static class Adapter extends RecyclerView.Adapter<Holder> {

        public List<Item> list = new ArrayList<>();

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void update(List<Item> itemList) {
            this.list.clear();
            this.list.addAll(itemList);
            notifyDataSetChanged();
        }
    }

    static class Holder extends RecyclerView.ViewHolder {

        private final TextView text_view_location;
        private final TextView text_view_address;
        private final TextView text_view_time;

        public Holder(@NonNull View itemView) {
            super(itemView);

            text_view_location = itemView.findViewById(R.id.text_view_location);
            text_view_address = itemView.findViewById(R.id.text_view_address);
            text_view_time = itemView.findViewById(R.id.text_view_time);
        }

        public void bind(Item item) {
            text_view_location.setText(item.latitude + " " + item.longitude);
            text_view_address.setText(item.address);
            text_view_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(item.time)));
        }
    }
}