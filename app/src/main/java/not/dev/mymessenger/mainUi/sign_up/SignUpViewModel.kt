package not.dev.mymessenger.mainUi.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import not.dev.mymessenger.dataBase.UserModel
import not.dev.mymessenger.mainUi.main.MainApplication

class SignUpViewModel :ViewModel() {
    private val _signUpEvent= MutableLiveData<SignUpEvent>()
    val signUpEvent: LiveData<SignUpEvent> get()=_signUpEvent

    private val _saveUserEvent= MutableLiveData<SaveUserEvent>()
    val saveUserEvent: LiveData<SaveUserEvent> get()=_saveUserEvent

    fun signUp(email: String, username: String, password: String) {
        _signUpEvent.value = SignUpEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                if (it.isSuccessful){
                    _signUpEvent.value=SignUpEvent.Success(it.result.user?.uid)
                }else{
                    _signUpEvent.value=SignUpEvent.Error(it.exception?.message.toString())
                }
            }.addOnFailureListener {
                _signUpEvent.value=SignUpEvent.Error(it.message.toString())
                }
        }
    }
    fun saveUserDB(username:String,token:String){
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.dbReference.child("users").child(token)
                .setValue(UserModel(username = username, userToken = token))
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
