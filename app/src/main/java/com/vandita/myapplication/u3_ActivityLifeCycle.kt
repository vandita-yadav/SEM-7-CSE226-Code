package com.vandita.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

class u3_ActivityLifeCycle : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LifecycleScreen()
        }
    }
}

@Composable
fun LifecycleScreen() {

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                Lifecycle.Event.ON_CREATE ->
                    Log.d("LifecycleDemo", "ON_CREATE")

                Lifecycle.Event.ON_START ->
                    Log.d("LifecycleDemo", "ON_START")

                Lifecycle.Event.ON_RESUME ->
                    Log.d("LifecycleDemo", "ON_RESUME")

                Lifecycle.Event.ON_PAUSE ->
                    Log.d("LifecycleDemo", "ON_PAUSE")

                Lifecycle.Event.ON_STOP ->
                    Log.d("LifecycleDemo", "ON_STOP")

                Lifecycle.Event.ON_DESTROY ->
                    Log.d("LifecycleDemo", "ON_DESTROY")

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lifecycle-Aware Components")
    }
}

@Preview(showBackground = true)
@Composable
fun LifecycleScreenPreview() {
    LifecycleScreen()
}