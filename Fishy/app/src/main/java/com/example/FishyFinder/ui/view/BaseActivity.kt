package com.example.FishyFinder.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.FishyFinder.R
import com.example.FishyFinder.ui.view.information.AirLautFragment
import com.example.FishyFinder.ui.view.information.AirTawarFragment
import com.example.FishyFinder.ui.view.information.FotoFragment
import com.example.FishyFinder.ui.view.information.GalleryFragment
import com.example.FishyFinder.ui.view.information.InformationFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        // Inisialisasi Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Tombol back di toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        // Cek apakah ada intent yang mengarah ke fragment tertentu
        val fragmentType = intent.getStringExtra("fragment")

        val fragment: Fragment = when (fragmentType) {
            "airlaut" -> AirLautFragment()
            "airtawar" -> AirTawarFragment()
            "information" -> InformationFragment()
            "riwayat" -> HistoryFragment()
            "camera" -> FotoFragment()
            "gallery" -> GalleryFragment()
            else -> FotoFragment() // Default ke FotoFragment
        }

        // Ganti fragment yang sedang ditampilkan
        replaceFragment(fragment)
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        // transaction.addToBackStack(null) ← HAPUS ini
        transaction.commit()
    }

}