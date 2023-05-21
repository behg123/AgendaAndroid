package com.example.agenda.addcontact

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.agenda.databinding.FragmentAddcontactBinding
import com.example.agenda.model.Contact
import com.example.agenda.model.Telefone
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddContactFragment : Fragment() {

    private var _binding: FragmentAddcontactBinding? = null
    private lateinit var myButton: Button
    private lateinit var listTelefone: ArrayList<Telefone>
    val db = Firebase.firestore
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddcontactBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val tipos_telefone = arrayOf("Casa", "Celular", "Trabalho")
        val spinner = binding.spinnerPhoneType
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, tipos_telefone)

        listTelefone = arrayListOf()
        spinner.adapter = arrayAdapter
        myButton = binding.addContactButton
        myButton.setOnClickListener {
            val nome = binding.getNomeContato.text.toString()
            val telefone = binding.getTelefoneContato.text.toString()
            val tipo = binding.spinnerPhoneType.selectedItem.toString()

            if (nome.isEmpty() || telefone.isEmpty()) {
                invalidMessage()
            } else {
                listTelefone.clear()
                val telefoneObjeto = Telefone(telefone, tipo)
                listTelefone.add(telefoneObjeto)
                val contatoNovo = Contact(nome,listTelefone)
                addContact(contatoNovo)
            }
        }
        return root
    }

    fun addContact(contatoNovo: Contact){
        getID { ID ->
            if (ID != null) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                db.collection("contato").document(ID.toString())
                    .set(contatoNovo)
                    .addOnSuccessListener { documentReference ->
                        sucessMessage()
                    }
                    .addOnFailureListener { e ->
                        errorMessage()
                    }            } else {
                Log.d("Mensagem", "Não há documentos na coleção")
            }
        }

    }

    fun getID(callback: (Int?) -> Unit) {
        db.collection("contato")
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val lastDocument = documents.documents[0]
                    val lastId = lastDocument.id
                    callback((lastId.toInt()) + 1)
                } else {
                    // Caso não haja documentos na coleção
                    callback(1)
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
                Log.w("Mensagem", "Erro ao obter documentos: ", exception)
            }
    }

    fun sucessMessage(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Sucesso")
        alertDialogBuilder.setMessage("Usuário inserido com sucesso")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Fechar o alerta
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun errorMessage(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Algo deu errado")
        alertDialogBuilder.setMessage("Não foi possível inserir o usuário")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Fechar o alerta
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun invalidMessage(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Campos Vazios")
        alertDialogBuilder.setMessage("Insira valores em todos os campos")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            // Lógica a ser executada quando o botão OK do alerta for clicado
            dialog.dismiss() // Fechar o alerta
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}