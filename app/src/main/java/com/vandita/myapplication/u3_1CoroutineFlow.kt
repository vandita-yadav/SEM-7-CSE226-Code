package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class ChatViewModel
{
    private val _onlineStatus = MutableStateFlow(true)
    val onlineStatus = _onlineStatus.asStateFlow()
    val newMessageEvent = MutableSharedFlow<String>()

    val messageFlow: Flow<String> = flow {
        delay(2000)
        emit("Hello")
        delay(2000)
        emit("How are you?")
        delay(2000)
        emit("I'm good, thanks!")
        delay(2000)
        emit("What are you doing?")
        delay(2000)
        emit("Nothing much")
        delay(2000)
        emit("Shall we meet?")
        delay(2000)
        emit("Let's meet tomorrow")
        delay(2000)
        emit("Ok sure, bye!")
    }

    fun changeOnlineStatus()
    {
        _onlineStatus.value = !_onlineStatus.value
    }
}

class u3_1CoroutineFlow : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    ChatScreen()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreen()
{
    val viewModel = remember{ChatViewModel()}
    val messages = remember{mutableListOf<String>()}
    val online by viewModel.onlineStatus.collectAsState()

    LaunchedEffect(Unit)
    {
        viewModel.messageFlow.collect { message ->
            messages.add(message)
            viewModel.newMessageEvent.emit("New message received!")
        }
    }

    LaunchedEffect(Unit)
    {
        viewModel.newMessageEvent.collect { event ->
            println(event)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp))
    {
        Text(text = "Messaging Application", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = if(online) {"\uD83D\uDFE2 Online"} else {"\uD83D\uDD34 Offline"},fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp))
        {
            items(messages.size) { message->
                Card(modifier = Modifier.fillMaxWidth())
                {
                    Text(text = "Friend: $messages[message]",
                        modifier = Modifier.padding(15.dp),
                        fontSize = 15.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Button(onClick = { viewModel.changeOnlineStatus()})
            {
                Text(text = if(online) {"Go Offline"} else {"Go Online"})
            }
        }
    }
}

