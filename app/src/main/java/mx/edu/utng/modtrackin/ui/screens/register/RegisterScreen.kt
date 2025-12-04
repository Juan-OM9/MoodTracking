package mx.edu.utng.modtrackin.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.viewmodel.RegisterViewModel

/**
 * Pantalla de registro de nuevos usuarios.
 *
 * Permite al usuario ingresar su nombre, correo y contraseña (confirmación incluida)
 * para crear una nueva cuenta. Utiliza [RegisterViewModel] para gestionar la lógica de autenticación
 * y la navegación tras el registro exitoso.
 *
 * @param navController El controlador de navegación para volver a la pantalla de login
 * o navegar a la pantalla de inicio ([NavRoutes.HOME_SCREEN]).
 * @param registerViewModel El ViewModel que gestiona el estado de la UI y la lógica de registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState = registerViewModel.uiState
    val context = LocalContext.current

    // Efecto que se dispara al cambiar el estado de registerSuccess en el ViewModel.
    // Navega a HOME_SCREEN y limpia la pila de navegación.
    LaunchedEffect(key1 = uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            navController.navigate(NavRoutes.HOME_SCREEN) {
                // Elimina la pantalla de login de la pila antes de navegar a HOME
                popUpTo(NavRoutes.LOGIN_SCREEN) { inclusive = true }
            }
        }
    }

    // Efecto que se dispara cuando hay un mensaje de error.
    // Muestra un Toast con el mensaje de error y luego limpia el estado del error.
    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            registerViewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header con información de la pantalla
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .padding(top = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    TextButton(
                        onClick = { navController.navigate(NavRoutes.LOGIN_SCREEN) },
                        enabled = !uiState.isLoading
                    ) {
                        Text("← Volver", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Crear Cuenta",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text("Únete a ModTrackin", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Formulario de registro
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Colores de campos de texto definidos para reutilización
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { registerViewModel.onNameChange(it) },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !uiState.isLoading
                )

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !uiState.isLoading
                )

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro
                Button(
                    onClick = { registerViewModel.register() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Crear Cuenta", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}