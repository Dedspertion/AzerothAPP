package com.example.myapplication.ui.pantallas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.ui.componentes.*

@Composable
fun PantallaJuego(preguntas: List<Pregunta>, modo: String, onRespuesta: (Boolean)->Unit, onClickContado: ()->Unit, onVolver: ()->Unit) {
 var indice by rememberSaveable { mutableStateOf(0) }
 var respuesta by rememberSaveable { mutableStateOf<Boolean?>(null) }
 var respuestasExamen by rememberSaveable { mutableStateOf(List<Boolean?>(preguntas.size) { null }) }
 var mostrarResultado by rememberSaveable { mutableStateOf(false) }
 val examen = modo=="examen"
 val simple = modo=="simple"
 if(preguntas.isEmpty()) { Column { Text(stringResource(R.string.sin_preguntas));Button(onClick=onVolver){Text(stringResource(R.string.volver))} };return }
 val pregunta=preguntas[indice]
 val elegida=if(examen) respuestasExamen[indice] else respuesta
 fun contar() { if(!examen) onClickContado() }
 fun mover(nuevo: Int) { contar();indice=nuevo;respuesta=null }
 Column(Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally) {
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
   TextButton(onClick={contar();onVolver()}) { Text(stringResource(R.string.menu)) }
   Text(stringResource(R.string.contador,indice+1,preguntas.size),modifier=Modifier.weight(1f),textAlign=TextAlign.End)
  }
  Text(pregunta.enunciado,Modifier.fillMaxWidth().padding(Dimens.margen),textAlign=TextAlign.Center,style=MaterialTheme.typography.titleMedium)
  if(simple) {
   LinearProgressIndicator(progress={(indice+1f)/preguntas.size},modifier=Modifier.fillMaxWidth().padding(Dimens.margen))
   Spacer(Modifier.weight(1f))
  } else {
   Box(Modifier.weight(1f).fillMaxWidth(),contentAlignment=Alignment.Center) {
    ImagenPregunta(pregunta.imagen,pregunta.enunciado,Modifier.fillMaxSize())
   }
  }
  MensajeResultado(elegida,pregunta.respuestaCorrecta)
  BotonesRespuesta(elegida,pregunta.respuestaCorrecta,!examen || elegida==null) { valor ->
   contar()
   if(examen) {
    respuestasExamen=respuestasExamen.mapIndexed { i, anterior -> if(i==indice) valor else anterior }
   } else { respuesta=valor;onRespuesta(valor==pregunta.respuestaCorrecta) }
  }
  if(!examen) {
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(Dimens.espacio)) {
    BotonNavegacion(stringResource(R.string.anterior),true,modifier=Modifier.weight(1f)) { mover((indice-1+preguntas.size)%preguntas.size) }
    Button(onClick={mover(preguntas.indices.random())},modifier=Modifier.weight(1f)) { Text(stringResource(R.string.aleatorio)) }
   }
  }
  BotonNavegacion(stringResource(if(examen && indice==preguntas.lastIndex) R.string.terminar else R.string.siguiente),
   enabled=!examen || elegida!=null,modifier=Modifier.fillMaxWidth()) {
   if(examen && indice==preguntas.lastIndex) mostrarResultado=true else mover((indice+1)%preguntas.size)
  }
 }
 if(mostrarResultado) {
  val aciertos=preguntas.indices.count { respuestasExamen[it]==preguntas[it].respuestaCorrecta }
  val imagen=if(aciertos==preguntas.size) R.drawable.paladin else if(aciertos*2>=preguntas.size) R.drawable.thrall else R.drawable.illidan
  val mensaje=if(aciertos==preguntas.size) R.string.examen_perfecto else if(aciertos*2>=preguntas.size) R.string.examen_aprobado else R.string.examen_suspendido
  AlertDialog(onDismissRequest={mostrarResultado=false;onVolver()},
   title={Text(stringResource(R.string.resultado,aciertos,preguntas.size))},
   text={Column { ImagenPregunta(imagen,stringResource(mensaje),Modifier.height(Dimens.mensaje*3));Text(stringResource(mensaje)) }},
   confirmButton={TextButton(onClick={mostrarResultado=false;onVolver()}) {Text(stringResource(R.string.menu))}},
   dismissButton={TextButton(onClick={indice=0;respuesta=null;respuestasExamen=List(preguntas.size){null};mostrarResultado=false}){Text(stringResource(R.string.repetir))}})
 }
}
