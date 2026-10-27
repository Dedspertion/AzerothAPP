package com.example.myapplication.ui.componentes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

object Dimens { val margen=16.dp; val espacio=8.dp; val icono=24.dp; val mensaje=56.dp }
val Verde = Color(0xFF16832C)
val Rojo = Color(0xFFD71920)
val Azul = Color(0xFF4D6099)
@Composable
fun ImagenPregunta(imagen: Int, descripcion: String, modifier: Modifier = Modifier) {
 Image(painterResource(imagen), contentDescription=descripcion, modifier=modifier.fillMaxWidth(), contentScale=ContentScale.Fit)
}
@Composable
fun MensajeResultado(respuesta: Boolean?, correcta: Boolean) {
 val texto = if(respuesta==null) "" else stringResource(
  if(respuesta==correcta) R.string.acierto else R.string.error,
  stringResource(if(correcta) R.string.verdadero else R.string.falso))
 Text(texto, color=if(respuesta==correcta) Verde else Rojo, textAlign=TextAlign.Center,
  modifier=Modifier.fillMaxWidth().heightIn(min=Dimens.mensaje).padding(horizontal=Dimens.margen))
}
@Composable
fun BotonesRespuesta(respuesta: Boolean?, correcta: Boolean, enabled: Boolean, onResponder: (Boolean)->Unit) {
 Row(horizontalArrangement=Arrangement.spacedBy(Dimens.espacio), verticalAlignment=Alignment.CenterVertically) {
  listOf(true,false).forEach { opcion ->
   val color = if(respuesta==null) Azul else if(opcion==correcta) Verde else Rojo
   Button(onClick={onResponder(opcion)}, enabled=enabled,
    colors=ButtonDefaults.buttonColors(containerColor=color,contentColor=Color.White,disabledContainerColor=color,disabledContentColor=Color.White)) {
    Text(stringResource(if(opcion) R.string.verdadero else R.string.falso))
   }
  }
 }
}
@Composable
fun BotonNavegacion(texto: String, atras: Boolean=false, enabled: Boolean=true, modifier: Modifier=Modifier, onClick: ()->Unit) {
 Button(onClick=onClick, enabled=enabled, modifier=modifier, colors=ButtonDefaults.buttonColors(containerColor=Azul,contentColor=Color.White)) {
  Icon(painterResource(if(atras) R.drawable.ic_arrow_back else R.drawable.ic_arrow_forward),null,Modifier.size(Dimens.icono))
  Spacer(Modifier.width(Dimens.espacio));Text(texto)
 }
}

@Composable
fun BotonTexto(texto: Int, onClick: () -> Unit) {
 Button(onClick=onClick, modifier=Modifier.fillMaxWidth()) {
  Text(stringResource(texto))
 }
}
