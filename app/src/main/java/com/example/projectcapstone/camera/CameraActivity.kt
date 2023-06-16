package com.example.projectcapstone.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dalila.submission_intermediete.camera.createCustomTempFile
import com.dalila.submission_intermediete.camera.reduceFileImage
import com.dalila.submission_intermediete.camera.uriToFile
import com.example.projectcapstone.databinding.ActivityCameraBinding
import com.example.projectcapstone.detailscan.DetailActivity
import com.example.projectcapstone.preferences.UserPreference
import com.example.projectcapstone.viewmodel.CameraViewModel
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private val cameraViewModel by viewModels<CameraViewModel>()
    private lateinit var currentPhotoPath: String
    private lateinit var pref: UserPreference

    private var getFile: File? = null

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Terjadi kegagalan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val action = supportActionBar
        action!!.title = "Photo Your Batik"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.camera.setOnClickListener {
            startTakePhoto()
        }

        binding.gallerySv.setOnClickListener {
            startGallery()
        }

        binding.upload.setOnClickListener {
            val description = binding.upload.text.toString()
            println(getFile)
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                cameraViewModel.uploadStory(pref.getUser().token, file, description) {cameraResponse ->
                    if (cameraResponse != null) {
                        val intent = Intent(this@CameraActivity, DetailActivity::class.java)
                        intent.putExtra("cameraResponse", cameraResponse)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            cameraViewModel.uploadResponse.observe(this) {
                if (it != null) {
                    val dialog = AlertDialog.Builder(this).apply {
                        setTitle("Upload Berhasil")
                        setMessage("Selamat anda sudah dapat melihat hasil foto anda di halaman detail")
                        setCancelable(false)
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(this@CameraActivity, DetailActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }.create()
                    dialog.show()
                }
            }
            cameraViewModel.error.observe(this) { error ->
                if (error != null) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }

        }

        cameraViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@CameraActivity, "com.example.projectcapstone.camera", it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@CameraActivity)
                getFile = myFile
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}