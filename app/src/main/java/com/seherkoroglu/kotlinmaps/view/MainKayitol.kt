package com.seherkoroglu.kotlinmaps.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.seherkoroglu.kotlinmaps.databinding.ActivityMainKayitolBinding

class MainKayitol : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainKayitolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnKaydet.setOnClickListener {
            var kullaniciBilgisi = binding.kayitKullaniciAdi.text.toString()
            var kullaniciParola = binding.kayitParola.text.toString()
            var SharedPreferences = this.getSharedPreferences("bilgiler", MODE_PRIVATE)
            var editor = SharedPreferences.edit()

            if (kullaniciParola == "" || kullaniciBilgisi=="") {
                Toast.makeText(
                    applicationContext,
                    "Kullanıcı adı veya parola boş kaydedilemez",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //veri ekleme
                editor.putString("kullanici", "$kullaniciBilgisi").apply()
                editor.putString("parola", "$kullaniciParola").apply()
                Toast.makeText(applicationContext, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                binding.kayitKullaniciAdi.text.clear()
                binding.kayitParola.text.clear()
            }
        }
        binding.btnGiriseDon.setOnClickListener {
            intent=Intent(applicationContext, GirisSayfasi::class.java)
            startActivity(intent)
        }
    }
}