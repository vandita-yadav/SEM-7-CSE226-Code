package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class u1_3ActivityModalBottomSheet : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Makes app draw behind status bar
        enableEdgeToEdge()
        setContent {
            // Calling our composable screen
            ModalSheetDemo()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun ModalSheetDemo()
{
    val sheetState = rememberStandardBottomSheetState(
    initialValue = SheetValue.PartiallyExpanded,
    skipHiddenState = true)

    Scaffold(topBar = {TopAppBar(title = {Text("Modal Sheet Demo")},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue, // Set your background color here
            titleContentColor = Color.White // Set your text color here
        ))})
    {
        padding->
        Box(modifier = Modifier.fillMaxSize().padding(20.dp))
        {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Spacer(modifier = Modifier.height(20.dp))

                Card(modifier = Modifier.fillMaxWidth().height(280.dp),
//                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(12.dp))
                {
                    Image(
                        painter = painterResource(id = R.drawable.mountains),
                        contentDescription = "Mountains Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}