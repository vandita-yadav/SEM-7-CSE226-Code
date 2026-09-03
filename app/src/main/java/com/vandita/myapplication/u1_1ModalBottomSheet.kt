package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class u1_1ModalBottomSheet : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Makes app draw behind status bar
        enableEdgeToEdge()
        setContent {
            // Calling our composable screen
            PlaceScreen()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PlaceScreen()
{
    // Controls the state (Expanded / Hidden) of Modal Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    // Boolean variable to decide whether Bottom Sheet should appear
    var showBottomSheet by remember{ mutableStateOf(false) }

    Scaffold(
        // Top App Bar
        topBar = {
            TopAppBar(
                title = {
                    Text("Nearby Place Finder")
                }
            )
        }

    ) { padding ->

        // Main container
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color.White))))
        {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally)
            {
                Spacer(modifier = Modifier.height(20.dp))

                // Card containing the place image
                Card(modifier = Modifier.fillMaxWidth().height(280.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp))
                {

                    Image(
                        painter = painterResource(id = R.drawable.town),
                        contentDescription = "LPU Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                }

                Spacer(modifier = Modifier.height(25.dp))

                // Heading
                Text(
                    text = "Explore Nearby Places",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sub Heading
                Text(
                    text = "Tap below to view place details",
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Button to open Bottom Sheet
                Button(onClick = {showBottomSheet = true })
                {
                    Text("Show Place Details")
                }

            }

            // Bottom Sheet appears only if this condition becomes true
            if (showBottomSheet) {

                ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState)
                {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp))
                    {
                        // Place Name
                        Text(
                            text = "📍 Lovely Professional University",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rating
                        Text(
                            text = "⭐ Rating : 4.4 (25K Reviews)",
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Location
                        Text(
                            text = "📍Phagwara, Punjab",
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        // Action Buttons
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                        {
                            Button(onClick = { }) { Text("Directions") }
                            Button(onClick = { }) { Text("Call") }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                }
            }
        }
    }
}