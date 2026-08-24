package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.CardDefaults
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class u1_13CustomComposable: ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{ Surface(modifier = Modifier.fillMaxSize()) { CustomComposableDemo()} }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CustomComposableDemo()
{
    val name = listOf("Rahul Sharma", "Priya Singh", "Amit Kumar", "Neha Gupta", "Samarth Saxena", "Raghav Joshi")

    val cgpa = listOf(8.1, 8.7, 9.4, 9.1, 7.8, 9.0)

    val coursename = listOf("B.Tech CSE", "B.Tech IT", "B.Tech CSE", "B.Tech IT", "B.Tech CSE", "B.Tech CSE")

    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = "Student Dashboard", style = MaterialTheme.typography.headlineLarge, fontSize = 40.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "B.Tech Student Record", style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally)
        {
            items(name.size)
            { index ->
                var expanded by remember { mutableStateOf(false) }

                Card(modifier = Modifier.fillMaxWidth().padding(10.dp).animateContentSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp))
                {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.Start)
                    {
                        Image(
                            painter = painterResource(id = R.drawable.profile_placeholder),
                            contentDescription = "User Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(64.dp).clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(text = name[index], style = MaterialTheme.typography.titleLarge)

                        if (expanded)
                        {
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(text = "Course: ${coursename[index]}", fontSize = 18.sp)

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(text = "CGPA: ${cgpa[index]}", fontSize = 18.sp)

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(text = "7th Semester", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Button(onClick = { expanded = !expanded }, modifier = Modifier.align(Alignment.CenterHorizontally))
                        {
                            Text(text = if (expanded) "Hide Details" else "View Details")
                        }
                    }
                }
            }
        }
    }
}