package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.Habit
import mx.edu.utng.modtrackin.data.repository.HabitRepository
import java.time.LocalDate

/**
 * Representa el estado actual de la UI de la pantalla de gestión de hábitos.
 *
 * @property habitList La lista de todos los [Habit] del usuario.
 * @property isLoading Indica si el repositorio está cargando los datos iniciales.
 * @property selectedDate La fecha seleccionada en el dashboard para registrar o ver el progreso.
 * @property isEditorOpen Indica si la pantalla de creación/edición de hábitos está visible.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el formulario o en el repositorio.
 * @property id El ID del hábito que se está editando (vacío si es nuevo).
 * @property title El título del hábito en el formulario.
 * @property description La descripción del hábito en el formulario.
 * @property category La categoría seleccionada para el hábito.
 * @property targetMinutesInput El valor de la meta diaria (en minutos) en formato String.
 */
data class HabitUiState(
    val habitList: List<Habit> = emptyList(),
    val isLoading: Boolean = false,

    // Control de fecha seleccionada
    val selectedDate: LocalDate = LocalDate.now(),

    // Editor
    val isEditorOpen: Boolean = false,
    val errorMessage: String? = null,

    // Campos formulario
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val targetMinutesInput: String = ""
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Hábitos.
 *
 * Es responsable de:
 * 1. Escuchar los cambios en tiempo real de la base de datos para mantener la lista actualizada.
 * 2. Manejar la navegación entre la vista principal y el editor.
 * 3. Actualizar el progreso diario de los hábitos.
 * 4. Gestionar el ciclo de vida (creación, edición y eliminación) de los hábitos.
 */
class HabitViewModel : ViewModel() {

    var uiState by mutableStateOf(HabitUiState())
        private set

    private val repository = HabitRepository()

    init {
        startListening()
    }

    /**
     * Inicia la escucha en tiempo real de los hábitos del usuario en el repositorio.
     *
     * La lista de hábitos se actualiza automáticamente en [uiState.habitList] cuando hay cambios.
     */
    private fun startListening() {
        uiState = uiState.copy(isLoading = true)
        repository.listenToHabits { habits ->
            uiState = uiState.copy(habitList = habits, isLoading = false)
        }
    }

    /**
     * Actualiza la fecha seleccionada en el dashboard.
     * @param newDate La nueva fecha [LocalDate] seleccionada.
     */
    fun changeDate(newDate: LocalDate) {
        uiState = uiState.copy(selectedDate = newDate)
    }

    // --- AGREGAR TIEMPO MANUALMENTE ---
    /**
     * Agrega minutos al registro de progreso de un hábito específico para la fecha seleccionada.
     *
     * Actualiza el mapa de historial del hábito y guarda el cambio en el repositorio.
     *
     * @param habit El [Habit] al que se le va a añadir tiempo.
     * @param minutesToAdd La cantidad de minutos a sumar al progreso del día.
     */
    fun addMinutesToHabit(habit: Habit, minutesToAdd: Int) {
        if (minutesToAdd <= 0) return

        val dateKey = uiState.selectedDate.toString()
        val currentMinutes = habit.history[dateKey] ?: 0
        val newMinutes = currentMinutes + minutesToAdd

        val newHistory = habit.history.toMutableMap()
        newHistory[dateKey] = newMinutes

        viewModelScope.launch {
            val updatedHabit = habit.copy(history = newHistory)
            repository.saveHabit(updatedHabit)
        }
    }

    // --- EDITOR ---
    /** Abre la pantalla del editor para crear un nuevo hábito, limpiando el estado. */
    fun openEditorNew() {
        uiState = uiState.copy(isEditorOpen = true, id = "", title = "", description = "", category = "", targetMinutesInput = "", errorMessage = null)
    }

    /**
     * Abre la pantalla del editor para modificar un hábito existente, cargando sus datos en el estado.
     * @param habit El [Habit] a modificar.
     */
    fun openEditorModify(habit: Habit) {
        uiState = uiState.copy(
            isEditorOpen = true,
            id = habit.id,
            title = habit.title,
            description = habit.description,
            category = habit.category,
            targetMinutesInput = habit.dailyGoal.toString(),
            errorMessage = null
        )
    }

    /** Cierra la pantalla del editor y regresa al dashboard principal. */
    fun closeEditor() {
        uiState = uiState.copy(isEditorOpen = false)
    }

    /** Actualiza el título del hábito en el estado del formulario. */
    fun onTitleChange(v: String) { uiState = uiState.copy(title = v) }

    /** Actualiza la descripción del hábito en el estado del formulario. */
    fun onDescriptionChange(v: String) { uiState = uiState.copy(description = v) }

    /**
     * Selecciona la categoría del hábito. Si el título está vacío, usa el nombre de la categoría como título inicial.
     * @param cat El nombre de la categoría seleccionada.
     */
    fun onCategorySelected(cat: String) {
        val newTitle = if (uiState.title.isEmpty()) cat else uiState.title
        uiState = uiState.copy(category = cat, title = newTitle)
    }

    /**
     * Actualiza la meta de minutos diarios en el estado, aceptando solo dígitos.
     * @param v El nuevo valor (string) de minutos.
     */
    fun onDurationChange(v: String) {
        if (v.all { it.isDigit() }) uiState = uiState.copy(targetMinutesInput = v)
    }

    /**
     * Valida y guarda (crea o actualiza) el hábito actual en el repositorio.
     * Si el título está vacío, establece un [errorMessage].
     */
    fun saveHabit() {
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(errorMessage = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            // Preserva el historial existente si estamos editando
            val existingHabit = uiState.habitList.find { it.id == uiState.id }
            val history = existingHabit?.history ?: emptyMap()

            val habit = Habit(
                id = uiState.id,
                userId = Firebase.auth.currentUser?.uid ?: "",
                title = uiState.title,
                description = uiState.description,
                category = uiState.category.ifBlank { "Otro" },
                dailyGoal = uiState.targetMinutesInput.toIntOrNull() ?: 60,
                history = history
            )

            val result = repository.saveHabit(habit)
            if (result.isSuccess) closeEditor()
        }
    }

    /** Elimina el hábito actualmente abierto en el editor del repositorio. */
    fun deleteHabit() {
        if (uiState.id.isNotEmpty()) {
            viewModelScope.launch {
                val result = repository.deleteHabit(uiState.id)
                if (result.isSuccess) closeEditor()
            }
        }
    }
}