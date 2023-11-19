package com.delitx.univlabmusicplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    companion object {
        private const val NOTIFICATION_ID = 200
        private const val NOTIFICATION_CHANNEL_NAME = "notification channel 1"
        private const val NOTIFICATION_CHANNEL_ID = "notification channel id 1"
    }

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var player: Player

    private var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(applicationContext)

    init {
        createNotificationChannel()
    }

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        buildNotification(mediaSession)
        startForegroundNotification(this)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    @UnstableApi
    private fun buildNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(applicationContext, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
            .setMediaDescriptionAdapter(
                PlayerServiceNotificationAdapter(
                    context = applicationContext,
                    pendingIntent = mediaSession.sessionActivity,
                ),
            )
            .build()
            .apply {
                setMediaSessionToken(mediaSession.sessionCompatToken)
                setUseFastForwardActionInCompactView(true)
                setUseRewindActionInCompactView(true)
                setUseNextActionInCompactView(false)
                setPriority(NotificationCompat.PRIORITY_LOW)
                setPlayer(player)
            }
    }

    private fun startForegroundNotification(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW,
        )
        notificationManager.createNotificationChannel(channel)
    }
}
