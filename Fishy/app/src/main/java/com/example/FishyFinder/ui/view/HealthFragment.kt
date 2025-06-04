package com.example.FishyFinder.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.FishyFinder.R

class HealthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foto, container, false)

        // Temukan tombol Retry
        val btnRetry = view.findViewById<Button>(R.id.btnProcessImage)

        // Ketika tombol diklik, kembali ke MainActivity
        btnRetry.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish() // Tutup activity yang sedang berjalan
        }

        return view
    }
}