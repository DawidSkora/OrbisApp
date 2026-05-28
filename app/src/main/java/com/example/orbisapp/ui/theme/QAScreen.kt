package com.example.orbisapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.orbisapp.model.QAPair

@Composable
fun QAScreen(viewModel: QAViewModel = viewModel()) {
    val pairs by viewModel.pairs.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var selectedId by remember { mutableStateOf<String?>(null) }
    var pairToEdit by remember { mutableStateOf<QAPair?>(null) }

    if (pairToEdit != null) {
        EditQADialog(
            pair = pairToEdit!!,
            onDismiss = { pairToEdit = null },
            onConfirm = { q, a ->
                viewModel.updatePair(pairToEdit!!.id, q, a)
                pairToEdit = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Orbis Q&A", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        error?.let {
            Text("Błąd: $it", color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(pairs) { pair ->
                QACard(
                    pair = pair,
                    isSelected = selectedId == pair.id,
                    onClick = {
                        selectedId = if (selectedId == pair.id) null else pair.id
                    },
                    onEdit = { pairToEdit = pair },
                    onDelete = { viewModel.deletePair(pair.id) }
                )
            }
        }
    }
}

@Composable
fun QACard(
    pair: QAPair,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(pair.question, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(pair.answer, style = MaterialTheme.typography.bodyMedium)

            if (isSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextButton(onClick = onEdit) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onDelete) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun EditQADialog(pair: QAPair, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var question by remember { mutableStateOf(pair.question) }
    var answer by remember { mutableStateOf(pair.answer) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj parę Q&A") },
        text = {
            Column {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Pytanie") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Odpowiedź") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(question, answer) }) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}