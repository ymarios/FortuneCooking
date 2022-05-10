package com.adl.fortunecooking

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setFullscreen()

        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog, will show while login account
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        val sharedPreference = getSharedPreferences("login", MODE_PRIVATE)
        val username = sharedPreference.getString("username","")
        val password = sharedPreference.getString("password","")
        val Checked = sharedPreference.getBoolean("checkbox", false)

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
    private var email = ""
    private var password = ""

    private fun login() {
       //1) Input Data
        email = txtEmail.text.toString().trim()
        password = txtPassword.text.toString().trim()

        //2)Validate Data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid email format...", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this,"Enter password...", Toast.LENGTH_SHORT).show()
        }
        else {
            loginuser()
        }

    }

    private fun loginuser() {
        //3) Login - Firebase Auth

        //show progress
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        val firebaseUser = firebaseAuth.currentUser

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //Login success
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                ref.child(firebaseUser!!.uid)
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //failed login
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    fun setFullscreen(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

//    private fun checkUser() {
//        //4) Check user type - Firebase Auth
//
//        progressDialog.setMessage("Checking User...")
//
//        val firebaseUser = firebaseAuth.currentUser
//
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.child(firebaseUser!!.uid)
//            .addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    progressDialog.dismiss()
//
//                    //get user type e.g. user/admin
//                    val userType = snapshot.child("").value
//                    if(userType == "user"){
//                        //its simple user, open user dashboard
//                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
//                        finish()
//                    }
//                    else if (userType == "admin"){
//                        //its admin, open admin dashboard
//                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
//                        finish()
//                    }
//                }
//            })
//    }


}