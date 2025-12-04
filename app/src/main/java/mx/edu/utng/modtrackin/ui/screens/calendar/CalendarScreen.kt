package mx.edu.utng.modtrackin.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Task
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Pantalla principal del calendario.
 *
 * Muestra una vista de calendario mensual y una lista de tareas pendientes [Task] asociadas
 * a la fecha seleccionada por el usuario.
 *
 * @param navController El controlador de navegación para cambiar entre pantallas.
 * @param taskViewModel El ViewModel encargado de gestionar y proporcionar los datos de las tareas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = viewModel()
) {
    val tasks = taskViewModel.uiState.taskList


    var currentMonth by remember { mutableStateOf(YearMonth.now()) }


    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Calendario", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoutes.HOME_SCREEN) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            MonthHeader(
                currentMonth = currentMonth,
                onPreviousClick = { currentMonth = currentMonth.minusMonths(1) },
                onNextClick = { currentMonth = currentMonth.plusMonths(1) }
            )


            DaysOfWeekHeader()


            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                tasks = tasks,
                onDateSelected = { date -> selectedDate = date }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)


            Text(
                text = "Tareas para el ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM"))}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            TaskListForDay(selectedDate = selectedDate, tasks = tasks)
        }
    }
}

/**
 * Componente Composable que muestra la navegación entre meses y el nombre del mes actual.
 *
 * @param currentMonth El objeto [YearMonth] que representa el mes actualmente visible.
 * @param onPreviousClick Función lambda a ejecutar cuando se presiona el botón para ir al mes anterior.
 * @param onNextClick Función lambda a ejecutar cuando se presiona el botón para ir al mes siguiente.
 */
@Composable
fun MonthHeader(
    currentMonth: YearMonth,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Anterior", tint = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = onNextClick) {
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Siguiente", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Componente Composable que muestra las abreviaturas de los días de la semana (DOM, LUN, etc.)
 * en la cabecera del calendario.
 */
@Composable
fun DaysOfWeekHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        val days = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

/**
 * Componente Composable que dibuja la cuadrícula del calendario con los días del mes actual.
 *
 * Muestra visualmente la fecha seleccionada, la fecha actual y marca los días que contienen tareas pendientes.
 *
 * @param currentMonth El objeto [YearMonth] que define qué mes se está mostrando.
 * @param selectedDate La [LocalDate] actualmente seleccionada por el usuario.
 * @param tasks La lista de todas las [Task] del usuario, utilizadas para marcar días con tareas.
 * @param onDateSelected Función lambda que se ejecuta cuando el usuario hace clic en una fecha.
 */
@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    tasks: List<Task>,
    onDateSelected: (LocalDate) -> Unit
) {

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)

    // Determina el desplazamiento inicial (offset) para colocar el primer día en la columna correcta.
    val startDayOffset = firstDayOfMonth.dayOfWeek.value % 7

    val totalCells = daysInMonth + startDayOffset

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.padding(horizontal = 8.dp).height(320.dp) // Altura fija para el calendario
    ) {

        // Celdas vacías para el espacio de los días de la semana anteriores al inicio del mes
        items(startDayOffset) {
            Box(modifier = Modifier.size(40.dp))
        }


        // Celdas para cada día del mes
        items(daysInMonth) { dayIndex ->
            val dayNum = dayIndex + 1
            val date = currentMonth.atDay(dayNum)
            val isSelected = date == selectedDate
            val isToday = date == LocalDate.now()

            // Verificamos si hay tareas en esta fecha
            val hasTasks = tasks.any { it.dueDate == date.toString() && !it.isCompleted }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(
                        when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            isToday -> MaterialTheme.colorScheme.surfaceVariant
                            else -> Color.Transparent
                        }
                    )
                    .clickable { onDateSelected(date) }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = dayNum.toString(),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                    )

                    // Puntito si hay tareas pendientes
                    if (hasTasks) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente Composable que muestra una lista deslizable de [Task] filtradas
 * por la fecha seleccionada.
 *
 * @param selectedDate La [LocalDate] para la cual se deben mostrar las tareas.
 * @param tasks La lista completa de [Task] del usuario.
 */
@Composable
fun TaskListForDay(
    selectedDate: LocalDate,
    tasks: List<Task>
) {
    val dayTasks = tasks.filter { it.dueDate == selectedDate.toString() }

    if (dayTasks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay tareas para este día", color = Color.Gray)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(220.dp)
        ) {
            items(dayTasks) { task ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = task.title,
                                fontWeight = FontWeight.Bold,
                                color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            )
                            if (task.description.isNotEmpty()) {
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = task.priority,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}