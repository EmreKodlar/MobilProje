package com.example.projekitap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



//-----Notification ------------------------

       String kisim = intent.getStringExtra("kisim");
        String kyazar = intent.getStringExtra("kyazar");
        String kkimden = intent.getStringExtra("kkimden");
        int kid = intent.getIntExtra("kid",0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"KitapUyari")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Alınacak Bir Kitabınız Var!")
                .setContentText("Kitap Adı: "+ kisim + " - Yazarı: "+ kyazar + " - Tavsiye Eden: "+ kkimden   )
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setStyle(new NotificationCompat.BigTextStyle() .bigText(kisim))

                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)


                .setAutoCancel(true);




       // mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManagerCompat nmc=NotificationManagerCompat.from(context);
        nmc.notify(kid, mBuilder.build()); // buradaki kid notification'ın id'si... O yüzden benzersiz olmalı...

//-----Alarm ------------------------
        Uri alarmMelodisi= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // TYPE_ALARM yaparsan, kullanıcın ayarladığı alarm sesi çalar
        Ringtone ringtone=RingtoneManager.getRingtone(context,alarmMelodisi);
        ringtone.play();

    }
}
