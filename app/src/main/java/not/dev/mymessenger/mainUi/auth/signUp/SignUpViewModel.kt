package not.dev.mymessenger.mainUi.auth.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import not.dev.mymessenger.core.base.dto.UserModel
import not.dev.mymessenger.mainUi.main.MainApplication

class SignUpViewModel: ViewModel() {
    private val _signUpEvent= MutableLiveData<SignUpEvent>()
    val signUpEvent: LiveData<SignUpEvent> get()=_signUpEvent

    private val _saveUserEvent= MutableLiveData<SaveUserEvent>()
    val saveUserEvent: LiveData<SaveUserEvent> get()=_saveUserEvent

    fun signUp(email: String, password: String,name:String,firstName:String) {
        _signUpEvent.value = SignUpEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val userID=it.result.user?.uid
                        if (userID!=null){
                            saveUserDB(userID,name,firstName)
                        }else{
                            _signUpEvent.value=SignUpEvent.Error("Token is null")
                        }
                    }else{
                        val exception=it.exception
                        if (exception is FirebaseAuthWeakPasswordException){
                            _signUpEvent.value=SignUpEvent.Error("Password is too weak")
                        }
                        else if(exception is FirebaseAuthInvalidCredentialsException){
                            _signUpEvent.value=SignUpEvent.Error("Invalid email")
                        }
                        else{
                            _signUpEvent.value=SignUpEvent.Error(it.exception?.message.toString())
                        }
                    }
                }.addOnFailureListener {
                    _signUpEvent.value=SignUpEvent.Error(it.message.toString())
                }
        }
    }
    fun saveUserDB(userId:String,name:String,firstName: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userModel= UserModel(
                userToken = userId,
                firstname = firstName,
                email = MainApplication.fbAuth.currentUser?.email,
                name = name
            )
            MainApplication.dbReference.child("users").child(userId)
                .setValue(userModel)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        _saveUserEvent.value=SaveUserEvent.Success("Sign Up Success")

                    }else{
                        _saveUserEvent.value=SaveUserEvent.Error(it.exception?.message.toString())
                    }
                }.addOnFailureListener {
                    _saveUserEvent.value=SaveUserEvent.Error(it.message.toString())
                }
        }
    }
}
sealed class SaveUserEvent(){
    data class Success(val token:String?):SaveUserEvent()
    data class Error(val message:String):SaveUserEvent()
    data object Loading:SaveUserEvent()
}
sealed class SignUpEvent(){
    data class Success(val token:String?):SignUpEvent()
    data class Error(val message:String):SignUpEvent()
    data object Loading:SignUpEvent()
}
