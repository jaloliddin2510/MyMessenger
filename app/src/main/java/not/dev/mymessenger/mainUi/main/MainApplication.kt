package not.dev.mymessenger.mainUi.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainApplication:Application() {
    companion object{

        lateinit var sharedPreferences: SharedPreferences
        lateinit var dbReference: DatabaseReference
        lateinit var fbAuth: FirebaseAuth
        const val TOKEN="token"

    }
    override fun onCreate() {
        super.onCreate()
        fbAuth= Firebase.auth
        dbReference=FirebaseDatabase.getInstance("https://mymessenger-e31f4-default-rtdb.firebaseio.com/").reference
        sharedPreferences=getSharedPreferences("message", Context.MODE_PRIVATE)
    }


}