package com.example.medsos

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class postAdapter(private var context: Context, private var postData: MutableList<postModel>, private var resource: Int): BaseAdapter() {

    var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var imageLoad = MainScope()

    override fun getCount(): Int {
        return postData.size
    }

    override fun getItem(position: Int): Any {
        return postData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var postView = convertView ?: inflater.inflate(R.layout.postitem, parent, false)

        var username = postView.findViewById<TextView>(R.id.username_post)
        var name = postView.findViewById<TextView>(R.id.name_post)
        var desc = postView.findViewById<TextView>(R.id.desc_post)
        var likes = postView.findViewById<TextView>(R.id.like_count_post)
        var comments = postView.findViewById<TextView>(R.id.somment_count_post )
        var image = postView.findViewById<ImageView>(R.id.image_post)
        var like = postView.findViewById<ImageView>(R.id.like_post)
        var comment = postView.findViewById<ImageView>(R.id.comment_post)
        var share = postView.findViewById<ImageView>(R.id.share_post)
        var connection: connection = connection()

        var data = getItem(position) as postModel
        username.text = data.username
        name.text = data.name
        desc.text = data.desc
        likes.text = data.loves.toString()
        comments.text = data.comments.toString()
        imageLoad.launch {
            var bitmap = getImageFromUrl(connection.connection + "post/image/" + data.image)
            image.setImageBitmap(bitmap)
            Log.d("cpb", "getView: image")
        }

        return postView
    }
}