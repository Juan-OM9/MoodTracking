package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.EmotionEntry
import mx.edu.utng.modtrackin.data.repository.EmotionRepository
import mx.edu.utng.modtrackin.ui.screens.emotions.Emocion

/**
 * Representa el estado actual de la UI de la pantalla de gestión emocional.
 *
 * @property isLoading Indica si alguna operación de datos (Firebase) está en curso.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el repositorio.
 * @property currentScreen Controla el paso actual en el flujo de registro (1: Principal, 2: Adjetivos, 3: Guardado, 4: Notas, 5: Historial).
 * @property selectedEmotion La [Emocion] seleccionada por el usuario en el Paso 1.
 * @property selectedAdjective El adjetivo seleccionado para matizar la emoción en el Paso 2.
 * @property dailyNote La nota o descripción escrita por el usuario para el registro.
 * @property todayEntry Contiene el [EmotionEntry] del día de hoy, si ya existe un registro.
 * @property history La lista completa de [EmotionEntry] del historial del usuario.
 */
data class EmotionUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentScreen: Int = 1,
    val selectedEmotion: Emocion? = null,
    val selectedAdjective: String = "",
    val dailyNote: String = "",
    val todayEntry: EmotionEntry? = null,
    val history: List<EmotionEntry> = emptyList()
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Agenda Emocional.
 *
 * Es responsable de:
 * 1. Monitorear el estado de autenticación de Firebase para cargar/limpiar datos.
 * 2. Gestionar la navegación del flujo de registro de 4 pasos.
 * 3. Interactuar con [EmotionRepository] para guardar el registro diario y obtener el historial.
 */
class EmotionViewModel : ViewModel() {

    var uiState by mutableStateOf(EmotionUiState())
        private set

    private val repository = EmotionRepository()

    /**
     * Detector de cambios de autenticación. Llama a [loadDataForUser] si el usuario
     * inicia sesión y limpia el estado si cierra sesión.
     */
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser

