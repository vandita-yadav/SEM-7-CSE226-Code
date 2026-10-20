package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

data class Event(
    val name: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String,
    val image: Int
)

val events = listOf(
    Event(
        "Hackathon", "10-09-2026", "10:00 AM", "Block 32 - 213",
        "A technology event of 36 hour hackathon", R.drawable.hackathon
    ),
    Event(
        "Cultural Fest", "15-09-2026", "5:00 PM", "University Auditorium SDMA",
        "A cultural event with music, dance, singing and other entertaining performances for freshers",
        R.drawable.fest
    ),
    Event(
        "Sports Meet", "20-09-2026", "9:00 AM", "University Ground",
        "An annual sports event with various indoor and outdoor games.",
        R.drawable.sports
    )
)

class CA1_EventDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nav = rememberNavController()
            var event by remember { mutableStateOf(events[0]) }

            NavHost(nav, startDestination = "dashboard") {
                composable("dashboard") {
                    EventManagementDashboard {
                        event = it
                        nav.navigate("eventInfo")
                    }
                }

                composable("eventInfo") {
                    EventInfoScreen(event) {
                        nav.navigate("registration")
                    }
                }

                composable("registration") {
                    RegistrationScreen(
                        onCancel = { nav.popBackStack() },
                        onHome = { nav.navigate("dashboard") }
                    )
                }
            }
        }
    }
}

@Composable
fun EventManagementDashboard(onEventClick: (Event) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 80.dp).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Event Dashboard LPU",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(25.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(events) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clickable { onEventClick(event) },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(event.image),
                            contentDescription = event.name,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Column {
                            Text(
                                event.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                event.date,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventInfoScreen(event: Event, onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.White)
            .padding(horizontal = 20.dp, vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Event Info",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(event.image),
            contentDescription = event.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(18.dp))
        )

        Spacer(modifier = Modifier.height(25.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Name of event: ${event.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Date: ${event.date}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Time: ${event.time}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Location/Venue: ${event.location}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Description: ${event.description}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                "Register",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
@Composable
fun RegistrationScreen(
    onCancel: () -> Unit,
    onHome: () -> Unit
) {
    var registered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(3000)
        registered = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!registered) {

            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                "Processing......",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(25.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.width(140.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.titleMedium
                )
            }

        } else {

            Text(
                "You are registered for this Event ✅",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = onHome,
                modifier = Modifier.width(160.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    "Home",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}