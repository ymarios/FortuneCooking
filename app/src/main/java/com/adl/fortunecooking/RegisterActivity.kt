package com.adl.fortunecooking

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.adl.fortunecooking.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding:ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        //progress dialog, will show while creating account
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        btn_register_submit.setOnClickListener({
            register()
        })

        btnBackRegister.setOnClickListener({
            onBackPressed()
        })
    }

    private var name = ""
    private var email = ""
    private var password = ""

    private fun register() {
    //1) Input Data
        name = et_register_nama.text.toString().trim()
        email = et_register_email.text.toString().trim()
        password = et_register_password.text.toString().trim()
        val cPassword = et_register_password2.text.toString().trim()

        //2) Validate Data
        if (name.isEmpty()){
            //empty name...
            Toast.makeText(this,"Enter your name...", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email pattern
            Toast.makeText(this,"Invalid Email Pattern...", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            //empty password
            Toast.makeText(this,"Enter password...", Toast.LENGTH_SHORT).show()
        }
        else if (cPassword.isEmpty()){
            //empty confirm password
            Toast.makeText(this,"Confirm password...", Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword){
            Toast.makeText(this,"Password doesn't match...", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }





//        auth.createUserWithEmailAndPassword(
//            et_register_email.text.toString(),
//            et_register_password.text.toString()
//        )
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//
//                    val profileUpdates = userProfileChangeRequest {
//                        displayName = et_register_nama.text.toString()
//                        photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//                    }
//
//                    user!!.updateProfile(profileUpdates)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Log.d(TAG, "User profile updated.")
//                            }
//                        }
//
//                    finish()
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                }
//            }

//        auth.createUserWithEmailAndPassword(et_register_email.text.toString(), et_register_password.text.toString()).addOnCompleteListener{
//                it ->
//            if(it.isSuccessful) {
//                finish()
//            }
//        }.addOnFailureListener{
//                exception -> Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()}
//    }


    }

    private fun createUserAccount() {
        //3) Create Account - Firebase Auth

        //show progress
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //account created
                updateUserInfo()
            }
            .addOnFailureListener { e->
                //failed creating account
                progressDialog.dismiss()
                Toast.makeText(this,"Failed creating account due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {
        //4) Save user info - Firebase Realtime Database

        progressDialog.setMessage("Saving user info...")

        //timestamp
        val timestamp = System.currentTimeMillis()

        //get current user uid, since user is registered so we can get it now
        val uid = firebaseAuth.uid

        //setup data to add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = "" //add empty, will do in profile edit
//        hashMap["userType"] = "user" //possible values are user/admin, will change value to admin manually on firebase db

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //user info saved, open user dashboard
                progressDialog.dismiss()
                Toast.makeText(this,"Account created...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, DashboardActivity::class.java))
                finish()

            }
            .addOnFailureListener { e->
                //failed adding data to db
                progressDialog.dismiss()
                Toast.makeText(this,"Failed saving user info due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}