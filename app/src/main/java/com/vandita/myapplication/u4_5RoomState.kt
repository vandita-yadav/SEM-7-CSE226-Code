package com.vandita.myapplication

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
class u4_5RoomState : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ShoppingScreen()
            }
        }
    }
}
@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)

@Dao
interface CartDao {

    @Insert
    suspend fun insert(item: CartItem)

    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE name = :name LIMIT 1")
    suspend fun getItemByName(name: String): CartItem?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Database(
    entities = [CartItem::class],
    version = 1,
    exportSchema = false
)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Application): CartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CartDatabase::class.java,
                    "shopping_cart_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

data class ProductInfo(
    val name: String,
    val price: Double
)

class CartViewModel(application: Application) :
    AndroidViewModel(application) {

    private val dao = CartDatabase.getDatabase(application).cartDao()

    val cartItems = dao.getAllItems()

    val products = listOf(
        ProductInfo("Milk", 60.0),
        ProductInfo("Bread", 40.0),
        ProductInfo("Eggs", 80.0),
        ProductInfo("Rice", 100.0),
        ProductInfo("Apple", 120.0),
        ProductInfo("Butter", 90.0)
    )

    fun addToCart(product: ProductInfo) {
        viewModelScope.launch {
            val existingItem = dao.getItemByName(product.name)

            if (existingItem != null) {
                dao.updateQuantity(
                    existingItem.id,
                    existingItem.quantity + 1
                )
            } else {
                dao.insert(
                    CartItem(
                        name = product.name,
                        price = product.price,
                        quantity = 1
                    )

                )
            }
        }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            dao.updateQuantity(
                item.id,
                item.quantity + 1
            )
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                dao.updateQuantity(
                    item.id,
                    item.quantity - 1
                )
            } else {
                dao.deleteItem(item.id)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            dao.clearCart()
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    viewModel: CartViewModel = viewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState(
        initial = emptyList()
    )

    val totalBill = cartItems.sumOf {
        it.price * it.quantity
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Shopping Cart")
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(
                    text = "Available Products",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(viewModel.products) { product ->

                val cartItem = cartItems.find { it.name == product.name }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("₹${product.price}")

                            if (cartItem != null) {
                                Text(
                                    text = "Subtotal: ₹${cartItem.price * cartItem.quantity}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        if (cartItem == null) {
                            Button(
                                onClick = {
                                    viewModel.addToCart(product)
                                }
                            ) {
                                Text("Add to Cart")
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.decreaseQuantity(cartItem)
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Text("−")
                                }

                                Text(
                                    text = "${cartItem.quantity}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Button(
                                    onClick = {
                                        viewModel.increaseQuantity(cartItem)
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Text("+")
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Items in cart: ${cartItems.sumOf { it.quantity }}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Total Bill: ₹$totalBill",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.clearCart()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = cartItems.isNotEmpty()
                        ) {
                            Text("Checkout / Clear Cart")
                        }
                    }
                }
            }
        }
    }
}