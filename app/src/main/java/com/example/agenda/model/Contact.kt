package com.example.agenda.model

data class Contact(
    val nome:String ?=null,
<<<<<<< HEAD
    val telefones:ArrayList<Telefone>?=null,

)
data class Telefone(
    val numero: String ?=null,
    val tipo: String ?=null
)

=======
    val telefone:String?=null,
    val tipoTelefone:String?=null

)
>>>>>>> origin/main
