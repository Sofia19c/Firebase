package com.example.nuevo_parchaosr.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.nuevo_parchaosr.activities.acces.LoginActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

abstract class BasicFragment : Fragment() {
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    auth = FirebaseAuth.getInstance()
  }

  override fun onResume() {
    super.onResume()
    checkAuth()
  }

  private fun checkAuth() {
    auth.currentUser!!.reload().addOnCompleteListener { task: Task<Void?> ->
      if (task.isSuccessful) {
        if (auth.currentUser == null) {
          val intent = Intent(activity, LoginActivity::class.java)
          startActivity(intent)
        }
      } else {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
      }
    }
  }
}

