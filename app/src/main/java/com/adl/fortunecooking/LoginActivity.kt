package com.adl.fortunecooking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

lateinit var uId:String
class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val sharedPreference = getSharedPreferences("login", MODE_PRIVATE)
        val username = sharedPreference.getString("username","")
        val password = sharedPreference.getString("password","")
        val Checked = sharedPreference.getBoolean("checbox", false)

        txtEmail.setText(username)
        txtPassword.setText(password)
        checkBox.setChecked(Checked)

        btnLogin.setOnClickListener{
            var editor = sharedPreference.edit()
            if(checkBox.isChecked){
                editor.putString("username",txtEmail.text.toString())
                editor.putString("password",txtPassword.text.toString())
                editor.putBoolean("checkbox",true)
                editor.commit()
            }else{
                editor.remove("username")
                editor.remove("password")
                editor.commit()
            }
            login()
        }

        txtSignUp.setOnClickListener({
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        })
    }

    private fun login() {
        auth.signInWithEmailAndPassword(txtEmail.text.toString(),txtPassword.text.toString()).addOnCompleteListener {
                it ->
            if(it.isSuccessful){
                //val edit=sharedPreference.edit()
                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                Log.d("User sekarang :", "${currentFirebaseUser!!.uid}" )
                //Toast.makeText(this, "" + currentFirebaseUser!!.uid, Toast.LENGTH_SHORT).show()
                //edit.putString("uid", currentFirebaseUser.uid)
                //edit.commit()
                uId = currentFirebaseUser.uid
                val intent = Intent(this,DashboardActivity::class.java)
                startActivity(intent)
            }
        }.addOnFailureListener{
                exception -> Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }


}