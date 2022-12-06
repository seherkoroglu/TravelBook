package com.seherkoroglu.kotlinmaps.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.seherkoroglu.kotlinmaps.databinding.ActivityGirisSayfasiBinding

class GirisSayfasi : AppCompatActivity() {
    private lateinit var binding: ActivityGirisSayfasiBinding
    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGirisSayfasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences=getSharedPreferences("bilgiler", MODE_PRIVATE)

        binding.btnGirisYap?.setOnClickListener {
            var kayitlikullinici = preferences.getString("kullanici","")
            var kayitliparola = preferences.getString("parola","")

            var girisKullanici = binding.girisKullaniciAdi.text.toString()
            var girisParola = binding.girisParola.text.toString()

            if((kayitlikullinici==girisKullanici)&&(kayitliparola==girisParola)){
                intent = Intent(applicationContext, IlkEkran::class.java)
                startActivity(intent)
            }else if ((girisKullanici == "")&&( girisParola == "")){
                Toast.makeText(applicationContext,"Kullanıcı adı ya da parola giriniz!",Toast.LENGTH_LONG).show()
            }else if(girisParola == ""){
                Toast.makeText(applicationContext,"Parolanızı giriniz!",Toast.LENGTH_LONG).show()
            }else if(girisKullanici == ""){
                Toast.makeText(applicationContext,"Kullanıcı adınızı giriniz!",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,"Giriş Bilgileri Hatalı!",Toast.LENGTH_LONG).show()
            }





        }
        binding.btnKayitOl.setOnClickListener {
            intent = Intent(applicationContext, MainKayitol::class.java)
            startActivity(intent)
    }
}}