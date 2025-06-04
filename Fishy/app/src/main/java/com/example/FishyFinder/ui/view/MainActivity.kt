package com.example.FishyFinder.ui.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.FishyFinder.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        val btnAmbilFoto = findViewById<Button>(R.id.btnAmbilFoto)
//        val btnPilihGaleri = findViewById<Button>(R.id.btnPilihGaleri)
        val informasiSection = findViewById<LinearLayout>(R.id.informasiSection)
        val riwayatSection = findViewById<LinearLayout>(R.id.riwayatSection)
        val uploadSection = findViewById<LinearLayout>(R.id.uploadSection)
        val btnAirLaut = findViewById<Button>(R.id.btnAirLaut)
        val btnAirTawar = findViewById<Button>(R.id.btnAirTawar)


//        btnAmbilFoto.setOnClickListener {
//            val intent = Intent(this, BaseActivity::class.java)
//            intent.putExtra("fragment", "camera")
//            startActivity(intent)
//        }
//
//        btnPilihGaleri.setOnClickListener {
//            val intent = Intent(this, BaseActivity::class.java)
//            intent.putExtra("fragment", "gallery")
//            startActivity(intent)
//        }

        btnAirLaut.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("fragment", "airlaut")
            startActivity(intent)
        }

        btnAirTawar.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("fragment", "airtawar")
            startActivity(intent)
        }

        informasiSection.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("fragment", "information")
            startActivity(intent)
        }

        uploadSection.setOnClickListener {
            val options = arrayOf("Ambil dari Kamera", "Pilih dari Galeri")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Sumber Gambar")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            builder.show()
        }

        riwayatSection.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("fragment", "riwayat")
            startActivity(intent)
        }

    }
    private fun openCamera() {
        val intent = Intent(this, BaseActivity::class.java)
        intent.putExtra("fragment", "camera")
        startActivity(intent)
    }

    private fun openGallery() {
        val intent = Intent(this, BaseActivity::class.java)
        intent.putExtra("fragment", "gallery")
        startActivity(intent)
    }


}