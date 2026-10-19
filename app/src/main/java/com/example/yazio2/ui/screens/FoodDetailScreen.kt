package com.example.yazio2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.yazio2.model.Food
import com.example.yazio2.util.NutritionCalculator

@Composable
fun FoodDetailScreen(
    food: Food,
    onBack: () -> Unit
) {
    var quantityText by remember { mutableStateOf("100") }
    val quantity = quantityText.toDoubleOrNull() ?: 0.0

    val currentKcal = NutritionCalculator.calculateCalories(food, quantity)
    val currentProt = NutritionCalculator.calculateProtein(food, quantity)
    val currentCarb = NutritionCalculator.calculateCarbs(food, quantity)
    val currentFat = NutritionCalculator.calculateFat(food, quantity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = food.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = quantityText,
            onValueChange = { quantityText = it },
            label = { Text("Quantidade desejada (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = "Valores Nutricionais", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                NutrientRow("Calorias", "${currentKcal.toInt()} kcal")
                NutrientRow("Proteína", "${currentProt.format(1)} g")
                NutrientRow("Carboidrato", "${currentCarb.format(1)} g")
                NutrientRow("Gordura", "${currentFat.format(1)} g")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}

@Composable
fun NutrientRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}
