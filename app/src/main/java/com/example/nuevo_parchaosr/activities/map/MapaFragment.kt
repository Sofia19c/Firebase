package com.example.nuevo_parchaosr.activities.map


import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nuevo_parchaosr.activities.BasicFragment
import com.example.nuevo_parchaosr.databinding.MapaBinding
import com.example.nuevo_parchaosr.services.FireBaseService
import com.example.nuevo_parchaosr.utils.PermissionHelper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class MapaFragment : BasicFragment(), OnMapReadyCallback,OnMarkerClickListener,
    GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMyLocationClickListener, OnLocationChangedListener{
    private lateinit var binding: MapaBinding
    private lateinit var map: GoogleMap
    private var locationButton: View? = null
    private var stayAlive: Boolean = false
    //temporales
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var fireBaseService = FireBaseService()
    private lateinit var locationCallback: LocationCallback
    private var correo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      val bundle = this.arguments
        if (bundle != null) {
            correo = bundle.getString("correo").toString()
        }
    }
     var permissionHelper = PermissionHelper()
    @SuppressLint("MissingPermission", "ResourceType")
    override fun onMapReady(p0: GoogleMap) {
        map=p0

        map.setOnMyLocationButtonClickListener(this)
        map.setOnMarkerClickListener(this)
        map.setOnMyLocationClickListener(this)

        enableLocation()
        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(com.example.nuevo_parchaosr.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationButton = mapFragment.view?.findViewById(0x2);
        locationButton?.visibility = View.GONE;
        map.uiSettings.isMapToolbarEnabled =false

    }

     @SuppressLint("MissingPermission")
     private fun enableLocation() {
         permissionHelper.getLocationPermission(activity)
         if(!::map.isInitialized) return
         if (permissionHelper.mLocationPermissionGranted) {
             map.isMyLocationEnabled=true
         }
         else
         {
             //activar permisos toast
             Toast.makeText(requireContext(), "Activa permisos de ubicacion.", Toast.LENGTH_SHORT).show()
         }
     }

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapaBinding.inflate(inflater)

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(com.example.nuevo_parchaosr.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
         binding.locate.setOnClickListener(View.OnClickListener {
             locationButton?.callOnClick()
         })
         binding.filter.setOnClickListener(View.OnClickListener {

         })
         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

         stayAlive = true

         startLocationUpdates()
         return binding.root
    }

    override fun onResume() {
        super.onResume()
      val bundle = this.arguments
      if (bundle != null) {
        correo = bundle.getString("correoUsuario").toString()
      }
      Toast.makeText(requireContext(), "Bienvenido $correo", Toast.LENGTH_SHORT).show()
        stayAlive= true
        startLocationUpdates()

       }

    override fun onPause() {


        stayAlive= false
        startLocationUpdates()
        super.onPause()

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapaFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    private fun createMarker(coordinates: LatLng, lugar:String) {
        val marker: MarkerOptions = MarkerOptions().position(coordinates).title(lugar)
        val err: Double = -999.99
        if(lugar == "Amigo")
            if( coordinates.longitude!=err)
            marker.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            else
                marker.icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        else
        fireBaseService.estaDisponibleuId(onError = { error ->
            println(error)
            Toast.makeText(
                requireContext(),
                "Peto ",
                Toast.LENGTH_SHORT
            ).show()
        },
            onSuccess = { disponible ->

                if (!disponible){
                    marker.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).alpha(0.1f)
                }
                else {
                    marker.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(coordinates,18f),
                        4000,
                        null
                    )
                }
            })

        map.addMarker(marker)


    }

   @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
       Toast.makeText(
           requireContext(),
           "Enviando Ubicacion",
           Toast.LENGTH_SHORT
       ).show()
       CoroutineScope(Dispatchers.IO).launch{
           var dispo = false
           fireBaseService.estaDisponibleuId(onError = { error ->
               println(error)
               Toast.makeText(
                   requireContext(),
                   "Peto ",
                   Toast.LENGTH_SHORT
               ).show()
           },
               onSuccess = { disponible ->
                   dispo = disponible
               })
           while (stayAlive) {
               sleep(5000)
               fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                   if (dispo) {
                       //Actualizar ubicacion
                       fireBaseService.actualizarUbicacionUsuario(
                           location.latitude,
                           location.longitude
                       )
                   } else {
                       val err: Double = -999.99
                       fireBaseService.actualizarUbicacionUsuario(
                           err,
                           err
                       )
                   }
                   map.clear()
                   createMarker(LatLng(location.latitude, location.longitude), "Tu")
               }

                    fireBaseService.obtenerUbicacionPorCorreo(correo, onSuccess =
                    {
                        latitud, longitud ->
                        createMarker(LatLng(latitud,longitud), "Amigo")
                    }
                    , onError = {
                        error ->
                            Toast.makeText(
                                requireContext(),
                                "Error: $error",
                                Toast.LENGTH_SHORT
                            ).show()
                        })




           }
       }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(requireContext(), "Never gonna give you up", Toast.LENGTH_SHORT).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "Centrando", Toast.LENGTH_SHORT).show()
        return false
    }

     override fun onMarkerClick(p0: Marker): Boolean {
         return true
     }

    override fun onLocationChanged(p0: Location) {
        Toast.makeText(requireContext(), "Latitud CHA"+p0.latitude, Toast.LENGTH_SHORT).show()
    }

}
