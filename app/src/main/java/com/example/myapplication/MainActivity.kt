/* myRun1 Lab 1 -- The User Profile

Complete the profile activity. Save the profile to the phone and reload when needed.

Name Jea Oh Lee
Student # 301351043
Course CMPT 362

Date Sept 20, 2024

 */
package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity"  // Logging tag
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var classEditText: EditText
    private lateinit var majorEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var profileImageView: ImageView
    private lateinit var imageUri: Uri
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private val tempImgFileName = "profile_image.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing UI elements
        firstNameEditText = findViewById(R.id.first_name_edit_text)
        lastNameEditText = findViewById(R.id.last_name_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        phoneEditText = findViewById(R.id.phone_edit_text)
        classEditText = findViewById(R.id.class_edit_text)
        majorEditText = findViewById(R.id.major_edit_text)
        genderRadioGroup = findViewById(R.id.gender_radio_group)
        profileImageView = findViewById(R.id.imageProfile)

        //initializing the button elements
        val saveButton: Button = findViewById(R.id.save_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)
        val changePhotoButton: Button = findViewById(R.id.change_photo_button)

        //initializing camera permission launcher
        requestCameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(tag, "Camera permission granted")
                openCamera()
            } else {
                Log.d(tag, "Camera permission denied")
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        //loading saved data when the app starts
        loadUserData()

        //saving data when the Save button is clicked
        saveButton.setOnClickListener {
            Log.d(tag, "Save button clicked")
            saveUserData()
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
        }

        //exiting without save (close app)
        cancelButton.setOnClickListener {
            Log.d(tag, "Cancel button clicked")
            finish()
        }

        //opening camera when change photo button is tapped
        changePhotoButton.setOnClickListener {
            Log.d(tag, "Change button clicked")
            checkPermissionsAndOpenCamera()
        }
    }

    //saving user data to SharedPreferences
    private fun saveUserData() {
        Log.d(tag, "Saving user data")
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("firstName", firstNameEditText.text.toString())
        editor.putString("lastName", lastNameEditText.text.toString())
        editor.putString("email", emailEditText.text.toString())
        editor.putString("phone", phoneEditText.text.toString())
        editor.putString("class", classEditText.text.toString())
        editor.putString("major", majorEditText.text.toString())

        //gender selection saved
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        if (selectedGenderId != -1) {
            val selectedGender = findViewById<RadioButton>(selectedGenderId).text.toString()
            editor.putString("gender", selectedGender)
            Log.d(tag, "Gender saved: $selectedGender")
        }

        //img URI saved
        if (::imageUri.isInitialized) {
            editor.putString("profileImageUri", imageUri.toString())
            Log.d(tag, "Image URI saved: $imageUri")
        }

        //saving the data
        editor.apply()
        Log.d(tag, "User data saved successfully")
    }

    //loading user data from SharedPreferences
    private fun loadUserData() {
        Log.d(tag, "Loading user data")
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        firstNameEditText.setText(sharedPref.getString("firstName", ""))
        lastNameEditText.setText(sharedPref.getString("lastName", ""))
        emailEditText.setText(sharedPref.getString("email", ""))
        phoneEditText.setText(sharedPref.getString("phone", ""))
        classEditText.setText(sharedPref.getString("class", ""))
        majorEditText.setText(sharedPref.getString("major", ""))

        //loading saved gender and displaying the saved decision
        val savedGender = sharedPref.getString("gender", "")
        if (savedGender == "Male") {
            findViewById<RadioButton>(R.id.radio_male).isChecked = true
        } else if (savedGender == "Female") {
            findViewById<RadioButton>(R.id.radio_female).isChecked = true
        }
        Log.d(tag, "Gender loaded: $savedGender")

        //loading saved profile img
        val savedImageUri = sharedPref.getString("profileImageUri", null)
        if (savedImageUri != null) {
            imageUri = Uri.parse(savedImageUri)
            profileImageView.setImageURI(imageUri)
        }
    }

    //checking if the camera has permissions and open
    private fun checkPermissionsAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "Camera permission already granted")
            openCamera()
        } else {
            Log.d(tag, "Requesting camera permission")
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    //opening the camera to take a photo
    private fun openCamera() {
        Log.d(tag, "Opening camera")
        val tempImgFile = createImageFile()
        imageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", tempImgFile)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        cameraResultLauncher.launch(takePictureIntent)
        Log.d(tag, "Camera intent launched")
    }

    //creating an img file to store the captured photo
    private fun createImageFile(): File {
        Log.d(tag, "Creating image file")
        val storageDir = getExternalFilesDir(null)
        if (storageDir == null) {
            Log.d(tag, "Failed to access storage directory")
            Toast.makeText(this, "Failed to access storage directory", Toast.LENGTH_SHORT).show()
            throw IOException("Storage directory is null")
        }
        return try {
            File.createTempFile(tempImgFileName, ".jpg", storageDir)
        } catch (ex: IOException) {
            Log.d(tag, "File creation failed")
            Toast.makeText(this, "File creation failed", Toast.LENGTH_SHORT).show()
            throw ex
        }
    }

    //handling img rotation using ExifInterface
    private fun getCorrectlyOrientedBitmap(imageUri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val exif: ExifInterface
        try {
            val fileDescriptor = contentResolver.openFileDescriptor(imageUri, "r")?.fileDescriptor
            exif = ExifInterface(fileDescriptor!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return bitmap
        }

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    //rotating the bitmap by the specified degrees
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    //handling camera result and rotate img if needed
    private val cameraResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && ::imageUri.isInitialized) {
                Log.d(tag, "Camera image captured successfully")

                //getting the correctly oriented image
                val bitmap = getCorrectlyOrientedBitmap(imageUri)
                if (bitmap != null) {
                    profileImageView.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(tag, "Failed to capture image")
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
}
