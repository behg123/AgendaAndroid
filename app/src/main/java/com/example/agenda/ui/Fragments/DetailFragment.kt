package com.example.agenda.ui.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda.R
import com.example.agenda.Utils
import com.example.agenda.model.Contact
import com.example.agenda.model.Telefone
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
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
        val telefonesContatoType = object : TypeToken<ArrayList<Telefone>>() {}.type
        val telefonesContato =
            gson.fromJson<ArrayList<Telefone>>(telefonesContatoJson, telefonesContatoType)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact)

        val id = idContato.toString()

        setSpinner(telefonesContato)
        setEditDefault(nomeContato.toString())
        checkFieldsAdd(id)
        checkFieldsEdit(id)
        deleteContact(id)
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

    fun setSpinner(telefonesContato: ArrayList<Telefone>) {
        val tipos_telefone = arrayOf("Casa", "Celular", "Trabalho")
        val telefones = arrayListOf<String>()

        for (telefone in telefonesContato) {
            telefones.add(telefone.numero!!)
        }

        val spinnerAdd = findViewById<Spinner>(R.id.newSpinnerPhoneType)
        val spinnerEdit = findViewById<Spinner>(R.id.editSpinnerPhoneType)
        val spinnerTelefone = findViewById<Spinner>(R.id.SpinnerTelefones)
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos_telefone)

        val arrayAdapterTelefone =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, telefones)

        listTelefone = arrayListOf()
        spinnerAdd.adapter = arrayAdapter
        spinnerEdit.adapter = arrayAdapter
        spinnerTelefone.adapter = arrayAdapterTelefone
    }

    fun checkFieldsAdd(id: String) {
        myButton = findViewById<Button>(R.id.addNewContactButton)
        myButton.setOnClickListener {
            val telefone = findViewById<TextInputLayout>(R.id.addNewTelefone).editText?.text.toString()
            val tipo = findViewById<Spinner>(R.id.newSpinnerPhoneType).selectedItem.toString()

            if (telefone.isNullOrEmpty()) {
                Utils.invalidMessage(this)
            } else {

                val telefoneObjeto = Telefone(telefone, tipo)

                addNewContact(id, telefoneObjeto)


                }
            }
        }

    fun checkFieldsEdit(id: String){
        myButton = findViewById<Button>(R.id.EditContactButton)
        myButton.setOnClickListener {
            val telefone = findViewById<Spinner>(R.id.SpinnerTelefones).selectedItem.toString()
            val nome = findViewById<TextInputLayout>(R.id.editNome).editText?.text.toString()
            val tipo = findViewById<Spinner>(R.id.editSpinnerPhoneType).selectedItem.toString()
            val telefoneNovo = findViewById<TextInputLayout>(R.id.editTelefone).editText?.text.toString()

            if (telefone.isNullOrEmpty() || nome.isNullOrEmpty() || tipo.isNullOrEmpty()) {
                Utils.invalidMessage(this)
            } else {
                val telefoneObjeto = Telefone(telefone, tipo)
                val telefoneNew = Telefone(telefoneNovo, tipo)

                editContact(id, nome, telefoneObjeto, telefoneNew)

            }
        }
    }

    fun editContact(id : String, nome: String, telefoneObjeto: Telefone, telefoneNovo: Telefone){
        val firestore = FirebaseFirestore.getInstance()
        val contatoRef = firestore.collection("contato").document(id)

        contatoRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val contato = documentSnapshot.toObject(Contact::class.java)
                    val listaTelefones = contato?.telefones

                    if (listaTelefones != null) {
                        val index = listaTelefones.indexOfFirst { it.numero == telefoneObjeto.numero }
                        if(listaTelefones.contains(telefoneNovo)){
                            Utils.alreadyExistsMessage(this)
                        } else{
                            if (index != -1) {

                                contato.nome = nome
                                contato.telefones[index] = telefoneNovo

                                contatoRef.set(contato)
                                    .addOnSuccessListener {
                                        Utils.sucessMessage(this)
                                        setResult(Activity.RESULT_OK, intent)
                                        finish()

                                    }
                                    .addOnFailureListener { e ->
                                        Utils.errorMessage(this)
                                    }
                            }
                        }


                    }
                }
            }
            .addOnFailureListener { e ->
                // Tratar falha na recuperação do documento
            }
    }



    fun addNewContact(id: String, telefoneObjeto: Telefone) {
        val firestore = FirebaseFirestore.getInstance()
        val contatoRef = firestore.collection("contato").document(id)

        contatoRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val contato = documentSnapshot.toObject(Contact::class.java)
                    val listaTelefones = contato?.telefones
                    if (listaTelefones != null) {
                        if(listaTelefones.contains(telefoneObjeto)){
                            Utils.alreadyExistsMessage(this)
                        } else {
                            listaTelefones.add(telefoneObjeto)
                            contatoRef.update("telefones", listaTelefones)
                                .addOnSuccessListener {
                                    Utils.sucessMessage(this)

                                    setResult(Activity.RESULT_OK)
                                    finish()
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

   private fun setEditDefault(nomeContato : String){
        val nome = findViewById<TextInputLayout>(R.id.editNome)
        nome.editText?.setText(nomeContato)


    }

    private fun deleteContact(id: String) {
        val firestore = Firebase.firestore
        val contatoRef = firestore.collection("contato").document(id)

        myButton = findViewById<Button>(R.id.deleteContact)
        myButton.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Excluir Contato")
            alertDialogBuilder.setMessage("Tem certeza de que deseja excluir este contato?")
            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                contatoRef.delete()
                    .addOnSuccessListener {
                        Utils.sucessMessage(this)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Utils.errorMessage(this)

                    }
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }
    }


}









