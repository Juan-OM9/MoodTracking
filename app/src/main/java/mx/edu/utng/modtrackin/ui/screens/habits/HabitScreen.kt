package mx.edu.utng.modtrackin.ui.screens.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Habit
import mx.edu.utng.modtrackin.ui.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Clase de datos que define una opción de categoría para un hábito.
 *
 * @property name El nombre descriptivo de la categoría (ej. "Ejercicio").
 * @property icon El icono de Compose Material asociado a la categoría.
 * @property color El color utilizado para la representación visual de la categoría.
 */
data class HabitCategoryOption(val name: String, val icon: ImageVector, val color: Color)

/**
 * Lista predefinida de categorías disponibles para clasificar los hábitos.
 */
val categories = listOf(
    HabitCategoryOption("Ejercicio", Icons.Default.FitnessCenter, Color(0xFF42A5F5)),
    HabitCategoryOption("Estudiar", Icons.Default.School, Color(0xFFAB47BC)),
    HabitCategoryOption("Comer sano", Icons.Default.Restaurant, Color(0xFF66BB6A)),
    HabitCategoryOption("Leer", Icons.Default.MenuBook, Color(0xFFFFCA28)),
    HabitCategoryOption("Descanso", Icons.Default.SelfImprovement, Color(0xFF26C6DA)),
    HabitCategoryOption("Otro", Icons.Default.Star, Color.Gray)
)

/**
 * Punto de entrada principal para la pantalla de gestión de hábitos.
 *
 * Actúa como un switch que decide si mostrar la vista principal de seguimiento ([HabitDashboardScreen])
 * o la vista de creación/edición ([HabitEditorScreen]) basándose en el estado del ViewModel.
 *
 * @param navController El controlador de navegación para cambiar de pantallas.
 * @param viewModel El ViewModel que gestiona el estado y la lógica de los hábitos.
 */
@Composable
fun HabitScreen(
    navController: NavController,
    viewModel: HabitViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    if (uiState.isEditorOpen) {
        HabitEditorScreen(viewModel)
    } else {
        HabitDashboardScreen(
            habits = uiState.habitList,
            selectedDate = uiState.selectedDate,
            onDateChange = { viewModel.changeDate(it) },
            onAddHabit = { viewModel.openEditorNew() },
            onEditHabit = { viewModel.openEditorModify(it) },
            onAddMinutes = { habit, mins -> viewModel.addMinutesToHabit(habit, mins) },
            onBack = { navController.popBackStack() }
        )
    }
}

// --- PANTALLA PRINCIPAL ---
/**
 * Pantalla de seguimiento y control diario de hábitos.
 *
 * Muestra un selector de fecha, un resumen del total de minutos dedicados y una lista
 * de tarjetas ([HabitControlCard]) para registrar el progreso de cada hábito.
 *
 * @param habits La lista de todos los hábitos del usuario.
 * @param selectedDate La fecha actual seleccionada para registrar el progreso.
 * @param onDateChange Lambda para cambiar la fecha seleccionada.
 * @param onAddHabit Lambda para abrir el editor para un nuevo hábito.
 * @param onEditHabit Lambda para abrir el editor para modificar un hábito existente.
 * @param onAddMinutes Lambda para agregar minutos de progreso a un hábito específico.
 * @param onBack Lambda para navegar hacia atrás.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDashboardScreen(
    habits: List<Habit>,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onAddHabit: () -> Unit,
    onEditHabit: (Habit) -> Unit,
    onAddMinutes: (Habit, Int) -> Unit,
    onBack: () -> Unit
) {
    val totalMinutesToday = habits.sumOf { it.history[selectedDate.toString()] ?: 0 }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Hábitos", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabit,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, "Nuevo") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            DateSelector(selectedDate, onDateChange)
            Spacer(modifier = Modifier.height(16.dp))

            // Resumen Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total de tiempo dedicado a tus habitos", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        text = formatMinutes(totalMinutesToday),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay hábitos. ¡Crea uno!", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = habits, key = { it.id }) { habit ->
                        HabitControlCard(
                            habit = habit,
                            selectedDate = selectedDate,
                            onEditClick = { onEditHabit(habit) },
                            onAddMinutes = { mins -> onAddMinutes(habit, mins) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente que permite navegar entre las fechas.
 *
 * Muestra la fecha seleccionada (o "Hoy") y proporciona botones para avanzar o retroceder un día.
 *
 * @param selectedDate La fecha actual seleccionada.
 * @param onDateChange Lambda para manejar el cambio de fecha.
 */
