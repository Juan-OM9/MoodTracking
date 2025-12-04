package mx.edu.utng.modtrackin.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Objeto de utilidad que contiene las constantes de configuración del canal de notificación
 * de la aplicación y la lógica para crear dicho canal en Android O (API 26) o superior.
 */
object NotificationHelper {
    /** El identificador único del canal de notificación. */
    const val CHANNEL_ID = "modtrackin_channel"
    /** El nombre visible para el usuario del canal de notificación. */
    const val CHANNEL_NAME = "Notificaciones ModTrackin"
    /** Descripción mostrada al usuario en la configuración del sistema. */
    const val CHANNEL_DESCRIPTION = "Recordatorios de tareas y motivación"
    /** Un ID de notificación genérico si se requiere un identificador fijo. */
    const val NOTIFICATION_ID = 1

    /**
     * Crea y registra el canal de notificación del sistema.
     *
     * Este paso es obligatorio para que las notificaciones se muestren en Android 8.0 (Oreo) o superior.
     * El canal está configurado con prioridad alta, luces y vibración.
     *
     * @param context El contexto de la aplicación, necesario para acceder a [NotificationManager].
     */
    fun createNotificationChannel(context: Context) {
        // La creación de canales solo es necesaria a partir de la API 26 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            // Obtiene el servicio de gestión de notificaciones y registra el canal
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}