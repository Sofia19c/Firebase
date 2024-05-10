package com.example.nuevo_parchaosr.activities.amigos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.nuevo_parchaosr.R
import com.example.nuevo_parchaosr.activities.map.MapaFragment
import com.example.nuevo_parchaosr.databinding.FragmentAmigosFragmentBinding
import com.example.nuevo_parchaosr.activities.BasicFragment
import com.example.nuevo_parchaosr.services.FireBaseService
import com.google.android.material.snackbar.Snackbar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AmigosFragment+.newInstance] factory method to
 * create an instance of this fragment.
 */
class AmigosFragment : BasicFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentAmigosFragmentBinding
    var fireBaseService = FireBaseService()


  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentAmigosFragmentBinding.inflate(inflater)
    return binding!!.root
  }

  companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AmigosFragment+.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AmigosFragment ().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


  override fun onResume() {
    super.onResume()
    fireBaseService.obtenerUsuariosDisponibles { listaUsuariosDisponibles ->
      if(!listaUsuariosDisponibles.isNullOrEmpty()){
        val listaDeNombresDeUsuarios = listaUsuariosDisponibles.map { it.nombre } //obtener solo los nombres de usuario
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_list_item, listaDeNombresDeUsuarios)
        binding?.listViewParches?.adapter = adapter
        binding?.listViewParches?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
          val bundle = Bundle()
          bundle.putString("correoUsuario", listaUsuariosDisponibles[position].correo)
          bundle.putString("nombreUsuario", listaUsuariosDisponibles[position].nombre)
          val mapaFragment = MapaFragment()
          mapaFragment.arguments = bundle
          replaceFragment(mapaFragment)
        }
      }else{
          val snackbar = Snackbar.make(binding.root, "No hay usuarios disponibles", Snackbar.LENGTH_LONG)
          snackbar.setAction("Cerrar") {
              snackbar.dismiss()
          }
          snackbar.show()

      }
    }


  }
  fun replaceFragment(fragment : Fragment){
    val fragmentTransaction = fragmentManager?.beginTransaction()
    fragmentTransaction?.replace(R.id.frame_layout,fragment)
    fragmentTransaction?.commit()
  }
}
