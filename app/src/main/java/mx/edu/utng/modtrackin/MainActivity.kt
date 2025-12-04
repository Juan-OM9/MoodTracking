package mx.edu.utng.modtrackin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.utng.modtrackin.navigation.AppNavigation
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.theme.ModTrackinTheme
import mx.edu.utng.modtrackin.utils.NotificationHelper
import mx.edu.utng.modtrackin.utils.NotificationScheduler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.createNotificationChannel(this)

        val scheduler = NotificationScheduler(this)
        scheduler.scheduleDailyMoodReminder()

        val auth = Firebase.auth
        val currentUser = auth.currentUser

        val startDestination = if (currentUser != null) {
            NavRoutes.HOME_SCREEN
        } else {
            NavRoutes.LOGIN_SCREEN
        }

        setContent {
            ModTrackinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}