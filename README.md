# 📱 ModTrackin: Agenda y Bienestar Personal con Firebase

## 🎯 Descripción del Proyecto

**ModTrackin** es una aplicación Android nativa desarrollada con **Kotlin** y **Jetpack Compose**. Su objetivo principal es ayudar a los usuarios a encontrar un equilibrio entre su productividad diaria y su salud mental, centralizando la gestión de tareas y hábitos junto con un registro consciente de emociones y ciclos de sueño.

El proyecto implementa una arquitectura moderna **MVVM (Model-View-ViewModel)** y utiliza **Firebase** como backend robusto para la autenticación segura y la sincronización de datos en la nube en tiempo real.

---

### 🌟 Características Principales

#### 🧠 Módulo de Bienestar
- ✅ **Agenda Emocional Interactiva:** Registro diario del estado de ánimo mediante emojis, selección de adjetivos y notas personales.
- ✅ **Feedback Inteligente:** Visualización de frases motivadoras y recomendaciones de actividades basadas en la emoción registrada.
- ✅ **Monitor de Sueño:** Registro preciso de horas de descanso con cálculo automático de duración y calidad.
- ✅ **Historial Visual:** Consulta cronológica de los registros emocionales pasados.

#### ⚡ Módulo de Productividad
- ✅ **Gestión de Tareas (To-Do):** Creación, edición y eliminación de tareas con prioridades (Alta/Media/Baja).
- ✅ **Seguimiento de Hábitos:** Monitoreo del progreso diario de hábitos con metas personalizadas.
- ✅ **Notas Rápidas:** Espacio dedicado para apuntes generales.
- ✅ **Notificaciones:** Sistema de recordatorios locales para no olvidar registrar actividades.

---

## 📸 Evidencias (Screenshots)

> Las capturas se encuentran en la carpeta `docs/screenshots`.

| Login y Registro | Dashboard Principal |
|:---:|:---:|
| ![Login](docs/screenshots/login.png) | ![Home](docs/screenshots/home.png) |

| Agenda Emocional | Gestión de Tareas |
|:---:|:---:|
| ![Emociones](docs/screenshots/emotion.png) | ![Tareas](docs/screenshots/tasks.png) |

| Hábitos |
|:---:|
| ![Hábitos](docs/screenshots/habits.png) | 

---
## 🏗️ Arquitectura del Proyecto

Este proyecto sigue estrictamente el patrón de diseño **MVVM** para asegurar un código limpio, escalable y fácil de mantener.

### 📐 Diagrama de Arquitectura

El flujo de datos viaja de manera reactiva desde la nube hasta la interfaz de usuario:

```text
+-------------------------------+
|         VIEW (UI)             |
|  (Pantallas Jetpack Compose)  |
+---------------+---------------+
                ^
                | (Observa UiState)
                v
+---------------+---------------+
|         VIEWMODEL             |
|    (Lógica de Negocio)        |
+---------------+---------------+
                ^
                | (Coroutines)
                v
+---------------+---------------+
|        REPOSITORY             |
|     (Fuente de Verdad)        |
+---------------+---------------+
                ^
                | (Internet)
                v
+---------------+---------------+
|         FIREBASE              |
|     (Cloud Firestore)         |
+-------------------------------+
```
---
## 🛠️ Tecnologías y Herramientas

Este proyecto utiliza un stack tecnológico moderno centrado en el ecosistema nativo de Android y la nube de Google:

* **Kotlin:** Lenguaje de programación principal utilizado en el 100% del proyecto.
* **Jetpack Compose:** Toolkit moderno para construir interfaces de usuario nativas de forma declarativa (Material Design 3).
* **MVVM (Model-View-ViewModel):** Patrón de arquitectura utilizado para desacoplar la lógica de negocio de la interfaz de usuario.
* **Firebase Auth:** Servicio para la gestión segura de autenticación (inicio de sesión y registro).
* **Firebase Firestore:** Base de datos NoSQL en la nube para la persistencia y sincronización de datos en tiempo real.
* **Coroutines & Flow:** Librerías para el manejo eficiente de operaciones asíncronas y flujos de datos reactivos.
* **AlarmManager:** Servicio del sistema utilizado para programar notificaciones y recordatorios locales.
* **KDoc:** Estándar de documentación técnica implementado en todo el código fuente.
---
## ⚙️ Instalación y Configuración

Sigue estos pasos para ejecutar el proyecto en tu entorno local.

### Requisitos Previos

* **Android Studio:** Versión recomendada Koala o superior.
* **JDK:** Versión 17 o superior.
* **Cuenta de Google:** Necesaria para configurar la consola de Firebase.

### PASO 1: Clonar el Repositorio

Abre tu terminal y ejecuta el siguiente comando para descargar el código fuente:

git clone https://github.com/Juan-OM9/MoodTracking

### PASO 2: Configurar Firebase (⚠️ IMPORTANTE)
Este proyecto utiliza servicios de Google que requieren un archivo de configuración (google-services.json) que no se incluye en el repositorio por razones de seguridad. Debes generar el tuyo propio:

Ve a la Consola de Firebase y crea un nuevo proyecto.

Agrega una aplicación Android al proyecto.

Ingresa el nombre del paquete de la aplicación: mx.edu.utng.modtrackin

Descarga el archivo google-services.json que te proporciona Firebase.

Mueve ese archivo a la carpeta app dentro de tu proyecto.

Ruta final: ModTrackin/app/google-services.json

En la consola de Firebase, habilita los siguientes servicios:

Authentication: Activa el proveedor de "Correo electrónico/Contraseña".

Firestore Database: Crea una base de datos (puedes iniciar en "modo prueba" para desarrollo).

### PASO 3: Ejecutar la Aplicación
Abre Android Studio.

Selecciona File > Open y busca la carpeta del proyecto clonado.

Espera a que Gradle termine de sincronizar las dependencias (puede tardar unos minutos la primera vez).

Conecta tu dispositivo Android o inicia un emulador (API 26+ recomendada).

Haz clic en el botón Run (▶️) en la barra de herramientas.
---

## 📁 Estructura del Proyecto

El código fuente está organizado siguiendo el principio de separación de responsabilidades, dividiendo la aplicación en capas lógicas claras:

```text
mx.edu.utng.modtrackin
├── 📦 data                  # Capa de Datos
│   ├── model                # Clases de datos (Data Classes) que definen la estructura de la información (Task, Habit, EmotionEntry, User).
│   └── repository           # Clases encargadas de la comunicación directa con Firebase Firestore y la abstracción de datos.
│
├── 🧭 navigation            # Configuración de la navegación
│   ├── NavRoutes.kt         # Definición de constantes para las rutas de navegación.
│   └── AppNavigation.kt     # Grafo de navegación (NavHost) que conecta todas las pantallas.
│
├── 🎨 ui                    # Capa de Presentación (Interfaz de Usuario)
│   ├── screens              # Pantallas Composable (Vistas)
│   │   ├── calendar         # Vista de calendario mensual.
│   │   ├── emotions         # Flujo de registro emocional.
│   │   ├── habits           # Seguimiento y edición de hábitos.
│   │   ├── home             # Dashboard principal.
│   │   ├── login            # Pantalla de inicio de sesión.
│   │   ├── notes            # Listado y editor de notas.
│   │   ├── register         # Pantalla de registro de usuarios.
│   │   ├── sleep            # Monitor de sueño.
│   │   └── tasks            # Gestión de tareas pendientes.
│   ├── theme                # Definiciones de diseño (Colores, Tipografía, Formas).
│   └── viewmodel            # ViewModels que gestionan el estado de la UI y la lógica de negocio.
│
└── 🛠️ utils                 # Utilidades y Herramientas
    ├── AppNotificationManager.kt # Gestión de notificaciones del sistema.
    ├── NotificationHelper.kt     # Configuración del canal de notificaciones.
    ├── NotificationReceiver.kt   # BroadcastReceiver para alarmas.
    └── NotificationScheduler.kt  # Programador de alarmas con AlarmManager.

```
---
### 📋 Pasos para la creacion del proyecto

### 🟢 FASE 1: Configuración del Proyecto
```text
### Paso 1: Crear el Proyecto en Android Studio

Abre Android Studio -> New Project.

Selecciona Empty Activity (la que tiene el icono de Jetpack Compose).

Name: ModTrackin

Package name: mx.edu.utng.modtrackin (Esto es muy importante para que coincida con el código).

Language: Kotlin.

Minimum SDK: API 26 (Android 8.0) o superior.

Click en Finish.
```
---
### Paso 2: Configurar Dependencias (Gradle) Abre el archivo build.gradle.kts (Module :app) y asegúrate de tener estas dependencias en el bloque dependencies.
```
Copia y pega las que te falten:
dependencies {
    // ... tus dependencias por defecto ...

    // Navegación Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Firebase (Usando BoM para manejar versiones)
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Coroutines & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Iconos extendidos (para los que usamos en el menú)
    implementation("androidx.compose.material:material-icons-extended:1.6.3")
}
```
---
### Paso 3: Conectar con Firebase
```
Ve a la consola de Firebase, crea el proyecto y descarga el archivo google-services.json.

Pega ese archivo dentro de la carpeta app de tu proyecto (vista "Project").

En build.gradle.kts (Project), agrega en plugins: id("com.google.gms.google-services") version "4.4.1" apply false

En build.gradle.kts (Module :app), agrega en plugins (arriba del todo): id("com.google.gms.google-services")
```
---
### 🟡 FASE 2: Estructura de Carpetas
Dentro de la carpeta mx.edu.utng.modtrackin, haz clic derecho -> New -> Package y crea estas carpetas para organizar el código:
```text
data

Dentro de data, crea: model

Dentro de data, crea: repository

ui

Dentro de ui, crea: screens

Dentro de ui, crea: viewmodel

Dentro de ui, crea: theme (ya debería existir)

navigation

utils
```
---
### 🟠 FASE 3: Crear los Archivos (Copiar y Pegar)
Ahora ve creando los archivos Kotlin (.kt) en cada carpeta correspondiente y pega el código que te di en las respuestas anteriores.
```text
### 1. En data/model
Crea estos archivos y pega sus respectivos códigos:
```
---
```
---
### Task.kt
package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa una tarea o elemento pendiente (To-Do).
 *
 * Contiene toda la información ingresada por el usuario en la pantalla de adición de tareas,
 * sirviendo como la "ficha de registro" en la base de datos de Firebase Firestore.
 *
 * @property id El identificador único de la tarea.
 * @property userId El identificador único del usuario propietario de la tarea.
 *
 * @property title El nombre o título breve de la tarea.
 * @property description Una descripción más detallada sobre lo que implica la tarea.
 * @property category La clasificación o grupo al que pertenece la tarea.
 *
 * @property priority La importancia relativa de la tarea, utilizada para su clasificación o gestión.
 * @property isCompleted Un indicador booleano que determina si la tarea ha sido finalizada o completada.
 * Las anotaciones [PropertyName] aseguran la correcta serialización en Firestore.
 *
 * @property createdAt La marca de tiempo del servidor de Firebase que indica la fecha y hora
 * en que se creó este registro de tarea.
 * @property dueDate La fecha límite o de vencimiento para completar la tarea, en formato de cadena.
 * @property reminder La configuración de recordatorio asociada a la tarea, en formato de cadena.
 */
data class Task(
    var id: String = "",
    val userId: String = "",


    val title: String = "",
    val description: String = "",
    val category: String = "",


    val priority: String = "Baja",

    @get:PropertyName("isCompleted")
    @set:PropertyName("isCompleted")
    var isCompleted: Boolean = false,

    @ServerTimestamp
    val createdAt: Date? = null,
    val dueDate: String = "",
    val reminder: String = ""
)
```
---
```
---
## Habit.kt
package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa un hábito o actividad que el usuario
 * desea rastrear y monitorear a diario.
 *
 * Esta clase se utiliza para almacenar la configuración y el progreso de un hábito en la
 * base de datos de Firebase Firestore.
 *
 * @property id El identificador único del hábito.
 * @property userId El identificador único del usuario propietario del hábito.
 *
 * @property title El nombre o título del hábito.
 * @property description Una descripción detallada del propósito del hábito.
 * @property category La clasificación o categoría a la que pertenece el hábito.
 *
 * @property dailyGoal La meta o el objetivo diario establecido por el usuario para este hábito.
 * Generalmente representa la cantidad de minutos o repeticiones.
 *
 * @property history Un mapa que registra el progreso diario del hábito, donde la clave es
 * la fecha (en formato de cadena) y el valor es la cantidad de progreso
 * registrado para ese día.
 *
 * @property createdAt La marca de tiempo del servidor de Firebase que indica la fecha y hora
 * en que se creó este registro de hábito.
 */
data class Habit(
    var id: String = "",
    val userId: String = "",

    val title: String = "",
    val description: String = "",
    val category: String = "Otro",

    val dailyGoal: Int = 60,

    val history: Map<String, Int> = emptyMap(),

    @ServerTimestamp
    val createdAt: Date? = null
)
```
---
```
---
### Note.kt
package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa una nota o entrada de diario simple
 * creada por el usuario.
 *
 * Se utiliza para guardar información textual y es un componente fundamental para
 * las funcionalidades de registro personal.
 *
 * @property id El identificador único de la nota.
 * @property userId El identificador único del usuario que creó la nota.
 * @property title El título de la nota.
 * @property content El contenido principal, cuerpo o texto de la nota.
 * @property timestamp La marca de tiempo del servidor de Firebase que indica la fecha y hora
 * de la creación de la nota.
 */
data class Note(
    var id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
```
---
```
---
### EmotionEntry.kt
package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa un registro emocional diario.
 *
 * Esta ficha se utiliza para almacenar y recuperar la información del estado de ánimo
 * y los detalles asociados del usuario en la base de datos de Firebase Firestore.
 *
 * @property id El identificador único del registro emocional dentro de la colección.
 * @property userId El identificador único del usuario que creó este registro.
 *
 * @property emotionId El ID de referencia de la emoción seleccionada
 * @property emotionEmoji El emoji asociado con la emoción para una visualización rápida
 * @property emotionText El nombre descriptivo de la emoción seleccionada
 * @property adjective El adjetivo adicional que califica o matiza la emoción principal
 * @property note Una nota o descripción detallada escrita por el usuario sobre el registro emocional.
 *
 * @property dateString La fecha del registro en formato de cadena (string) para facilitar su visualización
 * @property timestamp La marca de tiempo del servidor de Firebase que indica cuándo se guardó el registro.
 */
data class EmotionEntry(
    var id: String = "",
    val userId: String = "",

    // Datos de la emoción
    val emotionId: String = "",
    val emotionEmoji: String = "",
    val emotionText: String = "",
    val adjective: String = "",
    val note: String = "",

    val dateString: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
```
---
```
---
### SleepEntry.kt
package mx.edu.utng.modtrackin.data.model

import com.google.firebase.firestore.Exclude
import java.util.Date

/**
 * Clase de modelo de datos (data class) que representa un registro de sueño diario.
 *
 * Esta entidad se utiliza para almacenar los detalles del ciclo de sueño de un usuario
 * y registrar la calidad del mismo para su posterior correlación y análisis.
 *
 * @property id El identificador único del registro de sueño. Esta propiedad está excluida
 * de la serialización al guardar en Firebase Firestore.
 * @property userId El identificador único del usuario al que pertenece este registro de sueño.
 * @property date La fecha del registro de sueño en formato de cadena (string).
 * @property startTime La hora específica en que el usuario se durmió.
 * @property endTime La hora específica en que el usuario se despertó.
 * @property durationHours La duración total del sueño, calculada en horas.
 * @property quality La calificación de calidad del sueño reportada por el usuario, típicamente en una escala de 1 a 5.
 */
data class SleepEntry(
    @get:Exclude var id: String = "",
    val userId: String = "",
    val date: String = "", // YYYY-MM-DD
    val startTime: Date = Date(), // Hora en que se durmió
    val endTime: Date = Date(), // Hora en que se despertó
    val durationHours: Double = 0.0, // Duración calculada
    val quality: Int = 1 // Calidad reportada (1-5)
)
```
---
```
---
### User.kt
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
```
---
```
```
### 2. En data/repository
Crea estos archivos y pega sus códigos:
---
```
### TaskRepository.kt
package mx.edu.utng.modtrackin.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mx.edu.utng.modtrackin.data.model.Task

/**
 * Repositorio de datos encargado de la interacción con Firebase Firestore para la gestión
 * de las tareas pendientes ([Task]) del usuario.
 *
 * Proporciona métodos para obtener tareas una sola vez, escuchar cambios en tiempo real,
 * guardar (crear/actualizar) y eliminar tareas.
 */
class TaskRepository {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val tasksCollection = firestore.collection("tasks")

    /**
     * Obtiene el ID único del usuario actualmente autenticado.
     *
     * @return El ID del usuario actual, o `null` si no hay un usuario autenticado.
     */
    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Obtiene todas las tareas del usuario de forma asíncrona.
     *
     * Las tareas se filtran por el ID de usuario y se ordenan por la fecha de creación de forma descendente.
     *
     * @return Un [Result] que contiene una [List] de [Task], o una [Exception] si la operación falla
     * o si no hay un usuario autenticado.
     */
    suspend fun getAllTasks(): Result<List<Task>> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))
        return try {
            val snapshot = tasksCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val tasks = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Task::class.java)?.copy(id = doc.id)
                } catch (e: Exception) { null }
            }
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Establece un listener en tiempo real para obtener las tareas del usuario.
     *
     * Las tareas se filtran por el ID de usuario y se ordenan por la fecha de creación de forma descendente.
     * El listener se detiene automáticamente cuando deja de ser observado.
     *
     * @param onTasksUpdate Función lambda que se invoca cada vez que hay un cambio en la base de datos,
     * proporcionando la lista actualizada de [Task] del usuario.
     */
    fun listenToTasks(onTasksUpdate: (List<Task>) -> Unit) {
        val userId = getCurrentUserId() ?: return

        tasksCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Task::class.java)?.copy(id = doc.id)
                    }
                    onTasksUpdate(tasks)
                }
            }
    }

    /**
     * Guarda una tarea nueva o actualiza una tarea existente en Firestore.
     *
     * Si la tarea no tiene un ID, se añade un nuevo documento. Si ya tiene un ID,
     * se actualiza el documento correspondiente.
     *
     * @param task El objeto [Task] a guardar o actualizar.
     * @return Un [Result] que contiene [Unit] si la operación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun saveTask(task: Task): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))
        return try {
            val taskToSave = if (task.userId.isEmpty()) task.copy(userId = userId) else task
            if (task.id.isEmpty()) {
                tasksCollection.add(taskToSave).await()
            } else {
                tasksCollection.document(task.id).set(taskToSave).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una tarea específica de la base de datos de Firestore.
     *
     * @param taskId El identificador único de la tarea a eliminar.
     * @return Un [Result] que contiene [Unit] si la eliminación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            tasksCollection.document(taskId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```
