package com.example.nuevo_parchaosr.activities.acces

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.nuevo_parchaosr.R
import com.example.nuevo_parchaosr.activities.MainActivity
import com.example.nuevo_parchaosr.databinding.RegisterBinding
import com.example.nuevo_parchaosr.model.Usuario
import com.example.nuevo_parchaosr.services.FireBaseService
import com.example.nuevo_parchaosr.utils.PermissionHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivty : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    var auth = FirebaseAuth.getInstance()
    private var fireBaseService = FireBaseService()
  var permissionHelper = PermissionHelper()


  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    permissionHelper.getLocationPermission(this)
    if (!permissionHelper.mLocationPermissionGranted) {
      Toast.makeText(this, "Activa permisos de ubicacion.", Toast.LENGTH_SHORT).show()
    }

      binding.botonRegister.setOnClickListener {
        doRegister()
        }
      binding.inicioSesionDesdeRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    binding.passwordConfirmInputR.setOnKeyListener { _, keyCode, event ->
      if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
        doRegister()
        return@setOnKeyListener true
      }
      return@setOnKeyListener false
    }

    }
  @SuppressLint("MissingPermission")
  fun doRegister() {
    if (permissionHelper.mLocationPermissionGranted) {
      if (formulariosValidos()) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding.emailInputR.error = null
        binding.passwordInputR.error = null
        binding.identificacionInputR.error = null

        val progressDialog = ProgressDialog(this, R.style.CustomProgressDialog)
        progressDialog.setMessage("Creando cuenta...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        auth.createUserWithEmailAndPassword(
          binding.emailInputR.text.toString(),
          binding.passwordInputR.text.toString()
        )
          .addOnSuccessListener {
            progressDialog.dismiss()
            var usuario = Usuario()
            usuario.nombre = binding.nombreInputR.text.toString()
            usuario.apellido = binding.apellidoInputR.text.toString()
            usuario.numeroIdentificacion = binding.identificacionInputR.text.toString()
            usuario.correo = binding.emailInputR.text.toString()
            val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            usuario.latitud = location!!.latitude
            usuario.longitud = location!!.longitude
            fireBaseService.insertarUsuario(usuario)
            startActivity(
              Intent(
                this,
                MainActivity::class.java
              )
            )
          }
          .addOnFailureListener { e: Exception ->
            progressDialog.dismiss()
            if (e is FirebaseAuthUserCollisionException) {
              Toast.makeText(this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
            } else {
              Toast.makeText(this, "Fallo en la creación de la cuenta", Toast.LENGTH_SHORT).show()
            }
          }
      } else {
        return
      }
    } else {
      Toast.makeText(this, "Activa permisos de ubicacion.", Toast.LENGTH_SHORT).show()
    }
  }

  fun formulariosValidos(): Boolean{
    if (binding.emailInputR.text.toString().isEmpty() &&
      binding.passwordInputR.text.toString().isEmpty() &&
      binding.identificacionInputR.text.toString().isEmpty() &&
      binding.passwordConfirmInputR.text.toString().isEmpty() &&
              binding.nombreInputR.text.toString().isEmpty() &&
              binding.apellidoInputR.text.toString().isEmpty()
    ) {
      binding.emailInputR.error = getString(R.string.error_campos_vacios)
      binding.passwordInputR.error = getString(R.string.error_campos_vacios)
      binding.identificacionInputR.error = getString(R.string.error_campos_vacios)
      binding.passwordConfirmInputR.error = getString(R.string.error_campos_vacios)
        binding.nombreInputR.error = getString(R.string.error_campos_vacios)
        binding.apellidoInputR.error = getString(R.string.error_campos_vacios)
      return false
    } else{
      if (binding.emailInputR.text.toString().isEmpty()){
        binding.emailInputR.error = getString(R.string.error_campos_vacios)
        return false
      }
      if(binding.identificacionInputR.text.toString().isEmpty()){
        binding.identificacionInputR.error = getString(R.string.error_campos_vacios)
        return false
      }
      if (binding.passwordInputR.text.toString().isEmpty()){
        binding.passwordInputR.error = getString(R.string.error_campos_vacios)
        return false
      }
      if (binding.passwordConfirmInputR.text.toString().isEmpty()){
        binding.passwordConfirmInputR.error = getString(R.string.error_campos_vacios)
        return false
      }
      if(!Patterns.EMAIL_ADDRESS.matcher(binding.emailInputR.text.toString()).matches()){
        binding.emailInputR.error = getString(R.string.error_correo_invalido)
        return false
      }
      if(binding.passwordInputR.text.toString().length < 6){
        binding.passwordInputR.error = getString(R.string.error_password_corto)
        return false
      }
      if(!binding.checkboxConfirmarTerminos.isChecked){
        Toast.makeText(this, "Debes aceptar los terminos y condiciones", Toast.LENGTH_SHORT).show()
        return false
      }
      if (binding.passwordInputR.text.toString() != binding.passwordConfirmInputR.text.toString()){
        binding.passwordInputR.error = getString(R.string.error_passwords_no_coinciden)
        binding.passwordConfirmInputR.error = getString(R.string.error_passwords_no_coinciden)
        return false
      }
      if(binding.nombreInputR.text.toString().isEmpty()){
        binding.nombreInputR.error = getString(R.string.error_campos_vacios)
        return false
      }
      if (binding.apellidoInputR.text.toString().isEmpty()){
        binding.apellidoInputR.error = getString(R.string.error_campos_vacios)
        return false
      }
    }
    return true
  }
  override fun onBackPressed() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    }
}
