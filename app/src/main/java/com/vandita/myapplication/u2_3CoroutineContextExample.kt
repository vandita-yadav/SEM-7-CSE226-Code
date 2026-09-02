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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class u2_3CoroutineContextExample : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    StudentResultScreen()
                }
            }
        }
    }
}


// VIEWMODEL
class StudentResultViewModel : ViewModel()
{
    var message by mutableStateOf("Click a button")
        private set
    var isLoading by mutableStateOf(false)
        private set
    // COROUTINE BASICS + COROUTINE SCOPE
    fun checkResult()
    {
        viewModelScope.launch {
            isLoading = true
            message = "Checking result..."
            // Call suspend function
            val result = getStudentResult()
            message = result
            isLoading = false
        }
    }
    // DISPATCHERS
    fun loadFromServer() {
        viewModelScope.launch {
            isLoading = true
            message = "Connecting to server..."
            val data = withContext(Dispatchers.IO) {
                getDataFromServer()
            }
            message = data
            isLoading = false
        }
    }
    fun performCalculation() {
        viewModelScope.launch {
            message = "Performing calculation..."
            val result = withContext(Dispatchers.Default) {
                calculateResult()
            }
            message = result
        }
    }
    // SUSPEND FUNCTION
    private suspend fun getStudentResult(): String {
        // Simulate waiting for server
        delay(3000)
        return "🎉 Result: PASSED"
    }
    // IO WORK
    private suspend fun getDataFromServer(): String {
        delay(3000)
        return "📡 Data received from server"
    }
    // CPU-INTENSIVE WORK
    private fun calculateResult(): String {
        var total = 0L
        for (i in 1..5000000) {
            total += i
        }
        return "Calculation completed\nTotal = $total"
    }

    fun loadData() {
        TODO("Not yet implemented")
    }
}
// COMPOSE UI
@Composable
fun StudentResultScreen(viewModel: StudentResultViewModel = viewModel())
{
    val message = viewModel.message
    val isLoading = viewModel.isLoading
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
    {
        Text(text = "Student Result App", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = message)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { viewModel.checkResult() }) { Text("Check Result") }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { viewModel.loadFromServer() }) { Text("Load From Server")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { viewModel.performCalculation() }) { Text("Perform Calculation") }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) { CircularProgressIndicator() }
    }
}

/*
Create a composable screen with "load data" button, "cancel" button, status text, lazy column to show items
when load data is clicked:- launch a coroutine in viewmodelscope, use dispatchers.io to simulate fetching data use.delay(2000)
switch to dispatchers.main to update ui
when cancel is clicked, cancel coroutine, update status accordingly
show the current coroutine context (dispatcher.name) on screen.

fetches the data (simulated with delay), display the data in a list, allows user to start and cancel operation

header:- "Coroutine Demo"
then title "data loader"
then "status:idle/working/done"
two buttons:- green "load data" and red "cancel"
then lazy column list:-
item 1
item 2
item 3
item n
* */



