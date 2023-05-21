package com.example.agenda.ui.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.agenda.R
import com.example.agenda.Utils
import com.example.agenda.databinding.FragmentAddcontactBinding
import com.example.agenda.model.Contact
import com.example.agenda.model.Telefone
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailFragment: AppCompatActivity() {

    private lateinit var listTelefone: ArrayList<Telefone>
    private lateinit var myButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gson = Gson()
        val nomeContato = intent.getStringExtra("nomeContato")
        val idContato = intent.getIntExtra("idContato",0)
        val telefonesContatoJson = intent.getStringExtra("telefonesContatoJson")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact)

        val telefonesContatoType = object : TypeToken<ArrayList<Telefone>>() {}.type
        val telefonesContato =
            gson.fromJson<ArrayList<Telefone>>(telefonesContatoJson, telefonesContatoType)
//        for (telefone in telefonesContato) {
//            val numero = telefone.numero
//            val tipo = telefone.tipo
//        }

        setSpinner()
        checkFields(idContato.toString())


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Volta para a página anterior
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setSpinner() {
        val tipos_telefone = arrayOf("Casa", "Celular", "Trabalho")
        val spinner = findViewById<Spinner>(R.id.newSpinnerPhoneType)
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos_telefone)

        listTelefone = arrayListOf()
        spinner.adapter = arrayAdapter
    }

    fun checkFields(id: String) {
        myButton = findViewById<Button>(R.id.addNewContactButton)
        myButton.setOnClickListener {
            val telefone = findViewById<TextInputLayout>(R.id.addNewTelefone).editText?.text.toString()
            val tipo = findViewById<Spinner>(R.id.newSpinnerPhoneType).selectedItem.toString()

            if (telefone.isNullOrEmpty()) {
                Utils.invalidMessage(this)
            } else {

                listTelefone.clear()
                val telefoneObjeto = Telefone(telefone, tipo)
                listTelefone.add(telefoneObjeto)

                addNewContact(id, telefoneObjeto)


                }
            }
        }


    fun addNewContact(id: String, telefoneObjeto: Telefone) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val firestore = FirebaseFirestore.getInstance()
        val contatoRef = firestore.collection("contato").document(id)

        contatoRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val listaTelefones = documentSnapshot.get("telefones") as? ArrayList<Telefone>
                    if (listaTelefones != null) {
                        if(listaTelefones.contains(telefoneObjeto)){
                            Utils.alreadyExistsMessage(this)
                        } else {
                            listaTelefones.add(telefoneObjeto)
                            contatoRef.update("telefones", listaTelefones)
                                .addOnSuccessListener {
                                    Utils.sucessMessage(this)
                                }
                                .addOnFailureListener { e ->
                                    Utils.errorMessage(this)
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Tratar falha na recuperação do documento
            }
    }

    private fun showInvalidMessage() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Campos Vazios")
        alertDialogBuilder.setMessage("Insira valores em todos os campos")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Fechar o alerta
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}






