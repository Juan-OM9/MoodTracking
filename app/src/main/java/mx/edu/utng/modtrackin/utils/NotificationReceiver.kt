package mx.edu.utng.modtrackin.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receptor de difusión (BroadcastReceiver) utilizado para manejar la recepción de intents
 * programados (típicamente a través de AlarmManager) y disparar una notificación.
 *
 * Esta clase es el punto de entrada que se activa fuera de la aplicación principal
 * cuando un evento de tiempo o sistema ocurre.
 */
class NotificationReceiver : BroadcastReceiver() {

    /**
     * Método invocado cuando se recibe un intent de difusión.
     *
     * Extrae el título y el mensaje del intent y utiliza [AppNotificationManager] para
     * enviar la notificación al usuario.
     *
     * @param context El contexto en el que se ejecuta el receptor.
     * @param intent El objeto [Intent] que contiene la acción y los datos extra de la notificación
     * (título y mensaje).
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Extrae el título de los extras del intent, usando un valor predeterminado si no se encuentra.
        val title = intent.getStringExtra("title") ?: "Recordatorio"
        // Extrae el mensaje de los extras del intent.
        val message = intent.getStringExtra("message") ?: ""

        // Utiliza el gestor de notificaciones previamente definido para mostrar el mensaje.
        val manager = AppNotificationManager(context)
        manager.sendNotification(title, message)
    }
}