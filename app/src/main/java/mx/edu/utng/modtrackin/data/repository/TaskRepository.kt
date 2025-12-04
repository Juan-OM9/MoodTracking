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