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