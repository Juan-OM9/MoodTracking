package mx.edu.utng.modtrackin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.viewmodel.EmotionViewModel
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.NightsStay // Importación para el ícono de sueño

/**
 * Pantalla principal (Dashboard) de la aplicación.
 *
 * Muestra un saludo personalizado, opciones de navegación rápida a las funcionalidades
 * principales (Emociones, Tareas, Notas, Hábitos, Sueño, Calendario) y un menú para
 * cerrar la sesión del usuario.
 *
 * @param navController El controlador de navegación utilizado para transicionar a otras pantallas.
 * @param emotionViewModel El ViewModel para obtener el estado emocional actual del día
 * y mostrarlo en la tarjeta principal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    emotionViewModel: EmotionViewModel = viewModel()
) {
    val emotionState = emotionViewModel.uiState
    val todayEntry = emotionState.todayEntry

    var showMenu by remember { mutableStateOf(false) }
    val user = Firebase.auth.currentUser
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hola, ${user?.email?.split("@")?.get(0) ?: "Usuario"}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Vamos a ser productivos 🚀",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(45.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("👤", fontSize = 24.sp)
                                }
                            }
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            DropdownMenuItem(
                                text = { Text(user?.email ?: "Sin correo", color = MaterialTheme.colorScheme.onSurface) },
                                onClick = { },
                                leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.primary) },
                                enabled = false
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMenu = false
                                    Firebase.auth.signOut()
                                    navController.navigate(NavRoutes.LOGIN_SCREEN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                leadingIcon = { Icon(Icons.Default.ExitToApp, null, tint = MaterialTheme.colorScheme.error) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            val emotionGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFF9800), Color(0xFFFF5722))
            )

            // 1. TARJETA DE EMOCIONES (HERO)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable { navController.navigate(NavRoutes.EMOTIONS_SCREEN) }
                    .clip(RoundedCornerShape(24.dp)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().background(emotionGradient)) {
                    Row(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = todayEntry?.emotionEmoji ?: "🤔",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = "Estado de ánimo",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = todayEntry?.adjective ?: "Registrar ahora",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // --- 2. ACCESOS RÁPIDOS EN GRID (FILA 1) ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(160.dp)
            ) {
                // TAREAS
                ColorfulCard(
                    title = "Tareas",
                    icon = Icons.Default.CheckCircle,
                    gradient = Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1DE9B6))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.TASKS_SCREEN) }
                )

                // NOTAS
                ColorfulCard(
                    title = "Notas",
                    icon = Icons.Default.Edit,
                    gradient = Brush.linearGradient(listOf(Color(0xFF7B1FA2), Color(0xFFE040FB))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.NOTES_SCREEN) }
                )
            }

            // --- 3. ACCESOS RÁPIDOS EN GRID (FILA 2) ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(140.dp)
            ) {
                // HÁBITOS
                ColorfulCard(
                    title = "Hábitos",
                    icon = Icons.Default.Repeat,
                    gradient = Brush.linearGradient(listOf(Color(0xFFEF6C00), Color(0xFFFFB74D))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.HABITS_SCREEN) }
                )

                // SUEÑO (NUEVA FUNCIÓN)
                ColorfulCard(
                    title = "Sueño",
                    icon = Icons.Default.NightsStay,
                    gradient = Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF4A69A5))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.SLEEP_SCREEN) }
                )
            }

            // --- 4. CALENDARIO (ACCESO FULL WIDTH) ---
            ColorfulCard(
                title = "Calendario",
                icon = Icons.Default.DateRange,
                gradient = Brush.linearGradient(listOf(Color(0xFF1976D2), Color(0xFF2196F3))),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                onClick = { navController.navigate(NavRoutes.CALENDAR_SCREEN) },
                iconSize = 40.dp,
                isHorizontal = true
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- COMPONENTE COLORIDO (SIN CAMBIOS) ---
/**
 * Componente composable reutilizable para crear tarjetas de acceso rápido con gradientes de color.
 *
 * Muestra un icono y un título sobre un fondo con un [Brush] de gradiente.
 *
 * @param title El texto que se muestra en la tarjeta.
 * @param icon El icono [ImageVector] que se muestra en la tarjeta.
 * @param gradient El objeto [Brush] que define el color de fondo del gradiente.
 * @param modifier Modificador de Composable para aplicar a la tarjeta.
 * @param iconSize El tamaño del icono.
 * @param isHorizontal Si es `true`, el contenido se organiza horizontalmente (útil para tarjetas de ancho completo).
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta.
 */
@Composable
fun ColorfulCard(
    title: String,
    icon: ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier,
    iconSize: androidx.compose.ui.unit.Dp = 48.dp,
    isHorizontal: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            if (isHorizontal) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, null, modifier = Modifier.size(iconSize), tint = Color.White)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icon, null, modifier = Modifier.size(iconSize), tint = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}