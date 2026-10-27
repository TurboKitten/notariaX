package com.duoc.notariaplataforma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.notariaplataforma.ui.SplashScreen
import com.duoc.notariaplataforma.ui.theme.NotariaplataformaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotariaplataformaTheme {
                SplashScreen()
            }
        }
    }
}