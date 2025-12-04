package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.Note
import mx.edu.utng.modtrackin.data.repository.NotesRepository

/**
 * Representa el estado actual de la UI de la pantalla de Notas.
 *
 * @property notesList La lista de todas las [Note] del usuario.
 * @property isLoading Indica si el repositorio está cargando los datos iniciales.
 * @property isEditorOpen Indica si la pantalla de creación/edición de notas está visible.
 * @property currentNote El objeto [Note] que se está viendo o editando en el editor.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el repositorio.
 */
data class NotesUiState(
    val notesList: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isEditorOpen: Boolean = false,
    val currentNote: Note = Note(),
    val errorMessage: String? = null
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Notas.
 *
 * Es responsable de:
 * 1. Escuchar los cambios en tiempo real de la base de datos para mantener la lista actualizada.
 * 2. Gestionar la navegación entre la vista de lista y el editor.
 * 3. Manejar el estado y las acciones de edición, guardado y eliminación de una nota.
 */
class NotesViewModel : ViewModel() {
    var uiState by mutableStateOf(NotesUiState())
        private set

    private val repository = NotesRepository()

    init {
        startListening()
    }

    /**
     * Inicia la escucha en tiempo real de las notas del usuario en el repositorio.
     * La lista [notesList] se mantiene automáticamente sincronizada con la base de datos.
     */
    private fun startListening() {
        uiState = uiState.copy(isLoading = true)
        repository.listenToNotes { notes ->
            uiState = uiState.copy(notesList = notes, isLoading = false)
        }
    }

    /**
     * Recarga la lista de notas del repositorio sin utilizar el listener en tiempo real.
     * Se usa principalmente después de operaciones de guardado/eliminación para asegurar la consistencia.
     */
    private fun refreshNotes() {
        viewModelScope.launch {
            val result = repository.getAllNotes()
            result.onSuccess { notes ->
                uiState = uiState.copy(notesList = notes)
            }
        }
    }

    /**
     * Abre el editor para crear una nueva nota, inicializando [currentNote] a una instancia vacía.
     */
    fun openEditorNew() {
        uiState = uiState.copy(isEditorOpen = true, currentNote = Note(), errorMessage = null)
    }

    /**
     * Abre el editor para modificar una nota existente.
     * @param note El objeto [Note] a cargar y modificar.
     */
    fun openEditorModify(note: Note) {
        uiState = uiState.copy(isEditorOpen = true, currentNote = note, errorMessage = null)
    }

    /**
     * Cierra la pantalla del editor y regresa a la vista de lista.
     */
    fun closeEditor() {
        uiState = uiState.copy(isEditorOpen = false, errorMessage = null)
    }

    /** Actualiza el título de la nota que se está editando en [currentNote]. */
    fun updateTitle(t: String) { uiState = uiState.copy(currentNote = uiState.currentNote.copy(title = t)) }

    /** Actualiza el contenido de la nota que se está editando en [currentNote]. */
    fun updateContent(c: String) { uiState = uiState.copy(currentNote = uiState.currentNote.copy(content = c)) }

    /**
     * Guarda (crea o actualiza) la [currentNote] en el repositorio.
     * Si es exitoso, cierra el editor y recarga la lista.
     */
    fun saveCurrentNote() {
        viewModelScope.launch {
            val result = repository.saveNote(uiState.currentNote)
            if (result.isSuccess) {
                // Aunque el listener se encargaría de la actualización, se llama a refresh para asegurar.
                refreshNotes()
                closeEditor()
            } else {
                uiState = uiState.copy(errorMessage = "Error al guardar")
            }
        }
    }

    /**
     * Elimina la [currentNote] del repositorio, siempre y cuando su ID no esté vacío.
     * Si es exitoso, cierra el editor y recarga la lista.
     */
    fun deleteCurrentNote() {
        if (uiState.currentNote.id.isNotEmpty()) {
            viewModelScope.launch {
                val result = repository.deleteNote(uiState.currentNote.id)
                if (result.isSuccess) {
                    // Aunque el listener se encargaría de la actualización, se llama a refresh para asegurar.
                    refreshNotes()
                    closeEditor()
                } else {
                    uiState = uiState.copy(errorMessage = "Error al eliminar")
                }
            }
        }
    }
}