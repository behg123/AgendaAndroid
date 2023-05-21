package com.example.agenda.addcontact

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.agenda.R
import com.example.agenda.databinding.FragmentAddcontactBinding
import com.example.agenda.model.Contact
import com.example.agenda.model.Telefone
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddContactFragment : Fragment() {

    private var _binding: FragmentAddcontactBinding? = null
    private lateinit var myButton: Button
    private lateinit var listTelefone: ArrayList<Telefone>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddcontactBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tipos_telefone = arrayOf("Casa", "Celular", "Trabalho")
        val spinner = binding.spinnerPhoneType // Usando a referência do Spinner a partir do binding
        val db = Firebase.firestore
        listTelefone = arrayListOf()

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, tipos_telefone)
        spinner.adapter = arrayAdapter


        myButton = binding.addContactButton // Usando a referência do botão a partir do binding

        myButton.setOnClickListener {
            // Lógica a ser executada quando o botão for clicado
            // Por exemplo, exibir uma mensagem Toast
            // Criar o AlertDialog

            val nome = binding.getNomeContato.text.toString()
            val telefone = binding.getTelefoneContato.text.toString()
            val tipo = binding.spinnerPhoneType.selectedItem.toString()
            val alertDialogBuilder = AlertDialog.Builder(requireContext())

            if (nome.isEmpty() || telefone.isEmpty()) {
                // Exibir uma mensagem de erro caso o nome ou telefone sejam nulos ou vazios
                alertDialogBuilder.setTitle("Campos Vazios")
                alertDialogBuilder.setMessage("Insira valores em todos os campos")
                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    // Lógica a ser executada quando o botão OK do alerta for clicado
                    dialog.dismiss() // Fechar o alerta
                }
                val alertDialog = alertDialogBuilder.create()

                // Exibir o AlertDialog
                alertDialog.show()
            } else {



                val telefoneObjeto = Telefone(telefone, tipo)
                listTelefone.add(telefoneObjeto)

                val contatoNovo = Contact(
                    nome,
                    listTelefone
                )

                db.collection("contato")
                    .add(contatoNovo)
                    .addOnSuccessListener { documentReference ->
                        alertDialogBuilder.setTitle("Sucesso")
                        alertDialogBuilder.setMessage("Usuário inserido com sucesso")
                        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                            // Lógica a ser executada quando o botão OK do alerta for clicado
                            dialog.dismiss() // Fechar o alerta
                        }
                        val alertDialog = alertDialogBuilder.create()

                        // Exibir o AlertDialog
                        alertDialog.show()                    }
                    .addOnFailureListener { e ->
                        alertDialogBuilder.setTitle("Algo deu errado")
                        alertDialogBuilder.setMessage("Não foi possível inserir o usuário")
                        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                            // Lógica a ser executada quando o botão OK do alerta for clicado
                            dialog.dismiss() // Fechar o alerta
                        }
                        val alertDialog = alertDialogBuilder.create()

                        // Exibir o AlertDialog
                        alertDialog.show()
                    }

            }


        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}