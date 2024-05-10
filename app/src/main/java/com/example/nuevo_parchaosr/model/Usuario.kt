package com.example.nuevo_parchaosr.model

import lombok.Getter
import lombok.Setter
import lombok.ToString

@Getter
@Setter
@ToString
class Usuario {
    var nombre: String = ""
    var apellido: String =""
    var numeroIdentificacion: String = ""
    var latitud:Double = 0.0
    var longitud:Double = 0.0
    var correo = ""
    var disponible = false
    fun Usuario(){}
}