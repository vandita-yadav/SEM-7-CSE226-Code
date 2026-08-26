package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class u2_2ActivityCoroutineExample : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FoodOrderScreen2()
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun FoodOrderScreen2()
{
    var orderStatus by remember { mutableStateOf("No order placed 🙁") }
    var isLoading by remember { mutableStateOf(false) }
    var orderConfirmed by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading)
    {
        if (isLoading)
        {
            orderStatus = "⏲️ Preparing your order....."
            delay(3000)
            orderStatus = "Your order has been confirmed! ✅"
            isLoading = false
            orderConfirmed = true
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = orderStatus, fontSize = 25.sp)

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) { CircularProgressIndicator() }
        else {
            Button(onClick = { if (!orderConfirmed) { isLoading = true } })
            {
                if (!orderConfirmed) Text("Place order") else Text("Home")
            }
        }
    }
}




