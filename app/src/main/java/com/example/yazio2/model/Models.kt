package com.example.yazio2.model

import java.util.UUID

data class Food(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbohydratePer100g: Double,
    val fatPer100g: Double,
    val servingDescription: String? = null
)

data class FoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val food: Food,
    var quantityInGrams: Double
)

data class NutritionGoals(
    var calories: Double = 2000.0,
    var protein: Double = 150.0,
    var carbs: Double = 250.0,
    var fat: Double = 60.0
)

data class Meal(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val entries: MutableList<FoodEntry> = mutableListOf(),
    var goals: NutritionGoals = NutritionGoals()
)

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val meals: MutableList<Meal> = mutableListOf()
)
