package com.resonar.eq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import androidx.core.app.NotificationCompat;

public class MediaService extends Service {
  private PowerManager.WakeLock wl;

  @Override
  public IBinder onBind(Intent i) { return null; }

  @Override
  public int onStartCommand(Intent i, int flags, int id) {
    if (Build.VERSION.SDK_INT >= 26) {
      NotificationChannel c = new NotificationChannel("resonar", "Reproducción", NotificationManager.IMPORTANCE_LOW);
      ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(c);
    }
    Intent open = new Intent(this, MainActivity.class);
    open.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pi = PendingIntent.getActivity(this, 0, open, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    Notification n = new NotificationCompat.Builder(this, "resonar")
      .setSmallIcon(android.R.drawable.ic_media_play)
      .setContentTitle("Resonar EQ")
      .setContentText("Reproduciendo en segundo plano")
      .setContentIntent(pi)
      .setOngoing(true)
      .build();
    if (Build.VERSION.SDK_INT >= 29) startForeground(1, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
    else startForeground(1, n);
    if (wl == null) {
      PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
      wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "resonar:audio");
    }
    if (!wl.isHeld()) wl.acquire(6 * 60 * 60 * 1000L);
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    if (wl != null && wl.isHeld()) wl.release();
    super.onDestroy();
  }
}
