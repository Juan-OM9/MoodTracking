package mx.edu.utng.modtrackin.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Note
import mx.edu.utng.modtrackin.ui.viewmodel.NotesViewModel

/**
 * Punto de entrada principal para la gestión de notas.
 *
 * Actúa como un switch que decide si mostrar la lista de notas ([NotesListScreen]) o
 * la pantalla de edición ([NoteEditorScreen]) basándose en el estado [isEditorOpen]
 * del [NotesViewModel].
 *
 * @param navController El controlador de navegación.
 * @param viewModel El ViewModel que gestiona el estado y la lógica de las notas.
 */
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    if (uiState.isEditorOpen) {
        NoteEditorScreen(
            note = uiState.currentNote,
            onTitleChange = { viewModel.updateTitle(it) },
            onContentChange = { viewModel.updateContent(it) },
            onSave = { viewModel.saveCurrentNote() },
            onCancel = { viewModel.closeEditor() },
            onDelete = { viewModel.deleteCurrentNote() }
        )
    } else {
        NotesListScreen(
            notes = uiState.notesList,
            isLoading = uiState.isLoading,
            onAddClick = { viewModel.openEditorNew() },
            onNoteClick = { note -> viewModel.openEditorModify(note) },
            onBack = { navController.popBackStack() }
        )
    }
}

/**
 * Pantalla que muestra el listado de todas las notas del usuario.
 *
 * Muestra la lista en un [LazyColumn], maneja el estado de carga y el caso de lista vacía.
 * Incluye un Floating Action Button para agregar nuevas notas.
 *
 * @param notes La lista de [Note] recuperadas del ViewModel.
 * @param isLoading Indica si el repositorio está actualmente cargando los datos.
 * @param onAddClick Lambda para iniciar el proceso de agregar una nueva nota.
 * @param onNoteClick Lambda que se invoca al hacer clic en una tarjeta de nota para editarla.
 * @param onBack Lambda para volver a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    notes: List<Note>,
    isLoading: Boolean,
    onAddClick: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Notas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Nota", tint = Color.Black)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading && notes.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (notes.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🗒️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No hay notas. ¡Crea una!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = notes, key = { it.id }) { note ->
                        NoteCard(note = note, onClick = { onNoteClick(note) })
                    }
                }
            }
        }
    }
}

/**
 * Componente Composable que representa una vista previa de una [Note] individual en la lista.
 *
 * Muestra el título y un resumen del contenido, y es clickeable para abrir la edición.
 *
 * @param note El objeto [Note] cuyos datos se van a mostrar.
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta.
 */
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (note.title.isNotEmpty()) note.title else "(Sin título)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (note.content.isNotEmpty()) note.content else "...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Pantalla de edición de una nota, utilizada tanto para crear una nueva como para modificar una existente.
 *
 * Contiene campos de texto para el título y el contenido. Proporciona acciones de guardar,
 * cancelar y, si la nota existe, eliminar en la AppBar.
 *
 * @param note El objeto [Note] actual que se está editando (conectado al estado del ViewModel).
 * @param onTitleChange Lambda para actualizar el título.
 * @param onContentChange Lambda para actualizar el contenido.
 * @param onSave Lambda para ejecutar la acción de guardar la nota.
 * @param onCancel Lambda para salir del editor sin guardar cambios.
 * @param onDelete Lambda para eliminar la nota actual (solo si [note.id] no está vacío).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    note: Note,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (note.id.isEmpty()) "Nueva Nota" else "Editar Nota") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
                    }
                },
                actions = {

                    if (note.id.isNotEmpty()) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF9A9A))
                        }
                    }
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )

            OutlinedTextField(
                value = note.title,
                onValueChange = onTitleChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = note.content,
                onValueChange = onContentChange,
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = fieldColors,
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            )
        }
    }
}