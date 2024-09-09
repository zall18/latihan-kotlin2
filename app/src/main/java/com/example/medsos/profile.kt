package com.example.medsos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class profile : Fragment() {
    lateinit var session: SharedPreferences
    lateinit var postAdapter: postAdapter
    lateinit var postdata: MutableList<postModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username1 = view.findViewById<TextView>(R.id.username1)
        var username2 = view.findViewById<TextView>(R.id.username2)
        var name = view.findViewById<TextView>(R.id.name)
        var post_count = view.findViewById<TextView>(R.id.post_count)
        var listView = view.findViewById<ListView>(R.id.post_profile)
        var connection = connection()
        session = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE)
        var editor = session.edit()

        lifecycleScope.launch {

            var result = getRequest(connection.connection + "user", session.getString("token", ""))

            result.fold(
                onSuccess = {
                    response -> var jsonObject = JSONObject(response)

                     if(!jsonObject.getString("id").isNullOrEmpty()){
                         username1.text = jsonObject.getString("username")
                         username2.text = jsonObject.getString("username")
                         name.text = jsonObject.getString("name")
                     }
                },
                onFailure = {
                    error -> error.printStackTrace()
                }
            )


            var result2 = getRequest(connection.connection + "post", session.getString("token", ""))
            postdata = mutableListOf<postModel>()
            var post_counts:Int = 0
            result2.fold(

                onSuccess = {
                        response2 -> var jsonObject2 = JSONObject(response2)

                    var jsonArray = jsonObject2.getJSONArray("data")
                    for (i in 0 until jsonArray.length()){
                        Log.d("test", "onViewCreated: test")
                        var jsonObject3 = jsonArray.getJSONObject(i)
                        post_counts++
                        post_count.text = post_counts.toString() + " Postingan"
//                        lifecycleScope.launch {
                        var result3 = getRequest(connection.connection + "user/" + jsonObject3.getString("user_id"), null)

                        result3.fold(
                            onSuccess = {
                                    response3 -> var jsonObject4 = JSONObject(response3)

                                postdata.add(postModel(jsonObject4.getString("username"), jsonObject4.getString("name"), jsonObject3.getString("desc"), jsonObject3.getString("image"), jsonObject3.getString("loves"), jsonObject3.getString("comments")))
                                Log.d("image", "onViewCreated: " + jsonObject3.getString("image"))
                            },
                            onFailure = {
                                    error -> error.printStackTrace()
                            }
                        )
//                        }
                    }

                    postAdapter = postAdapter(requireContext().applicationContext, postdata, R.layout.postitem)
                    listView.adapter = postAdapter
                },
                onFailure = {
                        error -> error.printStackTrace()
                }

            )

        }

        var logout = view.findViewById<AppCompatButton>(R.id.logouy_button)
        logout.setOnClickListener {

            lifecycleScope.launch {
                var result = getRequest(connection.connection + "logout", session.getString("token", ""))

                result.fold(
                    onSuccess = {
                        response -> var jsonObject = JSONObject(response)
                        if(jsonObject.getString("status").equals("success")){
                            editor.remove("token")
                            editor.remove("id")
                            editor.commit()

                            startActivity(Intent(requireContext().applicationContext, MainActivity::class.java))
                        }
                    },
                    onFailure = {

                    }
                )
            }

        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profile().apply {

            }
    }
}