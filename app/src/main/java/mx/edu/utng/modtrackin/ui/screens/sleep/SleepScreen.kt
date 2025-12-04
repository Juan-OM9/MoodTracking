package mx.edu.utng.modtrackin.ui.screens.sleep

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import mx.edu.utng.modtrackin.ui.viewmodel.SleepEntry
import mx.edu.utng.modtrackin.ui.viewmodel.SleepViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

/**
 * Pantalla principal para el registro y visualización del sueño.
 *
 * Permite al usuario seleccionar las horas de inicio y fin del sueño, calificar la calidad
 * y ver el historial de registros. Gestiona la lógica de guardar, eliminar y la visualización
 * de errores mediante [SleepViewModel].
 *
 * @param navController El controlador de navegación para volver a la pantalla anterior.
 * @param sleepViewModel El ViewModel que gestiona el estado y la lógica de los registros de sueño.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
    navController: NavController,
    sleepViewModel: SleepViewModel = viewModel()
) {
    val uiState by sleepViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Estado para controlar qué registro se va a eliminar
    var entryToDelete by remember { mutableStateOf<SleepEntry?>(null) }

    // Efecto para mostrar el Toast al guardar exitosamente.
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            Toast.makeText(context, "Registro guardado con éxito", Toast.LENGTH_SHORT).show()
            delay(500)
            sleepViewModel.resetSaveState()
        }
    }

    // Efecto para mostrar Toast de errores.
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, "Error: ${uiState.errorMessage}", Toast.LENGTH_LONG).show()
            sleepViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Sueño", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                RegistroActual(
                    uiState = uiState,
                    onStartTimeUpdate = { sleepViewModel.updateStartTime(it) },
                    onEndTimeUpdate = { sleepViewModel.updateEndTime(it) },
                    onQualityUpdate = { sleepViewModel.updateQuality(it) },
                    onSave = { sleepViewModel.saveSleepEntry() }
                )
            }

            item {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    Text(
                        "Historial de Sueño",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uiState.isLoadingHistory) {
                item { CircularProgressIndicator() }
            } else if (uiState.sleepHistory.isEmpty()) {
                item {
                    Text(
                        "No hay registros de sueño todavía.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(uiState.sleepHistory, key = { it.id }) { entry ->
                    SleepHistoryItem(
                        entry = entry,
                        // Al hacer clic, se abre el diálogo de confirmación
                        onDeleteClick = { entryToDelete = entry }
                    )
                }
            }
        }
    }

    // --- DIÁLOGO DE CONFIRMACIÓN DE BORRADO ---
    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este registro de sueño? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Llama a la función de eliminar del ViewModel
                        sleepViewModel.deleteSleepEntry(entryToDelete!!.id)
                        entryToDelete = null // Cierra el diálogo
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { entryToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Componente Composable que permite registrar las horas de sueño y la calidad.
 *
 * Muestra la duración calculada, selectores de hora y el selector de calidad del sueño.
 *
 * @param uiState El estado de la UI del sueño, conteniendo las horas y calidad actuales.
 * @param onStartTimeUpdate Lambda para actualizar la hora de inicio del sueño.
 * @param onEndTimeUpdate Lambda para actualizar la hora de fin del sueño.
 * @param onQualityUpdate Lambda para actualizar la calificación de calidad.
 * @param onSave Lambda para ejecutar la acción de guardar el registro.
 */
