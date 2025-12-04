package mx.edu.utng.modtrackin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * Clase de datos que representa un registro de sueño recuperado del historial.
 *
 * @property id El identificador único del documento en Firestore.
 * @property date La fecha del registro (YYYY-MM-DD).
 * @property startTime La hora de inicio del sueño (HH:MM).
 * @property endTime La hora de despertar (HH:MM).
 * @property quality La calificación de calidad del sueño (1-5).
 * @property durationHours La duración total calculada del sueño en horas.
 */
data class SleepEntry(
    val id: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val quality: Int = 0,
    val durationHours: Double = 0.0
)

/**
 * Representa el estado actual de la UI de la pantalla de Registro de Sueño.
 *
 * @property startTime La hora de dormir seleccionada en el selector.
 * @property endTime La hora de despertar seleccionada en el selector.
 * @property quality La calificación de calidad seleccionada (1 a 5).
 * @property sleepHistory La lista de los últimos registros de sueño del usuario.
 * @property isLoadingHistory Indica si la lista de historial se está cargando.
 * @property isSaving Indica si la operación de guardar está en curso.
 * @property isSaved Indica si la última operación de guardar fue exitosa.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en Firebase.
 */
data class SleepUiState(
    val startTime: LocalTime = LocalTime.of(22, 0),
    val endTime: LocalTime = LocalTime.of(7, 0),
    val quality: Int = 3,
    val sleepHistory: List<SleepEntry> = emptyList(),
    val isLoadingHistory: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Registro de Sueño.
 *
 * Es responsable de:
 * 1. Capturar las horas de inicio/fin y la calidad del sueño.
 * 2. Guardar y eliminar registros de sueño en Firestore.
 * 3. Cargar y mantener actualizado el historial de sueño del usuario.
 * 4. Gestionar los estados de carga y errores de la UI.
 */
class SleepViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSleepHistory()
    }

    // --- FUNCIONES DE ACTUALIZACIÓN DE UI ---
    /** Actualiza la hora de inicio del sueño en el estado. */
    fun updateStartTime(newTime: LocalTime) {
        _uiState.update { it.copy(startTime = newTime) }
    }

    /** Actualiza la hora de despertar en el estado. */
    fun updateEndTime(newTime: LocalTime) {
        _uiState.update { it.copy(endTime = newTime) }
    }

    /**
     * Actualiza la calificación de calidad del sueño, asegurando que esté dentro del rango de 1 a 5.
     */
    fun updateQuality(newQuality: Int) {
        _uiState.update { it.copy(quality = newQuality.coerceIn(1, 5)) }
    }

    // --- FUNCIÓN PARA CARGAR HISTORIAL ---
    /**
     * Carga el historial de registros de sueño del usuario desde Firestore.
     *
     * Ordena los resultados por fecha descendente y limita la consulta a 30 registros.
     * También calcula la duración en horas.
     */
    private fun loadSleepHistory() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            _uiState.update { it.copy(errorMessage = "Usuario no autenticado para cargar historial.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingHistory = true) }
            Firebase.firestore.collection("sleep_entries")
                .whereEqualTo("userId", user.uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnSuccessListener { documents ->
                    val history = documents.mapNotNull { doc ->
                        // Lógica para manejar el cálculo de duración, incluyendo el cruce de medianoche
                        val start = LocalTime.parse(doc.getString("startTime"))
                        val end = LocalTime.parse(doc.getString("endTime"))
                        val durationMinutes = if (end.isAfter(start)) {
                            ChronoUnit.MINUTES.between(start, end)
                        } else {
                            val minutesUntilMidnight = ChronoUnit.MINUTES.between(start, LocalTime.MAX) + 1
                            val minutesAfterMidnight = ChronoUnit.MINUTES.between(LocalTime.MIN, end)
                            (minutesUntilMidnight + minutesAfterMidnight).coerceAtLeast(0)
                        }
                        val duration = durationMinutes / 60.0

                        SleepEntry(
                            id = doc.id,
                            date = doc.getString("date") ?: "",
                            startTime = doc.getString("startTime") ?: "",
                            endTime = doc.getString("endTime") ?: "",
                            quality = (doc.getLong("quality") ?: 0).toInt(),
                            durationHours = duration
                        )
                    }
                    _uiState.update { it.copy(sleepHistory = history, isLoadingHistory = false) }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoadingHistory = false) }
                    Log.e("SleepViewModel", "Error loading history", e)
                }
        }
    }

    // --- FUNCIÓN PARA GUARDAR ---
    /**
     * Guarda el registro de sueño actual (horas seleccionadas y calidad) en Firestore.
     *
     * Utiliza la fecha actual y guarda las horas como Strings. Recarga el historial tras el éxito.
     */
    fun saveSleepEntry() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser ?: return@launch
            _uiState.update { it.copy(isSaving = true) }

            val currentState = _uiState.value
            val sleepEntry = hashMapOf(
                "userId" to user.uid,
                "date" to LocalDate.now().toString(),
                "startTime" to currentState.startTime.toString(),
                "endTime" to currentState.endTime.toString(),
                "quality" to currentState.quality
            )

            Firebase.firestore.collection("sleep_entries")
                .add(sleepEntry)
                .addOnSuccessListener {
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                    loadSleepHistory() // Recarga el historial para mostrar el nuevo registro
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(isSaving = false, errorMessage = e.message) }
                }
        }
    }

    // --- FUNCIÓN PARA ELIMINAR ---
    /**
     * Elimina un registro de sueño específico de Firestore.
     *
     * Recarga el historial tras la eliminación exitosa.
     *
     * @param entryId El ID del documento de registro de sueño a eliminar.
     */
    fun deleteSleepEntry(entryId: String) {
        viewModelScope.launch {
            Firebase.firestore.collection("sleep_entries").document(entryId)
                .delete()
                .addOnSuccessListener {
                    Log.d("SleepViewModel", "Document $entryId successfully deleted!")
                    loadSleepHistory()
                }
                .addOnFailureListener { e ->
                    Log.w("SleepViewModel", "Error deleting document", e)
                    _uiState.update { it.copy(errorMessage = "Error al eliminar el registro.") }
                }
        }
    }

    // --- FUNCIONES DE ESTADO ---
    /** Restablece el indicador de éxito de guardado ([isSaved]). */
    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }

    /** Limpia el mensaje de error de la UI. */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}