package com.vandita.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Essential import to resolve List vs Int error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// --------------------------------------------------
// DATA CLASS
// --------------------------------------------------
data class Article(
    val title: String,
    val description: String,
    val imageUrl: String
)

// --------------------------------------------------
// VIEWMODEL
// --------------------------------------------------
class NewsViewModel : ViewModel() {

    private val _news = MutableStateFlow<List<Article>>(emptyList())
    val news: StateFlow<List<Article>> = _news

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            getNewsFlow()
                .catch {
                    // Handle failure and fallback to empty list
                    _news.value = emptyList()
                }
                .collect { articles ->
                    _news.value = articles
                }
        }
    }

    // Coroutine Flow emitting fetched articles
    private fun getNewsFlow(): Flow<List<Article>> = flow {
        val result = fetchNewsFromApi()
        emit(result)
    }.flowOn(Dispatchers.IO)

    private fun fetchNewsFromApi(): List<Article> {
        val apiKey = "YOUR_API_KEY"
        val url = URL("https://newsapi.org/v2/top-headlines?country=in&apiKey=$apiKey")

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        connection.connect()

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val jsonObject = JSONObject(response)
        val articlesArray = jsonObject.getJSONArray("articles")
        val articleList = mutableListOf<Article>()

        for (i in 0 until articlesArray.length()) {
            val article = articlesArray.getJSONObject(i)
            val title = article.optString("title", "No title")
            val description = article.optString("description", "No description available")
            val imageUrl = article.optString("urlToImage", "")

            articleList.add(
                Article(
                    title = title,
                    description = description,
                    imageUrl = imageUrl
                )
            )
        }

        return articleList
    }
}

// --------------------------------------------------
// ACTIVITY
// --------------------------------------------------
class u2_6ActivityCoroutine : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NewsScreen()
            }
        }
    }
}

// --------------------------------------------------
// COMPOSE SCREENS & COMPONENTS
// --------------------------------------------------
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = viewModel()
) {
    val news by viewModel.news.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "News App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { viewModel.loadNews() }) {
                Text("Reload")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NEWS LIST OR EMPTY VIEW
        if (news.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No news available")
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { viewModel.loadNews() }) {
                    Text("Load News")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(news) { article ->
                    NewsCard(article = article)
                }
            }
        }
    }
}

@Composable
fun NewsCard(
    article: Article
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            if (article.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = "News Image",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = article.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = article.description,
                    fontSize = 14.sp
                )
            }
        }
    }
}