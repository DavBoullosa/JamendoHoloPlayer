/*
 * Copyright (c) 2014 David Alejandro Fernández Sancho
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.oneoctopus.jamendoholoplayer.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.activities.PlayerActivity;


/**
 * Helper class for showing and canceling now playing
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NowPlayingNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NowPlaying";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context, final String trackTitle, final String artistName, final Bitmap image) {
        final Resources res = context.getResources();
        Intent goToPlayerActivity = new Intent(context, PlayerActivity.class);
        goToPlayerActivity.putExtra("fromNotification", true);

        final String ticker = String.format(res.getString(R.string.now_playing_notification_ticker), trackTitle, artistName);

        final NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(context)

                .setDefaults(Notification.STREAM_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(trackTitle)
                .setContentText(artistName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(image)
                .setTicker(ticker)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                goToPlayerActivity,
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image)
                        .setBigContentTitle(trackTitle)
                        .setSummaryText(artistName))

                        // Example additional actions for this notification. These will
                        // only show on devices running Android 4.1 or later, so you
                        // should ensure that the activity in this notification's
                        // content intent provides access to the same actions in
                        // another way.
//                    .addAction(
//                            R.drawable.previous,
//                            res.getString(R.string.previous),
//                            PendingIntent.getActivity(
//                                    context,
//                                    0,
//                                    Intent.createChooser(new Intent(ACTION.SEND)
//                                            .setType("text/plain")
//                                            .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
//                                    PendingIntent.FLAG_UPDATE_CURRENT))
//                    .addAction(
//                            R.drawable.ic_action_stat_reply,
//                            res.getString(R.string.action_reply),
//                            null)

                        // This is an on-going event
                .setOngoing(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(android.content.Context, android.app.Notification)}
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
