package com.example.yazio2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.yazio2.model.Food
import com.example.yazio2.util.NutritionCalculator

@Composable
fun CompareScreen(
    compareFoods: List<Pair<Food, Double>>,
    allFoods: List<Food>,
    onAddFoodToCompare: (Food) -> Unit,
    onRemoveFromCompare: (String) -> Unit,
    onUpdateQuantity: (String, Double) -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = "Comparação de Alimentos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (showSearch) {
            var searchQuery by remember { mutableStateOf("") }
            val searchResults = allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Pesquisar alimento para comparar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            searchResults.take(5).forEach { food ->
                ListItem(
                    headlineContent = { Text(food.name) },
                    modifier = Modifier.clickable {
                        onAddFoodToCompare(food)
                        showSearch = false
                        searchQuery = ""
                    }
                )
            }
            
            TextButton(onClick = { showSearch = false }, modifier = Modifier.padding(start = 16.dp)) {
                Text("Cancelar Busca")
            }

        } else {
            Button(
                onClick = { showSearch = true },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("+ Adicionar à comparação")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (compareFoods.isNotEmpty()) {
            val scrollState = rememberScrollState()

            Row(modifier = Modifier.horizontalScroll(scrollState).padding(horizontal = 16.dp)) {
                
                // Column 1: Labels
                Column(modifier = Modifier.width(100.dp)) {
                    Text("Alimento", fontWeight = FontWeight.Bold, modifier = Modifier.height(60.dp))
                    Text("Quant. (g)", fontWeight = FontWeight.Bold, modifier = Modifier.height(60.dp))
                    Text("kcal", fontWeight = FontWeight.Bold, modifier = Modifier.height(60.dp))
                    Text("Proteína", fontWeight = FontWeight.Bold, modifier = Modifier.height(60.dp))
                    Text("Carboidrato", fontWeight = FontWeight.Bold, modifier = Modifier.height(60.dp))
                    Text("Gordura", fontWeight = FontWeight.Bold, modifier = Modifier.height(60.dp))
                }

                // Calculate maxes for highlighting
                val kcalPercents = NutritionCalculator.calculateComparisonPercentages(compareFoods) { f, q -> NutritionCalculator.calculateCalories(f, q) }
                val protPercents = NutritionCalculator.calculateComparisonPercentages(compareFoods) { f, q -> NutritionCalculator.calculateProtein(f, q) }
                val carbPercents = NutritionCalculator.calculateComparisonPercentages(compareFoods) { f, q -> NutritionCalculator.calculateCarbs(f, q) }
                val fatPercents = NutritionCalculator.calculateComparisonPercentages(compareFoods) { f, q -> NutritionCalculator.calculateFat(f, q) }

                compareFoods.forEachIndexed { index, (food, qty) ->
                    Column(
                        modifier = Modifier.width(120.dp).padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Name and remove
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(60.dp)) {
                            Text(food.name, fontWeight = FontWeight.SemiBold, maxLines = 2, modifier = Modifier.weight(1f))
                            IconButton(onClick = { onRemoveFromCompare(food.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Remover")
                            }
                        }

                        // Quantity input
                        var qtyText by remember(food.id) { mutableStateOf(qty.toString()) }
                        OutlinedTextField(
                            value = qtyText,
                            onValueChange = { 
                                qtyText = it
                                it.toDoubleOrNull()?.let { newQty ->
                                    onUpdateQuantity(food.id, newQty)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.height(60.dp).padding(4.dp)
                        )

                        // Nutrients
                        val highlightColor = MaterialTheme.colorScheme.primaryContainer
                        
                        NutrientCell(
                            value = NutritionCalculator.calculateCalories(food, qty),
                            percent = kcalPercents[index],
                            unit = "kcal",
                            isHighest = kcalPercents[index] == 100.0,
                            highlightColor = highlightColor
                        )

                        NutrientCell(
                            value = NutritionCalculator.calculateProtein(food, qty),
                            percent = protPercents[index],
                            unit = "g",
                            isHighest = protPercents[index] == 100.0,
                            highlightColor = highlightColor
                        )

                        NutrientCell(
                            value = NutritionCalculator.calculateCarbs(food, qty),
                            percent = carbPercents[index],
                            unit = "g",
                            isHighest = carbPercents[index] == 100.0,
                            highlightColor = highlightColor
                        )

                        NutrientCell(
                            value = NutritionCalculator.calculateFat(food, qty),
                            percent = fatPercents[index],
                            unit = "g",
                            isHighest = fatPercents[index] == 100.0,
                            highlightColor = highlightColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NutrientCell(value: Double, percent: Double, unit: String, isHighest: Boolean, highlightColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(if (isHighest) highlightColor else Color.Transparent)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "${value.format(1)} $unit", fontWeight = if (isHighest) FontWeight.Bold else FontWeight.Normal)
        Text(text = "${percent.toInt()}%", style = MaterialTheme.typography.bodySmall)
    }
}
