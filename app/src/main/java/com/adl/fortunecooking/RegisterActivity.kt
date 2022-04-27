package com.adl.fortunecooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        btn_register_submit.setOnClickListener({
            register()
        })

        btnBackRegister.setOnClickListener({
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        })
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(et_register_email.text.toString(), et_register_password.text.toString()).addOnCompleteListener{
                it ->
            if(it.isSuccessful) {
                finish()
            }
        }.addOnFailureListener{
                exception -> Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()}
    }
}