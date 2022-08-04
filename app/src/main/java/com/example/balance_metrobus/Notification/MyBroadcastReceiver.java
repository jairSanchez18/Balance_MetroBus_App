package com.example.balance_metrobus.Notification;

import static android.provider.Settings.System.getString;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.balance_metrobus.MainActivity;
import com.example.balance_metrobus.R;

import java.util.Random;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "001";

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    private void showNotification(Context context) {
        int random = (int) (Math.random() * 6 - 1);
        String text = null;

        switch (random) {
            case 1:
                text = "Recuerda verificar tu saldo antes de salir de casa";
                break;
            case 2:
                text = "Realiza recargas frecuentemente";
                break;
            case 3:
                text = "Hola, visitanos para ver el estado de tus tarjetas";
                break;
            case 4:
                text = "Esperamos, te vaya bien hoy y siempre";
                break;
            case 5:
                text = "Puedes eliminar las tarjetas que ya no uses";
                break;
            default:
                text = "Hola, recuerda que puedes añadir muchas tarjetas";
        }


        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_iconapp)
                .setContentTitle("Metro Bus App")
                .setContentText(text);
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        createNotificationChannel(context);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Metro Bus Notification";
            String description = "Recordatorios";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}

