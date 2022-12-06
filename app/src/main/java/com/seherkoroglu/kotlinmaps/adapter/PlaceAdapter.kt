package com.seherkoroglu.kotlinmaps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.seherkoroglu.kotlinmaps.databinding.RecrowBinding
import com.seherkoroglu.kotlinmaps.model.Yer
import com.seherkoroglu.kotlinmaps.view.MapsActivity

class PlaceAdapter(val placeList: List<Yer>) : RecyclerView.Adapter<PlaceAdapter.Placeholder>() {
    class Placeholder (val recrowBinding: RecrowBinding): RecyclerView.ViewHolder(recrowBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Placeholder {
        val recrowBinding=RecrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Placeholder(recrowBinding)
    }

    override fun onBindViewHolder(holder: Placeholder, position: Int) {
        holder.recrowBinding.recViewTextView.setText(placeList[position].name)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, MapsActivity::class.java)
            intent.putExtra("SecilenYer", placeList[position])
            intent.putExtra("Bilgi", "eski")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class PlaceHolder(val recRowBinding: RecrowBinding) : RecyclerView.ViewHolder(recRowBinding.root) {

    }


}