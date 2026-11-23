package com.vandita.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class u5_1CameraPermission : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CameraPermissionScreen()
        }
    }
}

@Composable
fun CameraPermissionScreen()
{
    val context = LocalContext.current
    var permissionStatus by remember{ mutableStateOf("Permission not requested") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission())
    {   isGranted ->
        if(isGranted)
        {
            permissionStatus = "Camera Permission Granted"
        }
        else
        {
            permissionStatus = "Camera Permission Denied"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = "Camera Permission Demo", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(25.dp))

        Text(text = permissionStatus, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(25.dp))

        Button(onClick = {val permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
            if(permissionCheck== PackageManager.PERMISSION_GRANTED) permissionStatus = "Camera Permission Already Granted"
            else permissionLauncher.launch(android.Manifest.permission.CAMERA)
        })
        {
            Text(text = "Request Camera Permission")
        }
    }
}