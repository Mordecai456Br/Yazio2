package com.example.yazio2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yazio2.ui.screens.*
import com.example.yazio2.ui.theme.Yazio2Theme
import com.example.yazio2.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Yazio2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val navController = rememberNavController()

                    val patients by viewModel.patients.collectAsState()
                    val allFoods by viewModel.allFoods.collectAsState()
                    val recentFoods by viewModel.recentFoods.collectAsState()
                    val compareFoods by viewModel.compareFoods.collectAsState()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                patients = patients,
                                recentFoods = recentFoods,
                                allFoods = allFoods,
                                onPatientClick = { p -> navController.navigate("patient/${p.id}") },
                                onFoodClick = { f ->
                                    viewModel.addRecentFood(f)
                                    navController.navigate("foodDetail/${f.id}")
                                },
                                onNavigateToCompare = { navController.navigate("compare") }
                            )
                        }

                        composable("patient/{patientId}") { backStackEntry ->
                            val pId = backStackEntry.arguments?.getString("patientId")
                            val patient = patients.find { it.id == pId }
                            if (patient != null) {
                                PatientScreen(
                                    patient = patient,
                                    recentFoods = recentFoods,
                                    allFoods = allFoods,
                                    onFoodClick = { f ->
                                    viewModel.addRecentFood(f)
                                    navController.navigate("foodDetail/${f.id}")
                                },
                                    onMealClick = { mId -> navController.navigate("meal/${pId}/${mId}") }
                                )
                            }
                        }

                        composable("meal/{patientId}/{mealId}") { backStackEntry ->
                            val pId = backStackEntry.arguments?.getString("patientId")
                            val mId = backStackEntry.arguments?.getString("mealId")
                            val patient = patients.find { it.id == pId }
                            val meal = patient?.meals?.find { it.id == mId }

                            if (patient != null && meal != null) {
                                MealScreen(
                                    meal = meal,
                                    onAddFoodClick = { navController.navigate("addFood/${pId}/${mId}") },
                                    onUpdateQuantity = { entryId, qty -> viewModel.updateFoodQuantity(patient.id, meal.id, entryId, qty) },
                                    onRemoveFood = { entryId -> viewModel.removeFoodFromMeal(patient.id, meal.id, entryId) },
                                    onGoalsUpdate = { goals -> viewModel.updateMealGoals(patient.id, meal.id, goals) }
                                )
                            }
                        }

                        composable("addFood/{patientId}/{mealId}") { backStackEntry ->
                            val pId = backStackEntry.arguments?.getString("patientId")
                            val mId = backStackEntry.arguments?.getString("mealId")
                            if (pId != null && mId != null) {
                                AddFoodScreen(
                                    allFoods = allFoods,
                                    onFoodAdded = { food, qty ->
                                        viewModel.addFoodToMeal(pId, mId, food, qty)
                                        viewModel.addRecentFood(food)
                                        navController.popBackStack()
                                    },
                                    onCancel = { navController.popBackStack() }
                                )
                            }
                        }

                        composable("compare") {
                            CompareScreen(
                                compareFoods = compareFoods,
                                allFoods = allFoods,
                                onAddFoodToCompare = { f -> viewModel.addToCompare(f) },
                                onRemoveFromCompare = { fId -> viewModel.removeFromCompare(fId) },
                                onUpdateQuantity = { fId, qty -> viewModel.updateCompareQuantity(fId, qty) }
                            )
                        }

                        composable("foodDetail/{foodId}") { backStackEntry ->
                            val fId = backStackEntry.arguments?.getString("foodId")
                            val food = allFoods.find { it.id == fId }
                            if (food != null) {
                                FoodDetailScreen(
                                    food = food,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
