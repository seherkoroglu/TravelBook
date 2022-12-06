package com.seherkoroglu.kotlinmaps.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.seherkoroglu.kotlinmaps.R
import com.seherkoroglu.kotlinmaps.adapter.PlaceAdapter
import com.seherkoroglu.kotlinmaps.databinding.ActivityIlkEkranBinding
import com.seherkoroglu.kotlinmaps.databinding.ActivityMapsBinding
import com.seherkoroglu.kotlinmaps.model.Yer
import com.seherkoroglu.kotlinmaps.roomdb.YerDb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*




class IlkEkran : AppCompatActivity() {
    private lateinit var binding: ActivityIlkEkranBinding
    private val mDisposable = CompositeDisposable()
    private lateinit var places: ArrayList<Yer>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIlkEkranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        places = ArrayList()
        binding.haritayaGit.setOnClickListener {
            intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
        binding.cikis.setOnClickListener {
            intent = Intent(applicationContext, GirisSayfasi::class.java)
            startActivity(intent)
        }

        val db = Room.databaseBuilder(applicationContext,YerDb::class.java,"Yerler").build()
        val yerDao=db.yerDao()

       val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            yerDao.HepsiniAl()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::yanitiIsle)
        )
    }
    private fun yanitiIsle(placeList: List<Yer>){
    binding.recyclerView.layoutManager=LinearLayoutManager(this)
        val adapter = PlaceAdapter(placeList)
        binding.recyclerView.adapter=adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.yermenusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.yer_ekle){
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("Bilgi", "yeni")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }
}