@Composable
fun DateSelector(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onDateChange(selectedDate.minusDays(1)) }) {
            Icon(Icons.Default.ArrowBackIosNew, null, tint = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = if (selectedDate == LocalDate.now()) "Hoy" else selectedDate.format(DateTimeFormatter.ofPattern("EEEE", Locale("es", "ES"))).replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(onClick = { onDateChange(selectedDate.plusDays(1)) }) {
            Icon(Icons.Default.ArrowForwardIos, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Tarjeta individual para visualizar el estado y registrar el progreso de un hábito específico.
 *
 * Muestra la categoría, el título, el progreso actual vs. la meta y un campo para añadir minutos.
 *
 * @param habit El objeto [Habit] a controlar.
 * @param selectedDate La fecha actual seleccionada para buscar el progreso en [habit.history].
 * @param onEditClick Lambda que se ejecuta al hacer clic en la tarjeta para abrir el editor.
 * @param onAddMinutes Lambda que se ejecuta al presionar "Agregar" para registrar el tiempo.
 */
@Composable
fun HabitControlCard(
    habit: Habit,
    selectedDate: LocalDate,
    onEditClick: () -> Unit,
    onAddMinutes: (Int) -> Unit
) {
    val dateKey = selectedDate.toString()
    val currentMinutes = habit.history[dateKey] ?: 0
    val catOption = categories.find { it.name == habit.category } ?: categories.last()

    var minutesToAdd by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().clickable { onEditClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = catOption.color,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(catOption.icon, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(habit.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    // Muestra: "Llevas: Xh Ym / Meta: Zh"
                    Text(
                        "Llevas: ${formatMinutes(currentMinutes)} / Meta: ${formatMinutes(habit.dailyGoal)}",
                        fontSize = 12.sp,
                        color = if (currentMinutes >= habit.dailyGoal) catOption.color else Color.Gray,
                        fontWeight = if (currentMinutes >= habit.dailyGoal) FontWeight.Bold else FontWeight.Normal
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECCIÓN DE AGREGAR TIEMPO (CAMPO DE TEXTO) ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = minutesToAdd,
                    onValueChange = { if (it.all { char -> char.isDigit() }) minutesToAdd = it },
                    placeholder = { Text("Minutos") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = catOption.color,
                        cursorColor = catOption.color,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        val mins = minutesToAdd.toIntOrNull() ?: 0
                        if (mins > 0) {
                            onAddMinutes(mins)
                            minutesToAdd = "" // Limpiar campo
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = catOption.color),
                    shape = RoundedCornerShape(8.dp),
                    enabled = minutesToAdd.isNotEmpty()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
        }
    }
}

/**
 * Función de utilidad que convierte un total de minutos en un formato legible de horas y minutos.
 *
 * @param minutes La cantidad total de minutos.
 * @return Una cadena de texto formateada (ej. "1h 30m" o "45m").
 */
fun formatMinutes(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

// --- PANTALLA 2: EDITOR ---
/**
 * Pantalla para crear un nuevo hábito o editar uno existente.
 *
 * Permite al usuario seleccionar la categoría, ingresar el título, la descripción y la meta diaria.
 *
 * @param viewModel El ViewModel utilizado para gestionar la lógica del editor, incluyendo
 * la gestión de estado de los campos de texto y las acciones de guardar/eliminar/cerrar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditorScreen(viewModel: HabitViewModel) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.id.isEmpty()) "Nuevo Hábito" else "Editar Hábito", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { viewModel.closeEditor() }) { Icon(Icons.Default.ArrowBack, "Volver") } },
                actions = {
                    if (uiState.id.isNotEmpty()) {
                        IconButton(onClick = { viewModel.deleteHabit() }) { Icon(Icons.Default.Delete, "Borrar", tint = Color(0xFFEF9A9A)) }
                    }
                    IconButton(onClick = { viewModel.saveHabit() }) { Icon(Icons.Default.Check, "Guardar") }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground, navigationIconContentColor = MaterialTheme.colorScheme.primary, actionIconContentColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(padding).padding(16.dp)
        ) {
            Text("Categoría", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                items(categories) { category ->
                    FilterChip(
                        selected = uiState.category == category.name,
                        onClick = { viewModel.onCategorySelected(category.name) },
                        label = { Text(category.name) },
                        leadingIcon = { Icon(category.icon, null, modifier = Modifier.size(18.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = category.color.copy(alpha = 0.3f),
                            selectedLabelColor = category.color,
                            selectedLeadingIconColor = category.color
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )

            OutlinedTextField(
                value = uiState.title, onValueChange = { viewModel.onTitleChange(it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = colors, shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.targetMinutesInput, onValueChange = { viewModel.onDurationChange(it) }, label = { Text("Meta diaria (minutos)") }, modifier = Modifier.fillMaxWidth(), colors = colors, shape = RoundedCornerShape(12.dp), placeholder = { Text("Ej: 60") }
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.description, onValueChange = { viewModel.onDescriptionChange(it) }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = colors, shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.saveHabit() }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}