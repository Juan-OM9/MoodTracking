package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa un hábito o actividad que el usuario
 * desea rastrear y monitorear a diario.
 *
 * Esta clase se utiliza para almacenar la configuración y el progreso de un hábito en la
 * base de datos de Firebase Firestore.
 *
 * @property id El identificador único del hábito.
 * @property userId El identificador único del usuario propietario del hábito.
 *
 * @property title El nombre o título del hábito.
 * @property description Una descripción detallada del propósito del hábito.
 * @property category La clasificación o categoría a la que pertenece el hábito.
 *
 * @property dailyGoal La meta o el objetivo diario establecido por el usuario para este hábito.
 * Generalmente representa la cantidad de minutos o repeticiones.
 *
 * @property history Un mapa que registra el progreso diario del hábito, donde la clave es
 * la fecha (en formato de cadena) y el valor es la cantidad de progreso
 * registrado para ese día.
 *
 * @property createdAt La marca de tiempo del servidor de Firebase que indica la fecha y hora
 * en que se creó este registro de hábito.
 */
data class Habit(
    var id: String = "",
    val userId: String = "",

    val title: String = "",
    val description: String = "",
    val category: String = "Otro",

    val dailyGoal: Int = 60,

    val history: Map<String, Int> = emptyMap(),

    @ServerTimestamp
    val createdAt: Date? = null
)