package mx.edu.utng.modtrackin.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.ui.viewmodel.NotesViewModel

/**
 * Pantalla para la creación o edición de una nota.
 *
 * Muestra campos de texto para el título y el contenido de la nota, y proporciona opciones
 * en la AppBar y un botón inferior para guardar la nota utilizando el [NotesViewModel].
 *
 * @param navController El controlador de navegación para volver a la pantalla anterior
 * (normalmente [NotesScreen]) después de guardar o cancelar.
 * @param viewModel El ViewModel que gestiona el estado de la nota actual (título y contenido)
 * y ejecuta la lógica de guardado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,

    viewModel: NotesViewModel = viewModel()
) {
    val uiState = viewModel.uiState


    /**
     * Efecto que se ejecuta solo una vez al inicio para preparar el ViewModel para
     * la creación de una nueva nota.
     */
    LaunchedEffect(Unit) {
        viewModel.openEditorNew()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Nota", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {

                    /**
                     * Botón de guardar en la barra superior. Guarda la nota actual y vuelve atrás.
                     */
                    IconButton(onClick = {
                        viewModel.saveCurrentNote()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
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

            // CAMPO TÍTULO (CONECTADO AL UI STATE)
            OutlinedTextField(
                value = uiState.currentNote.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                shape = RoundedCornerShape(12.dp)
            )

            // CAMPO CONTENIDO (CONECTADO AL UI STATE)
            OutlinedTextField(
                value = uiState.currentNote.content,
                onValueChange = { viewModel.updateContent(it) },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = fieldColors,
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = androidx.compose.ui.unit.TextUnit.Unspecified
                )
            )

            /**
             * Botón inferior de guardar. Ejecuta la misma acción que el icono superior
             * y vuelve a la pantalla anterior.
             */
            Button(
                onClick = {
                    viewModel.saveCurrentNote()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Guardar Nota")
            }
        }
    }
}