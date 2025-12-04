package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.Task
import mx.edu.utng.modtrackin.data.repository.TaskRepository

/**
 * Representa el estado actual de la UI de la pantalla de Tareas.
 *
 * Contiene la lista de tareas, indicadores de carga y error, y los campos del formulario
 * de edición/creación.
 *
 * @property taskList La lista de todas las [Task] del usuario, observada en tiempo real.
 * @property isLoading Indica si alguna operación de datos (carga o guardado) está en curso.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el repositorio.
 * @property isEditorOpen Indica si la pantalla de creación/edición de tareas está visible.
 * @property id El ID de la tarea que se está editando (vacío si es nueva).
 * @property title El título de la tarea en el formulario.
 * @property description La descripción de la tarea en el formulario.
 * @property category La categoría de la tarea.
 * @property priority La prioridad seleccionada (Alta, Media, Baja).
 * @property dueDate La fecha de vencimiento en formato de cadena.
 * @property reminder La configuración de recordatorio.
 * @property isCompleted El estado de completado de la tarea.
 */
data class TaskUiState(
    val taskList: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditorOpen: Boolean = false,

    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "🎓 Académica",
    val priority: String = "Baja",
    val dueDate: String = "",
    val reminder: String = "",
    val isCompleted: Boolean = false
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Tareas.
 *
 * Es responsable de:
 * 1. Sincronizar la lista de tareas en tiempo real con [TaskRepository].
 * 2. Manejar el ciclo de vida de las tareas (crear, editar, eliminar y marcar como completada).
 * 3. Gestionar la apertura y cierre del editor de tareas.
 */
class TaskViewModel : ViewModel() {

    var uiState by mutableStateOf(TaskUiState())
        private set

    private val repository = TaskRepository()

    init {
        // Ejecuta una carga inicial para asegurar datos rápidos
        refreshTasks()
        // Inicia el listener para recibir actualizaciones en tiempo real
        startListening()
    }

    /**
     * Realiza una carga asíncrona única de todas las tareas del usuario.
     * Se usa principalmente al inicio para asegurar la presencia de datos.
     */
    private fun refreshTasks() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.getAllTasks()
            result.onSuccess { tasks ->
                uiState = uiState.copy(taskList = tasks, isLoading = false)
            }
            result.onFailure { uiState = uiState.copy(isLoading = false) }
        }
    }

    /**
     * Inicia la escucha en tiempo real de los cambios en la colección de tareas del usuario.
     * La lista [taskList] se actualiza automáticamente.
     */
    private fun startListening() {
        repository.listenToTasks { tasks ->
            uiState = uiState.copy(taskList = tasks, isLoading = false)
        }
    }

    /**
     * Abre el editor de tareas para la creación de una **nueva** tarea,
     * limpiando los campos del formulario.
     */
    fun openEditorNew() {
        uiState = uiState.copy(
            isEditorOpen = true,
            id = "", title = "", description = "",
            dueDate = "", reminder = "", isCompleted = false, errorMessage = null,
            // Restablece valores predeterminados para una nueva tarea
            category = "🎓 Académica", priority = "Baja"
        )
    }

    /**
     * Abre el editor de tareas para **modificar** una tarea existente,
     * cargando sus propiedades en el estado del formulario.
     *
     * @param task La [Task] a editar.
     */
    fun openEditorModify(task: Task) {
        uiState = uiState.copy(
            isEditorOpen = true,
            id = task.id, title = task.title, description = task.description,
            category = task.category, priority = task.priority,
            dueDate = task.dueDate, reminder = task.reminder,
            isCompleted = task.isCompleted, errorMessage = null
        )
    }

    /** Cierra el editor de tareas y regresa a la vista de lista. */
    fun closeEditor() {
        uiState = uiState.copy(isEditorOpen = false, errorMessage = null)
    }

    /**
     * Valida la entrada y guarda (crea o actualiza) la tarea en el repositorio.
     * Si la operación es exitosa, cierra el editor.
     */
    fun saveTask() {
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(errorMessage = "El título es obligatorio")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val taskToSave = Task(
                id = uiState.id,
                userId = Firebase.auth.currentUser?.uid ?: "",
                title = uiState.title,
                description = uiState.description,
                category = uiState.category,
                priority = uiState.priority,
                dueDate = uiState.dueDate,
                reminder = uiState.reminder,
                isCompleted = uiState.isCompleted
            )

            val result = repository.saveTask(taskToSave)
            if (result.isSuccess) {
                refreshTasks() // Asegura la actualización inmediata de la lista
                closeEditor()
            } else {
                uiState = uiState.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Elimina la tarea actualmente abierta en el editor del repositorio.
     * Solo se ejecuta si la tarea tiene un ID asignado.
     */
    fun deleteTask() {
        if (uiState.id.isNotEmpty()) {
            viewModelScope.launch {
                uiState = uiState.copy(isLoading = true)
                val result = repository.deleteTask(uiState.id)
                if (result.isSuccess) {
                    refreshTasks() // Asegura la actualización inmediata de la lista
                    closeEditor()
                } else {
                    uiState = uiState.copy(isLoading = false)
                }
            }
        }
    }

    /**
     * Cambia el estado `isCompleted` de una tarea específica y la guarda en el repositorio.
     *
     * @param task La [Task] cuyo estado de completado será invertido.
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            val result = repository.saveTask(updatedTask)
            if (result.isSuccess) {
                // La lista se actualiza automáticamente gracias a startListening,
                // pero refrescar puede ser una medida de seguridad si el listener es lento.
                refreshTasks()
            }
        }
    }

    // --- MANEJO DE ENTRADAS DEL FORMULARIO ---
    /** Actualiza el título en el estado. */
    fun onTitleChange(v: String) { uiState = uiState.copy(title = v) }
    /** Actualiza la descripción en el estado. */
    fun onDescriptionChange(v: String) { uiState = uiState.copy(description = v) }
    /** Actualiza la categoría en el estado. */
    fun onCategoryChange(v: String) { uiState = uiState.copy(category = v) }
    /** Actualiza la prioridad en el estado. */
    fun onPriorityChange(v: String) { uiState = uiState.copy(priority = v) }
    /** Actualiza la fecha de vencimiento en el estado. */
    fun onDueDateChange(v: String) { uiState = uiState.copy(dueDate = v) }
    /** Limpia el mensaje de error de la UI. */
    fun clearError() { uiState = uiState.copy(errorMessage = null) }
}