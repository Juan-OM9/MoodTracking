package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa una tarea o elemento pendiente (To-Do).
 *
 * Contiene toda la información ingresada por el usuario en la pantalla de adición de tareas,
 * sirviendo como la "ficha de registro" en la base de datos de Firebase Firestore.
 *
 * @property id El identificador único de la tarea.
 * @property userId El identificador único del usuario propietario de la tarea.
 *
 * @property title El nombre o título breve de la tarea.
 * @property description Una descripción más detallada sobre lo que implica la tarea.
 * @property category La clasificación o grupo al que pertenece la tarea.
 *
 * @property priority La importancia relativa de la tarea, utilizada para su clasificación o gestión.
 * @property isCompleted Un indicador booleano que determina si la tarea ha sido finalizada o completada.
 * Las anotaciones [PropertyName] aseguran la correcta serialización en Firestore.
 *
 * @property createdAt La marca de tiempo del servidor de Firebase que indica la fecha y hora
 * en que se creó este registro de tarea.
 * @property dueDate La fecha límite o de vencimiento para completar la tarea, en formato de cadena.
 * @property reminder La configuración de recordatorio asociada a la tarea, en formato de cadena.
 */
data class Task(
    var id: String = "",
    val userId: String = "",


    val title: String = "",
    val description: String = "",
    val category: String = "",


    val priority: String = "Baja",

    @get:PropertyName("isCompleted")
    @set:PropertyName("isCompleted")
    var isCompleted: Boolean = false,

    @ServerTimestamp
    val createdAt: Date? = null,
    val dueDate: String = "",
    val reminder: String = ""
)