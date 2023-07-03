import com.example.petscare.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseUtils {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signUpWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    onError(errorMessage)
                }
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    onError(errorMessage)
                }
            }
    }
    fun sendPasswordResetEmail(email: String, onComplete: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                val isSuccessful = task.isSuccessful
                onComplete(isSuccessful)
            }
    }

    fun signOut() {
        auth.signOut()
    }
}