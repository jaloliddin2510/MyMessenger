package not.dev.mymessenger.mainUi.homePaging.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import not.dev.mymessenger.dataBase.UserModel
import not.dev.mymessenger.mainUi.main.MainApplication

class AllViewModel : ViewModel() {
    private val _users = MutableLiveData<GetChatEvent>()
    val users: LiveData<GetChatEvent> = _users

    private val usersDataList = mutableListOf<UserModel>()

    private var valueListener: ValueEventListener? = null

    init {
        getChats()
    }

    fun getChats() = viewModelScope.launch(Dispatchers.IO) {
        val vl = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersDataList.clear()
                for (data in snapshot.children) {
                    data.getValue(UserModel::class.java)?.let(usersDataList::add)
                }
                _users.value = GetChatEvent.Success(usersDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                _users.value = GetChatEvent.Error(error.message)
            }
        }
        this@AllViewModel.valueListener = vl
        this@AllViewModel.valueListener?.let {
            MainApplication.dbReference.child("users").addValueEventListener(it)
        }
    }

    fun removeListener() {
        this.valueListener?.let {
            MainApplication.dbReference.removeEventListener(it)
        }
    }
}


sealed class GetChatEvent {
    data class Success(val users: List<UserModel>) : GetChatEvent()
    data class Error(val message: String) : GetChatEvent()
}