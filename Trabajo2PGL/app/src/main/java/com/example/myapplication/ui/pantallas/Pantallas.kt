package com.example.myapplication.ui.pantallas
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.data.Estadisticas
import com.example.myapplication.ui.componentes.*

@Composable
fun PantallaMenu(onJugar:()->Unit,onEstadisticas:()->Unit,onAgregar:()->Unit) {
 Column(Modifier.fillMaxSize().padding(Dimens.margen),verticalArrangement=Arrangement.Center,horizontalAlignment=Alignment.CenterHorizontally) {
  Text(stringResource(R.string.app_name),style=MaterialTheme.typography.headlineLarge)
  ImagenPregunta(R.drawable.arthas,stringResource(R.string.app_name),Modifier.weight(1f))
  BotonTexto(R.string.jugar, onJugar)
  BotonTexto(R.string.estadisticas, onEstadisticas)
  BotonTexto(R.string.agregar, onAgregar)
 }
}
@Composable
fun PantallaModos(onModo:(String)->Unit,onVolver:()->Unit) {
 Column(Modifier.fillMaxSize().padding(Dimens.margen),verticalArrangement=Arrangement.Center) {
  Text(stringResource(R.string.elige_modo),style=MaterialTheme.typography.headlineMedium)
  listOf("normal" to R.string.modo_normal,"examen" to R.string.modo_examen,"simple" to R.string.modo_simple).forEach { (modo,titulo) ->
   BotonTexto(titulo) { onModo(modo) }
  }
  Text(stringResource(R.string.explicacion_modos))
  TextButton(onClick=onVolver){Text(stringResource(R.string.volver))}
 }
}
@Composable
fun PantallaEstadisticas(e: Estadisticas,onVolver:()->Unit) {
 val total=e.aciertos+e.fallos
 val porcentaje=if(total==0) 0f else 100f*e.aciertos/total
 Column(Modifier.fillMaxSize().padding(Dimens.margen),verticalArrangement=Arrangement.Center) {
  Text(stringResource(R.string.estadisticas),style=MaterialTheme.typography.headlineMedium)
  Text(stringResource(R.string.total_aciertos,e.aciertos),color=Verde)
  Text(stringResource(R.string.total_fallos,e.fallos),color=Rojo)
  Text(stringResource(R.string.porcentaje_aciertos,porcentaje))
  Text(stringResource(R.string.porcentaje_fallos,if(total==0) 0f else 100f-porcentaje))
  Text(stringResource(R.string.total_clicks,e.clicks))
  Text(stringResource(R.string.nota_estadisticas))
  Button(onClick=onVolver){Text(stringResource(R.string.volver))}
 }
}
@Composable
fun PantallaAgregar(onGuardar:(Pregunta)->Boolean,onVolver:()->Unit) {
 var enunciado by rememberSaveable { mutableStateOf("") }
 var correcta by rememberSaveable { mutableStateOf(true) }
 var imagen by rememberSaveable { mutableStateOf(0) }
 val imagenes=listOf(R.drawable.arthas,R.drawable.thrall,R.drawable.paladin,R.drawable.orgrimmar,R.drawable.illidan)
 val nombres=listOf(R.string.arthas,R.string.thrall,R.string.paladin,R.string.orgrimmar,R.string.illidan)
 val context=LocalContext.current
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(Dimens.margen)) {
  Text(stringResource(R.string.agregar),style=MaterialTheme.typography.headlineMedium)
  OutlinedTextField(value=enunciado,onValueChange={enunciado=it},label={Text(stringResource(R.string.enunciado))},modifier=Modifier.fillMaxWidth(),maxLines=4)
  Row(verticalAlignment=Alignment.CenterVertically) {
   Text(stringResource(R.string.respuesta_correcta))
   Switch(checked=correcta,onCheckedChange={correcta=it})
   Text(stringResource(if(correcta) R.string.verdadero else R.string.falso))
  }
  Text(stringResource(R.string.elige_imagen))
  nombres.forEachIndexed { i,nombre -> Row(verticalAlignment=Alignment.CenterVertically) {
   RadioButton(selected=imagen==i,onClick={imagen=i});Text(stringResource(nombre))
  } }
  Button(enabled=enunciado.isNotBlank(),onClick={
   val guardado=onGuardar(Pregunta(enunciado.trim(),correcta,imagenes[imagen]))
   Toast.makeText(context,if(guardado) R.string.guardada else R.string.error_guardar,Toast.LENGTH_SHORT).show()
   if(guardado) enunciado=""
  },modifier=Modifier.fillMaxWidth()){Text(stringResource(R.string.guardar))}
  TextButton(onClick=onVolver){Text(stringResource(R.string.volver))}
 }
}
