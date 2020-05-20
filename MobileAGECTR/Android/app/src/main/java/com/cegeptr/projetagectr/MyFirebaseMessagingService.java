package com.cegeptr.projetagectr;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.cegeptr.projetagectr.logic.Const;
import com.cegeptr.projetagectr.logic.DataSingleton;
import com.cegeptr.projetagectr.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private DataSingleton data = DataSingleton.getInstance();

    private static final String TAG = "MyFirebaseMsgService";


    /** Méthode appelée quand un message est reçu.*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Vérifier s’il y a des données dans le message reçu.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* décider si le traitement est long */ true) {
                // utiliser une tâche asynchrone pour toute opération qui pourrait être longue

            } else {
                // Mettre du code si l'opération dure moins de 10 secondes
            }

        }

        // Vérifier si la notification contient des données
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //on lance la notification ici
        sendNotification( remoteMessage.getNotification().getBody());
    }



     /** si le token est mis à jour la méthode onNewToken sera appelée*/
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        try {
            data.catchUserToken(token);
            if (data.getConnectedUser() != null) {
                data.updateUserToken();
            }
        }
        catch (Exception e){

        }
    }

    /**
     * Méthode pour lancer une notification éventuellement pour l'utilisateur
     *il n'est pas toujours nécessaire de le faire
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "1793";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(getString(R.string.app_name))
                        .setSmallIcon(R.drawable.ic_book_black_24dp)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Vérifier si on est sur un appareil avec Android Oreo ou plus pour utiliser les canaux.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Titre de la chaine",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID de notification */, notificationBuilder.build());
    }
}
