package com.example.FishyFinder.ui.view.information

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.FishyFinder.Model.PredictionResponse
import com.example.FishyFinder.R
import com.example.FishyFinder.ui.view.BaseActivity
import com.example.FishyFinder.ui.view.ResultFragment
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FotoFragment : Fragment() {

    private lateinit var imagePreview: ImageView
    private lateinit var btnProcessImage: MaterialButton
    private var imageUri: Uri? = null
    private var photoFile: File? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri?.let {
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imagePreview.setImageBitmap(bitmap)
                    inputStream?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Gagal menampilkan gambar", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_foto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagePreview = view.findViewById(R.id.imagePreview)
        btnProcessImage = view.findViewById(R.id.btnProcessImage)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Ambil Foto"

        view.findViewById<View>(R.id.imagePreviewContainer).setOnClickListener {
            if (checkCameraPermission()) openCamera()
            else requestCameraPermission()
        }

        btnProcessImage.setOnClickListener {
            photoFile?.let { file ->
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

                ApiClient.instance.predictImage(multipartBody).enqueue(object : retrofit2.Callback<PredictionResponse> {
                    override fun onResponse(call: Call<PredictionResponse>, response: retrofit2.Response<PredictionResponse>) {
                        if (response.isSuccessful) {
                            val prediction = response.body()
                            val resultFragment = ResultFragment().apply {
                                arguments = Bundle().apply {
                                    putString("predicted_class", prediction?.predicted_class)
                                    putFloat("confidence", prediction?.confidence ?: 0f)
                                    putString("image_path", file.absolutePath)
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
            } ?: Toast.makeText(requireContext(), "Tidak ada gambar yang tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile!!
        )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraLauncher.launch(intent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(requireContext(), "Izin kamera dibutuhkan", Toast.LENGTH_SHORT).show()
        }
    }
}