        if (user != null) {
            // Usuario detectado: Cargamos SUS datos
            loadDataForUser()
        } else {
            // Usuario se fue: Limpieza total
            uiState = EmotionUiState()
        }
    }

    init {
        // Activamos el detector de autenticación al inicializar el ViewModel.
        Firebase.auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        // Eliminamos el listener para evitar fugas de memoria.
        Firebase.auth.removeAuthStateListener(authListener)
    }

    /**
     * Inicia la carga de datos del usuario autenticado: verifica el registro de hoy y trae el historial.
     */
    private fun loadDataForUser() {
        checkTodayEntry()
        fetchHistory()
    }

    // --- LISTAS Y LÓGICA DE NEGOCIO ESTÁTICA ---

    /** Lista de emociones básicas disponibles para la selección inicial. */
    val emocionesList = listOf(
        Emocion("alegre", "😄", "Alegre", Color(0xFFFFF176)),
        Emocion("neutral", "😐", "Neutral", Color(0xFFE0E0E0)),
        Emocion("triste", "😢", "Triste", Color(0xFF90CAF9)),
        Emocion("molesto", "😠", "Molesto", Color(0xFFEF9A9A)),
        Emocion("nervioso", "😰", "Nervioso", Color(0xFFCE93D8))
    )

    /** Mapa que define los adjetivos disponibles para cada ID de emoción. */
    val adjetivosMap = mapOf(
        "alegre" to listOf("Contento", "Entusiasmado", "Satisfecho", "Optimista", "Divertido", "Eufórico"),
        "neutral" to listOf("Indiferente", "Sereno", "Tranquilo", "Impasible", "Objetivo", "Despreocupado"),
        "triste" to listOf("Melancólico", "Desanimado", "Deprimido", "Nostálgico", "Afligido", "Desconsolado"),
        "molesto" to listOf("Irritado", "Frustrado", "Enfadado", "Furioso", "Fastidiado", "Resentido"),
        "nervioso" to listOf("Ansioso", "Inquieto", "Tenso", "Preocupado", "Temeroso", "Alterado")
    )

    // --- OPERACIONES DE DATOS ---

    /**
     * Verifica si el usuario ya tiene un registro emocional guardado para el día de hoy.
     *
     * Si existe, establece [todayEntry] y navega al paso 3 (Registro Guardado).
     * Si no existe, navega al paso 1 (Selección Principal).
     */
    fun checkTodayEntry() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.getTodayEmotion()
            result.onSuccess { entry ->
                if (entry != null) {
                    uiState = uiState.copy(isLoading = false, todayEntry = entry, currentScreen = 3)
                } else {
                    uiState = uiState.copy(isLoading = false, todayEntry = null, currentScreen = 1)
                }
            }
            result.onFailure { uiState = uiState.copy(isLoading = false, errorMessage = "Error al cargar datos") }
        }
    }

    /**
     * Guarda el registro emocional actual ([selectedEmotion], [selectedAdjective], [dailyNote])
     * en el repositorio.
     */
    fun saveEntry() {
        val emocion = uiState.selectedEmotion ?: return
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val newEntry = EmotionEntry(
                emotionId = emocion.id, emotionEmoji = emocion.emoji, emotionText = emocion.texto,
                adjective = uiState.selectedAdjective, note = uiState.dailyNote
            )
            val result = repository.saveEmotionEntry(newEntry)
            result.onSuccess {
                // Actualiza el estado y navega a la vista de guardado exitoso
                uiState = uiState.copy(isLoading = false, todayEntry = newEntry, currentScreen = 3)
                // Recarga el historial para que la lista esté actualizada
                fetchHistory()
            }
            result.onFailure { e -> uiState = uiState.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    /**
     * Obtiene el historial completo de registros emocionales del usuario y lo actualiza en [history].
     */
    fun fetchHistory() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.getHistory()
            result.onSuccess { list -> uiState = uiState.copy(isLoading = false, history = list) }
            result.onFailure { uiState = uiState.copy(isLoading = false, errorMessage = "Error al cargar historial") }
        }
    }

    // --- LÓGICA DE NAVEGACIÓN Y FLUJO ---

    /** Navega a la pantalla de Historial (Paso 5) y asegura que los datos estén cargados. */
    fun goToHistory() { fetchHistory(); uiState = uiState.copy(currentScreen = 5) }

    /**
     * Selecciona una emoción y avanza al paso de selección de adjetivos (Paso 2).
     * @param emocion El objeto [Emocion] seleccionado.
     */
    fun selectEmotion(emocion: Emocion) { uiState = uiState.copy(selectedEmotion = emocion, currentScreen = 2) }

    /**
     * Selecciona un adjetivo y avanza al paso de adición de notas (Paso 4).
     * @param adjetivo El adjetivo seleccionado.
     */
    fun selectAdjective(adjetivo: String) { uiState = uiState.copy(selectedAdjective = adjetivo, currentScreen = 4) }

    /**
     * Actualiza el valor de la nota diaria en el estado de la UI.
     * @param note El nuevo contenido de la nota.
     */
    fun updateNote(note: String) { uiState = uiState.copy(dailyNote = note) }

    /**
     * Navega al paso anterior dentro del flujo de registro, considerando las transiciones
     * desde el historial.
     */
    fun goBack() {
        val previousScreen = when (uiState.currentScreen) {
            5 -> if (uiState.todayEntry != null) 3 else 1 // Desde Historial, vuelve a Guardado o Principal
            4 -> 2 // Desde Notas, vuelve a Adjetivos
            2 -> 1 // Desde Adjetivos, vuelve a Principal
            else -> 1 // Caso predeterminado
        }
        uiState = uiState.copy(currentScreen = previousScreen)
    }

    /**
     * Reinicia completamente el flujo de registro, limpiando las selecciones temporales.
     * Se usa típicamente para comenzar un nuevo registro o editar el actual.
     */
    fun resetFlow() {
        uiState = uiState.copy(currentScreen = 1, selectedEmotion = null, selectedAdjective = "", dailyNote = "", todayEntry = null)
    }
}