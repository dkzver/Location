package com.simple.location;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.concurrent.Executors;

@Database(entities = {Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    public abstract DaoApp daoApp();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, "6.sql")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
//                                Item[] items = new Item[] {
//                                        new Item(Calendar.getInstance().getTimeInMillis(), "1"),
//                                        new Item(Calendar.getInstance().getTimeInMillis(), "2"),
//                                        new Item(Calendar.getInstance().getTimeInMillis(), "3"),
//                                        new Item(Calendar.getInstance().getTimeInMillis(), "3")
//                                };
//                                getInstance(appContext).daoApp().insertAll(items);

                            }
                        });
                        super.onCreate(db);
                    }
                })
                .build();

    }
}
