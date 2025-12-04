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