package com.example.projectcapstone.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcapstone.artikel.HalamanArtikel
import com.example.projectcapstone.R
import com.example.projectcapstone.pembuatabatik.PembuatanBatikMain
import com.example.projectcapstone.quiz.QuizMain
import com.example.projectcapstone.response.Fitur

class ListFiturAdapter(private val listFitur: ArrayList<Fitur>) : RecyclerView.Adapter<ListFiturAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_fitur, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, detail, description, photo) = listFitur[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name

        holder.itemView.setOnClickListener {

            if(position == 0){
                val intentDetail = Intent(holder.itemView.context, HalamanArtikel::class.java)
                intentDetail.putExtra("key_Fitur", listFitur[holder.adapterPosition])
                Toast.makeText(holder.itemView.context, "Artikel",Toast.LENGTH_LONG).show()
                holder.itemView.context.startActivity(intentDetail)
            }
            if(position == 1){
                val intentDetail = Intent(holder.itemView.context, QuizMain::class.java)
                intentDetail.putExtra("key_Fitur", listFitur[holder.adapterPosition])
                Toast.makeText(holder.itemView.context, "Quiz",Toast.LENGTH_LONG).show()
                holder.itemView.context.startActivity(intentDetail)
            }
            if(position == 2){
                val intentDetail = Intent(holder.itemView.context, PembuatanBatikMain::class.java)
                intentDetail.putExtra("key_Fitur", listFitur[holder.adapterPosition])
                Toast.makeText(holder.itemView.context, "Pembuatan Batik",Toast.LENGTH_LONG).show()
                holder.itemView.context.startActivity(intentDetail)
            }
            if(position == 3){
                Toast.makeText(holder.itemView.context, "Batik Nusantara",Toast.LENGTH_LONG).show()
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Fitur)
    }

    override fun getItemCount(): Int = listFitur.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.ARTI)
        val tvName: TextView = itemView.findViewById(R.id.fitur)

    }

}