package com.example.nuevo_parchaosr.utils.dialogs

import android.content.Context
import android.content.DialogInterface
import com.example.nuevo_parchaosr.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertUtils {

    fun showMaterialCloseSessionDialog(context : Context,positiveListener: DialogInterface.OnClickListener,
                                       negativeListener: DialogInterface.OnClickListener? = null) {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.AlertDialog)


        materialAlertDialogBuilder.setTitle(R.string.tituloCerrarSesion)
        materialAlertDialogBuilder.setMessage(R.string.auxCerrarSesion)
        materialAlertDialogBuilder.setPositiveButton(R.string.SiCerrarSesion,positiveListener)
        materialAlertDialogBuilder.setNegativeButton(R.string.NoCerrarSesion, negativeListener)
        materialAlertDialogBuilder.show()
    }
  fun showMaterialJoinParcheDialog(context : Context,positiveListener: DialogInterface.OnClickListener,
                                     negativeListener: DialogInterface.OnClickListener? = null,parche:String) {
    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.AlertDialog)


    materialAlertDialogBuilder.setTitle("Ingreso de parche")
    materialAlertDialogBuilder.setMessage("Deseas ingresar a $parche?")
    materialAlertDialogBuilder.setPositiveButton("Si, estoy seguro \uD83D\uDE0E",positiveListener)
    materialAlertDialogBuilder.setNegativeButton("No, este parche es zzzzz", negativeListener)
    materialAlertDialogBuilder.show()
  }
  fun showMaterialPermissionCamera(context : Context,positiveListener: DialogInterface.OnClickListener,
                                   negativeListener: DialogInterface.OnClickListener? = null) {
    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.AlertDialog)


    materialAlertDialogBuilder.setTitle("Permiso camara")
    materialAlertDialogBuilder.setMessage("El permiso de la cámara es necesario, por favor aceptar el uso de esta")
    materialAlertDialogBuilder.setPositiveButton("Ok",positiveListener)
    materialAlertDialogBuilder.setNegativeButton("Cancelar", negativeListener)
    materialAlertDialogBuilder.show()
  }
    fun showCreationDialog(context : Context,positiveListener: DialogInterface.OnClickListener) {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.AlertDialog)


        materialAlertDialogBuilder.setTitle("Creación del parche")
        materialAlertDialogBuilder.setMessage("El parche se ha creado exitosamente, ahora puedes invitar a tus amigos \uD83D\uDE0E")
        materialAlertDialogBuilder.setPositiveButton("Okay ",positiveListener)
        materialAlertDialogBuilder.show()
    }
}
