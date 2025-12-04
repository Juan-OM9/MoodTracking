package mx.edu.utng.modtrackin.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Clase de utilidad encargada de programar notificaciones recurrentes o exactas
 * utilizando el servicio [AlarmManager] del sistema Android.
 *
 * Incluye la lógica necesaria para solicitar el permiso `SCHEDULE_EXACT_ALARM`
 * en versiones de Android S (API 31) o superiores.
 *
 * @param context El contexto de la aplicación, necesario para acceder a servicios y lanzar Intents.
 */
class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Programa una notificación diaria recurrente para recordar al usuario el registro emocional.
     *
     * La alarma se establece para las 20:00 (8 PM) de la hora local del dispositivo.
     * Si la hora actual ya ha pasado, se programa para el día siguiente.
     * Requiere el permiso `SCHEDULE_EXACT_ALARM` en versiones recientes.
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleDailyMoodReminder() {
        if (!canScheduleAlarm()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", "¡Nuevo día, nuevas emociones! \uD83D\uDE01")
            putExtra("message", "Ya puedes hacer un nuevo registro de tus emociones.")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001, // Código único para la alarma diaria de ánimo
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20) // 8 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }


        if (Calendar.getInstance().after(calendar)) {
            // Si la hora de hoy ya pasó, programar para mañana.
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            // setRepeating se usa para alarmas diarias periódicas
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Programa una notificación exacta para recordar la fecha límite de una tarea.
     *
     * La alarma se establece a las 09:00 AM del día especificado en `dueDateString`.
     * Utiliza `setExactAndAllowWhileIdle` para garantizar precisión, si es posible.
     *
     * @param taskId El ID único de la tarea, utilizado como código de solicitud para el [PendingIntent].
     * @param taskTitle El título de la tarea, incluido en la notificación.
     * @param dueDateString La fecha de vencimiento en formato "yyyy-MM-dd".
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleTaskNotification(taskId: String, taskTitle: String, dueDateString: String) {
        if (!canScheduleAlarm()) {
            Toast.makeText(context, "Falta permiso de alarmas exactas", Toast.LENGTH_SHORT).show()
            return
        }

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = try {
            format.parse(dueDateString)
        } catch (e: Exception) {
            null
        }

        if (date == null) return

        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 9) // Recordatorio a las 9 AM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }


        // No programar si la fecha/hora ya pasó
        if (calendar.timeInMillis < System.currentTimeMillis()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", "⏳ Tarea por vencer: $taskTitle")
            putExtra("message", "Hoy es la fecha límite. ¡Tú puedes!")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode(), // Usa el hash del ID de la tarea como código de solicitud único
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // setExactAndAllowWhileIdle se usa para alarmas precisas que deben dispararse incluso en Doze Mode.
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Toast.makeText(context, "Recordatorio programado para el $dueDateString", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Scheduler", "Error al programar", e)
        }
    }

    /**
     * Verifica si la aplicación tiene el permiso `SCHEDULE_EXACT_ALARM`.
     *
     * Para Android S (API 31) y superiores, si no tiene el permiso, lanza un Intent
     * para llevar al usuario a la configuración del sistema para solicitarlo.
     *
     * @return `true` si el permiso está concedido o no es necesario (API < 31); `false` si el permiso falta y se solicitó.
     */
    private fun canScheduleAlarm(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Si el permiso falta, abre la configuración para que el usuario lo conceda
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                false
            } else {
                true
            }
        } else {
            // Permiso implícito en versiones anteriores a S
            true
        }
    }
}