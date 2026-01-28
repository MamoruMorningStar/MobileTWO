package com.example.mobiletwo.data

import android.content.Context
import androidx.core.content.edit

class CalculationRepository(context: Context) {
    private val prefs = context.getSharedPreferences("calculations_prefs", Context.MODE_PRIVATE)
    private val KEY_HISTORY = "history_list"

    fun getHistory(): List<Calculation> {
        val data = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            data.split("|").mapNotNull { entry ->
                if (entry.isBlank()) null else {
                    val parts = entry.split(";")
                    if (parts.size >= 4) {
                        Calculation(
                            id = parts[0],
                            total = parts[1].toDouble(),
                            people = parts[2].toInt(),
                            tipPercent = parts[3].toDouble()
                        )
                    } else null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveHistory(history: List<Calculation>) {
        val data = history.joinToString("|") {
            "${it.id};${it.total};${it.people};${it.tipPercent}"
        }
        prefs.edit { putString(KEY_HISTORY, data) }
    }
}
