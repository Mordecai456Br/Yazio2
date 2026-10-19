package com.example.yazio2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.yazio2.data.MockData
import com.example.yazio2.model.Food
import com.example.yazio2.model.FoodEntry
import com.example.yazio2.model.Meal
import com.example.yazio2.model.NutritionGoals
import com.example.yazio2.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _patients = MutableStateFlow(MockData.createMockPatients())
    val patients: StateFlow<List<Patient>> = _patients.asStateFlow()

    private val _allFoods = MutableStateFlow(MockData.foods)
    val allFoods: StateFlow<List<Food>> = _allFoods.asStateFlow()

    private val _recentFoods = MutableStateFlow<List<Food>>(emptyList())
    val recentFoods: StateFlow<List<Food>> = _recentFoods.asStateFlow()

    private val _compareFoods = MutableStateFlow<List<Pair<Food, Double>>>(emptyList())
    val compareFoods: StateFlow<List<Pair<Food, Double>>> = _compareFoods.asStateFlow()

    fun getPatient(id: String): Patient? {
        return _patients.value.find { it.id == id }
    }

    fun getMeal(patientId: String, mealId: String): Meal? {
        return getPatient(patientId)?.meals?.find { it.id == mealId }
    }

    fun searchFoods(query: String): List<Food> {
        if (query.isBlank()) return emptyList()
        return _allFoods.value.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun addRecentFood(food: Food) {
        _recentFoods.update { current ->
            val mutableList = current.toMutableList()
            mutableList.remove(food)
            mutableList.add(0, food)
            mutableList.take(10) // Keep last 10
        }
    }

    fun updateMealGoals(patientId: String, mealId: String, goals: NutritionGoals) {
        _patients.update { currentList ->
            currentList.map { p ->
                if (p.id == patientId) {
                    p.copy(meals = p.meals.map { m ->
                        if (m.id == mealId) {
                            m.copy(goals = goals.copy())
                        } else m
                    }.toMutableList())
                } else p
            }
        }
    }

    fun addFoodToMeal(patientId: String, mealId: String, food: Food, quantity: Double) {
        _patients.update { currentList ->
            currentList.map { p ->
                if (p.id == patientId) {
                    p.copy(meals = p.meals.map { m ->
                        if (m.id == mealId) {
                            val newEntries = m.entries.toMutableList()
                            newEntries.add(FoodEntry(food = food, quantityInGrams = quantity))
                            m.copy(entries = newEntries)
                        } else m
                    }.toMutableList())
                } else p
            }
        }
    }

    fun removeFoodFromMeal(patientId: String, mealId: String, entryId: String) {
        _patients.update { currentList ->
            currentList.map { p ->
                if (p.id == patientId) {
                    p.copy(meals = p.meals.map { m ->
                        if (m.id == mealId) {
                            val newEntries = m.entries.filter { it.id != entryId }.toMutableList()
                            m.copy(entries = newEntries)
                        } else m
                    }.toMutableList())
                } else p
            }
        }
    }

    fun updateFoodQuantity(patientId: String, mealId: String, entryId: String, quantity: Double) {
        _patients.update { currentList ->
            currentList.map { p ->
                if (p.id == patientId) {
                    p.copy(meals = p.meals.map { m ->
                        if (m.id == mealId) {
                            val newEntries = m.entries.map { e ->
                                if (e.id == entryId) e.copy(quantityInGrams = quantity) else e
                            }.toMutableList()
                            m.copy(entries = newEntries)
                        } else m
                    }.toMutableList())
                } else p
            }
        }
    }

    // Comparison functions
    fun addToCompare(food: Food, quantity: Double = 100.0) {
        _compareFoods.update { current ->
            if (current.any { it.first.id == food.id }) return@update current
            current + (food to quantity)
        }
    }

    fun removeFromCompare(foodId: String) {
        _compareFoods.update { current ->
            current.filter { it.first.id != foodId }
        }
    }

    fun updateCompareQuantity(foodId: String, quantity: Double) {
        _compareFoods.update { current ->
            current.map {
                if (it.first.id == foodId) it.copy(second = quantity) else it
            }
        }
    }
    
    fun clearCompare() {
        _compareFoods.value = emptyList()
    }
}
