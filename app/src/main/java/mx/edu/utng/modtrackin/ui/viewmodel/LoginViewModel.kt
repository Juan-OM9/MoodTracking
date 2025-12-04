package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Representa el estado actual de la UI de la pantalla de inicio de sesión.
 *
 * @property email El valor actual del campo de correo electrónico.
 * @property password El valor actual del campo de contraseña.
 * @property isLoading Indica si una operación de inicio de sesión está en curso.
 * @property errorMessage Contiene un mensaje de error si la autenticación falla o si los campos están vacíos.
 * @property loginSuccess Indica si el inicio de sesión se completó exitosamente.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)


/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Login.
 *
 * Es responsable de:
 * 1. Capturar la entrada del usuario (correo y contraseña).
 * 2. Realizar la autenticación contra Firebase Auth.
 * 3. Manejar los estados de carga, éxito y error durante el proceso de inicio de sesión.
 */
class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Actualiza el valor del correo electrónico en el estado.
     * @param email El nuevo valor del correo.
     */
    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email)
    }

    /**
     * Actualiza el valor de la contraseña en el estado.
     * @param password El nuevo valor de la contraseña.
     */
    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }

    /**
     * Limpia el mensaje de error de la UI.
     */
    fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }

    /**
     * Intenta iniciar sesión con el correo y la contraseña actuales almacenados en [uiState].
     *
     * Valida que los campos no estén vacíos. Si la autenticación falla, actualiza [errorMessage].
     * Si es exitosa, establece [loginSuccess] a `true`.
     */
    fun login() {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Correo y contraseña no pueden estar vacíos")
            return
        }


        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                // Ejecuta la autenticación de Firebase de forma asíncrona
                auth.signInWithEmailAndPassword(uiState.email, uiState.password).await()
                println("Login exitoso para: ${uiState.email}")
                uiState = uiState.copy(isLoading = false, loginSuccess = true)

            } catch (e: Exception) {
                println("Error de login: ${e.message}")
                uiState = uiState.copy(isLoading = false, errorMessage = "Error: Correo o contraseña incorrectos.")
            }
        }
    }
}