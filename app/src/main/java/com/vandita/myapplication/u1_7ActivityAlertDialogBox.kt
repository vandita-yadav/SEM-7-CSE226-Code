package com.vandita.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class u1_7ActivityAlertDialogBox: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AlertDialogDemo()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AlertDialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Show Alert Dialog")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },

            title = {
                Text("Delete File")
            },

            text = {
                Text("Are you sure you want to delete this file?")
            },

            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Green, contentColor = Color.White))
                {
                    Text("Delete")
                }
            },

            dismissButton = {
                OutlinedButton(
                    onClick = {Toast.makeText(context,
                        "File Deleted", Toast.LENGTH_SHORT).show()},
                    shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White))
                {
                    Text("Cancel")
                }
            }
        )
    }
}

//  what is key? is it constant or mutable and how it works in launched effect
//