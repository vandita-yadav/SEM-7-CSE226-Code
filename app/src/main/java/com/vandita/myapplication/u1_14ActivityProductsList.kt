package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

class u1_14ActivityProductsList : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CatalogueDemo()
                }
            }
        }
    }
}

data class Product(
    val name: String,
    val price: Double,
    val deliveryDate: String,
    val imageResId: Int
)

@Composable
@Preview(showBackground = true)
fun CatalogueDemo() {

    val products = listOf(
        Product("Wireless Headphone", 26.99, "28 August 2026", R.drawable.headphone),
        Product("Smart Watch", 37.65, "29 August 2026",R.drawable.smartwatch),
        Product("Backpack", 20.54, "30 August 2026",R.drawable.bagpack),
        Product("Running Shoes", 22.87, "1 September 2026",R.drawable.runningshoes),
        Product("Earbuds", 24.86, "2 September 2026",R.drawable.earbuds),
        Product("Power Adapter", 18.42, "3 September 2026",R.drawable.poweradaptor),
        Product("Tablet", 56.86, "10 September 2026",R.drawable.tablet),
        Product("Gaming Laptop", 1500.42, "13 September 2026",R.drawable.laptop)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Product Catalogue",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(products) { product ->

                var showDetails by remember {
                    mutableStateOf(false)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 20.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        // Product name + button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = product.imageResId),
                                contentDescription = product.name,
                                modifier = Modifier
                                    .height(70.dp)
                                    .padding(end = 12.dp)
                            )

                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = {
                                    showDetails = !showDetails
                                },
                                modifier = Modifier.width(110.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = if (showDetails) "Hide Details" else "View Details",
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        // Animated details
                        AnimatedVisibility(
                            visible = showDetails,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {

                                Text(
                                    text = "Price: $${product.price}",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = "Estimated Delivery: ${product.deliveryDate}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}