---
```
---
### HabitRepository.kt
package mx.edu.utng.modtrackin.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mx.edu.utng.modtrackin.data.model.Habit

/**
 * Repositorio de datos encargado de la interacción con Firebase Firestore para la gestión
 * y el monitoreo de los hábitos del usuario ([Habit]).
 *
 * Proporciona métodos para guardar, eliminar y escuchar cambios en tiempo real
 * en la colección de hábitos.
 */
class HabitRepository {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val habitsCollection = firestore.collection("habits")

    /**
     * Obtiene el ID único del usuario actualmente autenticado.
     *
     * @return El ID del usuario actual, o `null` si no hay un usuario autenticado.
     */
    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Establece un listener en tiempo real para obtener los hábitos del usuario.
     *
     * La lista de hábitos se ordena por la fecha de creación de forma descendente.
     *
     * @param onHabitsUpdate Función lambda que se invoca cada vez que hay un cambio
     * en la base de datos, proporcionando la lista actualizada de [Habit] del usuario.
     */
    fun listenToHabits(onHabitsUpdate: (List<Habit>) -> Unit) {
        val userId = getCurrentUserId() ?: return

        habitsCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("HabitRepo", "Error", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val habits = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(Habit::class.java)?.copy(id = doc.id)
                        } catch (e: Exception) { null }
                    }
                    onHabitsUpdate(habits)
                }
            }
    }

    /**
     * Guarda un hábito nuevo o actualiza un hábito existente en Firestore.
     *
     * Si el hábito no tiene un ID, se añade un nuevo documento. Si ya tiene un ID,
     * se actualiza el documento correspondiente.
     *
     * @param habit El objeto [Habit] a guardar o actualizar.
     * @return Un [Result] que contiene [Unit] si la operación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun saveHabit(habit: Habit): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))
        return try {
            val habitToSave = if (habit.userId.isEmpty()) habit.copy(userId = userId) else habit
            if (habit.id.isEmpty()) {
                habitsCollection.add(habitToSave).await()
            } else {
                habitsCollection.document(habit.id).set(habitToSave).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un hábito específico de la base de datos de Firestore.
     *
     * @param habitId El identificador único del hábito a eliminar.
     * @return Un [Result] que contiene [Unit] si la eliminación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun deleteHabit(habitId: String): Result<Unit> {
        return try {
            habitsCollection.document(habitId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```
---
```
---
### NotesRepository.kt
package mx.edu.utng.modtrackin.data.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mx.edu.utng.modtrackin.data.model.Note

/**
 * Repositorio de datos encargado de la interacción con Firebase Firestore para la gestión
 * de las notas o entradas de diario del usuario ([Note]).
 *
 * Proporciona métodos para obtener notas en tiempo real, recuperar todo el historial,
 * guardar (crear/actualizar) y eliminar notas.
 */
class NotesRepository {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private val notesCollection = firestore.collection("notes")

    /**
     * Obtiene el ID único del usuario actualmente autenticado.
     *
     * @return El ID del usuario actual, o `null` si no hay un usuario autenticado.
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Establece un listener en tiempo real para obtener las notas del usuario.
     *
     * Las notas se filtran por el ID de usuario y se ordenan por la marca de tiempo
     * de creación de forma descendente.
     *
     * @param onNotesUpdate Función lambda que se invoca cada vez que hay un cambio
     * en la base de datos, proporcionando la lista actualizada de [Note] del usuario.
     */
    fun listenToNotes(onNotesUpdate: (List<Note>) -> Unit) {
        val userId = getCurrentUserId() ?: return

        notesCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("NotesRepo", "Error listener", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val notes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Note::class.java)?.copy(id = doc.id)
                    }
                    onNotesUpdate(notes)
                }
            }
    }

    /**
     * Obtiene todas las notas del usuario de forma asíncrona, sin establecer un listener en tiempo real.
     *
     * @return Un [Result] que contiene una [List] de [Note] ordenadas por la marca de tiempo,
     * o una [Exception] si la operación falla o no hay usuario autenticado.
     */
    suspend fun getAllNotes(): Result<List<Note>> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))
        return try {
            val snapshot = notesCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val notes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Note::class.java)?.copy(id = doc.id)
            }
            Result.success(notes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Guarda una nota nueva o actualiza una nota existente en Firestore.
     *
     * Si la nota no tiene un ID, se añade un nuevo documento. Si ya tiene un ID,
     * se actualiza el documento correspondiente.
     *
     * @param note El objeto [Note] a guardar o actualizar.
     * @return Un [Result] que contiene [Unit] si la operación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun saveNote(note: Note): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))
        return try {
            val noteToSave = if (note.userId.isEmpty()) note.copy(userId = userId) else note

            if (note.id.isEmpty()) {
                notesCollection.add(noteToSave).await()
            } else {
                notesCollection.document(note.id).set(noteToSave).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una nota específica de la base de datos de Firestore.
     *
     * @param noteId El identificador único de la nota a eliminar.
     * @return Un [Result] que contiene [Unit] si la eliminación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            notesCollection.document(noteId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```
---
```
---
### EmotionRepository.kt
package mx.edu.utng.modtrackin.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import mx.edu.utng.modtrackin.data.model.EmotionEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio de datos encargado de la interacción con Firebase Firestore para la gestión
 * de los registros emocionales del usuario ([EmotionEntry]).
 *
 * Esta clase abstrae la fuente de datos (Firebase) y proporciona una interfaz limpia para
 * el manejo de las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) relacionadas con
 * el estado de ánimo.
 */
class EmotionRepository {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    private val collectionName = "emotions"
    private val emotionsCollection = firestore.collection(collectionName)

    /**
     * Obtiene el ID único del usuario actualmente autenticado.
     *
     * @return El ID del usuario actual, o `null` si no hay un usuario autenticado.
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Genera la fecha actual en formato de cadena "yyyy-MM-dd".
     *
     * @return La fecha actual formateada como String.
     */
    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Guarda un nuevo registro emocional en Firestore o actualiza el registro existente
     * para la fecha de hoy.
     *
     * El ID del documento se construye usando el `userId` y la fecha de hoy para garantizar
     * una entrada única por día y por usuario.
     *
     * @param entry El objeto [EmotionEntry] que contiene los datos a guardar.
     * @return Un [Result] que contiene [Unit] si la operación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun saveEmotionEntry(entry: EmotionEntry): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))

        return try {
            val today = getTodayDateString()
            val docId = "${userId}_$today"

            val entryWithIds = entry.copy(
                id = docId,
                userId = userId,
                dateString = today
            )

            emotionsCollection.document(docId).set(entryWithIds).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene el registro emocional guardado por el usuario para el día de hoy.
     *
     * Busca en Firestore usando el ID de documento único construido con el ID de usuario y la fecha actual.
     *
     * @return Un [Result] que contiene el objeto [EmotionEntry] si existe un registro,
     * `null` si no existe, o una [Exception] en caso de error.
     */
    suspend fun getTodayEmotion(): Result<EmotionEntry?> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))

        return try {
            val today = getTodayDateString()
            val docId = "${userId}_$today"

            val document = emotionsCollection.document(docId).get().await()

            if (document.exists()) {
                Result.success(document.toObject(EmotionEntry::class.java))
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene el historial completo de registros emocionales del usuario.
     *
     * Realiza una consulta a Firestore filtrando por el ID de usuario y ordenando los resultados
     * por fecha de forma descendente.
     *
     * @return Un [Result] que contiene una [List] de [EmotionEntry] con el historial del usuario,
     * o una [Exception] en caso de error.
     */
    suspend fun getHistory(): Result<List<EmotionEntry>> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("No user"))

        return try {
            val snapshot = emotionsCollection
                .whereEqualTo("userId", userId)
                .orderBy("dateString", Query.Direction.DESCENDING)
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    val entry = doc.toObject(EmotionEntry::class.java)
                    entry?.copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            }

            Result.success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
```
---
```
---
### SleepRepository.kt
package mx.edu.utng.modtrackin.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mx.edu.utng.modtrackin.data.model.SleepEntry
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Repositorio de datos encargado de la interacción con Firebase Firestore para la gestión
 * de los registros de sueño ([SleepEntry]) del usuario.
 *
 * Proporciona métodos para registrar, actualizar y recuperar el historial de sueño,
 * facilitando el análisis de los patrones de descanso.
 */
class SleepRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val sleepCollection = firestore.collection("sleeps")

    /**
     * Obtiene el ID único del usuario actualmente autenticado.
     *
     * @return El ID del usuario actual.
     * @throws IllegalStateException Si no hay un usuario autenticado.
     */
    private fun getCurrentUserId(): String = Firebase.auth.currentUser?.uid ?: throw IllegalStateException("Usuario no autenticado")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Guarda o actualiza un registro de sueño para el usuario actual.
     *
     * Utiliza la fecha de inicio del registro (`entry.startTime`) para crear un ID compuesto
     * único (`userId_yyyy-MM-dd`), asegurando que solo haya una entrada de sueño por día por usuario.
     *
     * @param entry El objeto [SleepEntry] que contiene los datos de sueño a guardar.
     * @return Un [Result] que contiene [Unit] si la operación fue exitosa, o una [Exception] en caso de fallo.
     */
    suspend fun saveSleepEntry(entry: SleepEntry): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            // Creamos un ID compuesto para garantizar unicidad por usuario y día
            val docId = "${userId}_${dateFormat.format(entry.startTime)}"

            val entryToSave = entry.copy(
                id = docId,
                userId = userId
            )

            sleepCollection.document(docId).set(entryToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene el historial completo de registros de sueño filtrado por el usuario actual.
     *
     * Los resultados se ordenan por la hora de inicio del sueño (`startTime`) de forma descendente.
     *
     * @return Un [Result] que contiene una [List] de [SleepEntry] con el historial,
     * o una [Exception] en caso de fallo.
     */
    suspend fun getSleepHistory(): Result<List<SleepEntry>> {
        val userId = getCurrentUserId()
        return try {
            val snapshot = sleepCollection
                .whereEqualTo("userId", userId) // 🔒 FILTRO DE SEGURIDAD
                .orderBy("startTime", Query.Direction.DESCENDING)
                .get()
                .await()

            val history = snapshot.documents.mapNotNull { doc ->
                doc.toObject(SleepEntry::class.java)?.copy(id = doc.id)
            }
            Result.success(history)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```
