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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import  kotlinx.coroutines.launch

class u3_2Suspended:  ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    OrderScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreen()
{
    var orderStatus by remember{ mutableStateOf("Waiting for order 🙁") }
    var paymentStatus by remember{ mutableStateOf("Payment pending ⏳") }
    var stockStatus by remember{ mutableStateOf("Updating stock 📦") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = "Order Status: $orderStatus", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Payment Status: $paymentStatus", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Stock Status: $stockStatus", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            scope.launch {
                val orderJob = launch {
                    orderStatus = "Restaurant accepting order...."
                    withContext(Dispatchers.IO){delay(3000)}
                    orderStatus = "Order confirmed 🥗"
                }
                val paymentJob = launch {
                    paymentStatus = "Payment in progress...."
                    withContext(Dispatchers.IO){delay(5000)}
                    paymentStatus = "Payment successful 💳"
                }
                val stockJob = launch {
                    stockStatus = "Updating stock...."
                    withContext(Dispatchers.IO){delay(4000)}
                    stockStatus = "Stock updated ✅"
                }
            }
        })
        {
            Text("Place Order")
        }
    }
}