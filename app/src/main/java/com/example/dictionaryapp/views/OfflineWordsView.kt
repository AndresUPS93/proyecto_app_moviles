package com.example.dictionaryapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionaryapp.utils.toResponse
import com.example.dictionaryapp.viewModels.OfflineWordsViewModel

@Composable
fun OfflineWordsView(
    viewModel: OfflineWordsViewModel = hiltViewModel()
) {
    val offlineWordsState = viewModel.offlineWords.collectAsState()
    val offlineWords = offlineWordsState.value

    LaunchedEffect(Unit) {
        viewModel.loadOfflineWords()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Saved Words",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(offlineWords) { wordEntity ->
                val wordResponse = wordEntity.toResponse()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Row con el título y el botón de eliminar
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = wordResponse.word ?: "",
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = {
                                viewModel.deleteWord(wordEntity)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Word"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        wordResponse.meanings.firstOrNull()
                            ?.definitions?.firstOrNull()
                            ?.definition?.let {
                                Text(
                                    "Definition: $it",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                    }
                }
            }
        }
    }
}