---
```
```
---
### 3. En ui/viewmodel
Crea estos archivos y pega sus códigos:
---
```
### TaskViewModel.kt
package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.Task
import mx.edu.utng.modtrackin.data.repository.TaskRepository

/**
 * Representa el estado actual de la UI de la pantalla de Tareas.
 *
 * Contiene la lista de tareas, indicadores de carga y error, y los campos del formulario
 * de edición/creación.
 *
 * @property taskList La lista de todas las [Task] del usuario, observada en tiempo real.
 * @property isLoading Indica si alguna operación de datos (carga o guardado) está en curso.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el repositorio.
 * @property isEditorOpen Indica si la pantalla de creación/edición de tareas está visible.
 * @property id El ID de la tarea que se está editando (vacío si es nueva).
 * @property title El título de la tarea en el formulario.
 * @property description La descripción de la tarea en el formulario.
 * @property category La categoría de la tarea.
 * @property priority La prioridad seleccionada (Alta, Media, Baja).
 * @property dueDate La fecha de vencimiento en formato de cadena.
 * @property reminder La configuración de recordatorio.
 * @property isCompleted El estado de completado de la tarea.
 */
data class TaskUiState(
    val taskList: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditorOpen: Boolean = false,

    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "🎓 Académica",
    val priority: String = "Baja",
    val dueDate: String = "",
    val reminder: String = "",
    val isCompleted: Boolean = false
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Tareas.
 *
 * Es responsable de:
 * 1. Sincronizar la lista de tareas en tiempo real con [TaskRepository].
 * 2. Manejar el ciclo de vida de las tareas (crear, editar, eliminar y marcar como completada).
 * 3. Gestionar la apertura y cierre del editor de tareas.
 */
class TaskViewModel : ViewModel() {

    var uiState by mutableStateOf(TaskUiState())
        private set

    private val repository = TaskRepository()

    init {
        // Ejecuta una carga inicial para asegurar datos rápidos
        refreshTasks()
        // Inicia el listener para recibir actualizaciones en tiempo real
        startListening()
    }

    /**
     * Realiza una carga asíncrona única de todas las tareas del usuario.
     * Se usa principalmente al inicio para asegurar la presencia de datos.
     */
    private fun refreshTasks() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.getAllTasks()
            result.onSuccess { tasks ->
                uiState = uiState.copy(taskList = tasks, isLoading = false)
            }
            result.onFailure { uiState = uiState.copy(isLoading = false) }
        }
    }

    /**
     * Inicia la escucha en tiempo real de los cambios en la colección de tareas del usuario.
     * La lista [taskList] se actualiza automáticamente.
     */
    private fun startListening() {
        repository.listenToTasks { tasks ->
            uiState = uiState.copy(taskList = tasks, isLoading = false)
        }
    }

    /**
     * Abre el editor de tareas para la creación de una **nueva** tarea,
     * limpiando los campos del formulario.
     */
    fun openEditorNew() {
        uiState = uiState.copy(
            isEditorOpen = true,
            id = "", title = "", description = "",
            dueDate = "", reminder = "", isCompleted = false, errorMessage = null,
            // Restablece valores predeterminados para una nueva tarea
            category = "🎓 Académica", priority = "Baja"
        )
    }

    /**
     * Abre el editor de tareas para **modificar** una tarea existente,
     * cargando sus propiedades en el estado del formulario.
     *
     * @param task La [Task] a editar.
     */
    fun openEditorModify(task: Task) {
        uiState = uiState.copy(
            isEditorOpen = true,
            id = task.id, title = task.title, description = task.description,
            category = task.category, priority = task.priority,
            dueDate = task.dueDate, reminder = task.reminder,
            isCompleted = task.isCompleted, errorMessage = null
        )
    }

    /** Cierra el editor de tareas y regresa a la vista de lista. */
    fun closeEditor() {
        uiState = uiState.copy(isEditorOpen = false, errorMessage = null)
    }

    /**
     * Valida la entrada y guarda (crea o actualiza) la tarea en el repositorio.
     * Si la operación es exitosa, cierra el editor.
     */
    fun saveTask() {
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(errorMessage = "El título es obligatorio")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val taskToSave = Task(
                id = uiState.id,
                userId = Firebase.auth.currentUser?.uid ?: "",
                title = uiState.title,
                description = uiState.description,
                category = uiState.category,
                priority = uiState.priority,
                dueDate = uiState.dueDate,
                reminder = uiState.reminder,
                isCompleted = uiState.isCompleted
            )

            val result = repository.saveTask(taskToSave)
            if (result.isSuccess) {
                refreshTasks() // Asegura la actualización inmediata de la lista
                closeEditor()
            } else {
                uiState = uiState.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Elimina la tarea actualmente abierta en el editor del repositorio.
     * Solo se ejecuta si la tarea tiene un ID asignado.
     */
    fun deleteTask() {
        if (uiState.id.isNotEmpty()) {
            viewModelScope.launch {
                uiState = uiState.copy(isLoading = true)
                val result = repository.deleteTask(uiState.id)
                if (result.isSuccess) {
                    refreshTasks() // Asegura la actualización inmediata de la lista
                    closeEditor()
                } else {
                    uiState = uiState.copy(isLoading = false)
                }
            }
        }
    }

    /**
     * Cambia el estado `isCompleted` de una tarea específica y la guarda en el repositorio.
     *
     * @param task La [Task] cuyo estado de completado será invertido.
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            val result = repository.saveTask(updatedTask)
            if (result.isSuccess) {
                // La lista se actualiza automáticamente gracias a startListening,
                // pero refrescar puede ser una medida de seguridad si el listener es lento.
                refreshTasks()
            }
        }
    }

    // --- MANEJO DE ENTRADAS DEL FORMULARIO ---
    /** Actualiza el título en el estado. */
    fun onTitleChange(v: String) { uiState = uiState.copy(title = v) }
    /** Actualiza la descripción en el estado. */
    fun onDescriptionChange(v: String) { uiState = uiState.copy(description = v) }
    /** Actualiza la categoría en el estado. */
    fun onCategoryChange(v: String) { uiState = uiState.copy(category = v) }
    /** Actualiza la prioridad en el estado. */
    fun onPriorityChange(v: String) { uiState = uiState.copy(priority = v) }
    /** Actualiza la fecha de vencimiento en el estado. */
    fun onDueDateChange(v: String) { uiState = uiState.copy(dueDate = v) }
    /** Limpia el mensaje de error de la UI. */
    fun clearError() { uiState = uiState.copy(errorMessage = null) }
}
```
---
```
---
### HabitViewModel.kt
package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.Habit
import mx.edu.utng.modtrackin.data.repository.HabitRepository
import java.time.LocalDate

/**
 * Representa el estado actual de la UI de la pantalla de gestión de hábitos.
 *
 * @property habitList La lista de todos los [Habit] del usuario.
 * @property isLoading Indica si el repositorio está cargando los datos iniciales.
 * @property selectedDate La fecha seleccionada en el dashboard para registrar o ver el progreso.
 * @property isEditorOpen Indica si la pantalla de creación/edición de hábitos está visible.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el formulario o en el repositorio.
 * @property id El ID del hábito que se está editando (vacío si es nuevo).
 * @property title El título del hábito en el formulario.
 * @property description La descripción del hábito en el formulario.
 * @property category La categoría seleccionada para el hábito.
 * @property targetMinutesInput El valor de la meta diaria (en minutos) en formato String.
 */
data class HabitUiState(
    val habitList: List<Habit> = emptyList(),
    val isLoading: Boolean = false,

    // Control de fecha seleccionada
    val selectedDate: LocalDate = LocalDate.now(),

    // Editor
    val isEditorOpen: Boolean = false,
    val errorMessage: String? = null,

    // Campos formulario
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val targetMinutesInput: String = ""
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Hábitos.
 *
 * Es responsable de:
 * 1. Escuchar los cambios en tiempo real de la base de datos para mantener la lista actualizada.
 * 2. Manejar la navegación entre la vista principal y el editor.
 * 3. Actualizar el progreso diario de los hábitos.
 * 4. Gestionar el ciclo de vida (creación, edición y eliminación) de los hábitos.
 */
class HabitViewModel : ViewModel() {

    var uiState by mutableStateOf(HabitUiState())
        private set

    private val repository = HabitRepository()

    init {
        startListening()
    }

    /**
     * Inicia la escucha en tiempo real de los hábitos del usuario en el repositorio.
     *
     * La lista de hábitos se actualiza automáticamente en [uiState.habitList] cuando hay cambios.
     */
    private fun startListening() {
        uiState = uiState.copy(isLoading = true)
        repository.listenToHabits { habits ->
            uiState = uiState.copy(habitList = habits, isLoading = false)
        }
    }

    /**
     * Actualiza la fecha seleccionada en el dashboard.
     * @param newDate La nueva fecha [LocalDate] seleccionada.
     */
    fun changeDate(newDate: LocalDate) {
        uiState = uiState.copy(selectedDate = newDate)
    }

    // --- AGREGAR TIEMPO MANUALMENTE ---
    /**
     * Agrega minutos al registro de progreso de un hábito específico para la fecha seleccionada.
     *
     * Actualiza el mapa de historial del hábito y guarda el cambio en el repositorio.
     *
     * @param habit El [Habit] al que se le va a añadir tiempo.
     * @param minutesToAdd La cantidad de minutos a sumar al progreso del día.
     */
    fun addMinutesToHabit(habit: Habit, minutesToAdd: Int) {
        if (minutesToAdd <= 0) return

        val dateKey = uiState.selectedDate.toString()
        val currentMinutes = habit.history[dateKey] ?: 0
        val newMinutes = currentMinutes + minutesToAdd

        val newHistory = habit.history.toMutableMap()
        newHistory[dateKey] = newMinutes

        viewModelScope.launch {
            val updatedHabit = habit.copy(history = newHistory)
            repository.saveHabit(updatedHabit)
        }
    }

    // --- EDITOR ---
    /** Abre la pantalla del editor para crear un nuevo hábito, limpiando el estado. */
    fun openEditorNew() {
        uiState = uiState.copy(isEditorOpen = true, id = "", title = "", description = "", category = "", targetMinutesInput = "", errorMessage = null)
    }

    /**
     * Abre la pantalla del editor para modificar un hábito existente, cargando sus datos en el estado.
     * @param habit El [Habit] a modificar.
     */
    fun openEditorModify(habit: Habit) {
        uiState = uiState.copy(
            isEditorOpen = true,
            id = habit.id,
            title = habit.title,
            description = habit.description,
            category = habit.category,
            targetMinutesInput = habit.dailyGoal.toString(),
            errorMessage = null
        )
    }

    /** Cierra la pantalla del editor y regresa al dashboard principal. */
    fun closeEditor() {
        uiState = uiState.copy(isEditorOpen = false)
    }

    /** Actualiza el título del hábito en el estado del formulario. */
    fun onTitleChange(v: String) { uiState = uiState.copy(title = v) }

    /** Actualiza la descripción del hábito en el estado del formulario. */
    fun onDescriptionChange(v: String) { uiState = uiState.copy(description = v) }

    /**
     * Selecciona la categoría del hábito. Si el título está vacío, usa el nombre de la categoría como título inicial.
     * @param cat El nombre de la categoría seleccionada.
     */
    fun onCategorySelected(cat: String) {
        val newTitle = if (uiState.title.isEmpty()) cat else uiState.title
        uiState = uiState.copy(category = cat, title = newTitle)
    }

    /**
     * Actualiza la meta de minutos diarios en el estado, aceptando solo dígitos.
     * @param v El nuevo valor (string) de minutos.
     */
    fun onDurationChange(v: String) {
        if (v.all { it.isDigit() }) uiState = uiState.copy(targetMinutesInput = v)
    }

    /**
     * Valida y guarda (crea o actualiza) el hábito actual en el repositorio.
     * Si el título está vacío, establece un [errorMessage].
     */
    fun saveHabit() {
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(errorMessage = "Escribe un nombre")
            return
        }

        viewModelScope.launch {
            // Preserva el historial existente si estamos editando
            val existingHabit = uiState.habitList.find { it.id == uiState.id }
            val history = existingHabit?.history ?: emptyMap()

            val habit = Habit(
                id = uiState.id,
                userId = Firebase.auth.currentUser?.uid ?: "",
                title = uiState.title,
                description = uiState.description,
                category = uiState.category.ifBlank { "Otro" },
                dailyGoal = uiState.targetMinutesInput.toIntOrNull() ?: 60,
                history = history
            )

            val result = repository.saveHabit(habit)
            if (result.isSuccess) closeEditor()
        }
    }

    /** Elimina el hábito actualmente abierto en el editor del repositorio. */
    fun deleteHabit() {
        if (uiState.id.isNotEmpty()) {
            viewModelScope.launch {
                val result = repository.deleteHabit(uiState.id)
                if (result.isSuccess) closeEditor()
            }
        }
    }
}
```
---
```
---
### NotesViewModel.kt
package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.Note
import mx.edu.utng.modtrackin.data.repository.NotesRepository

/**
 * Representa el estado actual de la UI de la pantalla de Notas.
 *
 * @property notesList La lista de todas las [Note] del usuario.
 * @property isLoading Indica si el repositorio está cargando los datos iniciales.
 * @property isEditorOpen Indica si la pantalla de creación/edición de notas está visible.
 * @property currentNote El objeto [Note] que se está viendo o editando en el editor.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el repositorio.
 */
data class NotesUiState(
    val notesList: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isEditorOpen: Boolean = false,
    val currentNote: Note = Note(),
    val errorMessage: String? = null
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Notas.
 *
 * Es responsable de:
 * 1. Escuchar los cambios en tiempo real de la base de datos para mantener la lista actualizada.
 * 2. Gestionar la navegación entre la vista de lista y el editor.
 * 3. Manejar el estado y las acciones de edición, guardado y eliminación de una nota.
 */
class NotesViewModel : ViewModel() {
    var uiState by mutableStateOf(NotesUiState())
        private set

    private val repository = NotesRepository()

    init {
        startListening()
    }

    /**
     * Inicia la escucha en tiempo real de las notas del usuario en el repositorio.
     * La lista [notesList] se mantiene automáticamente sincronizada con la base de datos.
     */
    private fun startListening() {
        uiState = uiState.copy(isLoading = true)
        repository.listenToNotes { notes ->
            uiState = uiState.copy(notesList = notes, isLoading = false)
        }
    }

    /**
     * Recarga la lista de notas del repositorio sin utilizar el listener en tiempo real.
     * Se usa principalmente después de operaciones de guardado/eliminación para asegurar la consistencia.
     */
    private fun refreshNotes() {
        viewModelScope.launch {
            val result = repository.getAllNotes()
            result.onSuccess { notes ->
                uiState = uiState.copy(notesList = notes)
            }
        }
    }

    /**
     * Abre el editor para crear una nueva nota, inicializando [currentNote] a una instancia vacía.
     */
    fun openEditorNew() {
        uiState = uiState.copy(isEditorOpen = true, currentNote = Note(), errorMessage = null)
    }

    /**
     * Abre el editor para modificar una nota existente.
     * @param note El objeto [Note] a cargar y modificar.
     */
    fun openEditorModify(note: Note) {
        uiState = uiState.copy(isEditorOpen = true, currentNote = note, errorMessage = null)
    }

    /**
     * Cierra la pantalla del editor y regresa a la vista de lista.
     */
    fun closeEditor() {
        uiState = uiState.copy(isEditorOpen = false, errorMessage = null)
    }

    /** Actualiza el título de la nota que se está editando en [currentNote]. */
    fun updateTitle(t: String) { uiState = uiState.copy(currentNote = uiState.currentNote.copy(title = t)) }

    /** Actualiza el contenido de la nota que se está editando en [currentNote]. */
    fun updateContent(c: String) { uiState = uiState.copy(currentNote = uiState.currentNote.copy(content = c)) }

    /**
     * Guarda (crea o actualiza) la [currentNote] en el repositorio.
     * Si es exitoso, cierra el editor y recarga la lista.
     */
    fun saveCurrentNote() {
        viewModelScope.launch {
            val result = repository.saveNote(uiState.currentNote)
            if (result.isSuccess) {
                // Aunque el listener se encargaría de la actualización, se llama a refresh para asegurar.
                refreshNotes()
                closeEditor()
            } else {
                uiState = uiState.copy(errorMessage = "Error al guardar")
            }
        }
    }

    /**
     * Elimina la [currentNote] del repositorio, siempre y cuando su ID no esté vacío.
     * Si es exitoso, cierra el editor y recarga la lista.
     */
    fun deleteCurrentNote() {
        if (uiState.currentNote.id.isNotEmpty()) {
            viewModelScope.launch {
                val result = repository.deleteNote(uiState.currentNote.id)
                if (result.isSuccess) {
                    // Aunque el listener se encargaría de la actualización, se llama a refresh para asegurar.
                    refreshNotes()
                    closeEditor()
                } else {
                    uiState = uiState.copy(errorMessage = "Error al eliminar")
                }
            }
        }
    }
}
```
---
```
---
### EmotionViewModel.kt
package mx.edu.utng.modtrackin.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mx.edu.utng.modtrackin.data.model.EmotionEntry
import mx.edu.utng.modtrackin.data.repository.EmotionRepository
import mx.edu.utng.modtrackin.ui.screens.emotions.Emocion

/**
 * Representa el estado actual de la UI de la pantalla de gestión emocional.
 *
 * @property isLoading Indica si alguna operación de datos (Firebase) está en curso.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en el repositorio.
 * @property currentScreen Controla el paso actual en el flujo de registro (1: Principal, 2: Adjetivos, 3: Guardado, 4: Notas, 5: Historial).
 * @property selectedEmotion La [Emocion] seleccionada por el usuario en el Paso 1.
 * @property selectedAdjective El adjetivo seleccionado para matizar la emoción en el Paso 2.
 * @property dailyNote La nota o descripción escrita por el usuario para el registro.
 * @property todayEntry Contiene el [EmotionEntry] del día de hoy, si ya existe un registro.
 * @property history La lista completa de [EmotionEntry] del historial del usuario.
 */
data class EmotionUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentScreen: Int = 1,
    val selectedEmotion: Emocion? = null,
    val selectedAdjective: String = "",
    val dailyNote: String = "",
    val todayEntry: EmotionEntry? = null,
    val history: List<EmotionEntry> = emptyList()
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Agenda Emocional.
 *
 * Es responsable de:
 * 1. Monitorear el estado de autenticación de Firebase para cargar/limpiar datos.
 * 2. Gestionar la navegación del flujo de registro de 4 pasos.
 * 3. Interactuar con [EmotionRepository] para guardar el registro diario y obtener el historial.
 */
class EmotionViewModel : ViewModel() {

    var uiState by mutableStateOf(EmotionUiState())
        private set

    private val repository = EmotionRepository()

    /**
     * Detector de cambios de autenticación. Llama a [loadDataForUser] si el usuario
     * inicia sesión y limpia el estado si cierra sesión.
     */
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser

        if (user != null) {
            // Usuario detectado: Cargamos SUS datos
            loadDataForUser()
        } else {
            // Usuario se fue: Limpieza total
            uiState = EmotionUiState()
        }
    }

    init {
        // Activamos el detector de autenticación al inicializar el ViewModel.
        Firebase.auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        // Eliminamos el listener para evitar fugas de memoria.
        Firebase.auth.removeAuthStateListener(authListener)
    }

    /**
     * Inicia la carga de datos del usuario autenticado: verifica el registro de hoy y trae el historial.
     */
    private fun loadDataForUser() {
        checkTodayEntry()
        fetchHistory()
    }

    // --- LISTAS Y LÓGICA DE NEGOCIO ESTÁTICA ---

    /** Lista de emociones básicas disponibles para la selección inicial. */
    val emocionesList = listOf(
        Emocion("alegre", "😄", "Alegre", Color(0xFFFFF176)),
        Emocion("neutral", "😐", "Neutral", Color(0xFFE0E0E0)),
        Emocion("triste", "😢", "Triste", Color(0xFF90CAF9)),
        Emocion("molesto", "😠", "Molesto", Color(0xFFEF9A9A)),
        Emocion("nervioso", "😰", "Nervioso", Color(0xFFCE93D8))
    )

    /** Mapa que define los adjetivos disponibles para cada ID de emoción. */
    val adjetivosMap = mapOf(
        "alegre" to listOf("Contento", "Entusiasmado", "Satisfecho", "Optimista", "Divertido", "Eufórico"),
        "neutral" to listOf("Indiferente", "Sereno", "Tranquilo", "Impasible", "Objetivo", "Despreocupado"),
        "triste" to listOf("Melancólico", "Desanimado", "Deprimido", "Nostálgico", "Afligido", "Desconsolado"),
        "molesto" to listOf("Irritado", "Frustrado", "Enfadado", "Furioso", "Fastidiado", "Resentido"),
        "nervioso" to listOf("Ansioso", "Inquieto", "Tenso", "Preocupado", "Temeroso", "Alterado")
    )

    // --- OPERACIONES DE DATOS ---

    /**
     * Verifica si el usuario ya tiene un registro emocional guardado para el día de hoy.
     *
     * Si existe, establece [todayEntry] y navega al paso 3 (Registro Guardado).
     * Si no existe, navega al paso 1 (Selección Principal).
     */
    fun checkTodayEntry() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.getTodayEmotion()
            result.onSuccess { entry ->
                if (entry != null) {
                    uiState = uiState.copy(isLoading = false, todayEntry = entry, currentScreen = 3)
                } else {
                    uiState = uiState.copy(isLoading = false, todayEntry = null, currentScreen = 1)
                }
            }
            result.onFailure { uiState = uiState.copy(isLoading = false, errorMessage = "Error al cargar datos") }
        }
    }

    /**
     * Guarda el registro emocional actual ([selectedEmotion], [selectedAdjective], [dailyNote])
     * en el repositorio.
     */
    fun saveEntry() {
        val emocion = uiState.selectedEmotion ?: return
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val newEntry = EmotionEntry(
                emotionId = emocion.id, emotionEmoji = emocion.emoji, emotionText = emocion.texto,
                adjective = uiState.selectedAdjective, note = uiState.dailyNote
            )
            val result = repository.saveEmotionEntry(newEntry)
            result.onSuccess {
                // Actualiza el estado y navega a la vista de guardado exitoso
                uiState = uiState.copy(isLoading = false, todayEntry = newEntry, currentScreen = 3)
                // Recarga el historial para que la lista esté actualizada
                fetchHistory()
            }
            result.onFailure { e -> uiState = uiState.copy(isLoading = false, errorMessage = e.message) }
        }
    }

    /**
     * Obtiene el historial completo de registros emocionales del usuario y lo actualiza en [history].
     */
    fun fetchHistory() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.getHistory()
            result.onSuccess { list -> uiState = uiState.copy(isLoading = false, history = list) }
            result.onFailure { uiState = uiState.copy(isLoading = false, errorMessage = "Error al cargar historial") }
        }
    }

    // --- LÓGICA DE NAVEGACIÓN Y FLUJO ---

    /** Navega a la pantalla de Historial (Paso 5) y asegura que los datos estén cargados. */
    fun goToHistory() { fetchHistory(); uiState = uiState.copy(currentScreen = 5) }

    /**
     * Selecciona una emoción y avanza al paso de selección de adjetivos (Paso 2).
     * @param emocion El objeto [Emocion] seleccionado.
     */
    fun selectEmotion(emocion: Emocion) { uiState = uiState.copy(selectedEmotion = emocion, currentScreen = 2) }

    /**
     * Selecciona un adjetivo y avanza al paso de adición de notas (Paso 4).
     * @param adjetivo El adjetivo seleccionado.
     */
    fun selectAdjective(adjetivo: String) { uiState = uiState.copy(selectedAdjective = adjetivo, currentScreen = 4) }

    /**
     * Actualiza el valor de la nota diaria en el estado de la UI.
     * @param note El nuevo contenido de la nota.
     */
    fun updateNote(note: String) { uiState = uiState.copy(dailyNote = note) }

    /**
     * Navega al paso anterior dentro del flujo de registro, considerando las transiciones
     * desde el historial.
     */
    fun goBack() {
        val previousScreen = when (uiState.currentScreen) {
            5 -> if (uiState.todayEntry != null) 3 else 1 // Desde Historial, vuelve a Guardado o Principal
            4 -> 2 // Desde Notas, vuelve a Adjetivos
            2 -> 1 // Desde Adjetivos, vuelve a Principal
            else -> 1 // Caso predeterminado
        }
        uiState = uiState.copy(currentScreen = previousScreen)
    }

    /**
     * Reinicia completamente el flujo de registro, limpiando las selecciones temporales.
     * Se usa típicamente para comenzar un nuevo registro o editar el actual.
     */
    fun resetFlow() {
        uiState = uiState.copy(currentScreen = 1, selectedEmotion = null, selectedAdjective = "", dailyNote = "", todayEntry = null)
    }
}
```
---
```
---
### SleepViewModel.kt
package mx.edu.utng.modtrackin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * Clase de datos que representa un registro de sueño recuperado del historial.
 *
 * @property id El identificador único del documento en Firestore.
 * @property date La fecha del registro (YYYY-MM-DD).
 * @property startTime La hora de inicio del sueño (HH:MM).
 * @property endTime La hora de despertar (HH:MM).
 * @property quality La calificación de calidad del sueño (1-5).
 * @property durationHours La duración total calculada del sueño en horas.
 */
data class SleepEntry(
    val id: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val quality: Int = 0,
    val durationHours: Double = 0.0
)

/**
 * Representa el estado actual de la UI de la pantalla de Registro de Sueño.
 *
 * @property startTime La hora de dormir seleccionada en el selector.
 * @property endTime La hora de despertar seleccionada en el selector.
 * @property quality La calificación de calidad seleccionada (1 a 5).
 * @property sleepHistory La lista de los últimos registros de sueño del usuario.
 * @property isLoadingHistory Indica si la lista de historial se está cargando.
 * @property isSaving Indica si la operación de guardar está en curso.
 * @property isSaved Indica si la última operación de guardar fue exitosa.
 * @property errorMessage Contiene un mensaje de error si ocurre una falla en Firebase.
 */
data class SleepUiState(
    val startTime: LocalTime = LocalTime.of(22, 0),
    val endTime: LocalTime = LocalTime.of(7, 0),
    val quality: Int = 3,
    val sleepHistory: List<SleepEntry> = emptyList(),
    val isLoadingHistory: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel que gestiona la lógica de negocio y el estado de la pantalla de Registro de Sueño.
 *
 * Es responsable de:
 * 1. Capturar las horas de inicio/fin y la calidad del sueño.
 * 2. Guardar y eliminar registros de sueño en Firestore.
 * 3. Cargar y mantener actualizado el historial de sueño del usuario.
 * 4. Gestionar los estados de carga y errores de la UI.
 */
class SleepViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSleepHistory()
    }

    // --- FUNCIONES DE ACTUALIZACIÓN DE UI ---
    /** Actualiza la hora de inicio del sueño en el estado. */
    fun updateStartTime(newTime: LocalTime) {
        _uiState.update { it.copy(startTime = newTime) }
    }

    /** Actualiza la hora de despertar en el estado. */
    fun updateEndTime(newTime: LocalTime) {
        _uiState.update { it.copy(endTime = newTime) }
    }

    /**
     * Actualiza la calificación de calidad del sueño, asegurando que esté dentro del rango de 1 a 5.
     */
    fun updateQuality(newQuality: Int) {
        _uiState.update { it.copy(quality = newQuality.coerceIn(1, 5)) }
    }

    // --- FUNCIÓN PARA CARGAR HISTORIAL ---
    /**
     * Carga el historial de registros de sueño del usuario desde Firestore.
     *
     * Ordena los resultados por fecha descendente y limita la consulta a 30 registros.
     * También calcula la duración en horas.
     */
    private fun loadSleepHistory() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            _uiState.update { it.copy(errorMessage = "Usuario no autenticado para cargar historial.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingHistory = true) }
            Firebase.firestore.collection("sleep_entries")
                .whereEqualTo("userId", user.uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnSuccessListener { documents ->
                    val history = documents.mapNotNull { doc ->
                        // Lógica para manejar el cálculo de duración, incluyendo el cruce de medianoche
                        val start = LocalTime.parse(doc.getString("startTime"))
                        val end = LocalTime.parse(doc.getString("endTime"))
                        val durationMinutes = if (end.isAfter(start)) {
                            ChronoUnit.MINUTES.between(start, end)
                        } else {
                            val minutesUntilMidnight = ChronoUnit.MINUTES.between(start, LocalTime.MAX) + 1
                            val minutesAfterMidnight = ChronoUnit.MINUTES.between(LocalTime.MIN, end)
                            (minutesUntilMidnight + minutesAfterMidnight).coerceAtLeast(0)
                        }
                        val duration = durationMinutes / 60.0

                        SleepEntry(
                            id = doc.id,
                            date = doc.getString("date") ?: "",
                            startTime = doc.getString("startTime") ?: "",
                            endTime = doc.getString("endTime") ?: "",
                            quality = (doc.getLong("quality") ?: 0).toInt(),
                            durationHours = duration
                        )
                    }
                    _uiState.update { it.copy(sleepHistory = history, isLoadingHistory = false) }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoadingHistory = false) }
                    Log.e("SleepViewModel", "Error loading history", e)
                }
        }
    }

    // --- FUNCIÓN PARA GUARDAR ---
    /**
     * Guarda el registro de sueño actual (horas seleccionadas y calidad) en Firestore.
     *
     * Utiliza la fecha actual y guarda las horas como Strings. Recarga el historial tras el éxito.
     */
    fun saveSleepEntry() {
        viewModelScope.launch {
            val user = Firebase.auth.currentUser ?: return@launch
            _uiState.update { it.copy(isSaving = true) }

            val currentState = _uiState.value
            val sleepEntry = hashMapOf(
                "userId" to user.uid,
                "date" to LocalDate.now().toString(),
                "startTime" to currentState.startTime.toString(),
                "endTime" to currentState.endTime.toString(),
                "quality" to currentState.quality
            )

            Firebase.firestore.collection("sleep_entries")
                .add(sleepEntry)
                .addOnSuccessListener {
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                    loadSleepHistory() // Recarga el historial para mostrar el nuevo registro
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(isSaving = false, errorMessage = e.message) }
                }
        }
    }

    // --- FUNCIÓN PARA ELIMINAR ---
    /**
     * Elimina un registro de sueño específico de Firestore.
     *
     * Recarga el historial tras la eliminación exitosa.
     *
     * @param entryId El ID del documento de registro de sueño a eliminar.
     */
    fun deleteSleepEntry(entryId: String) {
        viewModelScope.launch {
            Firebase.firestore.collection("sleep_entries").document(entryId)
                .delete()
                .addOnSuccessListener {
                    Log.d("SleepViewModel", "Document $entryId successfully deleted!")
                    loadSleepHistory()
                }
                .addOnFailureListener { e ->
                    Log.w("SleepViewModel", "Error deleting document", e)
                    _uiState.update { it.copy(errorMessage = "Error al eliminar el registro.") }
                }
        }
    }

    // --- FUNCIONES DE ESTADO ---
    /** Restablece el indicador de éxito de guardado ([isSaved]). */
    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }

    /** Limpia el mensaje de error de la UI. */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
```
---
```
---
### LoginViewModel.kt
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
```
---
```
---
### RegisterViewModel.kt
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
```
---
```
---
### 4. En utils
Crea estos archivos y pega sus códigos:
---
### NotificationHelper.kt
package mx.edu.utng.modtrackin.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Objeto de utilidad que contiene las constantes de configuración del canal de notificación
 * de la aplicación y la lógica para crear dicho canal en Android O (API 26) o superior.
 */
object NotificationHelper {
    /** El identificador único del canal de notificación. */
    const val CHANNEL_ID = "modtrackin_channel"
    /** El nombre visible para el usuario del canal de notificación. */
    const val CHANNEL_NAME = "Notificaciones ModTrackin"
    /** Descripción mostrada al usuario en la configuración del sistema. */
    const val CHANNEL_DESCRIPTION = "Recordatorios de tareas y motivación"
    /** Un ID de notificación genérico si se requiere un identificador fijo. */
    const val NOTIFICATION_ID = 1

    /**
     * Crea y registra el canal de notificación del sistema.
     *
     * Este paso es obligatorio para que las notificaciones se muestren en Android 8.0 (Oreo) o superior.
     * El canal está configurado con prioridad alta, luces y vibración.
     *
     * @param context El contexto de la aplicación, necesario para acceder a [NotificationManager].
     */
    fun createNotificationChannel(context: Context) {
        // La creación de canales solo es necesaria a partir de la API 26 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            // Obtiene el servicio de gestión de notificaciones y registra el canal
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
```
---
```
---
### AppNotificationManager.kt
package mx.edu.utng.modtrackin.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mx.edu.utng.modtrackin.R // Asegúrate de que este import sea correcto

class AppNotificationManager(private val context: Context) {

    fun sendNotification(title: String, message: String) {
        if (!checkPermission()) return

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(
                System.currentTimeMillis().toInt(),
                notification
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
---
### NotificationReceiver.kt
package mx.edu.utng.modtrackin.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receptor de difusión (BroadcastReceiver) utilizado para manejar la recepción de intents
 * programados (típicamente a través de AlarmManager) y disparar una notificación.
 *
 * Esta clase es el punto de entrada que se activa fuera de la aplicación principal
 * cuando un evento de tiempo o sistema ocurre.
 */
class NotificationReceiver : BroadcastReceiver() {

    /**
     * Método invocado cuando se recibe un intent de difusión.
     *
     * Extrae el título y el mensaje del intent y utiliza [AppNotificationManager] para
     * enviar la notificación al usuario.
     *
     * @param context El contexto en el que se ejecuta el receptor.
     * @param intent El objeto [Intent] que contiene la acción y los datos extra de la notificación
     * (título y mensaje).
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Extrae el título de los extras del intent, usando un valor predeterminado si no se encuentra.
        val title = intent.getStringExtra("title") ?: "Recordatorio"
        // Extrae el mensaje de los extras del intent.
        val message = intent.getStringExtra("message") ?: ""

        // Utiliza el gestor de notificaciones previamente definido para mostrar el mensaje.
        val manager = AppNotificationManager(context)
        manager.sendNotification(title, message)
    }
}
```
---
```
---
### NotificationScheduler.kt
package mx.edu.utng.modtrackin.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Clase de utilidad encargada de programar notificaciones recurrentes o exactas
 * utilizando el servicio [AlarmManager] del sistema Android.
 *
 * Incluye la lógica necesaria para solicitar el permiso `SCHEDULE_EXACT_ALARM`
 * en versiones de Android S (API 31) o superiores.
 *
 * @param context El contexto de la aplicación, necesario para acceder a servicios y lanzar Intents.
 */
class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Programa una notificación diaria recurrente para recordar al usuario el registro emocional.
     *
     * La alarma se establece para las 20:00 (8 PM) de la hora local del dispositivo.
     * Si la hora actual ya ha pasado, se programa para el día siguiente.
     * Requiere el permiso `SCHEDULE_EXACT_ALARM` en versiones recientes.
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleDailyMoodReminder() {
        if (!canScheduleAlarm()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", "¡Nuevo día, nuevas emociones! \uD83D\uDE01")
            putExtra("message", "Ya puedes hacer un nuevo registro de tus emociones.")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001, // Código único para la alarma diaria de ánimo
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20) // 8 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }


        if (Calendar.getInstance().after(calendar)) {
            // Si la hora de hoy ya pasó, programar para mañana.
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            // setRepeating se usa para alarmas diarias periódicas
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Programa una notificación exacta para recordar la fecha límite de una tarea.
     *
     * La alarma se establece a las 09:00 AM del día especificado en `dueDateString`.
     * Utiliza `setExactAndAllowWhileIdle` para garantizar precisión, si es posible.
     *
     * @param taskId El ID único de la tarea, utilizado como código de solicitud para el [PendingIntent].
     * @param taskTitle El título de la tarea, incluido en la notificación.
     * @param dueDateString La fecha de vencimiento en formato "yyyy-MM-dd".
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleTaskNotification(taskId: String, taskTitle: String, dueDateString: String) {
        if (!canScheduleAlarm()) {
            Toast.makeText(context, "Falta permiso de alarmas exactas", Toast.LENGTH_SHORT).show()
            return
        }

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = try {
            format.parse(dueDateString)
        } catch (e: Exception) {
            null
        }

        if (date == null) return

        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 9) // Recordatorio a las 9 AM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }


        // No programar si la fecha/hora ya pasó
        if (calendar.timeInMillis < System.currentTimeMillis()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", "⏳ Tarea por vencer: $taskTitle")
            putExtra("message", "Hoy es la fecha límite. ¡Tú puedes!")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode(), // Usa el hash del ID de la tarea como código de solicitud único
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // setExactAndAllowWhileIdle se usa para alarmas precisas que deben dispararse incluso en Doze Mode.
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Toast.makeText(context, "Recordatorio programado para el $dueDateString", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Scheduler", "Error al programar", e)
        }
    }

    /**
     * Verifica si la aplicación tiene el permiso `SCHEDULE_EXACT_ALARM`.
     *
     * Para Android S (API 31) y superiores, si no tiene el permiso, lanza un Intent
     * para llevar al usuario a la configuración del sistema para solicitarlo.
     *
     * @return `true` si el permiso está concedido o no es necesario (API < 31); `false` si el permiso falta y se solicitó.
     */
    private fun canScheduleAlarm(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Si el permiso falta, abre la configuración para que el usuario lo conceda
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                false
            } else {
                true
            }
        } else {
            // Permiso implícito en versiones anteriores a S
            true
        }
    }
}
```
---
### 5. En navigation
Crea estos archivos y pega sus códigos:
---
```
### NavRoutes.kt
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
```
---
```
---
### AppNavigation.kt
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
---
```
---
### 6. En ui/screens
Aquí organiza por subcarpetas si quieres ser ordenado, o ponlos todos directo. Crea los archivos y pega los códigos de las pantallas:
---
```
### LoginScreen.kt
package mx.edu.utng.modtrackin.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.viewmodel.LoginViewModel

/**
 * Pantalla de inicio de sesión de la aplicación.
 *
 * Permite al usuario ingresar sus credenciales (correo y contraseña) y maneja la autenticación
 * a través de [LoginViewModel]. Muestra mensajes de error y navega a la pantalla de inicio
 * tras una autenticación exitosa.
 *
 * @param navController El controlador de navegación para transicionar a la pantalla de registro
 * o a la pantalla de inicio.
 * @param loginViewModel El ViewModel que gestiona el estado de la UI y la lógica de autenticación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState = loginViewModel.uiState
    val context = LocalContext.current

    var passwordVisible by remember { mutableStateOf(false) }

    // Efecto que se dispara al cambiar el estado de loginSuccess en el ViewModel.
    // Navega a HOME_SCREEN y limpia la pila de navegación.
    LaunchedEffect(key1 = uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            navController.navigate(NavRoutes.HOME_SCREEN) {
                // Elimina la pantalla de login de la pila
                popUpTo(NavRoutes.LOGIN_SCREEN) { inclusive = true }
            }
        }
    }

    // Efecto que se dispara cuando hay un mensaje de error.
    // Muestra un Toast con el mensaje de error y luego limpia el estado del error en el ViewModel.
    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            loginViewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Icono de la aplicación
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 10.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "📅", fontSize = 50.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bienvenido a",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "ModTrackin",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Tu agenda inteligente 🚀",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Ver contraseña")
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Iniciar Sesión
            Button(
                onClick = { loginViewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading // Deshabilita el botón durante la carga
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Enlace a Registro
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes cuenta?", color = MaterialTheme.colorScheme.onSurface)
                TextButton(
                    onClick = { navController.navigate(NavRoutes.REGISTER_SCREEN) },
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        "Regístrate",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
```
---
```
---
### RegisterScreen.kt
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
```
---
```
---
### HomeScreen.kt
package mx.edu.utng.modtrackin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.viewmodel.EmotionViewModel
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.NightsStay // Importación para el ícono de sueño

/**
 * Pantalla principal (Dashboard) de la aplicación.
 *
 * Muestra un saludo personalizado, opciones de navegación rápida a las funcionalidades
 * principales (Emociones, Tareas, Notas, Hábitos, Sueño, Calendario) y un menú para
 * cerrar la sesión del usuario.
 *
 * @param navController El controlador de navegación utilizado para transicionar a otras pantallas.
 * @param emotionViewModel El ViewModel para obtener el estado emocional actual del día
 * y mostrarlo en la tarjeta principal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    emotionViewModel: EmotionViewModel = viewModel()
) {
    val emotionState = emotionViewModel.uiState
    val todayEntry = emotionState.todayEntry

    var showMenu by remember { mutableStateOf(false) }
    val user = Firebase.auth.currentUser
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hola, ${user?.email?.split("@")?.get(0) ?: "Usuario"}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Vamos a ser productivos 🚀",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(45.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("👤", fontSize = 24.sp)
                                }
                            }
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            DropdownMenuItem(
                                text = { Text(user?.email ?: "Sin correo", color = MaterialTheme.colorScheme.onSurface) },
                                onClick = { },
                                leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.primary) },
                                enabled = false
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMenu = false
                                    Firebase.auth.signOut()
                                    navController.navigate(NavRoutes.LOGIN_SCREEN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                leadingIcon = { Icon(Icons.Default.ExitToApp, null, tint = MaterialTheme.colorScheme.error) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            val emotionGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFF9800), Color(0xFFFF5722))
            )

            // 1. TARJETA DE EMOCIONES (HERO)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable { navController.navigate(NavRoutes.EMOTIONS_SCREEN) }
                    .clip(RoundedCornerShape(24.dp)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().background(emotionGradient)) {
                    Row(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = todayEntry?.emotionEmoji ?: "🤔",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = "Estado de ánimo",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = todayEntry?.adjective ?: "Registrar ahora",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // --- 2. ACCESOS RÁPIDOS EN GRID (FILA 1) ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(160.dp)
            ) {
                // TAREAS
                ColorfulCard(
                    title = "Tareas",
                    icon = Icons.Default.CheckCircle,
                    gradient = Brush.linearGradient(listOf(Color(0xFF43A047), Color(0xFF1DE9B6))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.TASKS_SCREEN) }
                )

                // NOTAS
                ColorfulCard(
                    title = "Notas",
                    icon = Icons.Default.Edit,
                    gradient = Brush.linearGradient(listOf(Color(0xFF7B1FA2), Color(0xFFE040FB))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.NOTES_SCREEN) }
                )
            }

            // --- 3. ACCESOS RÁPIDOS EN GRID (FILA 2) ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(140.dp)
            ) {
                // HÁBITOS
                ColorfulCard(
                    title = "Hábitos",
                    icon = Icons.Default.Repeat,
                    gradient = Brush.linearGradient(listOf(Color(0xFFEF6C00), Color(0xFFFFB74D))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.HABITS_SCREEN) }
                )

                // SUEÑO (NUEVA FUNCIÓN)
                ColorfulCard(
                    title = "Sueño",
                    icon = Icons.Default.NightsStay,
                    gradient = Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF4A69A5))),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(NavRoutes.SLEEP_SCREEN) }
                )
            }

            // --- 4. CALENDARIO (ACCESO FULL WIDTH) ---
            ColorfulCard(
                title = "Calendario",
                icon = Icons.Default.DateRange,
                gradient = Brush.linearGradient(listOf(Color(0xFF1976D2), Color(0xFF2196F3))),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                onClick = { navController.navigate(NavRoutes.CALENDAR_SCREEN) },
                iconSize = 40.dp,
                isHorizontal = true
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- COMPONENTE COLORIDO (SIN CAMBIOS) ---
/**
 * Componente composable reutilizable para crear tarjetas de acceso rápido con gradientes de color.
 *
 * Muestra un icono y un título sobre un fondo con un [Brush] de gradiente.
 *
 * @param title El texto que se muestra en la tarjeta.
 * @param icon El icono [ImageVector] que se muestra en la tarjeta.
 * @param gradient El objeto [Brush] que define el color de fondo del gradiente.
 * @param modifier Modificador de Composable para aplicar a la tarjeta.
 * @param iconSize El tamaño del icono.
 * @param isHorizontal Si es `true`, el contenido se organiza horizontalmente (útil para tarjetas de ancho completo).
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta.
 */
@Composable
fun ColorfulCard(
    title: String,
    icon: ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier,
    iconSize: androidx.compose.ui.unit.Dp = 48.dp,
    isHorizontal: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            if (isHorizontal) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, null, modifier = Modifier.size(iconSize), tint = Color.White)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icon, null, modifier = Modifier.size(iconSize), tint = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
```
---
```
---
### TasksScreen.kt (Incluye TasksListScreen, TaskEditorScreen, TaskCard)
package mx.edu.utng.modtrackin.ui.screens.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Task
import mx.edu.utng.modtrackin.ui.viewmodel.TaskViewModel
import java.time.Instant
import java.time.ZoneId

/**
 * Punto de entrada principal para la gestión de tareas pendientes.
 *
 * Actúa como un switch que decide si mostrar la lista de tareas ([TasksListScreen]) o
 * la pantalla de edición ([TaskEditorScreen]) basándose en el estado [isEditorOpen]
 * del [TaskViewModel].
 *
 * @param navController El controlador de navegación para volver a la pantalla anterior.
 * @param taskViewModel El ViewModel que gestiona el estado y la lógica de las tareas.
 */
@Composable
fun TasksScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = viewModel()
) {
    val uiState = taskViewModel.uiState

    if (uiState.isEditorOpen) {
        TaskEditorScreen(taskViewModel = taskViewModel)
    } else {
        TasksListScreen(
            uiState = uiState,
            onAddClick = { taskViewModel.openEditorNew() },
            onEditClick = { task -> taskViewModel.openEditorModify(task) },
            onToggleComplete = { task -> taskViewModel.toggleTaskCompletion(task) },
            onBack = { navController.popBackStack() }
        )
    }
}

/**
 * Pantalla que muestra el listado de todas las tareas del usuario.
 *
 * Muestra la lista en un [LazyColumn], maneja el estado de carga y el caso de lista vacía.
 * Incluye un Floating Action Button para agregar nuevas tareas.
 *
 * @param uiState El estado de la UI del TaskViewModel.
 * @param onAddClick Lambda para iniciar el proceso de agregar una nueva tarea.
 * @param onEditClick Lambda que se invoca al hacer clic en una tarjeta de tarea para editarla.
 * @param onToggleComplete Lambda para cambiar el estado de completado de una tarea.
 * @param onBack Lambda para volver a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    uiState: mx.edu.utng.modtrackin.ui.viewmodel.TaskUiState,
    onAddClick: () -> Unit,
    onEditClick: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Tareas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Tarea")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            if (uiState.isLoading && uiState.taskList.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            } else if (uiState.taskList.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📭", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No hay tareas pendientes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = uiState.taskList, key = { it.id.ifEmpty { it.hashCode() } }) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { onToggleComplete(task) },
                            onClick = { onEditClick(task) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente Composable que representa una tarjeta de tarea individual en la lista.
 *
 * Muestra el estado de completado, título, descripción, prioridad y fecha de vencimiento.
 * Es clickeable para abrir la edición.
 *
 * @param task El objeto [Task] cuyos datos se van a mostrar.
 * @param onToggleComplete Lambda para cambiar el estado `isCompleted` de la tarea.
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta para abrir el editor.
 */
@Composable
fun TaskCard(task: Task, onToggleComplete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.Top) {
            IconButton(
                onClick = onToggleComplete,
                modifier = Modifier.size(24.dp).offset(y = (-2).dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Completar",
                    tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                )
                if (task.description.isNotBlank()) {
                    Text(text = task.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Chip de Prioridad
                    SuggestionChip(
                        onClick = {},
                        label = { Text(task.priority, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color.Transparent)
                    )
                    // Chip de Fecha de Vencimiento
                    if (task.dueDate.isNotBlank()) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("📅 ${task.dueDate}", fontSize = 10.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = null
                        )
                    }
                }
            }
        }
    }
}

/**
 * Pantalla de edición de tareas, utilizada tanto para crear una nueva como para modificar una existente.
 *
 * Contiene campos de texto para título, descripción, un selector de fecha y un selector de prioridad.
 * Proporciona acciones de guardar, cancelar y eliminar en la AppBar.
 *
 * @param taskViewModel El ViewModel que gestiona el estado de edición y las acciones de la tarea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(taskViewModel: TaskViewModel) {
    val uiState = taskViewModel.uiState
    val scrollState = rememberScrollState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Diálogo de selección de fecha
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convierte milisegundos a fecha en formato UTC (LocalDate)
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                        taskViewModel.onDueDateChange(date.toString())
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.id.isEmpty()) "Nueva Tarea" else "Editar Tarea", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { taskViewModel.closeEditor() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (uiState.id.isNotEmpty()) {
                        IconButton(onClick = { taskViewModel.deleteTask() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF9A9A))
                        }
                    }
                    IconButton(onClick = { taskViewModel.saveTask() }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val inputColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )

            OutlinedTextField(
                value = uiState.title, onValueChange = { taskViewModel.onTitleChange(it) },
                label = { Text("Título") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                colors = inputColors, shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = uiState.description, onValueChange = { taskViewModel.onDescriptionChange(it) },
                label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(120.dp), maxLines = 5,
                colors = inputColors, shape = RoundedCornerShape(12.dp)
            )

            // Campo de Fecha de Vencimiento con botón de DatePicker
            OutlinedTextField(
                value = uiState.dueDate,
                onValueChange = { taskViewModel.onDueDateChange(it) },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = inputColors,
                shape = RoundedCornerShape(12.dp),
                readOnly = true // Se usa el DatePicker para establecer el valor
            )

            Text("Prioridad", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
            // Chips para selección de prioridad
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Alta", "Media", "Baja").forEach { priority ->
                    FilterChip(
                        selected = uiState.priority == priority,
                        onClick = { taskViewModel.onPriorityChange(priority) },
                        label = { Text(priority) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = MaterialTheme.colorScheme.outline,
                            selected = uiState.priority == priority,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            enabled = true
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { taskViewModel.saveTask() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(if (uiState.id.isEmpty()) "Guardar Tarea" else "Actualizar Tarea", fontWeight = FontWeight.Bold)
            }
        }
    }
}
```
---
```
---
### HabitScreen.kt
package mx.edu.utng.modtrackin.ui.screens.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Habit
import mx.edu.utng.modtrackin.ui.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Clase de datos que define una opción de categoría para un hábito.
 *
 * @property name El nombre descriptivo de la categoría (ej. "Ejercicio").
 * @property icon El icono de Compose Material asociado a la categoría.
 * @property color El color utilizado para la representación visual de la categoría.
 */
data class HabitCategoryOption(val name: String, val icon: ImageVector, val color: Color)

/**
 * Lista predefinida de categorías disponibles para clasificar los hábitos.
 */
val categories = listOf(
    HabitCategoryOption("Ejercicio", Icons.Default.FitnessCenter, Color(0xFF42A5F5)),
    HabitCategoryOption("Estudiar", Icons.Default.School, Color(0xFFAB47BC)),
    HabitCategoryOption("Comer sano", Icons.Default.Restaurant, Color(0xFF66BB6A)),
    HabitCategoryOption("Leer", Icons.Default.MenuBook, Color(0xFFFFCA28)),
    HabitCategoryOption("Descanso", Icons.Default.SelfImprovement, Color(0xFF26C6DA)),
    HabitCategoryOption("Otro", Icons.Default.Star, Color.Gray)
)

/**
 * Punto de entrada principal para la pantalla de gestión de hábitos.
 *
 * Actúa como un switch que decide si mostrar la vista principal de seguimiento ([HabitDashboardScreen])
 * o la vista de creación/edición ([HabitEditorScreen]) basándose en el estado del ViewModel.
 *
 * @param navController El controlador de navegación para cambiar de pantallas.
 * @param viewModel El ViewModel que gestiona el estado y la lógica de los hábitos.
 */
@Composable
fun HabitScreen(
    navController: NavController,
    viewModel: HabitViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    if (uiState.isEditorOpen) {
        HabitEditorScreen(viewModel)
    } else {
        HabitDashboardScreen(
            habits = uiState.habitList,
            selectedDate = uiState.selectedDate,
            onDateChange = { viewModel.changeDate(it) },
            onAddHabit = { viewModel.openEditorNew() },
            onEditHabit = { viewModel.openEditorModify(it) },
            onAddMinutes = { habit, mins -> viewModel.addMinutesToHabit(habit, mins) },
            onBack = { navController.popBackStack() }
        )
    }
}

// --- PANTALLA PRINCIPAL ---
/**
 * Pantalla de seguimiento y control diario de hábitos.
 *
 * Muestra un selector de fecha, un resumen del total de minutos dedicados y una lista
 * de tarjetas ([HabitControlCard]) para registrar el progreso de cada hábito.
 *
 * @param habits La lista de todos los hábitos del usuario.
 * @param selectedDate La fecha actual seleccionada para registrar el progreso.
 * @param onDateChange Lambda para cambiar la fecha seleccionada.
 * @param onAddHabit Lambda para abrir el editor para un nuevo hábito.
 * @param onEditHabit Lambda para abrir el editor para modificar un hábito existente.
 * @param onAddMinutes Lambda para agregar minutos de progreso a un hábito específico.
 * @param onBack Lambda para navegar hacia atrás.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDashboardScreen(
    habits: List<Habit>,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onAddHabit: () -> Unit,
    onEditHabit: (Habit) -> Unit,
    onAddMinutes: (Habit, Int) -> Unit,
    onBack: () -> Unit
) {
    val totalMinutesToday = habits.sumOf { it.history[selectedDate.toString()] ?: 0 }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Hábitos", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabit,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, "Nuevo") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            DateSelector(selectedDate, onDateChange)
            Spacer(modifier = Modifier.height(16.dp))

            // Resumen Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total de tiempo dedicado a tus habitos", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        text = formatMinutes(totalMinutesToday),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay hábitos. ¡Crea uno!", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = habits, key = { it.id }) { habit ->
                        HabitControlCard(
                            habit = habit,
                            selectedDate = selectedDate,
                            onEditClick = { onEditHabit(habit) },
                            onAddMinutes = { mins -> onAddMinutes(habit, mins) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente que permite navegar entre las fechas.
 *
 * Muestra la fecha seleccionada (o "Hoy") y proporciona botones para avanzar o retroceder un día.
 *
 * @param selectedDate La fecha actual seleccionada.
 * @param onDateChange Lambda para manejar el cambio de fecha.
 */
@Composable
fun DateSelector(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onDateChange(selectedDate.minusDays(1)) }) {
            Icon(Icons.Default.ArrowBackIosNew, null, tint = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = if (selectedDate == LocalDate.now()) "Hoy" else selectedDate.format(DateTimeFormatter.ofPattern("EEEE", Locale("es", "ES"))).replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(onClick = { onDateChange(selectedDate.plusDays(1)) }) {
            Icon(Icons.Default.ArrowForwardIos, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Tarjeta individual para visualizar el estado y registrar el progreso de un hábito específico.
 *
 * Muestra la categoría, el título, el progreso actual vs. la meta y un campo para añadir minutos.
 *
 * @param habit El objeto [Habit] a controlar.
 * @param selectedDate La fecha actual seleccionada para buscar el progreso en [habit.history].
 * @param onEditClick Lambda que se ejecuta al hacer clic en la tarjeta para abrir el editor.
 * @param onAddMinutes Lambda que se ejecuta al presionar "Agregar" para registrar el tiempo.
 */
@Composable
fun HabitControlCard(
    habit: Habit,
    selectedDate: LocalDate,
    onEditClick: () -> Unit,
    onAddMinutes: (Int) -> Unit
) {
    val dateKey = selectedDate.toString()
    val currentMinutes = habit.history[dateKey] ?: 0
    val catOption = categories.find { it.name == habit.category } ?: categories.last()

    var minutesToAdd by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().clickable { onEditClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = catOption.color,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(catOption.icon, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(habit.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    // Muestra: "Llevas: Xh Ym / Meta: Zh"
                    Text(
                        "Llevas: ${formatMinutes(currentMinutes)} / Meta: ${formatMinutes(habit.dailyGoal)}",
                        fontSize = 12.sp,
                        color = if (currentMinutes >= habit.dailyGoal) catOption.color else Color.Gray,
                        fontWeight = if (currentMinutes >= habit.dailyGoal) FontWeight.Bold else FontWeight.Normal
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECCIÓN DE AGREGAR TIEMPO (CAMPO DE TEXTO) ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = minutesToAdd,
                    onValueChange = { if (it.all { char -> char.isDigit() }) minutesToAdd = it },
                    placeholder = { Text("Minutos") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = catOption.color,
                        cursorColor = catOption.color,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        val mins = minutesToAdd.toIntOrNull() ?: 0
                        if (mins > 0) {
                            onAddMinutes(mins)
                            minutesToAdd = "" // Limpiar campo
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = catOption.color),
                    shape = RoundedCornerShape(8.dp),
                    enabled = minutesToAdd.isNotEmpty()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
        }
    }
}

/**
 * Función de utilidad que convierte un total de minutos en un formato legible de horas y minutos.
 *
 * @param minutes La cantidad total de minutos.
 * @return Una cadena de texto formateada (ej. "1h 30m" o "45m").
 */
fun formatMinutes(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

// --- PANTALLA 2: EDITOR ---
/**
 * Pantalla para crear un nuevo hábito o editar uno existente.
 *
 * Permite al usuario seleccionar la categoría, ingresar el título, la descripción y la meta diaria.
 *
 * @param viewModel El ViewModel utilizado para gestionar la lógica del editor, incluyendo
 * la gestión de estado de los campos de texto y las acciones de guardar/eliminar/cerrar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditorScreen(viewModel: HabitViewModel) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (uiState.id.isEmpty()) "Nuevo Hábito" else "Editar Hábito", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { viewModel.closeEditor() }) { Icon(Icons.Default.ArrowBack, "Volver") } },
                actions = {
                    if (uiState.id.isNotEmpty()) {
                        IconButton(onClick = { viewModel.deleteHabit() }) { Icon(Icons.Default.Delete, "Borrar", tint = Color(0xFFEF9A9A)) }
                    }
                    IconButton(onClick = { viewModel.saveHabit() }) { Icon(Icons.Default.Check, "Guardar") }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground, navigationIconContentColor = MaterialTheme.colorScheme.primary, actionIconContentColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(padding).padding(16.dp)
        ) {
            Text("Categoría", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                items(categories) { category ->
                    FilterChip(
                        selected = uiState.category == category.name,
                        onClick = { viewModel.onCategorySelected(category.name) },
                        label = { Text(category.name) },
                        leadingIcon = { Icon(category.icon, null, modifier = Modifier.size(18.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = category.color.copy(alpha = 0.3f),
                            selectedLabelColor = category.color,
                            selectedLeadingIconColor = category.color
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )

            OutlinedTextField(
                value = uiState.title, onValueChange = { viewModel.onTitleChange(it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = colors, shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.targetMinutesInput, onValueChange = { viewModel.onDurationChange(it) }, label = { Text("Meta diaria (minutos)") }, modifier = Modifier.fillMaxWidth(), colors = colors, shape = RoundedCornerShape(12.dp), placeholder = { Text("Ej: 60") }
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = uiState.description, onValueChange = { viewModel.onDescriptionChange(it) }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = colors, shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.saveHabit() }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}
```
---
```
---
### EmotionsScreen.kt
package mx.edu.utng.modtrackin.ui.screens.emotions

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.EmotionEntry
import mx.edu.utng.modtrackin.ui.viewmodel.EmotionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Clase de datos que define una emoción básica disponible para selección.
 *
 * @property id El identificador único de la emoción para la lógica interna (e.g., "alegre").
 * @property emoji El carácter emoji asociado a la emoción.
 * @property texto El nombre descriptivo de la emoción.
 * @property color El color asociado a la emoción para la representación visual.
 */
data class Emocion(val id: String, val emoji: String, val texto: String, val color: Color)

/**
 * Pantalla principal para el registro y visualización del estado emocional diario.
 *
 * Gestiona un flujo de múltiples pasos (selección de emoción -> selección de adjetivo ->
 * adición de notas -> registro guardado) y también permite ver el historial.
 *
 * @param navController El controlador de navegación para cambiar entre pantallas.
 * @param emotionViewModel El ViewModel que gestiona el estado, la lógica y la interacción
 * con el repositorio de emociones.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionsScreen(
    navController: NavController,
    emotionViewModel: EmotionViewModel = viewModel()
) {
    val uiState = emotionViewModel.uiState
    val context = LocalContext.current

    val emociones = listOf(
        Emocion("alegre", "😄", "Alegre", Color(0xFFFFF176)),
        Emocion("neutral", "😐", "Neutral", Color(0xFFE0E0E0)),
        Emocion("triste", "😢", "Triste", Color(0xFF90CAF9)),
        Emocion("molesto", "😠", "Molesto", Color(0xFFEF9A9A)),
        Emocion("nervioso", "😰", "Nervioso", Color(0xFFCE93D8))
    )

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (uiState.currentScreen == 5) "Historial" else "Agenda Emocional",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {

                        if (uiState.currentScreen == 1 || uiState.currentScreen == 3) {
                            navController.popBackStack() // Sale de la pantalla o a HOME
                        } else {

                            emotionViewModel.goBack() // Navega al paso anterior dentro del flujo
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {

                if (uiState.currentScreen == 5) {
                    // Muestra la pantalla de Historial
                    PantallaHistorial(
                        historial = uiState.history,
                        onVolver = { emotionViewModel.goBack() }
                    )
                } else {
                    // Muestra el flujo de registro (Pasos 1 a 4)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        when (uiState.currentScreen) {
                            1 -> PantallaPrincipal(
                                emociones = emociones,
                                onEmocionClick = { emotionViewModel.selectEmotion(it) },
                                onVerHistorialClick = { emotionViewModel.goToHistory() }
                            )

                            2 -> uiState.selectedEmotion?.let {
                                val adjetivos = emotionViewModel.adjetivosMap[it.id] ?: emptyList()
                                PantallaAdjetivos(
                                    emocion = it,
                                    adjetivos = adjetivos,
                                    onAdjetivoClick = { adj -> emotionViewModel.selectAdjective(adj) },
                                    onVolver = { emotionViewModel.goBack() }
                                )
                            }

                            3 -> uiState.todayEntry?.let { entry ->
                                val emocionGuardada = emociones.find { e -> e.id == entry.emotionId }
                                    ?: Emocion("unknown", entry.emotionEmoji, entry.emotionText, Color.Gray)

                                PantallaRegistroGuardado(
                                    registro = entry,
                                    emocion = emocionGuardada,
                                    onNuevoRegistro = { emotionViewModel.resetFlow() },
                                    onVerHistorial = { emotionViewModel.goToHistory() },
                                    frasesMotivadoras = emptyList()
                                )
                            }

                            4 -> if (uiState.selectedEmotion != null) {
                                PantallaNotas(
                                    emocion = uiState.selectedEmotion!!,
                                    adjetivo = uiState.selectedAdjective,
                                    notas = uiState.dailyNote,
                                    onNotasChange = { emotionViewModel.updateNote(it) },
                                    onGuardar = { emotionViewModel.saveEntry() },
                                    onVolver = { emotionViewModel.goBack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Primer paso del flujo de registro: permite al usuario seleccionar una emoción principal.
 *
 * @param emociones Lista de objetos [Emocion] disponibles para seleccionar.
 * @param onEmocionClick Lambda que se invoca con la emoción seleccionada.
 * @param onVerHistorialClick Lambda que navega a la vista de historial.
 */
@Composable
fun PantallaPrincipal(
    emociones: List<Emocion>,
    onEmocionClick: (Emocion) -> Unit,
    onVerHistorialClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "¿Cómo te sientes hoy?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        emociones.forEach { emocion ->
            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                onClick = { onEmocionClick(emocion) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emocion.emoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        emocion.texto,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVerHistorialClick,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver días anteriores", color = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Segundo paso del flujo de registro: permite al usuario seleccionar un adjetivo para matizar la emoción.
 *
 * @param emocion La [Emocion] seleccionada en el paso anterior.
 * @param adjetivos Lista de adjetivos disponibles para esa emoción.
 * @param onAdjetivoClick Lambda que se invoca con el adjetivo seleccionado.
 * @param onVolver Lambda para volver al paso anterior (selección de emoción).
 */
@Composable
fun PantallaAdjetivos(
    emocion: Emocion,
    adjetivos: List<String>,
    onAdjetivoClick: (String) -> Unit,
    onVolver: () -> Unit
) {
    Column {
        Text(
            text = "Te sientes ${emocion.texto.lowercase()}...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        adjetivos.forEach { adjetivo ->
            Button(
                onClick = { onAdjetivoClick(adjetivo) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(adjetivo)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

/**
 * Tercer paso del flujo de registro: permite al usuario escribir una nota o descripción de su día.
 *
 * @param emocion La [Emocion] principal seleccionada.
 * @param adjetivo El adjetivo seleccionado.
 * @param notas El texto actual de las notas (vinculado al estado del ViewModel).
 * @param onNotasChange Lambda para actualizar el texto de la nota en el ViewModel.
 * @param onGuardar Lambda para ejecutar la acción de guardar el [EmotionEntry] en la base de datos.
 * @param onVolver Lambda para volver al paso anterior (selección de adjetivo).
 */
@Composable
fun PantallaNotas(
    emocion: Emocion,
    adjetivo: String,
    notas: String,
    onNotasChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit
) {
    Column {
        Text(
            text = "Describe tu día (${emocion.texto} - $adjetivo)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = notas,
            onValueChange = onNotasChange,
            label = { Text("Escribe tus notas aquí...") },
            modifier = Modifier.fillMaxWidth().height(160.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onGuardar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Guardar registro")
        }
    }
}

/**
 * Cuarto paso del flujo de registro: Muestra el registro guardado de hoy, junto con
 * una frase motivadora y recomendaciones basadas en la emoción registrada.
 *
 * @param registro El objeto [EmotionEntry] guardado para el día de hoy.
 * @param emocion El objeto [Emocion] correspondiente al registro.
 * @param frasesMotivadoras (Ignorado, ya que se genera internamente con [obtenerFraseMotivadora]).
 * @param onNuevoRegistro Lambda para reiniciar el flujo y editar el registro de hoy.
 * @param onVerHistorial Lambda para navegar a la vista de historial.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaRegistroGuardado(
    registro: EmotionEntry,
    emocion: Emocion,
    frasesMotivadoras: List<String>,
    onNuevoRegistro: () -> Unit,
    onVerHistorial: () -> Unit
) {
    val fraseDelDia = remember { obtenerFraseMotivadora() }
    val recomendaciones = remember(registro.emotionId) { obtenerRecomendaciones(registro.emotionId) }

    // Formateo de la fecha (asumiendo formato "YYYY-MM-DD")
    val fechaTexto = try {
        if (registro.dateString.isNotEmpty()) {
            val partes = registro.dateString.split("-")
            if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else registro.dateString
        } else "Hoy"
    } catch (e: Exception) { "Hoy" }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("Registro guardado", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("📅 $fechaTexto", color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(16.dp))
        Text("${emocion.emoji} ${emocion.texto} - ${registro.adjective}", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            if (registro.note.isNotBlank()) registro.note else "(Sin notas adicionales)",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("💬 Frase del día:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)
        Text("“$fraseDelDia”", textAlign = TextAlign.Center, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(12.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💡", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Recomendaciones", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                recomendaciones.forEach { tip ->
                    Text("• $tip", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onNuevoRegistro, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Text("Editar registro de hoy")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onVerHistorial, border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)) {
            Text("Ver historial completo", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

/**
 * Pantalla para visualizar el historial completo de los registros emocionales del usuario.
 *
 * @param historial La lista de objetos [EmotionEntry] recuperados de la base de datos.
 * @param onVolver Lambda para volver a la pantalla principal de registro emocional.
 */
@Composable
fun PantallaHistorial(
    historial: List<EmotionEntry>,
    onVolver: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (historial.isEmpty()) {
                item {
                    Text(
                        "No hay registros anteriores aún.",
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(historial) { entry ->
                    ItemHistorial(entry)
                }
            }
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text("Volver al inicio", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Componente Composable que representa un único elemento en la lista de historial.
 *
 * Muestra el día, mes, emoji, texto de la emoción y un resumen de la nota.
 *
 * @param entry El objeto [EmotionEntry] que contiene los datos del registro.
 */
@Composable
fun ItemHistorial(entry: EmotionEntry) {
    val partes = entry.dateString.split("-")
    val dia = if (partes.size >= 3) partes[2] else "??"

    val mesMap = mapOf(
        "01" to "ENE", "02" to "FEB", "03" to "MAR", "04" to "ABR",
        "05" to "MAY", "06" to "JUN", "07" to "JUL", "08" to "AGO",
        "09" to "SEP", "10" to "OCT", "11" to "NOV", "12" to "DIC"
    )
    val mesNum = if (partes.size >= 3) partes[1] else "00"
    val mes = mesMap[mesNum] ?: "---"

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = dia, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                Text(text = mes, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = if (entry.emotionEmoji.isNotEmpty()) entry.emotionEmoji else "❓", fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${entry.emotionText} - ${entry.adjective}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (entry.note.isNotEmpty()) {
                    Text(text = entry.note, maxLines = 1, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/**
 * Función de utilidad que selecciona una frase motivadora aleatoria de un banco predefinido.
 *
 * @return Una cadena de texto con una frase motivadora.
 */
fun obtenerFraseMotivadora(): String {
    return listOf(

        "El éxito es la suma de pequeños esfuerzos repetidos cada día.",
        "No te detengas hasta que te sientas orgulloso.",
        "Tu actitud determina tu dirección.",
        "Cree en ti y todo será posible.",
        "Hoy es un buen día para tener un gran día.",
        "Pequeños pasos te llevan a grandes lugares.",
        "Hazlo con miedo, pero hazlo.",
        "La disciplina te lleva donde la motivación no alcanza.",
        "Nunca es tarde para ser lo que podrías haber sido.",
        "Si puedes soñarlo, puedes hacerlo.",


        "La calma es un superpoder.",
        "Respira, suelta y confía.",
        "La felicidad no es una meta, es el camino.",
        "Todo pasa, esto también pasará.",
        "Un día a la vez.",
        "No necesitas tener todas las respuestas hoy.",
        "La paz viene de adentro, no la busques fuera.",
        "A veces, descansar es lo más productivo que puedes hacer.",
        "No permitas que el comportamiento de otros destruya tu paz interior.",


        "Eres más fuerte de lo que crees.",
        "Sé amable contigo mismo.",
        "Eres suficiente tal y como eres.",
        "Tus emociones son válidas.",
        "Cuidar de ti mismo no es egoísmo, es necesario.",
        "Florece donde te planten.",
        "Tu único límite es tu mente.",
        "Brilla con luz propia.",
        "Mañana será una nueva oportunidad.",
        "Lo estás haciendo mejor de lo que piensas."
    ).random()
}

/**
 * Función de utilidad que devuelve una lista de recomendaciones específicas basadas en la emoción registrada.
 *
 * @param emocionId El ID de la emoción (e.g., "triste", "molesto").
 * @return Una lista de cadenas de texto con consejos o actividades recomendadas.
 */
fun obtenerRecomendaciones(emocionId: String): List<String> {
    val bancoDeConsejos = when (emocionId) {
        "alegre" -> listOf(
            "Comparte tu buena energía llamando a un amigo.",
            "Anota 3 cosas por las que estás agradecido hoy.",
            "Aprovecha este impulso para realizar una tarea difícil.",
            "Date un pequeño gusto o premio, te lo mereces.",
            "Guarda este momento: toma una foto o escribe un recuerdo.",
            "Contagia tu alegría: haz un cumplido sincero a alguien."
        )
        "triste" -> listOf(
            "Está bien no estar bien, date permiso de sentir.",
            "Escucha esa playlist que te reconforta el alma.",
            "Habla con alguien de confianza, no te aísles.",
            "Sal a caminar 10 minutos para tomar aire fresco.",
            "Evita redes sociales por un rato y descansa la mente.",
            "Escribe lo que sientes en una hoja y luego rómpela."
        )
        "molesto" -> listOf(
            "Realiza la técnica 4-7-8: Inhala en 4s, retén 7s, exhala 8s.",
            "Aléjate físicamente de la situación que te enojó.",
            "Haz ejercicio intenso para quemar la adrenalina.",
            "Escribe una carta de enojo (pero no la envíes).",
            "Lávate la cara con agua fría para 'resetear' tu sistema.",
            "Escucha música con mucha energía o ruido blanco."
        )
        "nervioso" -> listOf(
            "Usa la técnica 5-4-3-2-1 para conectarte con el presente.",
            "Haz una lista de lo que SÍ puedes controlar ahora.",
            "Bebe un vaso de agua lentamente.",
            "Reduce la cafeína por el resto del día.",
            "Organiza tu espacio físico, el orden externo trae calma interna.",
            "Cierra los ojos y visualiza un lugar seguro por 2 minutos."
        )
        "neutral" -> listOf(
            "Es un buen momento para leer o aprender algo nuevo.",
            "Organiza tu agenda para mañana con calma.",
            "Dedica 5 minutos a meditar sin expectativas.",
            "Llama a un familiar solo para saludar.",
            "Ordena tu habitación o espacio de trabajo.",
            "Haz estiramientos suaves para activar tu cuerpo."
        )
        else -> listOf(
            "Tómate un momento para respirar conscientemente.",
            "Hidrátate, bebe un vaso de agua.",
            "Estira los brazos y el cuello suavemente."
        )
    }

    // Devuelve dos recomendaciones aleatorias
    return bancoDeConsejos.shuffled().take(2)
}
```
---
```
---
### NotesScreen.kt (Incluye AddNoteScreen)
package mx.edu.utng.modtrackin.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Note
import mx.edu.utng.modtrackin.ui.viewmodel.NotesViewModel

/**
 * Punto de entrada principal para la gestión de notas.
 *
 * Actúa como un switch que decide si mostrar la lista de notas ([NotesListScreen]) o
 * la pantalla de edición ([NoteEditorScreen]) basándose en el estado [isEditorOpen]
 * del [NotesViewModel].
 *
 * @param navController El controlador de navegación.
 * @param viewModel El ViewModel que gestiona el estado y la lógica de las notas.
 */
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    if (uiState.isEditorOpen) {
        NoteEditorScreen(
            note = uiState.currentNote,
            onTitleChange = { viewModel.updateTitle(it) },
            onContentChange = { viewModel.updateContent(it) },
            onSave = { viewModel.saveCurrentNote() },
            onCancel = { viewModel.closeEditor() },
            onDelete = { viewModel.deleteCurrentNote() }
        )
    } else {
        NotesListScreen(
            notes = uiState.notesList,
            isLoading = uiState.isLoading,
            onAddClick = { viewModel.openEditorNew() },
            onNoteClick = { note -> viewModel.openEditorModify(note) },
            onBack = { navController.popBackStack() }
        )
    }
}

/**
 * Pantalla que muestra el listado de todas las notas del usuario.
 *
 * Muestra la lista en un [LazyColumn], maneja el estado de carga y el caso de lista vacía.
 * Incluye un Floating Action Button para agregar nuevas notas.
 *
 * @param notes La lista de [Note] recuperadas del ViewModel.
 * @param isLoading Indica si el repositorio está actualmente cargando los datos.
 * @param onAddClick Lambda para iniciar el proceso de agregar una nueva nota.
 * @param onNoteClick Lambda que se invoca al hacer clic en una tarjeta de nota para editarla.
 * @param onBack Lambda para volver a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    notes: List<Note>,
    isLoading: Boolean,
    onAddClick: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Notas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Nota", tint = Color.Black)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading && notes.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (notes.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🗒️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No hay notas. ¡Crea una!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = notes, key = { it.id }) { note ->
                        NoteCard(note = note, onClick = { onNoteClick(note) })
                    }
                }
            }
        }
    }
}

/**
 * Componente Composable que representa una vista previa de una [Note] individual en la lista.
 *
 * Muestra el título y un resumen del contenido, y es clickeable para abrir la edición.
 *
 * @param note El objeto [Note] cuyos datos se van a mostrar.
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta.
 */
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (note.title.isNotEmpty()) note.title else "(Sin título)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (note.content.isNotEmpty()) note.content else "...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Pantalla de edición de una nota, utilizada tanto para crear una nueva como para modificar una existente.
 *
 * Contiene campos de texto para el título y el contenido. Proporciona acciones de guardar,
 * cancelar y, si la nota existe, eliminar en la AppBar.
 *
 * @param note El objeto [Note] actual que se está editando (conectado al estado del ViewModel).
 * @param onTitleChange Lambda para actualizar el título.
 * @param onContentChange Lambda para actualizar el contenido.
 * @param onSave Lambda para ejecutar la acción de guardar la nota.
 * @param onCancel Lambda para salir del editor sin guardar cambios.
 * @param onDelete Lambda para eliminar la nota actual (solo si [note.id] no está vacío).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    note: Note,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (note.id.isEmpty()) "Nueva Nota" else "Editar Nota") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
                    }
                },
                actions = {

                    if (note.id.isNotEmpty()) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF9A9A))
                        }
                    }
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
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

            OutlinedTextField(
                value = note.title,
                onValueChange = onTitleChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = note.content,
                onValueChange = onContentChange,
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = fieldColors,
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            )
        }
    }
}
```
---
```
---
### AddNoteScreen (dentro de la misma carpeta NotesScreen)
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
```
---
```
---
### CalendarScreen.kt
package mx.edu.utng.modtrackin.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.edu.utng.modtrackin.data.model.Task
import mx.edu.utng.modtrackin.navigation.NavRoutes
import mx.edu.utng.modtrackin.ui.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Pantalla principal del calendario.
 *
 * Muestra una vista de calendario mensual y una lista de tareas pendientes [Task] asociadas
 * a la fecha seleccionada por el usuario.
 *
 * @param navController El controlador de navegación para cambiar entre pantallas.
 * @param taskViewModel El ViewModel encargado de gestionar y proporcionar los datos de las tareas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = viewModel()
) {
    val tasks = taskViewModel.uiState.taskList


    var currentMonth by remember { mutableStateOf(YearMonth.now()) }


    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Calendario", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavRoutes.HOME_SCREEN) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            MonthHeader(
                currentMonth = currentMonth,
                onPreviousClick = { currentMonth = currentMonth.minusMonths(1) },
                onNextClick = { currentMonth = currentMonth.plusMonths(1) }
            )


            DaysOfWeekHeader()


            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                tasks = tasks,
                onDateSelected = { date -> selectedDate = date }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)


            Text(
                text = "Tareas para el ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMM"))}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            TaskListForDay(selectedDate = selectedDate, tasks = tasks)
        }
    }
}

/**
 * Componente Composable que muestra la navegación entre meses y el nombre del mes actual.
 *
 * @param currentMonth El objeto [YearMonth] que representa el mes actualmente visible.
 * @param onPreviousClick Función lambda a ejecutar cuando se presiona el botón para ir al mes anterior.
 * @param onNextClick Función lambda a ejecutar cuando se presiona el botón para ir al mes siguiente.
 */
@Composable
fun MonthHeader(
    currentMonth: YearMonth,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Anterior", tint = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = onNextClick) {
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Siguiente", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/**
 * Componente Composable que muestra las abreviaturas de los días de la semana (DOM, LUN, etc.)
 * en la cabecera del calendario.
 */
@Composable
fun DaysOfWeekHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        val days = listOf("DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB")
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

/**
 * Componente Composable que dibuja la cuadrícula del calendario con los días del mes actual.
 *
 * Muestra visualmente la fecha seleccionada, la fecha actual y marca los días que contienen tareas pendientes.
 *
 * @param currentMonth El objeto [YearMonth] que define qué mes se está mostrando.
 * @param selectedDate La [LocalDate] actualmente seleccionada por el usuario.
 * @param tasks La lista de todas las [Task] del usuario, utilizadas para marcar días con tareas.
 * @param onDateSelected Función lambda que se ejecuta cuando el usuario hace clic en una fecha.
 */
@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    tasks: List<Task>,
    onDateSelected: (LocalDate) -> Unit
) {

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)

    // Determina el desplazamiento inicial (offset) para colocar el primer día en la columna correcta.
    val startDayOffset = firstDayOfMonth.dayOfWeek.value % 7

    val totalCells = daysInMonth + startDayOffset

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.padding(horizontal = 8.dp).height(320.dp) // Altura fija para el calendario
    ) {

        // Celdas vacías para el espacio de los días de la semana anteriores al inicio del mes
        items(startDayOffset) {
            Box(modifier = Modifier.size(40.dp))
        }


        // Celdas para cada día del mes
        items(daysInMonth) { dayIndex ->
            val dayNum = dayIndex + 1
            val date = currentMonth.atDay(dayNum)
            val isSelected = date == selectedDate
            val isToday = date == LocalDate.now()

            // Verificamos si hay tareas en esta fecha
            val hasTasks = tasks.any { it.dueDate == date.toString() && !it.isCompleted }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(
                        when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            isToday -> MaterialTheme.colorScheme.surfaceVariant
                            else -> Color.Transparent
                        }
                    )
                    .clickable { onDateSelected(date) }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = dayNum.toString(),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                    )

                    // Puntito si hay tareas pendientes
                    if (hasTasks) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente Composable que muestra una lista deslizable de [Task] filtradas
 * por la fecha seleccionada.
 *
 * @param selectedDate La [LocalDate] para la cual se deben mostrar las tareas.
 * @param tasks La lista completa de [Task] del usuario.
 */
@Composable
fun TaskListForDay(
    selectedDate: LocalDate,
    tasks: List<Task>
) {
    val dayTasks = tasks.filter { it.dueDate == selectedDate.toString() }

    if (dayTasks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay tareas para este día", color = Color.Gray)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(220.dp)
        ) {
            items(dayTasks) { task ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = task.title,
                                fontWeight = FontWeight.Bold,
                                color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            )
                            if (task.description.isNotEmpty()) {
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = task.priority,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
```
---
```
---
### SleepScreen.kt
package mx.edu.utng.modtrackin.ui.screens.sleep

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import mx.edu.utng.modtrackin.ui.viewmodel.SleepEntry
import mx.edu.utng.modtrackin.ui.viewmodel.SleepViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

/**
 * Pantalla principal para el registro y visualización del sueño.
 *
 * Permite al usuario seleccionar las horas de inicio y fin del sueño, calificar la calidad
 * y ver el historial de registros. Gestiona la lógica de guardar, eliminar y la visualización
 * de errores mediante [SleepViewModel].
 *
 * @param navController El controlador de navegación para volver a la pantalla anterior.
 * @param sleepViewModel El ViewModel que gestiona el estado y la lógica de los registros de sueño.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
    navController: NavController,
    sleepViewModel: SleepViewModel = viewModel()
) {
    val uiState by sleepViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Estado para controlar qué registro se va a eliminar
    var entryToDelete by remember { mutableStateOf<SleepEntry?>(null) }

    // Efecto para mostrar el Toast al guardar exitosamente.
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            Toast.makeText(context, "Registro guardado con éxito", Toast.LENGTH_SHORT).show()
            delay(500)
            sleepViewModel.resetSaveState()
        }
    }

    // Efecto para mostrar Toast de errores.
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, "Error: ${uiState.errorMessage}", Toast.LENGTH_LONG).show()
            sleepViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Sueño", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                RegistroActual(
                    uiState = uiState,
                    onStartTimeUpdate = { sleepViewModel.updateStartTime(it) },
                    onEndTimeUpdate = { sleepViewModel.updateEndTime(it) },
                    onQualityUpdate = { sleepViewModel.updateQuality(it) },
                    onSave = { sleepViewModel.saveSleepEntry() }
                )
            }

            item {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    Text(
                        "Historial de Sueño",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uiState.isLoadingHistory) {
                item { CircularProgressIndicator() }
            } else if (uiState.sleepHistory.isEmpty()) {
                item {
                    Text(
                        "No hay registros de sueño todavía.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(uiState.sleepHistory, key = { it.id }) { entry ->
                    SleepHistoryItem(
                        entry = entry,
                        // Al hacer clic, se abre el diálogo de confirmación
                        onDeleteClick = { entryToDelete = entry }
                    )
                }
            }
        }
    }

    // --- DIÁLOGO DE CONFIRMACIÓN DE BORRADO ---
    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este registro de sueño? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Llama a la función de eliminar del ViewModel
                        sleepViewModel.deleteSleepEntry(entryToDelete!!.id)
                        entryToDelete = null // Cierra el diálogo
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { entryToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Componente Composable que permite registrar las horas de sueño y la calidad.
 *
 * Muestra la duración calculada, selectores de hora y el selector de calidad del sueño.
 *
 * @param uiState El estado de la UI del sueño, conteniendo las horas y calidad actuales.
 * @param onStartTimeUpdate Lambda para actualizar la hora de inicio del sueño.
 * @param onEndTimeUpdate Lambda para actualizar la hora de fin del sueño.
 * @param onQualityUpdate Lambda para actualizar la calificación de calidad.
 * @param onSave Lambda para ejecutar la acción de guardar el registro.
 */
@Composable
fun RegistroActual(
    uiState: mx.edu.utng.modtrackin.ui.viewmodel.SleepUiState,
    onStartTimeUpdate: (LocalTime) -> Unit,
    onEndTimeUpdate: (LocalTime) -> Unit,
    onQualityUpdate: (Int) -> Unit,
    onSave: () -> Unit
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Lógica para calcular la duración, manejando el cruce de medianoche
    val durationMinutes = if (uiState.endTime.isAfter(uiState.startTime)) {
        ChronoUnit.MINUTES.between(uiState.startTime, uiState.endTime)
    } else {
        val minutesUntilMidnight = ChronoUnit.MINUTES.between(uiState.startTime, LocalTime.MAX) + 1
        val minutesAfterMidnight = ChronoUnit.MINUTES.between(LocalTime.MIN, uiState.endTime)
        (minutesUntilMidnight + minutesAfterMidnight).coerceAtLeast(0)
    }
    val durationHours = durationMinutes / 60.0
    val durationText = String.format("%.1f", durationHours)

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Duración en tiempo real ---
        Text("Duración Total", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "$durationText hrs",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Selectores de hora ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TimePickerCard(
                modifier = Modifier.weight(1f),
                label = "Hora de Dormir",
                time = uiState.startTime,
                icon = { Icon(Icons.Default.NightsStay, null, tint = MaterialTheme.colorScheme.primary) },
                onClick = { showStartTimePicker = true }
            )
            TimePickerCard(
                modifier = Modifier.weight(1f),
                label = "Hora de Despertar",
                time = uiState.endTime,
                icon = { Icon(Icons.Default.WbSunny, null, tint = MaterialTheme.colorScheme.secondary) },
                onClick = { showEndTimePicker = true }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Selector de calidad ---
        SleepQualitySelector(
            quality = uiState.quality,
            onQualityChange = onQualityUpdate
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botón de guardar ---
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isSaving,
            shape = RoundedCornerShape(16.dp)
        ) {
            AnimatedVisibility(visible = uiState.isSaving) {
                CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            }
            AnimatedVisibility(visible = !uiState.isSaving) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Registro", fontSize = 16.sp)
                }
            }
        }
    }

    // Diálogos de selección de hora
    if (showStartTimePicker) {
        SleepTimePickerDialog(
            initialTime = uiState.startTime,
            onDismiss = { showStartTimePicker = false },
            onConfirm = { newTime ->
                onStartTimeUpdate(newTime)
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        SleepTimePickerDialog(
            initialTime = uiState.endTime,
            onDismiss = { showEndTimePicker = false },
            onConfirm = { newTime ->
                onEndTimeUpdate(newTime)
                showEndTimePicker = false
            }
        )
    }
}

/**
 * Componente Composable para mostrar un elemento individual del historial de sueño.
 *
 * Muestra la fecha, el rango de horas, la duración y la calidad del sueño, e incluye
 * un botón para eliminar el registro.
 *
 * @param entry El objeto [SleepEntry] con los datos del registro.
 * @param onDeleteClick Lambda que se ejecuta al hacer clic en el icono de eliminar.
 */
@Composable
fun SleepHistoryItem(
    entry: SleepEntry,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = LocalDate.parse(entry.date).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${entry.startTime} - ${entry.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format("%.1f", entry.durationHours)}h",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                val qualityEmojis = listOf("😠", "😟", "😐", "😊", "🤩")
                if (entry.quality in 1..qualityEmojis.size) {
                    Text(
                        text = qualityEmojis[entry.quality - 1],
                        fontSize = 20.sp
                    )
                }
            }
            // --- BOTÓN DE ELIMINAR ---
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar registro",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Componente Composable de tarjeta reutilizable para mostrar y seleccionar una hora.
 *
 * @param modifier Modificador de Composable.
 * @param label El título del selector (ej. "Hora de Dormir").
 * @param time La hora actual seleccionada.
 * @param icon Un composable que dibuja el icono asociado.
 * @param onClick Lambda que se ejecuta al hacer clic en la tarjeta para abrir el selector de hora.
 */
@Composable
fun TimePickerCard(
    modifier: Modifier = Modifier,
    label: String,
    time: LocalTime,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(
                text = time.format(timeFormatter),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Componente Composable para seleccionar la calidad del sueño mediante una escala de emojis (1 a 5).
 *
 * @param quality El valor de calidad actualmente seleccionado.
 * @param onQualityChange Lambda que se invoca con el nuevo valor de calidad seleccionado (1-5).
 */
@Composable
fun SleepQualitySelector(
    quality: Int,
    onQualityChange: (Int) -> Unit
) {
    val qualityEmojis = listOf("😠", "😟", "😐", "😊", "🤩")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Calidad del Sueño",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            qualityEmojis.forEachIndexed { index, emoji ->
                val actualQuality = index + 1
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (actualQuality == quality) MaterialTheme.colorScheme.primaryContainer
                            else Color.Transparent,
                            CircleShape
                        )
                        .clickable { onQualityChange(actualQuality) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 28.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

/**
 * Diálogo que envuelve el [TimePicker] de Material 3 para la selección de una hora específica.
 *
 * @param initialTime La hora inicial mostrada en el selector.
 * @param onDismiss Lambda que se ejecuta al cerrar el diálogo sin confirmar.
 * @param onConfirm Lambda que se ejecuta al confirmar la selección, devolviendo la nueva [LocalTime].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimePickerDialog(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(LocalTime.of(timePickerState.hour, timePickerState.minute))
            }) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}
---

```
---
### 🔴 FASE 4: El Punto de Entrada (MainActivity)
Este archivo es el único que nos faltaba unir. Abre MainActivity.kt (que ya existe) y reemplaza TODO su contenido por esto:
```
package mx.edu.utng.modtrackin

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import mx.edu.utng.modtrackin.navigation.AppNavigation
import mx.edu.utng.modtrackin.ui.theme.ModTrackinTheme // Asegúrate que tu tema se llame así
import mx.edu.utng.modtrackin.utils.NotificationHelper

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Crear el canal de notificaciones al iniciar la app
        NotificationHelper.createNotificationChannel(this)

        setContent {
            ModTrackinTheme {
                // A container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 2. Llamamos a nuestra navegación principal
                    AppNavigation(startDestination = "login") 
                }
            }
        }
    }
}
```
---
### 🟣 FASE 5: Permisos y Manifiesto

Abre el archivo AndroidManifest.xml (en la carpeta manifests). Debes agregar los permisos y registrar el receptor de notificaciones.
```
Sustituye o asegúrate de que tenga esto:
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
    <uses-permission android:name="android.permission.USE_EXACT_ALARM" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.ModTrackin"
        tools:targetApi="31">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.ModTrackin">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name=".utils.NotificationReceiver" />

    </application>

</manifest>
```
---

### 🏁 FASE FINAL: Ejecutar
```
Dale al botón de "Sync Project with Gradle Files" (el elefante).

Si te marca error en ModTrackinTheme, verifica el nombre de tu tema en ui/theme/Theme.kt.

Conecta tu celular o inicia el emulador.

Dale a Run (▶️).
```
---
### 🧑‍💻 Autores

Juan Gilberto Mejia Ortiz y Halan Fernando Rodriguez Guerrero
Institución: Universidad Tecnológica del Norte de Guanajuato (UTNG)
Materia: Desarrollo de Aplicaciones Móviles
