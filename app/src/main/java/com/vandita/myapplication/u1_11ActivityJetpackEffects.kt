package com.vandita.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class u1_11ActivityJetpackEffects : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()){
                    EffectDemo()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun EffectDemo() {

    var currentTime by remember {
        mutableStateOf(
            SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
        )
    }

    var isRunning by remember {
        mutableStateOf(false)
    }

    // LaunchedEffect starts when the screen appears
    // and restarts whenever isRunning changes.
    LaunchedEffect(isRunning) {

        if (isRunning) {
            while (true) {
                currentTime = SimpleDateFormat(
                    "hh:mm:ss a",
                    Locale.getDefault()
                ).format(Date())

                delay(1000)
            }
        }
    }

    // DisposableEffect cleans up when the timer stops
    // or when the composable leaves the screen.
    DisposableEffect(isRunning) {

        onDispose {
            Log.d("ComposeDemo", "Timer disposed/stopped")
        }
    }

    // SideEffect runs after every successful recomposition
    SideEffect {
        Log.d(
            "ComposeDemo",
            "UI Updated: $currentTime"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Top bar
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text("Compose Effects Demo")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Time Card
        Card(
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Current Time",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentTime,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Start Timer
            Button(
                onClick = {
                    isRunning = true
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Timer"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Start Timer")
            }

            // Stop Timer
            Button(
                onClick = {
                    isRunning = false
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC62828)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Stop Timer"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Stop Timer")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Status row
        Text(
            text = if (isRunning) "Timer Running" else "Timer Stopped",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (isRunning)
                Color(0xFF2E7D32)
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Logs will appear in Logcat",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}





















/*
Top bar with title "compose effects demo", a card showing the current time (Auto-update every second)
a button "start timer" / "stop timer" to control timer
a text below card to show "logs will appear in locat"
a small status row showing timer state (running/stopped)
use remember{mutableStateOf()} for time and isRunning
use launched effect (unit) or launchedeffect(isruuning) to clean up when the timer stops or screen is disposed
when the screen first appears or when timer is started (isrunning = true), start a coroutine
side disposable effect (isrunning) to clean up when timer stops or creen is disposed
use side effect to log every successful recomposition  with every updated time"I/ComposeDemo: UI Updated: 11:45:31 AM"


* */