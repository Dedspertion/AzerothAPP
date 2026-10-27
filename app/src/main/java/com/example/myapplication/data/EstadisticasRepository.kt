package com.example.myapplication.data
import android.content.Context
import com.example.myapplication.R

data class Estadisticas(val aciertos: Int = 0, val fallos: Int = 0, val clicks: Int = 0)
class EstadisticasRepository(context: Context) {
 private val prefs = context.getSharedPreferences(context.getString(R.string.prefs_nombre), Context.MODE_PRIVATE)
 fun cargar() = Estadisticas(prefs.getInt("aciertos", 0), prefs.getInt("fallos", 0), prefs.getInt("clicks", 0))
 fun guardar(e: Estadisticas) { prefs.edit().putInt("aciertos", e.aciertos).putInt("fallos", e.fallos).putInt("clicks", e.clicks).apply() }
}
