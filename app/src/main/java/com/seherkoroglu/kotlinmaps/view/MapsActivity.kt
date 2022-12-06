package com.seherkoroglu.kotlinmaps.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.seherkoroglu.kotlinmaps.R
import com.seherkoroglu.kotlinmaps.databinding.ActivityMapsBinding
import com.seherkoroglu.kotlinmaps.model.Yer
import com.seherkoroglu.kotlinmaps.roomdb.YerDao
import com.seherkoroglu.kotlinmaps.roomdb.YerDb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var sharedPreferences : SharedPreferences
    private var trackBoolean : Boolean?=null
    private var selectedLatitude : Double? = null
    private var selectedLongitude : Double? = null
    private lateinit var db: YerDb
    private lateinit var yerDao: YerDao
    private val compositeDisposable = CompositeDisposable()
    private var IlkEkrandanYer : Yer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        registerLauncher()
        selectedLatitude=0.0
        selectedLongitude=0.0
        binding.save?.isEnabled  =false
        sharedPreferences=getSharedPreferences("com.seherkoroglu.kotlinmaps", MODE_PRIVATE)
        trackBoolean=false


        db = Room.databaseBuilder(applicationContext,YerDb::class.java,"Yerler").build()
        yerDao = db.yerDao()


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)

        val intent = intent
        val Bilgi = intent.getStringExtra("Bilgi")

        if(Bilgi == "yeni"){
            binding.save?.visibility = View.VISIBLE
            binding.delete?.visibility = View.GONE
            //casting
            locationManager=this.getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener= object : LocationListener {
                override fun onLocationChanged(location: Location){
                    trackBoolean=sharedPreferences.getBoolean("trackBoolean",false)
                    if(!trackBoolean!!){
                        val userLocation = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                        sharedPreferences.edit().putBoolean("trackBoolean",true).apply()

                }

                }
            }
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(binding.root,"Konum İzni Gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("izin ver"){
                        //izin istegi
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }else{
                    //izin istegi
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }else{
                //izin verildi
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(lastLocation != null){
                    val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                }
            }

        }else{
            //Sqlite data && intent data
            mMap.clear()
            IlkEkrandanYer = intent.getSerializableExtra("SecilenYer") as Yer?
            IlkEkrandanYer?.let {
                val latLng =LatLng(it.latitude,it.longitude)

                mMap.addMarker((MarkerOptions().position(latLng).title(it.name)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))

                binding.editTextTextPersonName?.setText(it.name)
                binding.save?.visibility  = View.GONE
                binding.delete?.visibility = View.VISIBLE
            }
        }



        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVİDER,0,0F,LocationListener)
        // Add a marker in paris and move the camera
        //val eiffel = LatLng(48.856111 , 2.298333)
       // mMap.addMarker(MarkerOptions().position(eiffel).title("Eiffel Tower"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel, 15f))
    }

    private fun registerLauncher(){
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
        if(result){
            if(ContextCompat.checkSelfPermission(this@MapsActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //izin verildi
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(lastLocation != null){
                    val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
            }


        }else{
            //izin verilmedi
            Toast.makeText(this@MapsActivity,"İzin Gerekli!",Toast.LENGTH_LONG).show()
        }
        }
    }
}

    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        selectedLatitude=p0.latitude
        selectedLongitude=p0.longitude
        binding.save?.isEnabled  =true

    }

    fun save(view: View){
        if(selectedLatitude != null && selectedLongitude != null){
            val yer = Yer(binding.editTextTextPersonName?.text.toString(),selectedLatitude!!,selectedLongitude!!)
           compositeDisposable.add(
               yerDao.insert(yer)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(this::yanitiIsle)
           )
        }
    }

    private fun yanitiIsle(){
        val intent =Intent(this,IlkEkran::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun delete(view: View){
        IlkEkrandanYer?.let {
            compositeDisposable.add(
                yerDao.sil(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::yanitiIsle)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }
}