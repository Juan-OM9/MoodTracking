// Archivo: settings.gradle.kts (COMPLETO Y CORREGIDO)

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // YA NO NECESITAMOS EL REPOSITORIO DE JETBRAINS
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ModTrackin"
include(":app")
