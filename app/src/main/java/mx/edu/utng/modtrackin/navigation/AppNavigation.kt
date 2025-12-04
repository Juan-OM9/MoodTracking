package mx.edu.utng.modtrackin.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.edu.utng.modtrackin.ui.screens.calendar.CalendarScreen
import mx.edu.utng.modtrackin.ui.screens.emotions.EmotionsScreen
import mx.edu.utng.modtrackin.ui.screens.habits.HabitScreen
import mx.edu.utng.modtrackin.ui.screens.home.HomeScreen
import mx.edu.utng.modtrackin.ui.screens.login.LoginScreen
import mx.edu.utng.modtrackin.ui.screens.notes.AddNoteScreen
import mx.edu.utng.modtrackin.ui.screens.notes.NotesScreen
import mx.edu.utng.modtrackin.ui.screens.register.RegisterScreen
import mx.edu.utng.modtrackin.ui.screens.tasks.TasksScreen
import mx.edu.utng.modtrackin.ui.screens.sleep.SleepScreen
import mx.edu.utng.modtrackin.ui.viewmodel.EmotionViewModel
import mx.edu.utng.modtrackin.ui.viewmodel.TaskViewModel

/**
 * Define y gestiona el grafo de navegación principal de la aplicación utilizando Compose Navigation.
 *
 * Utiliza [NavHost] para definir las rutas y enlazar las funciones composables de las pantallas
 * de la aplicación. También inicializa y comparte los ViewModels necesarios.
 *
 * @param startDestination La ruta inicial ([NavRoutes]) desde donde comenzará la navegación
 * de la aplicación (ej. [NavRoutes.LOGIN_SCREEN] o [NavRoutes.HOME_SCREEN]).
 */
@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()

    // Inicialización y obtención de ViewModels para compartir estados entre pantallas.
    val taskViewModel: TaskViewModel = viewModel()
    val emotionViewModel: EmotionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }

        composable(NavRoutes.REGISTER_SCREEN) {
            RegisterScreen(navController = navController)
        }

        composable(NavRoutes.HOME_SCREEN) {
            HomeScreen(
                navController = navController,
                emotionViewModel = emotionViewModel
            )
        }
        composable(NavRoutes.TASKS_SCREEN) {
            TasksScreen(
                navController = navController,
                taskViewModel = taskViewModel
            )
        }

        composable(NavRoutes.CALENDAR_SCREEN) {
            CalendarScreen(
                navController = navController,
                taskViewModel = taskViewModel
            )
        }

        composable(NavRoutes.NOTES_SCREEN) {
            NotesScreen(navController = navController)
        }

        composable(NavRoutes.ADD_NOTE_SCREEN) {
            AddNoteScreen(navController = navController)
        }

        composable(NavRoutes.EMOTIONS_SCREEN) {
            // Se requiere API 26 (Oreo) o superior para el manejo de emociones (posiblemente por DateTimeFormatter)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                EmotionsScreen(
                    navController = navController,
                    emotionViewModel = emotionViewModel
                )
            }
        }

        composable(NavRoutes.HABITS_SCREEN) {
            HabitScreen(navController = navController)
        }

        composable(NavRoutes.SLEEP_SCREEN) {
            SleepScreen(navController = navController)
        }
    }
}