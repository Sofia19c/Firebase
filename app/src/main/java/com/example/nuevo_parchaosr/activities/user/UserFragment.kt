package com.example.nuevo_parchaosr.activities.user

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.example.nuevo_parchaosr.R
import com.example.nuevo_parchaosr.activities.BasicFragment
import com.example.nuevo_parchaosr.activities.acces.LoginActivity
import com.example.nuevo_parchaosr.activities.amigos.AmigosFragment
import com.example.nuevo_parchaosr.databinding.FragmentUserBinding
import com.example.nuevo_parchaosr.services.FireBaseService
import com.example.nuevo_parchaosr.utils.PermissionHelper
import com.example.nuevo_parchaosr.utils.dialogs.AlertUtils
import com.google.firebase.auth.FirebaseAuth
import lombok.Getter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Getter
class UserFragment : BasicFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentUserBinding
    var cambios:Boolean = false


    var auth = FirebaseAuth.getInstance()
    private val fireBaseService = FireBaseService()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.CerrarCuenta.setOnClickListener {
            val positiveListener = DialogInterface.OnClickListener { dialog, which ->
                FirebaseAuth.getInstance().signOut();
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            val negativeListener = DialogInterface.OnClickListener { dialog, which ->
                Log.e("Negativo", "Usuario continua en linea")
            }
            AlertUtils.showMaterialCloseSessionDialog(
                requireContext(),
                positiveListener,
                negativeListener
            )
        }

        binding.disponibleSwitch.setOnCheckedChangeListener { _, isChecked ->
            cambios = true
        }
        binding.EditarCuentaButton.setOnClickListener {

            if (binding.nombreInput.text?.isNotEmpty() == true) {
                cambios = true
                fireBaseService.actualizarNombreUsuario(binding.nombreInput.text.toString())
            }
            if (binding.apellidoInput.text?.isNotEmpty() == true) {
                fireBaseService.actualizarApellidoUsuario(binding.apellidoInput.text.toString())
                cambios = true
            }
            if (binding.identificacionInput.text?.isNotEmpty() == true) {
                fireBaseService.actualizarIdentificacionUsuario(binding.identificacionInput.text.toString())
                cambios = true
            }
            /*
            if (binding.emailInput.text?.isNotEmpty() == true) {
                fireBaseService.actualizarCorreoUsuario(binding.emailInput.text.toString(),
            }
            if (binding.contraseniaInput.text?.isNotEmpty() == true) {
                fireBaseService.actualizarContrasenaUsuario(binding.contraseniaInput.text.toString())
            }
            */
            if (binding.identificacionInput.text?.isNotEmpty() == true) {
                fireBaseService.actualizarIdentificacionUsuario(binding.identificacionInput.text.toString())
                cambios = true
            }

            fireBaseService.actualizarDisponibilidadUsuario(binding.disponibleSwitch.isChecked)
            if (cambios) {
                Toast.makeText(activity, "Datos actualizados", Toast.LENGTH_SHORT).show()
            }
            replaceFragment(AmigosFragment())
        }

    }
    override fun onResume() {
        super.onResume()
        cambios= false
        fireBaseService.obtenerUsuarioPorId(auth.currentUser!!.uid) { usuario ->
            binding.nombreInput.hint = usuario?.nombre
            binding.apellidoInput.hint = usuario?.apellido
            binding.emailInput.setText(auth.currentUser?.email.toString())
            binding.identificacionInput.hint = usuario?.numeroIdentificacion
            binding.disponibleSwitch.isChecked = usuario?.disponible == true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
    }
}

