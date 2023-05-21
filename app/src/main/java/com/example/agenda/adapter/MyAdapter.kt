package com.example.agenda.adapter

import TelefoneAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.model.Contact


class MyAdapter(private val contactList: ArrayList<Contact>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private lateinit var telefoneAdapter: TelefoneAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.contact, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contactList.size




    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = contactList[position]
        holder.nome.text = currentitem.nome


        telefoneAdapter = TelefoneAdapter(currentitem.telefones)
        telefoneAdapter.notifyDataSetChanged()

        holder.recyclerViewTelefone.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = telefoneAdapter
        }
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val nome: TextView = itemView.findViewById<TextView>(R.id.nomeContato)
        //val telefone: TextView = itemView.findViewById<TextView>(R.id.telefoneContato)

        val recyclerViewTelefone: RecyclerView = itemView.findViewById(R.id.recyclerViewTelefone)


    }

}