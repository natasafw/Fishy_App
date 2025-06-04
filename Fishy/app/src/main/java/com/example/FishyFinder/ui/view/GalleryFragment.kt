package com.example.FishyFinder.ui.view.information

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.FishyFinder.Model.PredictionResponse
import com.example.FishyFinder.R
import com.example.FishyFinder.ui.view.BaseActivity
import com.example.FishyFinder.ui.view.ResultFragment
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream

class GalleryFragment : Fragment() {

    private lateinit var imagePreview: ImageView
    private lateinit var btnProcessImage: MaterialButton
    private var selectedImageUri: Uri? = null

    // ActivityResultLauncher untuk ambil gambar dari galeri
    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imagePreview.setImageURI(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Pilih dari Gallery"

        // Inisialisasi elemen UI
        imagePreview = view.findViewById(R.id.imagePreview)
        btnProcessImage = view.findViewById(R.id.btnProcessImage)

        // Klik pada imagePreview untuk buka galeri
        view.findViewById<View>(R.id.imagePreviewContainer).setOnClickListener {
            getImageFromGallery.launch("image/*")
        }

        // Klik tombol "Proses Gambar"
        btnProcessImage.setOnClickListener {
            selectedImageUri?.let { uri ->
                val filePath = getRealPathFromURI(uri)
                if (filePath != null) {
                    val file = File(filePath)
                    val requestFile = okhttp3.RequestBody.create(
                        "image/*".toMediaTypeOrNull(), file
                    )
                    val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

                    // Panggil endpoint Flask
                    ApiClient.instance.predictImage(multipartBody).enqueue(object : retrofit2.Callback<PredictionResponse> {
                        override fun onResponse(call: Call<PredictionResponse>, response: retrofit2.Response<PredictionResponse>) {
                            if (response.isSuccessful) {
                                val prediction = response.body()
                                val resultFragment = ResultFragment().apply {
                                    arguments = Bundle().apply {
                                        putString("predicted_class", prediction?.predicted_class)
                                        putFloat("confidence", prediction?.confidence ?: 0f)
                                        putString("image_path", filePath)
                                    }
                                }
                                (activity as? BaseActivity)?.replaceFragment(resultFragment)
                            } else {
                                Toast.makeText(requireContext(), "Gagal menerima hasil prediksi", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                            t.printStackTrace()
                            Toast.makeText(requireContext(), "Gagal menghubungi server: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    })
                } else {
                    Toast.makeText(requireContext(), "Gagal membaca path gambar", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getRealPathFromURI(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().cacheDir, "captured_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

