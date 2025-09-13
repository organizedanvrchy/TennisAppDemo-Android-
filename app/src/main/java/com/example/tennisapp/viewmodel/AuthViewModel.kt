package com.example.tennisapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.tennisapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore


    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, it.exception?.localizedMessage)
                }
            }
    }

    fun signup(email: String, name: String, password: String, securityQuestion: String, answer: String, onResult: (Boolean, String?) -> Unit) {

        //Check that user provided password is at least 8 character and has a special symbol
        val passwordPattern = Regex("^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")
        if (!password.matches(passwordPattern)) {
            onResult(false, "Password must be 8+ characters with at least one special symbol.")
            return
        }

        //Check that user provided e-mail is a valid email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onResult(false, "Enter a valid email address.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    // Assign UserID to account
                    val userId = it.result?.user?.uid
                    // Store UserID in UserModel along with name and email
                    val userModel = UserModel(name, email, userId!!, securityQuestion, answer)

                    firestore
                        .collection("users")
                        .document(userId)
                        .set(userModel)
                        .addOnCompleteListener { signUp ->
                            if(signUp.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, "Something went wrong...")
                            }
                        }
                } else {
                    onResult(false, it.exception?.localizedMessage)
                }
            }
    }

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    fun getSecurityQuestion(email: String, onResult: (String?) -> Unit){
        firestore
            .collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val question = snapshot.documents.firstOrNull()
                    ?.getString("securityQuestion")
                    onResult(question)
            }
            .addOnFailureListener { e ->
                onResult(null)
            }
    }

    fun checkSecurityQuestionAnswer(email: String, answer: String, onResult: (Boolean, String?) -> Unit){
        firestore
            .collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                if (doc != null) {

                    val storedAnswer = doc.getString("answer") ?: ""
                    val matches = storedAnswer.trim().equals(answer.trim(), ignoreCase = false)

                    if (matches) {
                        // Pass true and maybe the userId or null
                        onResult(true, doc.id)
                    } else {
                        onResult(false, "Incorrect answer")
                    }
                } else {
                    onResult(false, "No user found with that email")
                }
            }
    }

    fun signInAsGuest(onResult: (Boolean, String?) -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val guestId = task.result?.user?.uid
                    val guestModel = UserModel(
                        "Guest",
                        null,
                        guestId!!,
                        securityQuestion = null,
                        answer = null
                    )

                    firestore.collection("users")
                        .document(guestId)
                        .set(guestModel)
                        .addOnCompleteListener { setTask ->
                            if (setTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, setTask.exception?.localizedMessage)
                            }
                        }
                } else {
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    fun signOutGuest() {
        val guestUser = auth.currentUser

        // Delete Guest User from Database
        if (guestUser?.email == null) {
            firestore
                .collection("users")
                .document(guestUser?.uid.toString())
                .delete()

            guestUser?.delete()
        }
    }

    fun logout() {
        auth.signOut()
    }
}
