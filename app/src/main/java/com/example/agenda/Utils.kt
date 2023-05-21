package com.example.agenda

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.agenda.model.Contact
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Utils {
    companion object {
        val db = Firebase.firestore

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

        fun addContact(contatoNovo: Contact, context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            db.collection("contato").document(contatoNovo.id.toString())
                .set(contatoNovo)
                .addOnSuccessListener { documentReference ->
                    sucessMessage(context)
                }
                .addOnFailureListener { e ->
                    errorMessage(context)
                }
        }

        fun invalidMessage(context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Campos Vazios")
            alertDialogBuilder.setMessage("Insira valores em todos os campos")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                // Lógica a ser executada quando o botão OK do alerta for clicado
                dialog.dismiss() // Fechar o alerta
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        fun alreadyExistsMessage(context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Item repetido")
            alertDialogBuilder.setMessage("Este item ja existe na lista")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                // Lógica a ser executada quando o botão OK do alerta for clicado
                dialog.dismiss() // Fechar o alerta
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        fun sucessMessage(context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Sucesso")
            alertDialogBuilder.setMessage("Usuário inserido com sucesso")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Fechar o alerta
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        fun errorMessage(context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Algo deu errado")
            alertDialogBuilder.setMessage("Não foi possível inserir o usuário")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Fechar o alerta
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


    }


}