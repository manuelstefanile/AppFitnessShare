package com.example.appfitness.Pagina3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.appfitness.R;

public class TimerService extends Service {

    private static final int NOTIFICATION_ID = 123;
    private CountDownTimer countDownTimer;
    public static long millisecondi;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimer() {
        if(PopupEsercizio.pausaTimer)
            TimerInPausa();
        else {
            countDownTimer = new CountDownTimer(millisecondi, 1000) { // 60 secondi
                @Override
                public void onTick(long millisUntilFinished) {
                    updateNotification(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    // Vibra due volte quando il timer è completato
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                            // Versioni precedenti ad Android Oreo
                            vibrator.vibrate(new long[]{0, 100, 200, 300}, -1); // Vibra per 100ms, pausa per 200ms, vibra per 300ms, -1 indica di ripetere solo una volta
                    }
                    stopSelf(); // Termina il servizio quando il timer è completato
                }
            };
            countDownTimer.start();
        }
    }
    private void TimerInPausa(){
        // Costruisci l'intent per aprire l'activity principale
        Intent notificationIntent = new Intent(this, PaginaScheda_Pag3.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Costruisci la notifica con l'intent appena creato
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Timer")
                .setContentText("Tempo in pausa ")
                .setSmallIcon(R.drawable.ic_baseline_close_24)
                .setContentIntent(pendingIntent) // Associa l'intent alla notifica
                .build();

        // Mostra la notifica
        startForeground(NOTIFICATION_ID, notification);
    }

    private void updateNotification(long millisUntilFinished) {
        // Costruisci l'intent per aprire l'activity principale
        Intent notificationIntent = new Intent(this, PaginaScheda_Pag3.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Costruisci la notifica con l'intent appena creato
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Timer")
                .setContentText("Tempo rimanente: " + millisUntilFinished / 1000 + " secondi")
                .setSmallIcon(R.drawable.ic_baseline_close_24)
                .setContentIntent(pendingIntent) // Associa l'intent alla notifica
                .build();

        // Mostra la notifica
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
