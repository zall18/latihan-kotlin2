package com.example.medsos

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class home : Fragment() {

    lateinit var postAdapter: postAdapter
    lateinit var postdata: MutableList<postModel>
    lateinit var session: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE)
        var connection = connection()
        var listview = view.findViewById<ListView>(R.id.list_post)

//        lifecycleScope.launch {
//
//            var result = getRequest(connection.connection + "post", session.getString("token", ""))
//            postdata = mutableListOf<postModel>()
//            result.fold(
//
//                onSuccess = {
//                    response -> var jsonObject = JSONObject(response)
//
//                    var jsonArray = jsonObject.getJSONArray("data")
//                    for (i in 0 until jsonArray.length()){
//                        Log.d("test", "onViewCreated: test")
//                        var jsonObject2 = jsonArray.getJSONObject(i)
//
////                        lifecycleScope.launch {
//                            var result2 = getRequest(connection.connection + "user/" + jsonObject2.getString("user_id"), null)
//
//                            result2.fold(
//                                onSuccess = {
//                                            response2 -> var jsonObject3 = JSONObject(response2)
//                                    postdata.add(postModel(jsonObject3.getString("username"), jsonObject3.getString("name"), jsonObject2.getString("desc"), jsonObject2.getString("image"), jsonObject2.getString("loves"), jsonObject2.getString("comments")))
//                                    Log.d("image", "onViewCreated: " + jsonObject2.getString("image"))
//                                },
//                                onFailure = {
//                                    error -> error.printStackTrace()
//                                }
//                            )
////                        }
//                    }
//
//                    postAdapter = postAdapter(requireContext().applicationContext, postdata, R.layout.postitem)
//                    listview.adapter = postAdapter
//                },
//                onFailure = {
//                    error -> error.printStackTrace()
//                }
//
//            )
//
//        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            home().apply {
            }
    }
}