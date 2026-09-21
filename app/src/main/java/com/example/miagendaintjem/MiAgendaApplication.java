package com.example.miagendaintjem;

import android.app.Application;

import com.example.miagendaintjem.notifications.NotificationHelper;

public final class MiAgendaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createChannel(this);
    }
}