@Composable
fun RegistroActual(
    uiState: mx.edu.utng.modtrackin.ui.viewmodel.SleepUiState,
    onStartTimeUpdate: (LocalTime) -> Unit,
    onEndTimeUpdate: (LocalTime) -> Unit,
    onQualityUpdate: (Int) -> Unit,
    onSave: () -> Unit
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Lógica para calcular la duración, manejando el cruce de medianoche
    val durationMinutes = if (uiState.endTime.isAfter(uiState.startTime)) {
        ChronoUnit.MINUTES.between(uiState.startTime, uiState.endTime)
    } else {
        val minutesUntilMidnight = ChronoUnit.MINUTES.between(uiState.startTime, LocalTime.MAX) + 1
        val minutesAfterMidnight = ChronoUnit.MINUTES.between(LocalTime.MIN, uiState.endTime)
        (minutesUntilMidnight + minutesAfterMidnight).coerceAtLeast(0)
    }
    val durationHours = durationMinutes / 60.0
    val durationText = String.format("%.1f", durationHours)

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Duración en tiempo real ---
        Text("Duración Total", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "$durationText hrs",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Selectores de hora ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TimePickerCard(
                modifier = Modifier.weight(1f),
                label = "Hora de Dormir",
                time = uiState.startTime,
                icon = { Icon(Icons.Default.NightsStay, null, tint = MaterialTheme.colorScheme.primary) },
                onClick = { showStartTimePicker = true }
            )
            TimePickerCard(
                modifier = Modifier.weight(1f),
                label = "Hora de Despertar",
                time = uiState.endTime,
                icon = { Icon(Icons.Default.WbSunny, null, tint = MaterialTheme.colorScheme.secondary) },
                onClick = { showEndTimePicker = true }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Selector de calidad ---
        SleepQualitySelector(
            quality = uiState.quality,
            onQualityChange = onQualityUpdate
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botón de guardar ---
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isSaving,
            shape = RoundedCornerShape(16.dp)
        ) {
            AnimatedVisibility(visible = uiState.isSaving) {
                CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            }
            AnimatedVisibility(visible = !uiState.isSaving) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Registro", fontSize = 16.sp)
                }
            }
        }
    }

    // Diálogos de selección de hora
    if (showStartTimePicker) {
        SleepTimePickerDialog(
            initialTime = uiState.startTime,
            onDismiss = { showStartTimePicker = false },
            onConfirm = { newTime ->
                onStartTimeUpdate(newTime)
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        SleepTimePickerDialog(
            initialTime = uiState.endTime,
            onDismiss = { showEndTimePicker = false },
            onConfirm = { newTime ->
                onEndTimeUpdate(newTime)
                showEndTimePicker = false
            }
        )
    }
}

/**
 * Componente Composable para mostrar un elemento individual del historial de sueño.
 *
 * Muestra la fecha, el rango de horas, la duración y la calidad del sueño, e incluye
 * un botón para eliminar el registro.
 *
 * @param entry El objeto [SleepEntry] con los datos del registro.
 * @param onDeleteClick Lambda que se ejecuta al hacer clic en el icono de eliminar.
 */
@Composable
fun SleepHistoryItem(
    entry: SleepEntry,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = LocalDate.parse(entry.date).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${entry.startTime} - ${entry.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format("%.1f", entry.durationHours)}h",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                val qualityEmojis = listOf("😠", "😟", "😐", "😊", "🤩")
                if (entry.quality in 1..qualityEmojis.size) {
                    Text(
                        text = qualityEmojis[entry.quality - 1],
                        fontSize = 20.sp
                    )
                }
            }
            // --- BOTÓN DE ELIMINAR ---
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar registro",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Componente Composable de tarjeta reutilizable para mostrar y seleccionar una hora.
 *
 * @param modifier Modificador de Composable.
 * @param label El título del selector (ej. "Hora de Dormir").
 * @param time La hora actual seleccionada.
 * @param icon Un composable que dibuja el icono asociado.
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta para abrir el selector de hora.
 */
@Composable
fun TimePickerCard(
    modifier: Modifier = Modifier,
    label: String,
    time: LocalTime,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(
                text = time.format(timeFormatter),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Componente Composable para seleccionar la calidad del sueño mediante una escala de emojis (1 a 5).
 *
 * @param quality El valor de calidad actualmente seleccionado.
 * @param onQualityChange Lambda que se invoca con el nuevo valor de calidad seleccionado (1-5).
 */
@Composable
fun SleepQualitySelector(
    quality: Int,
    onQualityChange: (Int) -> Unit
) {
    val qualityEmojis = listOf("😠", "😟", "😐", "😊", "🤩")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Calidad del Sueño",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            qualityEmojis.forEachIndexed { index, emoji ->
                val actualQuality = index + 1
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (actualQuality == quality) MaterialTheme.colorScheme.primaryContainer
                            else Color.Transparent,
                            CircleShape
                        )
                        .clickable { onQualityChange(actualQuality) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 28.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

/**
 * Diálogo que envuelve el [TimePicker] de Material 3 para la selección de una hora específica.
 *
 * @param initialTime La hora inicial mostrada en el selector.
 * @param onDismiss Lambda que se ejecuta al cerrar el diálogo sin confirmar.
 * @param onConfirm Lambda que se ejecuta al confirmar la selección, devolviendo la nueva [LocalTime].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimePickerDialog(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(LocalTime.of(timePickerState.hour, timePickerState.minute))
            }) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}