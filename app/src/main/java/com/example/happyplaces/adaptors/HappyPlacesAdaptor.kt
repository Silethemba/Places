package com.example.happyplaces.adaptors

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.AddHappyPlaceActivity
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdaptor(private val context: Context,
    private var list: ArrayList<HappyPlaceModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemHappyPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            holder.bindItem(model)

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener?.onClick(position, model)
                }
            }
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    private class MyViewHolder(val itemBinding: ItemHappyPlaceBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(happyPlace: HappyPlaceModel){
            itemBinding.tvTitle.text = happyPlace.title
            itemBinding.tvDescription.text = happyPlace.description
            itemBinding.ivPlaceImage.setImageURI(Uri.parse(happyPlace.image))
        }
    }

    interface OnClickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }
}