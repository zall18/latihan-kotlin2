package com.example.medsos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Gallery
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Base64

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addpost.newInstance] factory method to
 * create an instance of this fragment.
 */
class addpost : Fragment() {
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    lateinit var session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addpost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var image_upload_button = view.findViewById<AppCompatButton>(R.id.upload_image_button)
        var image_upload = view.findViewById<ImageView>(R.id.image_upload)
        var connection = connection()
        session = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE)
        var bitmap: Bitmap

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data ?: return@registerForActivityResult
                val selectedImageUri = data.data ?: return@registerForActivityResult

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                    image_upload.setImageBitmap(bitmap)
                }catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(requireContext().applicationContext, "Failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        image_upload_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"  // Filter to only pick images
            pickImageLauncher.launch(intent)
        }

        var post_upload = view.findViewById<AppCompatButton>(R.id.upload_button)
        var desc = view.findViewById<EditText>(R.id.desc_input)
        post_upload.setOnClickListener {

            var ecodedImage = Base64.getEncoder()

            var jsonObject = JSONObject().apply {
                put("desc", desc.text.toString())
                put("tag", "-")
                put("loves", "0")
                put("comments", "0")
                put("user_id", session.getString("id", ""))
                put("image", "coba")
            }

            lifecycleScope.launch {

                var result = postRequest(connection.connection + "post/create" , jsonObject , session.getString("token", ""))

                result.fold(
                    onSuccess = {
                        response -> var jsonObject2 = JSONObject(response)
                        if(jsonObject2.getString("status").equals("success")){
                            startActivity(Intent(requireContext().applicationContext, bottomNav::class.java))
                        }
                    },
                    onFailure = {
                        error -> error.printStackTrace()
                    }
                )

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addpost().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}