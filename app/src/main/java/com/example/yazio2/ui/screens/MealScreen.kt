package com.example.yazio2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.yazio2.model.Meal
import com.example.yazio2.model.NutritionGoals
import com.example.yazio2.util.NutritionCalculator

@Composable
fun MealScreen(
    meal: Meal,
    onAddFoodClick: () -> Unit,
    onUpdateQuantity: (String, Double) -> Unit,
    onRemoveFood: (String) -> Unit,
    onGoalsUpdate: (NutritionGoals) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(meal.entries) { entry ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entry.food.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = { onRemoveFood(entry.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remover")
                        }
                    }

                    var quantityText by remember(entry.id) { mutableStateOf(entry.quantityInGrams.toString()) }
                    
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it
                            it.toDoubleOrNull()?.let { qty ->
                                onUpdateQuantity(entry.id, qty)
                            }
                        },
                        label = { Text("Quantidade (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )

                    val cals = NutritionCalculator.calculateCalories(entry.food, entry.quantityInGrams)
                    val prot = NutritionCalculator.calculateProtein(entry.food, entry.quantityInGrams)
                    val carb = NutritionCalculator.calculateCarbs(entry.food, entry.quantityInGrams)
                    val fat = NutritionCalculator.calculateFat(entry.food, entry.quantityInGrams)

                    Text("${cals.toInt()} kcal | ${prot.format(1)}g P | ${carb.format(1)}g C | ${fat.format(1)}g G")
                }
            }
        }

        item {
            Button(
                onClick = onAddFoodClick,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("+ Adicionar alimento")
            }
        }

        item {
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text("Metas da Refeição", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            var goalKcal by remember(meal.id) { mutableStateOf(meal.goals.calories.toString()) }
            var goalProt by remember(meal.id) { mutableStateOf(meal.goals.protein.toString()) }
            var goalCarb by remember(meal.id) { mutableStateOf(meal.goals.carbs.toString()) }
            var goalFat by remember(meal.id) { mutableStateOf(meal.goals.fat.toString()) }

            val updateGoals = {
                onGoalsUpdate(
                    NutritionGoals(
                        calories = goalKcal.toDoubleOrNull() ?: meal.goals.calories,
                        protein = goalProt.toDoubleOrNull() ?: meal.goals.protein,
                        carbs = goalCarb.toDoubleOrNull() ?: meal.goals.carbs,
                        fat = goalFat.toDoubleOrNull() ?: meal.goals.fat
                    )
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = goalKcal,
                    onValueChange = { goalKcal = it; updateGoals() },
                    label = { Text("Kcal") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = goalProt,
                    onValueChange = { goalProt = it; updateGoals() },
                    label = { Text("Prot(g)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                OutlinedTextField(
                    value = goalCarb,
                    onValueChange = { goalCarb = it; updateGoals() },
                    label = { Text("Carb(g)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = goalFat,
                    onValueChange = { goalFat = it; updateGoals() },
                    label = { Text("Gord(g)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            val currentKcal = NutritionCalculator.calculateMealCalories(meal)
            val currentProt = NutritionCalculator.calculateMealProtein(meal)
            val currentCarb = NutritionCalculator.calculateMealCarbs(meal)
            val currentFat = NutritionCalculator.calculateMealFat(meal)

            ProgressBar("Calorias", currentKcal, meal.goals.calories, "kcal")
            ProgressBar("Proteína", currentProt, meal.goals.protein, "g")
            ProgressBar("Carboidratos", currentCarb, meal.goals.carbs, "g")
            ProgressBar("Gordura", currentFat, meal.goals.fat, "g")
        }
    }
}

@Composable
fun ProgressBar(label: String, current: Double, goal: Double, unit: String) {
    val progress = NutritionCalculator.calculateGoalProgress(current, goal)
    val percentage = (progress * 100).toInt()
    
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text("${current.toInt()} / ${goal.toInt()} $unit ($percentage%)", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = if (progress > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
