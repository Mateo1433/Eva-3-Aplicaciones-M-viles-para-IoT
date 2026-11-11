package com.example.riegointeligente

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.riegointeligente.ui.theme.RiegoInteligenteTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RiegoInteligenteTheme {
                LoginScreen()
            }
        }
    }
}

/*
 * Pantalla principal de Login.
 * El usuario y contraseña se guardan localmente mediante SharedPreferences.
 */
@Composable
fun LoginScreen() {

    val context = LocalContext.current

    var usuario by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var mostrarRegistro by remember { mutableStateOf(false) }
    var mostrarPanel by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    if (mostrarPanel) {
        DashboardScreen()
        return
    }

    if (mostrarRegistro) {
        RegistroScreen(
            volver = {
                mostrarRegistro = false
                mensaje = ""
            },
            usuarioCreado = { novoUsuario ->
                usuario = novoUsuario
                contraseña = ""
                mostrarRegistro = false
                mensaje = "Usuario creado correctamente"
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🌱 Riego Inteligente",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Sistema de Riego IoT",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                mensaje = ""
            },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = {
                contraseña = it
                mensaje = ""
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val preferencias = context.getSharedPreferences(
                    "RiegoInteligente",
                    Context.MODE_PRIVATE
                )

                val usuarioGuardado = preferencias.getString("usuario", "")
                val contraseñaGuardada = preferencias.getString("contraseña", "")

                if (usuario.isNotBlank() &&
                    contraseña.isNotBlank() &&
                    usuario == usuarioGuardado &&
                    contraseña == contraseñaGuardada
                ) {
                    mensaje = ""
                    mostrarPanel = true
                } else {
                    mensaje = "Usuario o contraseña incorrectos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("INGRESAR")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                mostrarRegistro = true
                mensaje = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CREAR USUARIO")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


/*
 * Pantalla para crear un nuevo usuario.
 */
@Composable
fun RegistroScreen(
    volver: () -> Unit,
    usuarioCreado: (String) -> Unit
) {

    val context = LocalContext.current

    var usuario by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var confirmarContraseña by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "👤 Crear usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                mensaje = ""
            },
            label = { Text("Nuevo usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = {
                contraseña = it
                mensaje = ""
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = confirmarContraseña,
            onValueChange = {
                confirmarContraseña = it
                mensaje = ""
            },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (usuario.isBlank() || contraseña.isBlank()) {
                    mensaje = "Completa todos los campos"
                } else if (contraseña != confirmarContraseña) {
                    mensaje = "Las contraseñas no coinciden"
                } else {
                    val preferencias = context.getSharedPreferences(
                        "RiegoInteligente",
                        Context.MODE_PRIVATE
                    )

                    preferencias.edit()
                        .putString("usuario", usuario)
                        .putString("contraseña", contraseña)
                        .apply()

                    usuarioCreado(usuario)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CREAR CUENTA")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { volver() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VOLVER")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


/*
 * Panel principal de la aplicación con Control Dual (Manual / Automático).
 */
@Composable
fun DashboardScreen() {

    var riegoActivo by remember { mutableStateOf(false) }
    var modoAutomatico by remember { mutableStateOf(true) } // Control dual: por defecto Automático
    var mostrarHistorial by remember { mutableStateOf(false) }

    if (mostrarHistorial) {
        HistorialScreen(
            volver = { mostrarHistorial = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🌱 Riego Inteligente",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Panel de Control IoT",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Requerimiento Unidad 3: Control Dual (Switch para alternar Manual / Automático)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (modoAutomatico) "Modo: Automático (Sensor)" else "Modo: Manual",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = modoAutomatico,
                onCheckedChange = { modoAutomatico = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Indicadores de lectura de sensores
        Text(
            text = "💧 Humedad del suelo: 45%",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "🌡️ Temperatura: 24 °C",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "💦 Humedad ambiental: 60%",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Estado del Actuador (Bomba / Relé)
        if (riegoActivo) {
            Text(
                text = "💧 Estado del riego: ACTIVO",
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Text(
                text = "⛔ Estado del riego: APAGADO",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Botones manuales habilitados únicamente si el modo manual está activo
        Button(
            onClick = { if (!modoAutomatico) riegoActivo = true },
            enabled = !modoAutomatico,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ACTIVAR RIEGO (MANUAL)")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { if (!modoAutomatico) riegoActivo = false },
            enabled = !modoAutomatico,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("DESACTIVAR RIEGO (MANUAL)")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { mostrarHistorial = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VER HISTORIAL")
        }
    }
}


/*
 * Pantalla de historial de mediciones y eventos.
 */
@Composable
fun HistorialScreen(
    volver: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "📊 Historial",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "19:20  -  Humedad: 32%")
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "19:25  -  Humedad: 29%")
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "19:30  -  Riego activado (Auto)")
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "19:35  -  Humedad: 41%")
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { volver() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VOLVER")
        }
    }
}