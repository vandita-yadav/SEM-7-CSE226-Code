package com.vandita.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text

class u1_9DisposableEffect : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()){
                    DisposableEffectApp()
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DisposableEffectApp()
{
    var showScreen by remember{mutableStateOf(true)}
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Button(onClick = {showScreen = !showScreen})
        {
            Text(if(showScreen) "Remove Screen" else "Show Screen")
        }
        if(showScreen){MyScreen()}
    }
}
@Composable
fun MyScreen()
{
    val context = LocalContext.current
    DisposableEffect(Unit)
    {
        Toast.makeText(context, "Screen Opened", Toast.LENGTH_SHORT).show()

        onDispose {
            Toast.makeText(context, "Screen Closed", Toast.LENGTH_SHORT).show()
        }
    }
        Text(
            text = "Hello! This screen is using Disposable Effect",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
}