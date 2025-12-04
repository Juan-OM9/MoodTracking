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