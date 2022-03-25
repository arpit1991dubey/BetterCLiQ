package com.example.bettercliq

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

private var title = arrayOf("Invalid phone number error","The VR feature is awesome ", "Add photos in review section")
private var image = arrayOf("B","P","E","S")
private var status= arrayOf("Submitted","Under Review","Resolved")
private var details= arrayOf("Raised on 10/02/2022","Raised on 22/02/2022","Raised on 19/02/2022")
private var ids= arrayOf("ID - B1024","ID - P0015","ID - E7895")

class RecycleAdapter :RecyclerView.Adapter<RecycleAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapter.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: RecycleAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text= title[position]
        holder.itemDetail.text= details[position]
        //holder.itemStatus.text= status[position]
       holder.itemId.text= ids[position]
        holder.itemImage.text=image[position]
        if (holder.itemId.text=="ID - B1024") {
            holder.itemStatusColor.setBackgroundColor(Color.RED)
    }
    else if (holder.itemId.text=="ID - P0015") {
            holder.itemStatusColor.setBackgroundColor(Color.YELLOW)
    }
        else
        {
            holder.itemStatusColor.setBackgroundColor(Color.GREEN)
        }
    }
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemImage:TextView
        var itemTitle:TextView
        var itemDetail:TextView
       // var itemStatus:TextView
       var itemId:TextView
        var itemStatusColor:TextView
        init {
            itemId=itemView.findViewById(R.id.repId)
        //    itemStatus= itemView.findViewById(R.id.repId)
            itemImage= itemView.findViewById(R.id.img)
            itemTitle= itemView.findViewById(R.id.title)
            itemDetail= itemView.findViewById(R.id.detail)
            itemStatusColor =itemView.findViewById(R.id.statusColor)
        }
    }
}