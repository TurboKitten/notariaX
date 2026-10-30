package com.duoc.notariaplataforma.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.notariaplataforma.R
import com.duoc.notariaplataforma.util.SesionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val alpha = remember { Animatable(0f) }
    val context = LocalContext.current
    val sesionManager = remember { SesionManager(context) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        delay(2000)
        if (sesionManager.haySesionActiva()) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_notaria),
            contentDescription = "Logo NotariaX",
            modifier = Modifier
                .size(300.dp)
                .alpha(alpha.value)
        )
        Text(
            text = "NotariaXPlorer",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.alpha(alpha.value)
        )
        Text(
            text = "Plataforma Notarial Digital",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.alpha(alpha.value)
        )
    }
}