package com.example.yazio2.util

import com.example.yazio2.model.Food
import com.example.yazio2.model.FoodEntry
import com.example.yazio2.model.Meal

object NutritionCalculator {
    fun calculateCalories(food: Food, grams: Double): Double = (grams / 100.0) * food.caloriesPer100g
    fun calculateProtein(food: Food, grams: Double): Double = (grams / 100.0) * food.proteinPer100g
    fun calculateCarbs(food: Food, grams: Double): Double = (grams / 100.0) * food.carbohydratePer100g
    fun calculateFat(food: Food, grams: Double): Double = (grams / 100.0) * food.fatPer100g

    fun calculateMealCalories(meal: Meal): Double = meal.entries.sumOf { calculateCalories(it.food, it.quantityInGrams) }
    fun calculateMealProtein(meal: Meal): Double = meal.entries.sumOf { calculateProtein(it.food, it.quantityInGrams) }
    fun calculateMealCarbs(meal: Meal): Double = meal.entries.sumOf { calculateCarbs(it.food, it.quantityInGrams) }
    fun calculateMealFat(meal: Meal): Double = meal.entries.sumOf { calculateFat(it.food, it.quantityInGrams) }

    fun calculateGoalProgress(current: Double, goal: Double): Float {
        if (goal == 0.0) return 0f
        return (current / goal).toFloat()
    }

    // For food comparison
    fun calculateComparisonPercentages(
        items: List<Pair<Food, Double>>,
        selector: (Food, Double) -> Double
    ): List<Double> {
        if (items.isEmpty()) return emptyList()
        val values = items.map { selector(it.first, it.second) }
        val max = values.maxOrNull() ?: 1.0
        val safeMax = if (max <= 0.0) 1.0 else max
        return values.map { (it / safeMax) * 100.0 }
    }
}
