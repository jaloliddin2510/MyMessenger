package not.dev.api.data.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import not.dev.mymessenger.mainUi.main.MainApplication

class HomeViewModel : ViewModel() {
private val _signInEvent= MutableLiveData<SignInEvent>()
val signInEvent: LiveData<SignInEvent> get()=_signInEvent


fun signIn(email:String,password:String){
    viewModelScope.launch(Dispatchers.IO) {
        MainApplication.fbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    _signInEvent.value=SignInEvent.Success("Success",it.result.user?.uid)
                }else{
                    _signInEvent.value=SignInEvent.Failure(it.exception?.message.toString())
                }
            }.addOnFailureListener {
                _signInEvent.value=SignInEvent.Failure(it.message.toString())
            }
    }
}

}
sealed class SignInEvent(){
    data class Success(val message: String,val token:String?):SignInEvent()
    data class Failure(val message:String):SignInEvent()
    data object Loading:SignInEvent()

}