package com.example.nuevo_parchaosr.activities.acces

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.widget.Toast
import com.example.nuevo_parchaosr.R
import com.example.nuevo_parchaosr.activities.BasicActivity
import com.example.nuevo_parchaosr.activities.MainActivity
import com.example.nuevo_parchaosr.databinding.LoginBinding
import com.example.nuevo_parchaosr.services.FireBaseService
import com.example.nuevo_parchaosr.utils.PermissionHelper
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BasicActivity() {
    private lateinit var binding: LoginBinding
    var auth = FirebaseAuth.getInstance()
    var fireBaseService = FireBaseService()
    var permissionHelper = PermissionHelper()
    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        permissionHelper.getLocationPermission(this)
        binding.loginButton.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            doLogin()
        }

        binding.registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivty::class.java)
            startActivity(intent)
        }
        binding.passwordInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doLogin()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

    }
    @SuppressLint("MissingPermission")
    fun doLogin(){
        if (binding.emailInput.text.toString().isEmpty() &&
            binding.passwordInput.text.toString().isEmpty()
        ) {
            binding.emailInput.error = getString(R.string.error_campos_vacios)
            binding.passwordInput.error = getString(R.string.error_campos_vacios)
            return
        } else{
            if (binding.emailInput.text.toString().isEmpty()){
                binding.emailInput.error = getString(R.string.error_campos_vacios)
                return
            }
            if (binding.passwordInput.text.toString().isEmpty()){
                binding.passwordInput.error = getString(R.string.error_campos_vacios)
                return
            }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.text.toString()).matches()){
                binding.emailInput.error = getString(R.string.error_correo_invalido)
                return
            }

        }

        if (!permissionHelper.mLocationPermissionGranted) {
            Toast.makeText(this, "Activa permisos de ubicacion.", Toast.LENGTH_SHORT).show()
            return
        }
        binding.emailInput.error = null
        binding.passwordInput.error = null
      val progressDialog = ProgressDialog(this, R.style.CustomProgressDialog)
      progressDialog.setMessage("Entrando a la cuenta...")
      progressDialog.setCancelable(false)
      progressDialog.show()
        auth.signInWithEmailAndPassword(
            binding.emailInput.text.toString(),
            binding.passwordInput.text.toString()
        )
            .addOnSuccessListener {
              progressDialog.dismiss()
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                var latitud = location!!.latitude
                var longitud = location!!.longitude
                fireBaseService.actualizarUbicacionUsuario(latitud, longitud)
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                ) }
            .addOnFailureListener {
              progressDialog.dismiss()
              Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
    }


}
