package com.example.dictionaryapp.views
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionaryapp.viewModels.WordsListViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.dictionaryapp.utils.toEntity
import com.example.dictionaryapp.viewModels.OfflineWordsViewModel


@Composable
fun SearchView(
    viewModel: WordsListViewModel = hiltViewModel(),
    offlineViewModel: OfflineWordsViewModel = hiltViewModel()
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val words by viewModel.words.collectAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Dictionary",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("Enter a word") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Button(
                    onClick = { viewModel.searchWord(textState.text.trim()) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                errorMessage != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage ?: "", color = Color.Red)
                }

                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(words) { word ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = word.word ?: "",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Button(
                                        onClick = {
                                            offlineViewModel.saveWord(word.toEntity())
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Word '${word.word}' saved offline"
                                                )
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = "Save")
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Save")
                                    }
                                }

                                // Phonetic audio button (if available)
                                val audioUrl = word.phonetics.firstOrNull { !it.audio.isNullOrBlank() }?.audio
                                if (!audioUrl.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    IconButton(onClick = {
                                        try {
                                            val mediaPlayer = MediaPlayer().apply {
                                                setDataSource(audioUrl)
                                                prepare()
                                                start()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Audio",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                word.meanings.forEach { meaning ->
                                    Spacer(modifier = Modifier.height(8.dp))

                                    meaning.partOfSpeech?.let {
                                        Text("(${it})", fontSize = 14.sp, color = Color.Gray)
                                    }

                                    val definitionsWithExample = meaning.definitions.filter { !it.definition.isNullOrBlank() }.take(3)
                                    definitionsWithExample.forEachIndexed { index, def ->
                                        Text("${index + 1}. ${def.definition}", fontSize = 16.sp)
                                        def.example?.let {
                                            Text(
                                                "Ejemplo: $it",
                                                fontSize = 14.sp,
                                                color = Color.DarkGray,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                        }
                                    }

                                    if (meaning.synonyms.isNotEmpty()) {
                                        Text("Synonyms:", fontSize = 14.sp, color = Color.Blue)
                                        meaning.synonyms.distinct().forEach {
                                            Text("- $it", fontSize = 14.sp)
                                        }
                                    }

                                    if (meaning.antonyms.isNotEmpty()) {
                                        Text("Antonyms:", fontSize = 14.sp, color = Color.Red)
                                        meaning.antonyms.distinct().forEach {
                                            Text("- $it", fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}


