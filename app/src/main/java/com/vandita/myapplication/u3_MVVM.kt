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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class u3_MVVM : ComponentActivity()
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
                    FoodScreen()
                }
            }
        }
    }
}

data class FoodItemData(val name:String, val quantity:Int, val price:Int)

class FoodCartViewModel: ViewModel()
{
    var foodItems = mutableStateListOf(
        FoodItemData("Pizza 🍕", 1, 100),
        FoodItemData("Burger 🍔", 1, 50),
        FoodItemData("Pasta 🍝", 1, 80),
        FoodItemData("Salad 🥗", 1, 30),
        FoodItemData("Juice 🥤", 1, 20),
        FoodItemData("Ice Cream 🍦", 1, 40),
        FoodItemData("Pastry 🍰", 1, 60)
    )

    fun addItem(index:Int)
    {
        foodItems[index] = foodItems[index].copy(quantity = foodItems[index].quantity + 1)
    }

    fun removeItem(index: Int) {
        if (foodItems[index].quantity > 0) {
            foodItems[index] = foodItems[index].copy(
                quantity = foodItems[index].quantity - 1
            )
        }
    }

    fun totalPrice(): Int
    {
        return foodItems.sumOf{it.quantity * it.price}
    }
}

@Composable
@Preview(showBackground = true)
fun FoodScreen(viewModel: FoodCartViewModel = viewModel())
{
    Column(modifier = Modifier.fillMaxSize().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = "Food Cart 🛒", fontSize = 30.sp,color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(20.dp))

        viewModel.foodItems.forEachIndexed { index, item->
            FoodItem(
                name = item.name,
                quantity = item.quantity,
                price = item.price,
                onAddClick={viewModel.addItem(index)},
                onRemoveClick={viewModel.removeItem(index)}
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Total Price = ₹${viewModel.totalPrice()}", fontSize = 20.sp)
    }
}

@Composable
fun FoodItem(name: String, quantity: Int, price: Int,
    onAddClick: () -> Unit, onRemoveClick: () -> Unit)
{
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {

        Column(modifier = Modifier.weight(1f))
        {
            Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "₹$price", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.W600)
        }

        // Decrement button
        Button(onClick = onRemoveClick)
        {
            Text(text = "-", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Quantity
        Text(text = quantity.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.width(10.dp))

        // Increment button
        Button(onClick = onAddClick)
        {
            Text(text = "+", fontSize = 20.sp)
        }
    }
}
