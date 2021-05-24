package com.example.mycats.cats.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mycats.R
import com.example.mycats.databinding.ActivityFullScreenBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FullScreenActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    private var image: Bitmap? = null
    private var selectedOption: String? = null
    var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFullScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_full_screen)

        imageView = binding.fullScreenImageView

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title = "Full Screen Image"

        val imageUrl: String? = intent.getStringExtra("Full Image")
        imageUrl.let {
            binding.imageUrl = imageUrl
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_share -> share()
            R.id.action_save -> saveImage(this, image!!)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        selectedOption = "Share"
        this.image = getBitmapFromView(imageView)
        checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            when {
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                -> {
                    if (selectedOption == "Share") {
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/*"
                        share.putExtra(Intent.EXTRA_STREAM, getImageUrl(this, image!!))
                        startActivity(Intent.createChooser(share, "Share Using"))
                    } else if (selectedOption == "Save") {
                        val path = Environment.getExternalStorageDirectory()
                        val dir = File(path.absolutePath)
                        dir.mkdirs()
                        val file = File(dir, "Image")
                        val out: OutputStream

                        try {
                            out = FileOutputStream(file)
                            image!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                            out.flush()
                            out.close()

                            Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show()
                        } catch (e: Exception){
                            Toast.makeText(this, "Could Not Save Image", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkPermissions() {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (permission in permissions) {
            result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission)
            }
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 100)
        }
    }

    private fun getBitmapFromView(view: ImageView): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

    private fun getImageUrl(context: Context, image: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, image, "Title", null)

        return Uri.parse(path)
    }

    private fun saveImage(context: Context, image: Bitmap) {
        selectedOption = "Save"
        this.image = getBitmapFromView(imageView)
        checkPermissions()
    }

}