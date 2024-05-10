package com.example.nuevo_parchaosr.services

import android.util.Log
import com.example.nuevo_parchaosr.model.Usuario
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FireBaseService {

    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val auth = FirebaseAuth.getInstance()
    fun insertarUsuario(usuario: Usuario) {
        val referenceUsuario = database.getReference("usuarios/${auth.currentUser?.uid}")
        referenceUsuario.setValue(usuario)
    }

    fun actualizarCorreoUsuario(correoN: String,contrasena: String,emailViejo:String):Boolean {
        val credential = EmailAuthProvider.getCredential(auth.currentUser!!.email!!, contrasena)
        auth.currentUser!!.reauthenticate(credential)
            .addOnSuccessListener {
                // 3. Actualizar el correo electrónico del usuario
                auth.currentUser!!.updateEmail(correoN)
                    .addOnSuccessListener {
                        // El correo electrónico se actualizó con éxito
                    }
                    .addOnFailureListener { e ->
                        // Se produjo un error al actualizar el correo electrónico
                    }
            }
            .addOnFailureListener { e ->
                // Se produjo un error al volver a autenticar al usuario
            }
        return false
    }
    fun actualizarContrasenaUsuario(contrasena: String):Boolean{
        val user = auth.currentUser
        var cambiado = false
        user?.let {
            user.updatePassword(contrasena).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cambiado = true
                }
            }
        }
        return cambiado
    }
    fun obtenerUsuarioPorId(idUsuario: String, callback: (Usuario?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val referenciaUsuario = database.getReference("usuarios/$idUsuario")
        referenciaUsuario.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                callback(usuario)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
    fun actualizarUbicacionUsuario(latitud: Double, longitud: Double) {
        val referenceUsuario = database.getReference("usuarios/${auth.currentUser?.uid}")
        referenceUsuario.child("latitud").setValue(latitud)
        referenceUsuario.child("longitud").setValue(longitud)
    }
    fun obtenerUsuariosDisponibles( callback: (List<Usuario>) -> Unit) {
        val referenciaUsuarios = database.getReference("usuarios")
        referenciaUsuarios.orderByChild("disponible").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usuarios = mutableListOf<Usuario>()
                    for (childSnapshot in snapshot.children) {
                        val usuario = childSnapshot.getValue(Usuario::class.java)
                        if (usuario != null && usuario.correo != auth.currentUser?.email.toString()) {
                            usuarios.add(usuario)
                        }
                    }
                    callback(usuarios)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }
    fun actualizarNombreUsuario(nombre: String) {
    val user = auth.currentUser

    user?.let {
      val databaseReference = database.getReference("usuarios/${user.uid}")
      databaseReference.child("nombre").setValue(nombre)
    }
  }

  fun actualizarApellidoUsuario(apellido: String) {
    val user = auth.currentUser

    user?.let {
      val databaseReference = database.getReference("usuarios/${user.uid}")
      databaseReference.child("apellido").setValue(apellido)
    }
  }
  fun actualizarIdentificacionUsuario(identificacion: String) {
    val user = auth.currentUser

    user?.let {
      val databaseReference = database.getReference("usuarios/${user.uid}")
      databaseReference.child("numeroIdentificacion").setValue(identificacion)
    }
  }

    fun actualizarDisponibilidadUsuario(b: Boolean) {
        val user = auth.currentUser
        user?.let {
            val databaseReference = database.getReference("usuarios/${user.uid}")
            databaseReference.child("disponible").setValue(b)
        }
    }
    fun obtenerDisponibilidadUsuario(onSuccess: (Boolean) -> Unit, onError: (DatabaseError) -> Unit) {
        val user = auth.currentUser
        user?.let {
            val databaseReference = database.getReference("usuarios/${user.uid}/disponible")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val disponible = dataSnapshot.getValue(Boolean::class.java)
                    onSuccess.invoke(disponible ?: false)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    onError.invoke(databaseError)
                }
            })
        }
    }
    //Funcion inutil
    fun actualizarUbicacionPorUsuario(usuario: Usuario,nuevaLatitud: Double,nuevaLongitud: Double) {
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        val query: Query = ref.orderByChild("correo").equalTo(usuario.correo)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val usuarioSnapshot = dataSnapshot.children.first()
                    val usuarioId = usuarioSnapshot.key!!
                    Log.i("Usuario",usuarioId)
                    Log.i("Usuario",dataSnapshot.children.first().toString())
                    val usuarioRef = ref.child(usuarioId)
                    val actualizacion: MutableMap<String, Any> = HashMap()
                    actualizacion["latitud"] = nuevaLatitud
                    actualizacion["longitud"] = nuevaLongitud
                    Log.i("Actualizacion",actualizacion.toString())
                    usuarioRef.updateChildren(actualizacion)
                        .addOnSuccessListener {
                            Log.i("Actualizacion","Actualizacion exitosa")
                        }
                        .addOnFailureListener {
                            Log.i("Actualizacion","Actualizacion fallida")
                        }
                } else {
                   Log.i("Error","No existe el usuario")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error en caso de que ocurra.
            }
        })


    }
    //Actualizar la ubicación del propio usuario en firebase
    fun actualizarUbicacionPorUid(nuevaLatitud: Double,nuevaLongitud: Double){
        var userEmail = auth.currentUser!!.email.toString()
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        val query: Query = ref.orderByChild("correo").equalTo(userEmail)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val usuarioSnapshot = dataSnapshot.children.first()
                    val usuarioId = usuarioSnapshot.key!!
                    val usuarioRef = ref.child(usuarioId)
                    val actualizacion: MutableMap<String, Any> = HashMap()
                    actualizacion["latitud"] = nuevaLatitud
                    actualizacion["longitud"] = nuevaLongitud
                    usuarioRef.updateChildren(actualizacion)
                        .addOnSuccessListener {
                            Log.i("Actualizacion","Actualizacion exitosa")
                        }
                        .addOnFailureListener {
                            Log.i("Actualizacion","Actualizacion fallida")
                        }
                } else {
                    Log.i("Error","No existe el usuario")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error en caso de que ocurra.
            }
        })
    }
    //Dado un usuario obtener la ubicacion de este
    fun obtenerUbicacionPorCorreo(correo: String, onSuccess: (latitud: Double, longitud: Double) -> Unit, onError: (error: DatabaseError) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        val query: Query = ref.orderByChild("correo").equalTo(correo)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val usuarioSnapshot = dataSnapshot.children.first()
                    val latitud = usuarioSnapshot.child("latitud").getValue(Double::class.java)
                    val longitud = usuarioSnapshot.child("longitud").getValue(Double::class.java)

                    if (latitud != null && longitud != null) {
                        Log.i("Latitud",latitud.toString())
                        Log.i("Longitud",longitud.toString())
                        onSuccess(latitud, longitud)
                    } else {
                        onError(DatabaseError.fromException(Exception("Error al obtener latitud y longitud")))
                    }
                } else {
                    onError(DatabaseError.fromException(Exception("No se encontró el usuario")))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onError(databaseError)
            }
        })
    }
    fun estaDisponibleuId(onSuccess: (disponible: Boolean) -> Unit, onError: (error: DatabaseError) -> Unit){
        var userEmail = auth.currentUser!!.email.toString()
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        val query: Query = ref.orderByChild("correo").equalTo(userEmail)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val disponible = dataSnapshot.exists()
                onSuccess(disponible)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onError(databaseError)
            }
        })
    }
}
