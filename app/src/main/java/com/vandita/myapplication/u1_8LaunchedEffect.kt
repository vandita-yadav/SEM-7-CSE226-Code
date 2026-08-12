package com.vandita.myapplication

import android.os.Bundle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

class u1_8LaunchedEffect: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()){
                LaunchedEffectExample()
                }
            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun LaunchedEffectExample()
{
    var isLoaded by remember {mutableStateOf(false)}

    LaunchedEffect(isLoaded)
    {
        if(!isLoaded)
        {
            delay(3000)
            isLoaded = true
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = if(isLoaded) {"Welcome to Jetpack Compose"} else {"Loading....."},
            style = MaterialTheme.typography.headlineMedium)
        Button(onClick = {isLoaded = true})
        {
            Text("Reset")
        }
    }
}