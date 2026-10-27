package com.example.myapplication
// Cada pregunta incluye el recurso gráfico que la acompaña.
data class Pregunta(val enunciado: String, val respuestaCorrecta: Boolean, val imagen: Int = R.drawable.arthas)
