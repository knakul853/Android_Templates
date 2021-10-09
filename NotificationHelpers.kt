
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.raywenderlich.android.memo.R
import com.raywenderlich.android.memo.ui.MainActivity

private const val CHANNEL_ID = "StarWarsChannel"
private const val CHANNEL_NAME = "StarWarsChannelName"
private const val CHANNEL_DESCRIPTION = "StarWarsChannelDescription"

class NotificationHelper(private val context: Context) {


  private val contentIntent by lazy {
    PendingIntent.getActivity(
      context,
      0,
      Intent(context, MainActivity::class.java),
      PendingIntent.FLAG_UPDATE_CURRENT
    )
  }

  
  /**
   * Sets up the notification channels for API 26+.
   * Note: This uses package name + channel name to create unique channelId's.
   *
   * @param context     application context
   * @param importance  importance level for the notificaiton channel
   * @param showBadge   whether the channel should have a notification badge
   * @param name        name for the notification channel
   * @param description description for the notification channel
   */
  fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

      val channelId = "${context.packageName}-${name}"
      val channel = NotificationChannel(channelId, name, importance)
      channel.description = description
      channel.setShowBadge(showBadge)

      val notificationManager = context.getSystemService(NotificationManager::class.java)
      notificationManager.createNotificationChannel(channel)
    }

  }

  private val notificationManager by lazy {
    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createChannel() =
    // 1
    NotificationChannel(
      CHANNEL_ID,
      CHANNEL_NAME,
      NotificationManager.IMPORTANCE_DEFAULT
    ).apply {

      // 2
      description = CHANNEL_DESCRIPTION
      setSound(null, null)
    }

  // 1
  private val notificationBuilder: NotificationCompat.Builder by lazy {
    NotificationCompat.Builder(context, CHANNEL_ID)
      .setContentTitle(context.getString(R.string.app_name))
      .setSound(null)
      .setContentIntent(contentIntent)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      // 3
      .setAutoCancel(true)
  }
  
  /**
   * Helps issue the default application channels (package name + app name) notifications.
   * Note: this shows the use of [NotificationCompat.BigTextStyle] for expanded notifications.
   *
   * @param context    current application context
   * @param title      title for the notification
   * @param message    content text for the notification when it's not expanded
   * @param bigText    long form text for the expanded notification
   * @param autoCancel `true` or `false` for auto cancelling a notification.
   * if this is true, a [PendingIntent] is attached to the notification to
   * open the application.
   */
  fun createSampleDataNotification(context: Context, title: String, message: String,
                                   bigText: String, autoCancel: Boolean) {

    val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

    val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
      setSmallIcon(R.drawable.ic_stat_medicine)
      setContentTitle(title)
      setContentText(message)
      setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
      priority = NotificationCompat.PRIORITY_DEFAULT
      setAutoCancel(autoCancel)
      val intent = Intent(context, MainActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
      setContentIntent(pendingIntent)
    }

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1001, notificationBuilder.build())
  }



  fun getNotification(): Notification {
    // 1
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notificationManager.createNotificationChannel(createChannel())
    }

    // 2
    return notificationBuilder.build()
  }
  fun updateNotification(notificationText: String? = null) {
    // 1
    notificationText?.let { notificationBuilder.setContentText(it) }
    // 2
    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
  }

  companion object {
    const val NOTIFICATION_ID = 99
  }
}
