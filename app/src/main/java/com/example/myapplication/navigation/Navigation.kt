package com.example.myapplication.navigation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import com.example.myapplication.ui.componentes.Dimens
import com.example.myapplication.data.*
import com.example.myapplication.ui.pantallas.*

@Serializable data object Menu: NavKey
@Serializable data object Modos: NavKey
@Serializable data object EstadisticasKey: NavKey
@Serializable data object Agregar: NavKey
@Serializable data class Juego(val modo:String): NavKey

@Composable
fun AzerothApp() {
 val context=LocalContext.current
 val repositorio=remember { PreguntaRepository(context) }
 val estadisticasRepo=remember { EstadisticasRepository(context) }
 var preguntas by remember { mutableStateOf(repositorio.cargarPreguntas()) }
 var estadisticas by remember { mutableStateOf(estadisticasRepo.cargar()) }
 val pila=rememberNavBackStack(Menu)
 fun contar() { estadisticas=estadisticas.copy(clicks=estadisticas.clicks+1);estadisticasRepo.guardar(estadisticas) }
 fun volver() { if(pila.size>1)pila.removeAt(pila.lastIndex) }
 Surface(modifier=Modifier.fillMaxSize(),color=Color(0xFFFFFBFF),contentColor=Color.Black) {
  Box(Modifier.safeDrawingPadding().padding(horizontal=Dimens.espacio)) {
   NavDisplay(backStack=pila,onBack={volver()},entryProvider=entryProvider {
    entry<Menu> { PantallaMenu({contar();pila.add(Modos)},{contar();pila.add(EstadisticasKey)},{contar();pila.add(Agregar)}) }
    entry<Modos> { PantallaModos({modo->contar();pila.add(Juego(modo))},{contar();volver()}) }
    entry<EstadisticasKey> { PantallaEstadisticas(estadisticas){contar();volver()} }
    entry<Agregar> { PantallaAgregar(onGuardar={pregunta->
     contar()
     try {repositorio.guardarPregunta(pregunta);preguntas=repositorio.cargarPreguntas();true} catch(e:Exception){false}
    },onVolver={contar();volver()}) }
    entry<Juego> { clave -> PantallaJuego(preguntas,clave.modo,onRespuesta={acierto->
     estadisticas=if(acierto) estadisticas.copy(aciertos=estadisticas.aciertos+1) else estadisticas.copy(fallos=estadisticas.fallos+1)
     estadisticasRepo.guardar(estadisticas)
    },onClickContado={contar()},onVolver={volver()}) }
   })
  }
 }
}
