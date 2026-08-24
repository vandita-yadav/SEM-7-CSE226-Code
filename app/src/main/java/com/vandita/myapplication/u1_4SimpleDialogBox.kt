package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class u1_4SimpleDialogBox : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Makes app draw behind status bar
        enableEdgeToEdge()
        setContent {
            // Calling our composable screen
            BookAppointmentScreen()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun BookAppointmentScreen()
{
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center)
    {
        Button(onClick = {showDialog = true})
        {
            Text("Book Appointment")
        }
        if (showDialog)
        {
            Dialog(onDismissRequest = { showDialog = false })
            {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)))
                {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Icon(imageVector = Icons.Default.Face, contentDescription = null, modifier = Modifier.size(70.dp), tint = Color(0xFF1565C0))

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "Book Appointment", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Patient Name") }, modifier = Modifier.fillMaxWidth())

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Appointment Date") }, modifier = Modifier.fillMaxWidth())

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Appointment Time") }, modifier = Modifier.fillMaxWidth())

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                        {

                            OutlinedButton(onClick = { showDialog = false })
                            {
                                Text("Cancel")
                            }

                            Button(onClick = { showDialog = false })
                            {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }
    }
}