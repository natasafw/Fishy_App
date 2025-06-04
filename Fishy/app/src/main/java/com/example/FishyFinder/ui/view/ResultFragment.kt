package com.example.FishyFinder.ui.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.FishyFinder.R

class ResultFragment : Fragment() {

    private lateinit var imageResult: ImageView
    private lateinit var resultText: TextView
    private lateinit var detectedPlantsTitle: TextView
    private lateinit var detectedPlants: TextView
    private lateinit var detectedPlantsDescription: TextView
    private lateinit var allProbabilitiesText: TextView

    // Map dari predicted class ke label dan deskripsi
    private val predictionMap = mapOf(
        "IkanBawal_AirTawar" to Pair(
            "Ikan Bawal dari air tawar",
            "Ikan Bawal dari air tawar, memiliki rasa daging yang lembut dan gurih."
        ),
        "IkanGurame_AirTawar" to Pair(
            "Ikan Gurami dari air tawar",
            "Ikan Gurami dari air tawar, populer di masakan rumah."
        ),
        "IkanKerapu_AirLaut" to Pair(
            "Ikan Kerapu dari laut",
            "Ikan Kerapu dari laut, umumnya segar dan kaya protein."
        ),
        "IkanLepu_AirLaut" to Pair(
            "Ikan Lepu dari laut",
            "Ikan Lepu dari laut, hati-hati karena beberapa spesies memiliki racun."
        ),
        "IkanMas_AirTawar" to Pair(
            "Ikan Mas dari air tawar",
            "Ikan Mas dari air tawar, cocok untuk pepes atau goreng."
        ),
        "IkanSebelah_AirLaut" to Pair(
            "Ikan Sebelah dari laut",
            "Ikan Sebelah dari laut, biasa digunakan dalam masakan tim atau goreng."
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        imageResult = view.findViewById(R.id.imageResult)
        resultText = view.findViewById(R.id.resultText)
        detectedPlantsTitle = view.findViewById(R.id.detectedPlantsTitle)
        detectedPlants = view.findViewById(R.id.detectedPlants)
        detectedPlantsDescription = view.findViewById(R.id.detectedPlantsDescription)
        allProbabilitiesText = view.findViewById(R.id.allProbabilitiesText)

        val predictedClass = arguments?.getString("predicted_class") ?: "Tidak diketahui"
        val confidence = arguments?.getFloat("confidence") ?: 0f
        val imagePath = arguments?.getString("image_path")

        imagePath?.let {
            val bitmap = BitmapFactory.decodeFile(it)
            if (bitmap != null) {
                imageResult.setImageBitmap(bitmap)
            } else {
                imageResult.setImageResource(R.drawable.ic_gambar)
            }
        }

        val (label, description) = predictionMap[predictedClass] ?: Pair("Tidak diketahui", "Deskripsi tidak tersedia.")

        resultText.text = "Hasil Deteksi"
        detectedPlants.text = label
        detectedPlantsTitle.text = if (label.contains("laut", ignoreCase = true)) "Habitat: Air Laut" else "Habitat: Air Tawar"
        detectedPlantsDescription.text = description
        allProbabilitiesText.text = "Akurasi deteksi: %.2f%%".format(confidence * 100)

        return view
    }
}
