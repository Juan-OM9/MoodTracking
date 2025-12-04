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