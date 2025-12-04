package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.utng.modtrackin.data.model.User

/**
 * Representa el estado actual de la UI de la pantalla de Registro.
 *
 * @property name El valor actual del campo de nombre completo.
 * @property email El valor actual del campo de correo electrónico.
 * @property password El valor actual del campo de contraseña.
 * @property confirmPassword El valor actual del campo de confirmación de contraseña.
 * @property isLoading Indica si una operación de registro está en curso.
 * @property errorMessage Contiene un mensaje de error si la validación o el registro fallan.
 * @property registerSuccess Indica si el registro se completó exitosamente.
 */
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registerSuccess: Boolean = false
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Registro.
 *
 * Es responsable de:
 * 1. Capturar la entrada del usuario para el registro (nombre, correo, contraseñas).
 * 2. Realizar validaciones de formulario (campos vacíos, coincidencia de contraseñas, longitud).
 * 3. Realizar la autenticación y creación del usuario en Firebase Auth.
 * 4. Guardar la información adicional del usuario ([User]) en Firebase Firestore.
 */
class RegisterViewModel : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    /** Actualiza el valor del nombre en el estado. */
    fun onNameChange(name: String) { uiState = uiState.copy(name = name) }

    /** Actualiza el valor del correo electrónico en el estado. */
    fun onEmailChange(email: String) { uiState = uiState.copy(email = email) }

    /** Actualiza el valor de la contraseña en el estado. */
    fun onPasswordChange(password: String) { uiState = uiState.copy(password = password) }

    /** Actualiza el valor de la confirmación de contraseña en el estado. */
    fun onConfirmPasswordChange(confirmPassword: String) { uiState = uiState.copy(confirmPassword = confirmPassword) }

    /** Limpia el mensaje de error de la UI. */
    fun clearError() { uiState = uiState.copy(errorMessage = null) }

    /**
     * Intenta registrar un nuevo usuario con las credenciales y el nombre actuales.
     *
     * Realiza validaciones locales de los campos antes de proceder con Firebase.
     * Si es exitoso, crea el registro de autenticación y el documento en Firestore.
     */
    fun register() {
        // --- VALIDACIONES LOCALES ---
        if (uiState.name.isBlank() || uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Todos los campos son obligatorios")
            return
        }
        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden")
            return
        }
        if (uiState.password.length < 6) {
            uiState = uiState.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                // 1. Crear usuario en Firebase Auth
                val authResult = auth.createUserWithEmailAndPassword(uiState.email, uiState.password).await()
                val userId = authResult.user?.uid

                if (userId != null) {
                    // 2. Crear objeto User para Firestore
                    val user = User(
                        uid = userId,
                        name = uiState.name,
                        email = uiState.email
                    )
                    // 3. Guardar la información de perfil en Firestore
                    firestore.collection("users").document(userId).set(user).await()

                    uiState = uiState.copy(isLoading = false, registerSuccess = true)
                }
            } catch (e: Exception) {
                // Maneja errores de Firebase (e.g., email ya en uso, formato inválido)
                uiState = uiState.copy(isLoading = false, errorMessage = e.message ?: "Error al registrar")
            }
        }
    }
}