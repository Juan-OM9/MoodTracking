package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa un registro emocional diario.
 *
 * Esta ficha se utiliza para almacenar y recuperar la información del estado de ánimo
 * y los detalles asociados del usuario en la base de datos de Firebase Firestore.
 *
 * @property id El identificador único del registro emocional dentro de la colección.
 * @property userId El identificador único del usuario que creó este registro.
 *
 * @property emotionId El ID de referencia de la emoción seleccionada
 * @property emotionEmoji El emoji asociado con la emoción para una visualización rápida
 * @property emotionText El nombre descriptivo de la emoción seleccionada
 * @property adjective El adjetivo adicional que califica o matiza la emoción principal
 * @property note Una nota o descripción detallada escrita por el usuario sobre el registro emocional.
 *
 * @property dateString La fecha del registro en formato de cadena (string) para facilitar su visualización
 * @property timestamp La marca de tiempo del servidor de Firebase que indica cuándo se guardó el registro.
 */
data class EmotionEntry(
    var id: String = "",
    val userId: String = "",

    // Datos de la emoción
    val emotionId: String = "",
    val emotionEmoji: String = "",
    val emotionText: String = "",
    val adjective: String = "",
    val note: String = "",

    val dateString: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)