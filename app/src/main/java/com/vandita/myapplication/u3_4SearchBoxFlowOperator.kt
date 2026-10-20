package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class u3_4SearchBoxFlowOperator : ComponentActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchScreen() {

    var searchText by remember {
        mutableStateOf("")
    }

    var suggestions by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    val words = listOf(
        "apple","application", "applied", "application", "apply", "appliance",
        "applause", "banana", "orange", "grapes")

    LaunchedEffect(searchText) {

        flow {
            emit(searchText)
        }
            .debounce(500)
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .map { query ->
                words.filter {
                    it.startsWith(query, ignoreCase = true)
                }
            }
            .collect { result ->
                suggestions = result
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Search",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            label = {
                Text("Search word")
            },
            placeholder = {
                Text("Type Here")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (suggestions.isNotEmpty()) {

            Text(
                text = "Suggestions:",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            suggestions.forEach { suggestion ->

                Text(
                    text = suggestion,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}
