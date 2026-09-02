package com.vandita.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class u2_4DispatchersActivity : ComponentActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme { Surface(modifier = Modifier.fillMaxSize()) { DispatcherActivity() } }
        }
    }
}



class DispatcherActivityViewModel : ViewModel()
{

    var status by mutableStateOf("Idle")
        private set

    var items by mutableStateOf<List<String>>(emptyList())
        private set

    var dispatcherName by mutableStateOf("Main")
        private set

    private var loadJob: Job? = null


    fun loadData()
    {
        if (loadJob?.isActive == true) { return }

        loadJob = viewModelScope.launch { status = "Working"
            dispatcherName = "Main"

            try {

                val data = withContext(Dispatchers.IO)
                {
                    dispatcherName = "IO"
                    delay(2000)
                    listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
                }

                withContext(Dispatchers.Main)
                { dispatcherName = "Main"
                    items = data
                    status = "Done"
                }

            }
            catch (e: kotlinx.coroutines.CancellationException) {
                status = "Cancelled"
                dispatcherName = "Main"
                throw e
            }
        }
    }


    fun cancelData() {

        if (loadJob?.isActive == true) {
            loadJob?.cancel()
            loadJob = null
        }

        items = emptyList()

        status = "Cancelled"
        dispatcherName = "Main"
    }
}


@Composable
@Preview(showBackground = true)
fun DispatcherActivity(
    viewModel: DispatcherActivityViewModel = viewModel()
) {

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally )
    {
        Text(text = "Coroutine Demo")

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Data Loader")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Status: ${viewModel.status}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Dispatcher: ${viewModel.dispatcherName}")

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.loadData()
            }
        ) {
            Text("Load Data")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.cancelData()
            }
        ) {
            Text("Cancel")
        }

        Spacer(modifier = Modifier.height(8.dp))

        val context = LocalContext.current

        Button(
            onClick = {

                viewModel.cancelData()

                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                context.startActivity(intent)
            }
        ) {
            Text("Home")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f))
        {

            items(viewModel.items) { item ->

                Text(
                    text = item,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

/*
on clicking the cancel button, the items should disappear
add one home button, which on click, takes us to the homescreen of the phone/emulator
* **/



/*

```kotlin
package com.vandita.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class u2_4DispatchersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DispatcherActivity()
                }
            }
        }
    }
}


class DispatcherActivityViewModel : ViewModel() {

    var status by mutableStateOf("Idle")
        private set

    var items by mutableStateOf<List<String>>(emptyList())
        private set

    var dispatcherName by mutableStateOf("Main")
        private set

    private var loadJob: Job? = null


    fun loadData() {

        if (loadJob?.isActive == true) {
            return
        }

        loadJob = viewModelScope.launch {

            status = "Working"
            dispatcherName = "Main"

            try {

                val data = withContext(Dispatchers.IO) {

                    dispatcherName = "IO"

                    delay(2000)

                    listOf(
                        "Item 1",
                        "Item 2",
                        "Item 3",
                        "Item 4",
                        "Item 5"
                    )
                }

                withContext(Dispatchers.Main) {

                    dispatcherName = "Main"
                    items = data
                    status = "Done"
                }

            } catch (e: CancellationException) {

                status = "Cancelled"
                dispatcherName = "Main"

                throw e
            }
        }
    }


    fun cancelData() {

        if (loadJob?.isActive == true) {

            loadJob?.cancel()
            loadJob = null
        }

        // Clear the items when Cancel is clicked
        items = emptyList()

        status = "Cancelled"
        dispatcherName = "Main"
    }
}


@Composable
@Preview(showBackground = true)
fun DispatcherActivity(
    viewModel: DispatcherActivityViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Coroutine Demo")

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Data Loader")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Status: ${viewModel.status}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Dispatcher: ${viewModel.dispatcherName}")

        Spacer(modifier = Modifier.height(24.dp))


        // LOAD DATA
        Button(
            onClick = {
                viewModel.loadData()
            }
        ) {
            Text("Load Data")
        }

        Spacer(modifier = Modifier.height(8.dp))


        // CANCEL
        Button(
            onClick = {
                viewModel.cancelData()
            }
        ) {
            Text("Cancel")
        }

        Spacer(modifier = Modifier.height(8.dp))


        // HOME
        Button(
            onClick = {

                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                viewModel.cancelData()

                // Go to phone/emulator home screen
                // Note: the Activity itself is not destroyed.
                // The launcher/home screen is brought to the foreground.
                androidx.compose.ui.platform.LocalContext.current.startActivity(intent)
            }
        ) {
            Text("Home")
        }

        Spacer(modifier = Modifier.height(24.dp))


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(viewModel.items) { item ->

                Text(
                    text = item,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
```

**One correction:** `LocalContext.current` cannot be safely accessed directly inside the `onClick` expression like that. Use this version for the `DispatcherActivity()` function instead:

```kotlin
@Composable
@Preview(showBackground = true)
fun DispatcherActivity(
    viewModel: DispatcherActivityViewModel = viewModel()
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Coroutine Demo")

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Data Loader")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Status: ${viewModel.status}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Dispatcher: ${viewModel.dispatcherName}")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.loadData()
            }
        ) {
            Text("Load Data")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.cancelData()
            }
        ) {
            Text("Cancel")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                viewModel.cancelData()

                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                context.startActivity(intent)
            }
        ) {
            Text("Home")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(viewModel.items) { item ->
                Text(
                    text = item,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
```

### What changed

**Cancel:**

```kotlin
items = emptyList()
```

So clicking Cancel immediately removes all the displayed items.

**Home:**

```kotlin
val intent = Intent(Intent.ACTION_MAIN).apply {
    addCategory(Intent.CATEGORY_HOME)
}

context.startActivity(intent)
```

This tells Android: **"Open the device's Home/Launcher."**

Also, I kept:

```kotlin
viewModel.cancelData()
```

inside Home, so if the coroutine is currently loading data and you press Home, the loading operation gets cancelled too.


* */

