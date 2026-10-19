package com.example.yazio2.data

import com.example.yazio2.model.Food
import com.example.yazio2.model.FoodEntry
import com.example.yazio2.model.Meal
import com.example.yazio2.model.NutritionGoals
import com.example.yazio2.model.Patient

object MockData {
    val foods = listOf(
        Food(name = "Arroz branco", caloriesPer100g = 130.0, proteinPer100g = 2.7, carbohydratePer100g = 28.2, fatPer100g = 0.3),
        Food(name = "Feijão", caloriesPer100g = 76.0, proteinPer100g = 4.8, carbohydratePer100g = 13.6, fatPer100g = 0.5),
        Food(name = "Frango grelhado", caloriesPer100g = 165.0, proteinPer100g = 31.0, carbohydratePer100g = 0.0, fatPer100g = 3.6),
        Food(name = "Carne bovina", caloriesPer100g = 250.0, proteinPer100g = 26.0, carbohydratePer100g = 0.0, fatPer100g = 15.0),
        Food(name = "Ovo", caloriesPer100g = 155.0, proteinPer100g = 13.0, carbohydratePer100g = 1.1, fatPer100g = 11.0),
        Food(name = "Banana", caloriesPer100g = 89.0, proteinPer100g = 1.1, carbohydratePer100g = 23.0, fatPer100g = 0.3),
        Food(name = "Maçã", caloriesPer100g = 52.0, proteinPer100g = 0.3, carbohydratePer100g = 14.0, fatPer100g = 0.2),
        Food(name = "Abacate", caloriesPer100g = 160.0, proteinPer100g = 2.0, carbohydratePer100g = 9.0, fatPer100g = 15.0),
        Food(name = "Batata doce", caloriesPer100g = 86.0, proteinPer100g = 1.6, carbohydratePer100g = 20.0, fatPer100g = 0.1),
        Food(name = "Aveia", caloriesPer100g = 389.0, proteinPer100g = 16.9, carbohydratePer100g = 66.3, fatPer100g = 6.9),
        Food(name = "Iogurte natural", caloriesPer100g = 59.0, proteinPer100g = 10.0, carbohydratePer100g = 3.6, fatPer100g = 0.4),
        Food(name = "Pão integral", caloriesPer100g = 247.0, proteinPer100g = 13.0, carbohydratePer100g = 41.0, fatPer100g = 3.4)
    )

    fun createMockPatients(): List<Patient> {
        val p1 = Patient(
            name = "Gabriel Freitas",
            meals = mutableListOf(
                Meal(
                    name = "Café da manhã",
                    entries = mutableListOf(
                        FoodEntry(food = foods.find { it.name == "Ovo" }!!, quantityInGrams = 100.0),
                        FoodEntry(food = foods.find { it.name == "Pão integral" }!!, quantityInGrams = 50.0)
                    ),
                    goals = NutritionGoals(calories = 400.0, protein = 20.0, carbs = 40.0, fat = 15.0)
                ),
                Meal(
                    name = "Almoço",
                    entries = mutableListOf(
                        FoodEntry(food = foods.find { it.name == "Arroz branco" }!!, quantityInGrams = 150.0),
                        FoodEntry(food = foods.find { it.name == "Feijão" }!!, quantityInGrams = 100.0),
                        FoodEntry(food = foods.find { it.name == "Frango grelhado" }!!, quantityInGrams = 120.0)
                    ),
                    goals = NutritionGoals(calories = 600.0, protein = 40.0, carbs = 70.0, fat = 15.0)
                ),
                Meal(
                    name = "Jantar",
                    entries = mutableListOf(
                        FoodEntry(food = foods.find { it.name == "Batata doce" }!!, quantityInGrams = 200.0),
                        FoodEntry(food = foods.find { it.name == "Carne bovina" }!!, quantityInGrams = 150.0)
                    ),
                    goals = NutritionGoals(calories = 500.0, protein = 35.0, carbs = 50.0, fat = 20.0)
                )
            )
        )
        val p2 = Patient(
            name = "João Silva",
            meals = mutableListOf(
                Meal(
                    name = "Lanche da tarde",
                    entries = mutableListOf(
                        FoodEntry(food = foods.find { it.name == "Banana" }!!, quantityInGrams = 150.0),
                        FoodEntry(food = foods.find { it.name == "Aveia" }!!, quantityInGrams = 30.0)
                    )
                )
            )
        )
        return listOf(p1, p2)
    }
}
