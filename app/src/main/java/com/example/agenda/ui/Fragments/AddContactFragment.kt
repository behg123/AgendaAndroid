package com.example.agenda.ui.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.agenda.Utils
import com.example.agenda.databinding.FragmentAddcontactBinding
import com.example.agenda.model.Contact
import com.example.agenda.model.Telefone

class AddContactFragment : Fragment() {

    private var _binding: FragmentAddcontactBinding? = null
    private lateinit var myButton: Button
    private lateinit var listTelefone: ArrayList<Telefone>
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
                Utils.invalidMessage(requireContext())
            } else {
                listTelefone.clear()
                val telefoneObjeto = Telefone(telefone, tipo)
                listTelefone.add(telefoneObjeto)
                Utils.getID { ID ->
                    if (ID != null) {
                        val contatoNovo = Contact(ID, nome, listTelefone)
                        Utils.addContact(contatoNovo, requireContext())

                    } else {
                        Log.d("Mensagem", "Não há documentos na coleção")
                    }
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