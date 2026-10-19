package com.example.yazio2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yazio2.model.Food
import com.example.yazio2.model.Patient
import com.example.yazio2.ui.components.FoodSearchBar
import com.example.yazio2.ui.components.RecentlyViewedSection

@Composable
fun HomeScreen(
    patients: List<Patient>,
    recentFoods: List<Food>,
    allFoods: List<Food>,
    onPatientClick: (Patient) -> Unit,
    onFoodClick: (Food) -> Unit,
    onNavigateToCompare: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val isSearching = searchQuery.isNotBlank()
    val searchResults = if (isSearching) {
        allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        FoodSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        if (isSearching) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults) { food ->
                    ListItem(
                        headlineContent = { Text(food.name) },
                        supportingContent = { Text("${food.caloriesPer100g.toInt()} kcal") },
                        modifier = Modifier.clickable {
                            onFoodClick(food)
                            searchQuery = ""
                        }
                    )
                }
            }
        } else {
            RecentlyViewedSection(
                recentFoods = recentFoods,
                onFoodClick = onFoodClick,
                onSearchRequest = {}
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pacientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = onNavigateToCompare) {
                    Text("Comparar Alimentos")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(patients) { patient ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPatientClick(patient) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = patient.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${patient.meals.size} refeições cadastradas",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
