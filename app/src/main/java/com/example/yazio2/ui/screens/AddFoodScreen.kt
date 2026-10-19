package com.example.yazio2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.yazio2.model.Food

@Composable
fun AddFoodScreen(
    allFoods: List<Food>,
    onFoodAdded: (Food, Double) -> Unit,
    onCancel: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = if (searchQuery.isNotBlank()) {
        allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        emptyList()
    }

    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var quantityText by remember { mutableStateOf("100") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        if (selectedFood == null) {
            Text(
                text = "Adicionar Alimento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Pesquisar alimento") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                items(searchResults) { food ->
                    ListItem(
                        headlineContent = { Text(food.name) },
                        supportingContent = { Text("${food.caloriesPer100g.toInt()} kcal / 100g") },
                        modifier = Modifier.clickable {
                            selectedFood = food
                        }
                    )
                }
            }
        } else {
            val food = selectedFood!!
            Text(
                text = "Quantidade de ${food.name}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                label = { Text("Quantidade (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { selectedFood = null }) {
                    Text("Voltar")
                }
                Button(
                    onClick = {
                        val qty = quantityText.toDoubleOrNull() ?: 100.0
                        onFoodAdded(food, qty)
                    }
                ) {
                    Text("Adicionar")
                }
            }
        }
    }
}
