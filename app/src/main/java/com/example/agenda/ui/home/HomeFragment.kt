package com.example.agenda.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.adapter.MyAdapter
import com.example.agenda.databinding.FragmentHomeBinding
import com.example.agenda.model.Contact
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var listContacts: ArrayList<Contact>
    private val db = Firebase.firestore
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerViewContacts = view.findViewById(R.id.recyclerViewContatos)
        recyclerViewContacts.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewContacts.setHasFixedSize(true)

        listContacts = arrayListOf()
        adapter = MyAdapter(listContacts, requireContext())
        recyclerViewContacts.adapter = adapter

        getUserData()

        return view
    }

    private fun getUserData() {
        db.collection("contato")
            .get()
            .addOnSuccessListener { result ->
                listContacts.clear()
                for (document in result) {
                    val contacts = document.toObject(Contact::class.java)
                    listContacts.add(contacts)
                }

                listContacts.sortBy { it.nome.toString().lowercase() }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}