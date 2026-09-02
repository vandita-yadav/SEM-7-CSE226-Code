package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class u2_5ActivityStructuredConcurrency : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoodDeliveryApp()
                }
            }
        }
    }
}

data class FoodExplorer(val name: String, val price: Double, val imageResId: Int, val description: String)
enum class AppState { LIST, DETAILS, LOADING, SUCCESS }

data class Review(
    val text: String,
    val reviewer: String
)

val reviews = listOf(
    Review("Great taste!", "Alex"),
    Review("Really delicious!", "Sarah"),
    Review("Loved it!", "John"),
    Review("Amazing food!", "Emily"),
    Review("Would order again!", "Michael")
)

@Composable
@Preview(showBackground = true)
fun FoodDeliveryApp()
{
    var currentState by remember { mutableStateOf(AppState.LIST) }
    var selectedItem by remember { mutableStateOf<FoodExplorer?>(null) }

    val products = listOf(
        FoodExplorer("Pizza", 8.99, R.drawable.pizza, "A delicious classic pizza topped with rich tomato sauce, melted mozzarella cheese, basil, and a perfectly baked golden crust."),
        FoodExplorer("Veg Burger", 6.99, R.drawable.vegburger, "A crispy and flavorful vegetable/meat patty served with fresh lettuce, pickles, cheese, juicy tomatoes, creamy mayonnaise, and a soft toasted bun."),
        FoodExplorer("Caesar Salad", 5.46, R.drawable.caesarsalad, "Fresh and crunchy Romaine lettuce tossed with creamy Caesar dressing, parmesan cheese, and crispy seasoned toppings."),
        FoodExplorer("Pasta Alfredo", 7.99, R.drawable.pastafredo, "Creamy fettuccine pasta cooked in a rich Alfredo sauce made with parmesan cheese, butter, and a hint of garlic."),
        FoodExplorer("Mango Smoothie", 4.12, R.drawable.mangosmoothie, "A refreshing tropical smoothie made with sweet ripe mangoes, blended until smooth and creamy for a naturally fruity flavor."),
        FoodExplorer("Choco Lava Cake", 6.53, R.drawable.chocolava, "A warm and soft chocolate cake with a rich molten chocolate center that melts deliciously with every bite."),
        FoodExplorer("Butterscotch Ice cream", 4.23, R.drawable.butterscotchicecream, "Rich and creamy butterscotch ice cream blended with crunchy praline bits for the perfect combination of sweetness and texture."),
        FoodExplorer("Blueberries Pancake", 7.56, R.drawable.blueberriespancake, "Fluffy golden buttermilk pancakes filled with fresh blueberries and served with a deliciously sweet and fruity flavor."),
        FoodExplorer("Cheesecake", 9.54, R.drawable.cheesecake, "A smooth and creamy New York-style cheesecake with a rich, velvety filling resting on a buttery and lightly crisp crust."),
        FoodExplorer("French Fries", 4.51, R.drawable.frenchfries, "Golden and crispy potato fries seasoned with just the right amount of salt, offering a crunchy outside and soft, fluffy center."),
        FoodExplorer("Coca Cola", 2.65, R.drawable.coke, "A refreshing and chilled carbonated soft drink with a classic cola flavor, perfect for enjoying alongside your favorite meal."),
        FoodExplorer("Coffee", 3.49, R.drawable.coffee, "A rich and aromatic coffee brewed to perfection, offering a smooth, bold flavor and a warm, comforting experience."),
        FoodExplorer("Tea", 2.99, R.drawable.tea, "A soothing and refreshing cup of freshly brewed tea with a delicate aroma and balanced flavor, perfect for any time of day."),
        FoodExplorer("Matcha", 4.49, R.drawable.matcha, "A smooth and vibrant matcha drink made with finely ground green tea, offering a refreshing earthy flavor with a naturally creamy finish."),
        FoodExplorer("Cold Coffee", 3.99, R.drawable.coldcoffee, "A creamy and refreshing chilled coffee blended with smooth milk and rich coffee, creating the perfect balance of sweetness and bold flavor.")
    )

    when (currentState)
    {
        AppState.LIST -> {
            FoodListScreen(products = products, onItemClick = { item -> selectedItem = item
                currentState = AppState.DETAILS }) }

        AppState.DETAILS -> {
            selectedItem?.let { item ->
                FoodDetailsScreen(
                    item = item,
                    onOrderClick = { currentState = AppState.LOADING },
                    onCancelClick = { currentState = AppState.LIST }
                )
            }
        }
        AppState.LOADING -> {
            LoadingScreen(
                onCancelClick = { currentState = AppState.DETAILS },
                onTimeUp = { currentState = AppState.SUCCESS }
            )
        }
        AppState.SUCCESS -> {
            SuccessScreen(
                onBackToMenuClick = {
                    selectedItem = null
                    currentState = AppState.LIST
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(products: List<FoodExplorer>, onItemClick: (FoodExplorer) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color.White)
        .padding(horizontal = 24.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally,
        )
    {
        Text(text = "Food Explorer", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp))
        {
            items(products) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    onClick = { onItemClick(product) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = product.imageResId),
                            contentDescription = product.name,
                            modifier = Modifier.height(70.dp).padding(end = 12.dp)
                        )
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${product.price}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDetailsScreen(
    item: FoodExplorer,
    onOrderClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Food Image
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Item Name
        Text(
            text = item.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Price
        Text(
            text = "$${item.price}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Description
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reviews
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    reviews.forEach { review ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = review.text,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "★★★★★",
                                fontSize = 16.sp,
                                color = Color(0xFFFFC107)
                            )

                            Text(
                                text = " - ${review.reviewer}",
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onOrderClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Order")
            }
        }
    }
}


@Composable
fun LoadingScreen(onCancelClick: () -> Unit, onTimeUp: () -> Unit) {
    // Timer that triggers success after 5 seconds if not cancelled
    LaunchedEffect(Unit) {
        delay(3000L)
        onTimeUp()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Processing Order...", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(onClick = onCancelClick) {
            Text("Cancel")
        }
    }
}

@Composable
fun SuccessScreen(onBackToMenuClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Order placed successfully! 🎉",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBackToMenuClick) {
            Text("Back to Menu")
        }
    }
}