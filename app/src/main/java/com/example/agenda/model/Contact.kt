package com.example.agenda.model

data class Contact(
    val id: Int ?=null,
    val nome:String ?=null,
    val telefones:ArrayList<Telefone>?=null
)
data class Telefone(
    val numero: String ?=null,
    val tipo: String ?=null



)

