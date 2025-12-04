package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa una nota o entrada de diario simple
 * creada por el usuario.
 *
 * Se utiliza para guardar información textual y es un componente fundamental para
 * las funcionalidades de registro personal.
 *
 * @property id El identificador único de la nota.
 * @property userId El identificador único del usuario que creó la nota.
 * @property title El título de la nota.
 * @property content El contenido principal, cuerpo o texto de la nota.
 * @property timestamp La marca de tiempo del servidor de Firebase que indica la fecha y hora
 * de la creación de la nota.
 */
data class Note(
    var id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)