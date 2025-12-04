// Archivo: build.gradle.kts del Proyecto (COMPLETO Y CORREGIDO)

// ELIMINA CUALQUIER LÍNEA 'import' QUE HAYA AQUÍ ARRIBA

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // ¡¡¡ELIMINA ESTA LÍNEA!!! ESTA ES LA LÍNEA QUE ROMPE TODO.
    // alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false // Cambiado para que coincida con el TOML
}



