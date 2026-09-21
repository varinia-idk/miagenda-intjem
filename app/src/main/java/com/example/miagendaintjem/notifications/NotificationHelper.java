package com.example.miagendaintjem.notifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.miagendaintjem.MainActivity;
import com.example.miagendaintjem.R;
import com.example.miagendaintjem.model.Tarea;

public final class NotificationHelper {
    private static final String HIGH_PRIORITY_CHANNEL = "high_priority_tasks";

    private NotificationHelper() {
    }

    public static void createChannel(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                HIGH_PRIORITY_CHANNEL,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription(
                context.getString(R.string.notification_channel_description)
        );

        NotificationManager manager =
                context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @SuppressLint("MissingPermission")
    public static void notifyHighPriority(
            @NonNull Context context,
            @NonNull Tarea task
    ) {
        boolean hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            return;
        }

        Intent openAppIntent = new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                task.getId(),
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, HIGH_PRIORITY_CHANNEL)
                        .setSmallIcon(R.drawable.ic_agenda)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(
                                R.string.notification_body,
                                task.getTitulo(),
                                task.getMateria()
                        ))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify(task.getId(), notification.build());
    }
}
