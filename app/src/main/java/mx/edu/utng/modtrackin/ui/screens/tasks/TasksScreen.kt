package mx.edu.utng.modtrackin.ui.screens.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Task
import mx.edu.utng.modtrackin.ui.viewmodel.TaskViewModel
import java.time.Instant
import java.time.ZoneId

/**
 * Punto de entrada principal para la gestión de tareas pendientes.
 *
 * Actúa como un switch que decide si mostrar la lista de tareas ([TasksListScreen]) o
 * la pantalla de edición ([TaskEditorScreen]) basándose en el estado [isEditorOpen]
 * del [TaskViewModel].
 *
 * @param navController El controlador de navegación para volver a la pantalla anterior.
 * @param taskViewModel El ViewModel que gestiona el estado y la lógica de las tareas.
 */
@Composable
fun TasksScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = viewModel()
) {
    val uiState = taskViewModel.uiState

    if (uiState.isEditorOpen) {
        TaskEditorScreen(taskViewModel = taskViewModel)
    } else {
        TasksListScreen(
            uiState = uiState,
            onAddClick = { taskViewModel.openEditorNew() },
            onEditClick = { task -> taskViewModel.openEditorModify(task) },
            onToggleComplete = { task -> taskViewModel.toggleTaskCompletion(task) },
            onBack = { navController.popBackStack() }
        )
    }
}

/**
 * Pantalla que muestra el listado de todas las tareas del usuario.
 *
 * Muestra la lista en un [LazyColumn], maneja el estado de carga y el caso de lista vacía.
 * Incluye un Floating Action Button para agregar nuevas tareas.
 *
 * @param uiState El estado de la UI del TaskViewModel.
 * @param onAddClick Lambda para iniciar el proceso de agregar una nueva tarea.
 * @param onEditClick Lambda que se invoca al hacer clic en una tarjeta de tarea para editarla.
 * @param onToggleComplete Lambda para cambiar el estado de completado de una tarea.
 * @param onBack Lambda para volver a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    uiState: mx.edu.utng.modtrackin.ui.viewmodel.TaskUiState,
    onAddClick: () -> Unit,
    onEditClick: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Tareas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Tarea")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            if (uiState.isLoading && uiState.taskList.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            } else if (uiState.taskList.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📭", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No hay tareas pendientes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = uiState.taskList, key = { it.id.ifEmpty { it.hashCode() } }) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { onToggleComplete(task) },
                            onClick = { onEditClick(task) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente Composable que representa una tarjeta de tarea individual en la lista.
 *
 * Muestra el estado de completado, título, descripción, prioridad y fecha de vencimiento.
 * Es clickeable para abrir la edición.
 *
 * @param task El objeto [Task] cuyos datos se van a mostrar.
 * @param onToggleComplete Lambda para cambiar el estado `isCompleted` de la tarea.
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta para abrir el editor.
 */
@Composable
fun TaskCard(task: Task, onToggleComplete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.Top) {
            IconButton(
                onClick = onToggleComplete,
                modifier = Modifier.size(24.dp).offset(y = (-2).dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Completar",
                    tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                )
                if (task.description.isNotBlank()) {
                    Text(text = task.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Chip de Prioridad
                    SuggestionChip(
                        onClick = {},
                        label = { Text(task.priority, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color.Transparent)
                    )
                    // Chip de Fecha de Vencimiento
                    if (task.dueDate.isNotBlank()) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("📅 ${task.dueDate}", fontSize = 10.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = null
                        )
                    }
                }
            }
        }
    }
}

/**
 * Pantalla de edición de tareas, utilizada tanto para crear una nueva como para modificar una existente.
 *
 * Contiene campos de texto para título, descripción, un selector de fecha y un selector de prioridad.
 * Proporciona acciones de guardar, cancelar y eliminar en la AppBar.
 *
 * @param taskViewModel El ViewModel que gestiona el estado de edición y las acciones de la tarea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(taskViewModel: TaskViewModel) {
    val uiState = taskViewModel.uiState
    val scrollState = rememberScrollState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Diálogo de selección de fecha
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convierte milisegundos a fecha en formato UTC (LocalDate)
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                        taskViewModel.onDueDateChange(date.toString())
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.id.isEmpty()) "Nueva Tarea" else "Editar Tarea", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { taskViewModel.closeEditor() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (uiState.id.isNotEmpty()) {
                        IconButton(onClick = { taskViewModel.deleteTask() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF9A9A))
                        }
                    }
                    IconButton(onClick = { taskViewModel.saveTask() }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val inputColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )

            OutlinedTextField(
                value = uiState.title, onValueChange = { taskViewModel.onTitleChange(it) },
                label = { Text("Título") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                colors = inputColors, shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = uiState.description, onValueChange = { taskViewModel.onDescriptionChange(it) },
                label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(120.dp), maxLines = 5,
                colors = inputColors, shape = RoundedCornerShape(12.dp)
            )

            // Campo de Fecha de Vencimiento con botón de DatePicker
            OutlinedTextField(
                value = uiState.dueDate,
                onValueChange = { taskViewModel.onDueDateChange(it) },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = inputColors,
                shape = RoundedCornerShape(12.dp),
                readOnly = true // Se usa el DatePicker para establecer el valor
            )

            Text("Prioridad", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
            // Chips para selección de prioridad
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Alta", "Media", "Baja").forEach { priority ->
                    FilterChip(
                        selected = uiState.priority == priority,
                        onClick = { taskViewModel.onPriorityChange(priority) },
                        label = { Text(priority) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = MaterialTheme.colorScheme.outline,
                            selected = uiState.priority == priority,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            enabled = true
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { taskViewModel.saveTask() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(if (uiState.id.isEmpty()) "Guardar Tarea" else "Actualizar Tarea", fontWeight = FontWeight.Bold)
            }
        }
    }
}