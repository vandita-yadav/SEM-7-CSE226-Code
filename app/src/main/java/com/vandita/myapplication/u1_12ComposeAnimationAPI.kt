package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class u1_12ComposeAnimationAPI: ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Makes app draw behind status bar
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                Surface(
                modifier = Modifier.fillMaxSize())
                {
                StudentProfile()}
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun StudentProfile()
{
    var expanded by remember{ mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if(expanded) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary,
        label = "Button Color"
    )

    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Card(modifier = Modifier.animateContentSize().padding(8.dp))
        {
            Column(modifier = Modifier.padding(8.dp))
            {
                Text(text = " Vandita Yadav",
                    style = MaterialTheme.typography.headlineMedium)

                Text(text = " B.Tech CSE | 4th Year",
                    style = MaterialTheme.typography.headlineSmall)

                AnimatedVisibility(visible = expanded, enter = fadeIn(), exit = fadeOut())
                {
                    Column(modifier = Modifier.padding(8.dp))
                    {
                        Text("Email: vandita22yadav@gmail.com")
                        Text("Mobile: +91 9415588772")
                        Text("Attendance: 94%")
                    }
                }
                Button(onClick={
                    expanded = !expanded
                }, modifier = Modifier.padding(8.dp))
                {
                    Text(text = if(expanded) "Hide details"
                    else "View details")
                }
            }
        }
    }
}