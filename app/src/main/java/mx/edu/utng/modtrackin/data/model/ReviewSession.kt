package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.Exclude
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa un registro de sueño diario.
 *
 * Esta entidad se utiliza para almacenar los detalles del ciclo de sueño de un usuario
 * y registrar la calidad del mismo para su posterior correlación y análisis.
 *
 * @property id El identificador único del registro de sueño. Esta propiedad está excluida
 * de la serialización al guardar en Firebase Firestore.
 * @property userId El identificador único del usuario al que pertenece este registro de sueño.
 * @property date La fecha del registro de sueño en formato de cadena (string).
 * @property startTime La hora específica en que el usuario se durmió.
 * @property endTime La hora específica en que el usuario se despertó.
 * @property durationHours La duración total del sueño, calculada en horas.
 * @property quality La calificación de calidad del sueño reportada por el usuario, típicamente en una escala de 1 a 5.
 */
data class SleepEntry(
    @get:Exclude var id: String = "",
    val userId: String = "",
    val date: String = "", // YYYY-MM-DD
    val startTime: Date = Date(), // Hora en que se durmió
    val endTime: Date = Date(), // Hora en que se despertó
    val durationHours: Double = 0.0, // Duración calculada
    val quality: Int = 1 // Calidad reportada (1-5)
)