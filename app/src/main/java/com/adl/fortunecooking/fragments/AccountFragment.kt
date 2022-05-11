package com.adl.fortunecooking.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.fortunecooking.*
import com.adl.fortunecooking.R
import com.adl.fortunecooking.adapter.FavAdapter
import com.adl.fortunecooking.adapter.MyVidAdapter
import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_favorites.*

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [AccountFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
class AccountFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    lateinit var lstDataResep : ArrayList<ResepModel>
    lateinit var database: DatabaseReference
    lateinit var myVidAdapter: MyVidAdapter

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()
        getMyVideo()
        btnLogOut.setOnClickListener({
            activity?.let{
                val intent = Intent (it, LoginActivity::class.java)
                it.startActivity(intent)
            }
        })

        btnEditProfil.setOnClickListener({
            activity?.let{
                val intent = Intent(it, ProfileEditActivity::class.java)
                it.startActivity(intent)
                }
        })

    }

    private fun loadUserInfo() {
        //db referencee to load user info
        val user = Firebase.auth.currentUser

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val uid = "${snapshot.child("uid").value}"

                    //set data
                    txtNamaAcc.text = name
                    txtEmailAcc.text = email

                    //set image
                    try {
                        Glide.with(this@AccountFragment)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_person_gray)
                            .into(imgProfile)
                    }
                    catch (e: Exception){

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun getMyVideo(){
        val currentUser = Firebase.auth.currentUser
        database= FirebaseDatabase.getInstance().reference.child("Videos")
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                lstDataResep.clear()
                for (data in snapshot.children){
                    if(data.child("userId").getValue(String::class.java) == currentUser?.uid!!){
                        val namaresep= data.child("title").getValue(String::class.java)
                        val imagelink = data.child("ImageUri").getValue(String::class.java)
                        val videolink = data.child("videoUri").getValue(String::class.java)
                        val userUid = data.child("userId").getValue(String::class.java)
                        val rating = data.child("rating").getValue(String::class.java)
                        val idVideo = data.child("id").getValue(String::class.java)
                        val desc = data.child("Deskripsi").getValue(String::class.java)
                        val resep = data.child("Resep").getValue(String::class.java)
                        val step = data.child("Step").getValue(String::class.java)
                        lstDataResep.add( ResepModel(idVideo.toString(),namaresep.toString(),userUid.toString(),imagelink.toString(), videolink.toString(),rating.toString(),resep.toString(),step.toString(), desc.toString()))
                    }

                    // Log.d("TAG", "nama: ${namaresep}\nimagelink: ${imagelink}")
                }
                myVidAdapter.notifyDataSetChanged()

            } else {
                Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
            }
        }
        lstDataResep = ArrayList<ResepModel>()

        myVidAdapter = MyVidAdapter(lstDataResep)
        rcMyVideo.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = myVidAdapter
        }
//        rcFavorite.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,true)
//            adapter = resepAdapter
//        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//
//    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment AccountFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AccountFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}