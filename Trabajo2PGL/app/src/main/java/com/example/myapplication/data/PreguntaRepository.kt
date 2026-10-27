package com.example.myapplication.data
import android.content.Context
import com.example.myapplication.*
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

// La interfaz no abre archivos: toda la persistencia está aquí.
class PreguntaRepository(private val context: Context) {
 private val fichero get() = File(context.filesDir, "preguntas.jsonl")
 private fun nombreImagen(id: Int) = context.resources.getResourceEntryName(id)
 private fun imagen(nombre: String) = when(nombre) {
  "thrall" -> R.drawable.thrall
  "paladin" -> R.drawable.paladin
  "orgrimmar" -> R.drawable.orgrimmar
  "illidan" -> R.drawable.illidan
  else -> R.drawable.arthas
 }
 fun cargarPreguntas(): List<Pregunta> {
  if (!fichero.exists()) listaPreguntas.forEach { guardarPregunta(it) }
  return FileInputStream(fichero).bufferedReader(Charsets.UTF_8).useLines { lineas ->
   lineas.filter { it.isNotBlank() }.map { linea ->
    val dato = JSONObject(linea)
    Pregunta(dato.getString("enunciado"), dato.getBoolean("respuesta"), imagen(dato.optString("imagen", "arthas")))
   }.toList()
  }
 }
 fun guardarPregunta(pregunta: Pregunta) {
  val dato = JSONObject().put("enunciado", pregunta.enunciado).put("respuesta", pregunta.respuestaCorrecta).put("imagen", nombreImagen(pregunta.imagen))
  FileOutputStream(fichero, true).bufferedWriter(Charsets.UTF_8).use { it.appendLine(dato.toString()) }
 }
}
