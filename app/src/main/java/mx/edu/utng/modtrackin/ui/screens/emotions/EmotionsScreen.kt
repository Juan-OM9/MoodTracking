package mx.edu.utng.modtrackin.ui.screens.emotions

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.EmotionEntry
import mx.edu.utng.modtrackin.ui.viewmodel.EmotionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Clase de datos que define una emoción básica disponible para selección.
 *
 * @property id El identificador único de la emoción para la lógica interna (e.g., "alegre").
 * @property emoji El carácter emoji asociado a la emoción.
 * @property texto El nombre descriptivo de la emoción.
 * @property color El color asociado a la emoción para la representación visual.
 */
data class Emocion(val id: String, val emoji: String, val texto: String, val color: Color)

/**
 * Pantalla principal para el registro y visualización del estado emocional diario.
 *
 * Gestiona un flujo de múltiples pasos (selección de emoción -> selección de adjetivo ->
 * adición de notas -> registro guardado) y también permite ver el historial.
 *
 * @param navController El controlador de navegación para cambiar entre pantallas.
 * @param emotionViewModel El ViewModel que gestiona el estado, la lógica y la interacción
 * con el repositorio de emociones.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionsScreen(
    navController: NavController,
    emotionViewModel: EmotionViewModel = viewModel()
) {
    val uiState = emotionViewModel.uiState
    val context = LocalContext.current

    val emociones = listOf(
        Emocion("alegre", "😄", "Alegre", Color(0xFFFFF176)),
        Emocion("neutral", "😐", "Neutral", Color(0xFFE0E0E0)),
        Emocion("triste", "😢", "Triste", Color(0xFF90CAF9)),
        Emocion("molesto", "😠", "Molesto", Color(0xFFEF9A9A)),
        Emocion("nervioso", "😰", "Nervioso", Color(0xFFCE93D8))
    )

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (uiState.currentScreen == 5) "Historial" else "Agenda Emocional",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {

                        if (uiState.currentScreen == 1 || uiState.currentScreen == 3) {
                            navController.popBackStack() // Sale de la pantalla o a HOME
                        } else {

                            emotionViewModel.goBack() // Navega al paso anterior dentro del flujo
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {

                if (uiState.currentScreen == 5) {
                    // Muestra la pantalla de Historial
                    PantallaHistorial(
                        historial = uiState.history,
                        onVolver = { emotionViewModel.goBack() }
                    )
                } else {
                    // Muestra el flujo de registro (Pasos 1 a 4)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        when (uiState.currentScreen) {
                            1 -> PantallaPrincipal(
                                emociones = emociones,
                                onEmocionClick = { emotionViewModel.selectEmotion(it) },
                                onVerHistorialClick = { emotionViewModel.goToHistory() }
                            )

                            2 -> uiState.selectedEmotion?.let {
                                val adjetivos = emotionViewModel.adjetivosMap[it.id] ?: emptyList()
                                PantallaAdjetivos(
                                    emocion = it,
                                    adjetivos = adjetivos,
                                    onAdjetivoClick = { adj -> emotionViewModel.selectAdjective(adj) },
                                    onVolver = { emotionViewModel.goBack() }
                                )
                            }

                            3 -> uiState.todayEntry?.let { entry ->
                                val emocionGuardada = emociones.find { e -> e.id == entry.emotionId }
                                    ?: Emocion("unknown", entry.emotionEmoji, entry.emotionText, Color.Gray)

                                PantallaRegistroGuardado(
                                    registro = entry,
                                    emocion = emocionGuardada,
                                    onNuevoRegistro = { emotionViewModel.resetFlow() },
                                    onVerHistorial = { emotionViewModel.goToHistory() },
                                    frasesMotivadoras = emptyList()
                                )
                            }

                            4 -> if (uiState.selectedEmotion != null) {
                                PantallaNotas(
                                    emocion = uiState.selectedEmotion!!,
                                    adjetivo = uiState.selectedAdjective,
                                    notas = uiState.dailyNote,
                                    onNotasChange = { emotionViewModel.updateNote(it) },
                                    onGuardar = { emotionViewModel.saveEntry() },
                                    onVolver = { emotionViewModel.goBack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Primer paso del flujo de registro: permite al usuario seleccionar una emoción principal.
 *
 * @param emociones Lista de objetos [Emocion] disponibles para seleccionar.
 * @param onEmocionClick Lambda que se invoca con la emoción seleccionada.
 * @param onVerHistorialClick Lambda que navega a la vista de historial.
 */
