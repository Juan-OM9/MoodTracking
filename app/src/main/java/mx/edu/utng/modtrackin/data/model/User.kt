package mx.edu.utng.modtrackin.data.model

/**
 * Clase de modelo de datos (data class) que representa la información de perfil de un usuario.
 *
 * Esta "ficha de registro" almacena los datos básicos del usuario en la base de datos Firestore,
 * incluyendo el identificador único de autenticación y los datos personales esenciales.
 *
 * @property uid El identificador único (User ID) proporcionado por el sistema de autenticación
 * (por ejemplo, Firebase Auth).
 * @property name El nombre completo del usuario.
 * @property email La dirección de correo electrónico del usuario.
 */
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = ""
)