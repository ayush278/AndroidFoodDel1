package com.example.fooddel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ExtrasListAdapter(private var context: Context,private var extraslist:MutableList<ExtrasModel>):
    RecyclerView.Adapter<ExtrasListAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.extras_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val extrasModel = extraslist[position]

        Log.e("Error Message", "zz ${extrasModel.name}")
        Log.e("Error Message", "zz ${extrasModel.imagePath}")
holder.foodImage.contentDescription="extrasModel.name"
        holder.foodName.text = extrasModel.name;
     //   holder.foodImage.setImageResource(extrasModel.imagePath)
        holder.foodImage.setImageResource(extrasModel.imagePath);



    }

    override fun getItemCount(): Int {
        return extraslist.size
    }

    class ItemViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val foodName: TextView = ItemView.findViewById(R.id.extrasNameTextView)
        val foodImage: ImageView = ItemView.findViewById(R.id.extrasImage)

    }
}