@Composable
fun PantallaPrincipal(
    emociones: List<Emocion>,
    onEmocionClick: (Emocion) -> Unit,
    onVerHistorialClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "¿Cómo te sientes hoy?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        emociones.forEach { emocion ->
            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                onClick = { onEmocionClick(emocion) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emocion.emoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        emocion.texto,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVerHistorialClick,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver días anteriores", color = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Segundo paso del flujo de registro: permite al usuario seleccionar un adjetivo para matizar la emoción.
 *
 * @param emocion La [Emocion] seleccionada en el paso anterior.
 * @param adjetivos Lista de adjetivos disponibles para esa emoción.
 * @param onAdjetivoClick Lambda que se invoca con el adjetivo seleccionado.
 * @param onVolver Lambda para volver al paso anterior (selección de emoción).
 */
@Composable
fun PantallaAdjetivos(
    emocion: Emocion,
    adjetivos: List<String>,
    onAdjetivoClick: (String) -> Unit,
    onVolver: () -> Unit
) {
    Column {
        Text(
            text = "Te sientes ${emocion.texto.lowercase()}...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        adjetivos.forEach { adjetivo ->
            Button(
                onClick = { onAdjetivoClick(adjetivo) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(adjetivo)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

/**
 * Tercer paso del flujo de registro: permite al usuario escribir una nota o descripción de su día.
 *
 * @param emocion La [Emocion] principal seleccionada.
 * @param adjetivo El adjetivo seleccionado.
 * @param notas El texto actual de las notas (vinculado al estado del ViewModel).
 * @param onNotasChange Lambda para actualizar el texto de la nota en el ViewModel.
 * @param onGuardar Lambda para ejecutar la acción de guardar el [EmotionEntry] en la base de datos.
 * @param onVolver Lambda para volver al paso anterior (selección de adjetivo).
 */
@Composable
fun PantallaNotas(
    emocion: Emocion,
    adjetivo: String,
    notas: String,
    onNotasChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit
) {
    Column {
        Text(
            text = "Describe tu día (${emocion.texto} - $adjetivo)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = notas,
            onValueChange = onNotasChange,
            label = { Text("Escribe tus notas aquí...") },
            modifier = Modifier.fillMaxWidth().height(160.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onGuardar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Guardar registro")
        }
    }
}

/**
 * Cuarto paso del flujo de registro: Muestra el registro guardado de hoy, junto con
 * una frase motivadora y recomendaciones basadas en la emoción registrada.
 *
 * @param registro El objeto [EmotionEntry] guardado para el día de hoy.
 * @param emocion El objeto [Emocion] correspondiente al registro.
 * @param frasesMotivadoras (Ignorado, ya que se genera internamente con [obtenerFraseMotivadora]).
 * @param onNuevoRegistro Lambda para reiniciar el flujo y editar el registro de hoy.
 * @param onVerHistorial Lambda para navegar a la vista de historial.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaRegistroGuardado(
    registro: EmotionEntry,
    emocion: Emocion,
    frasesMotivadoras: List<String>,
    onNuevoRegistro: () -> Unit,
    onVerHistorial: () -> Unit
) {
    val fraseDelDia = remember { obtenerFraseMotivadora() }
    val recomendaciones = remember(registro.emotionId) { obtenerRecomendaciones(registro.emotionId) }

    // Formateo de la fecha (asumiendo formato "YYYY-MM-DD")
    val fechaTexto = try {
        if (registro.dateString.isNotEmpty()) {
            val partes = registro.dateString.split("-")
            if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else registro.dateString
        } else "Hoy"
    } catch (e: Exception) { "Hoy" }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("Registro guardado", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("📅 $fechaTexto", color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(16.dp))
        Text("${emocion.emoji} ${emocion.texto} - ${registro.adjective}", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            if (registro.note.isNotBlank()) registro.note else "(Sin notas adicionales)",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("💬 Frase del día:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)
        Text("“$fraseDelDia”", textAlign = TextAlign.Center, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(12.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💡", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Recomendaciones", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                recomendaciones.forEach { tip ->
                    Text("• $tip", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onNuevoRegistro, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Text("Editar registro de hoy")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onVerHistorial, border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)) {
            Text("Ver historial completo", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

/**
 * Pantalla para visualizar el historial completo de los registros emocionales del usuario.
 *
 * @param historial La lista de objetos [EmotionEntry] recuperados de la base de datos.
 * @param onVolver Lambda para volver a la pantalla principal de registro emocional.
 */
@Composable
fun PantallaHistorial(
    historial: List<EmotionEntry>,
    onVolver: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (historial.isEmpty()) {
                item {
                    Text(
                        "No hay registros anteriores aún.",
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(historial) { entry ->
                    ItemHistorial(entry)
                }
            }
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text("Volver al inicio", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Componente Composable que representa un único elemento en la lista de historial.
 *
 * Muestra el día, mes, emoji, texto de la emoción y un resumen de la nota.
 *
 * @param entry El objeto [EmotionEntry] que contiene los datos del registro.
 */
@Composable
fun ItemHistorial(entry: EmotionEntry) {
    val partes = entry.dateString.split("-")
    val dia = if (partes.size >= 3) partes[2] else "??"

    val mesMap = mapOf(
        "01" to "ENE", "02" to "FEB", "03" to "MAR", "04" to "ABR",
        "05" to "MAY", "06" to "JUN", "07" to "JUL", "08" to "AGO",
        "09" to "SEP", "10" to "OCT", "11" to "NOV", "12" to "DIC"
    )
    val mesNum = if (partes.size >= 3) partes[1] else "00"
    val mes = mesMap[mesNum] ?: "---"

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = dia, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                Text(text = mes, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = if (entry.emotionEmoji.isNotEmpty()) entry.emotionEmoji else "❓", fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${entry.emotionText} - ${entry.adjective}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (entry.note.isNotEmpty()) {
                    Text(text = entry.note, maxLines = 1, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/**
 * Función de utilidad que selecciona una frase motivadora aleatoria de un banco predefinido.
 *
 * @return Una cadena de texto con una frase motivadora.
 */
fun obtenerFraseMotivadora(): String {
    return listOf(

        "El éxito es la suma de pequeños esfuerzos repetidos cada día.",
        "No te detengas hasta que te sientas orgulloso.",
        "Tu actitud determina tu dirección.",
        "Cree en ti y todo será posible.",
        "Hoy es un buen día para tener un gran día.",
        "Pequeños pasos te llevan a grandes lugares.",
        "Hazlo con miedo, pero hazlo.",
        "La disciplina te lleva donde la motivación no alcanza.",
        "Nunca es tarde para ser lo que podrías haber sido.",
        "Si puedes soñarlo, puedes hacerlo.",


        "La calma es un superpoder.",
        "Respira, suelta y confía.",
        "La felicidad no es una meta, es el camino.",
        "Todo pasa, esto también pasará.",
        "Un día a la vez.",
        "No necesitas tener todas las respuestas hoy.",
        "La paz viene de adentro, no la busques fuera.",
        "A veces, descansar es lo más productivo que puedes hacer.",
        "No permitas que el comportamiento de otros destruya tu paz interior.",


        "Eres más fuerte de lo que crees.",
        "Sé amable contigo mismo.",
        "Eres suficiente tal y como eres.",
        "Tus emociones son válidas.",
        "Cuidar de ti mismo no es egoísmo, es necesario.",
        "Florece donde te planten.",
        "Tu único límite es tu mente.",
        "Brilla con luz propia.",
        "Mañana será una nueva oportunidad.",
        "Lo estás haciendo mejor de lo que piensas."
    ).random()
}

/**
 * Función de utilidad que devuelve una lista de recomendaciones específicas basadas en la emoción registrada.
 *
 * @param emocionId El ID de la emoción (e.g., "triste", "molesto").
 * @return Una lista de cadenas de texto con consejos o actividades recomendadas.
 */
fun obtenerRecomendaciones(emocionId: String): List<String> {
    val bancoDeConsejos = when (emocionId) {
        "alegre" -> listOf(
            "Comparte tu buena energía llamando a un amigo.",
            "Anota 3 cosas por las que estás agradecido hoy.",
            "Aprovecha este impulso para realizar una tarea difícil.",
            "Date un pequeño gusto o premio, te lo mereces.",
            "Guarda este momento: toma una foto o escribe un recuerdo.",
            "Contagia tu alegría: haz un cumplido sincero a alguien."
        )
        "triste" -> listOf(
            "Está bien no estar bien, date permiso de sentir.",
            "Escucha esa playlist que te reconforta el alma.",
            "Habla con alguien de confianza, no te aísles.",
            "Sal a caminar 10 minutos para tomar aire fresco.",
            "Evita redes sociales por un rato y descansa la mente.",
            "Escribe lo que sientes en una hoja y luego rómpela."
        )
        "molesto" -> listOf(
            "Realiza la técnica 4-7-8: Inhala en 4s, retén 7s, exhala 8s.",
            "Aléjate físicamente de la situación que te enojó.",
            "Haz ejercicio intenso para quemar la adrenalina.",
            "Escribe una carta de enojo (pero no la envíes).",
            "Lávate la cara con agua fría para 'resetear' tu sistema.",
            "Escucha música con mucha energía o ruido blanco."
        )
        "nervioso" -> listOf(
            "Usa la técnica 5-4-3-2-1 para conectarte con el presente.",
            "Haz una lista de lo que SÍ puedes controlar ahora.",
            "Bebe un vaso de agua lentamente.",
            "Reduce la cafeína por el resto del día.",
            "Organiza tu espacio físico, el orden externo trae calma interna.",
            "Cierra los ojos y visualiza un lugar seguro por 2 minutos."
        )
        "neutral" -> listOf(
            "Es un buen momento para leer o aprender algo nuevo.",
            "Organiza tu agenda para mañana con calma.",
            "Dedica 5 minutos a meditar sin expectativas.",
            "Llama a un familiar solo para saludar.",
            "Ordena tu habitación o espacio de trabajo.",
            "Haz estiramientos suaves para activar tu cuerpo."
        )
        else -> listOf(
            "Tómate un momento para respirar conscientemente.",
            "Hidrátate, bebe un vaso de agua.",
            "Estira los brazos y el cuello suavemente."
        )
    }

    // Devuelve dos recomendaciones aleatorias
    return bancoDeConsejos.shuffled().take(2)
}