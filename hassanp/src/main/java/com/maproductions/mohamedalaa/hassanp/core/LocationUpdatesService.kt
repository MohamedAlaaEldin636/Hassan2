package com.maproductions.mohamedalaa.hassanp.core

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.maproductions.mohamedalaa.shared.R as SR

/*@AndroidEntryPoint
class LocationUpdatesService : Service() {

    companion object {
        private const val PACKAGE_NAME = "com.maproductions.mohamedalaa.hassanp.core"

        private val TAG = LocationUpdatesService::class.java.simpleName

        private const val CHANNEL_ID = "LocationUpdatesService_CHANNEL_ID"

        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

        private const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"

        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000L
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2L

        const val KEY_ORDER_ID = "KEY_ORDER_ID"
        const val KEY_STOP_SERVICE = "KEY_STOP_SERVICE"

        *//** Change UI IN CASE SCREEN IS OPENED *//*
        const val BROADCAST_DONE_EXECUTION = "BROADCAST_DONE_EXECUTION"

        //private const val NOTIFICATIONS_TAG = "LocationUpdatesService.NOTIFICATIONS_TAG"

        fun startTrackingForegroundService(context: Context, orderId: Int) =
            startOrStopTrackingForegroundService(context, orderId, true)
        fun stopTrackingForegroundService(context: Context, orderId: Int) =
            startOrStopTrackingForegroundService(context, orderId, false)
        private fun startOrStopTrackingForegroundService(context: Context, orderId: Int, startNotStop: Boolean) {
            val intent = Intent(context, LocationUpdatesService::class.java).also {
                it.putExtra(KEY_ORDER_ID, orderId)
                it.putExtra(KEY_STOP_SERVICE, !startNotStop)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            }else {
                context.startService(intent)
            }
        }
    }

    private lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private lateinit var serviceHandler: Handler

    private lateinit var mainThreadHandler: Handler

    private var lastLocation: Location? = null

    @Inject
    lateinit var repoSettings: RepoSettings

    private var orderId = -1

    *//**
     * - Created the following [fusedLocationClient], [locationRequest], [locationCallback],
     * [notificationManager] with a channel.
     *//*
    override fun onCreate() {
        Timber.e("onCreate")

        // For hilt injection isa.
        super.onCreate()

        mainThreadHandler = Handler(Looper.getMainLooper())

        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                broadcastToApi(locationResult.lastLocation)
            }
        }

        notificationManager = getSystemService()!!

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(SR.string.app_name)

            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            channel.importance = NotificationManager.IMPORTANCE_HIGH

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.e("onStartCommand")

        orderId = intent?.getIntExtra(KEY_ORDER_ID, -1) ?: -1
        val stopService = intent?.getBooleanExtra(KEY_STOP_SERVICE, false) ?: false

        if (stopService) {
            Timber.e("stop service")

            serviceHandler.post {
                // to indicate loading isa.
                // https://stackoverflow.com/questions/6406730/updating-an-ongoing-notification-quietly
                // and sendBroadcast of loading for ui xD

                sendBroadcast(Intent(BROADCAST_DONE_EXECUTION))

                // check if no other notifications exists isa.
                val count = notificationManager.activeNotifications.count { it.isOngoing }
                Timber.e("count $count")
                if (count <= 1) {
                    fusedLocationClient.removeLocationUpdates(locationCallback)

                    stopForeground(false)
                    stopSelf()
                }

                if (count > 0) {
                    notificationManager.cancel(orderId)
                }
            }

            return START_NOT_STICKY
        }

        //region Notification
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_main)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentTitle(getString(SR.string.the_follow_on_map))
            .setContentText(getString(SR.string.foreground_service_body_follow_on_map))
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(SR.string.foreground_service_body_follow_on_map)))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setOngoing(true)
            .setShowWhen(true)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
            .build()
        //endregion

        //notificationManager.notify(NOTIFICATIONS_TAG, orderId, notification)

        startForeground(orderId, notification)

        // 1. get current location
        // 2. update firebase then
        // 3. get periodic location and save to firebase if > 10 m isa.

        getCurrentLocationThenPeriodicLocation()

        return START_STICKY
    }

    private fun broadcastToApi(location: Location) {
        val performUpdate = lastLocation.let { lastLocation ->
            if (lastLocation == null) {
                true
            }else {
                val distanceInMeters = SphericalUtil.computeDistanceBetween(
                    LatLng(lastLocation.latitude, lastLocation.longitude),
                    LatLng(location.latitude, location.longitude),
                )

                distanceInMeters > 10
            }
        }

        lastLocation = location

        Timber.e("111 -> performUpdate $performUpdate orderId $orderId")
        if (performUpdate && orderId != -1) {
            Timber.e("222 -> performUpdate $performUpdate orderId $orderId")
            serviceHandler.post {
                runBlocking {
                    repoSettings.sendTrackingCurrentLocation(
                        orderId,
                        location.latitude.toString(),
                        location.longitude.toString(),
                    )
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)

        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun getCurrentLocationThenPeriodicLocation() {
        try {
            val cancellationToken = object : CancellationToken() {
                override fun onCanceledRequested(listener: OnTokenCanceledListener): CancellationToken = this

                override fun isCancellationRequested(): Boolean = false
            }

            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationToken
            ).addOnCompleteListener { task ->
                val location = task.result

                if (task.isSuccessful && location != null) {
                    broadcastToApi(location)

                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper() ?: Looper.getMainLooper()
                    )
                }else {
                    Timber.e("Failed to get current location")
                }
            }
        }catch (unlikely: SecurityException) {
            Timber.e("Lost location permission $unlikely")
        }
    }

}*/
