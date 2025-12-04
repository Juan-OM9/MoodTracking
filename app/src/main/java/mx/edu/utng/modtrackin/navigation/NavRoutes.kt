package mx.edu.utng.modtrackin.navigation

/**
 * Objeto que contiene las constantes de cadena (String) utilizadas para definir las rutas
 * de navegación (destinos) en el grafo de [NavHost].
 *
 * Utilizar constantes en lugar de cadenas literales evita errores de escritura al navegar
 * entre las diferentes pantallas de la aplicación.
 */
object NavRoutes {
    /** Ruta para la pantalla de inicio de sesión. */
    const val LOGIN_SCREEN = "login"
    /** Ruta para la pantalla de registro de nuevos usuarios. */
    const val REGISTER_SCREEN = "register"
    /** Ruta para la pantalla de inicio o dashboard principal. */
    const val HOME_SCREEN = "home"
    /** Ruta para la pantalla de gestión de tareas pendientes (To-Do List). */
    const val TASKS_SCREEN = "tasks"
    /** Ruta para la pantalla de calendario o vista de programación. */
    const val CALENDAR_SCREEN = "calendar"
    /** Ruta para la pantalla principal de listado de notas. */
    const val NOTES_SCREEN = "notes_screen"
    /** Ruta para la pantalla de creación o edición de una nota. */
    const val ADD_NOTE_SCREEN = "addNote"
    /** Ruta para la pantalla de registro y visualización del estado emocional. */
    const val EMOTIONS_SCREEN = "emotions"
    /** Ruta para la pantalla de seguimiento de hábitos. */
    const val HABITS_SCREEN = "habit_Screen"
    /** Ruta para la pantalla de registro y análisis de sueño. */
    const val SLEEP_SCREEN = "sleep"

}