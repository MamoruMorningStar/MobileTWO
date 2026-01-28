package com.example.mobiletwo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobiletwo.data.Calculation
import com.example.mobiletwo.data.CalculationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class SplitViewModel(
    private val repository: CalculationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplitUiState())
    val uiState: StateFlow<SplitUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val history = repository.getHistory()
        _uiState.update { it.copy(calculations = history) }
    }

    fun onEvent(event: SplitEvent) {
        when (event) {
            is SplitEvent.UpdateTotal -> _uiState.update { it.copy(total = event.total) }
            is SplitEvent.UpdatePersons -> _uiState.update { it.copy(persons = event.persons) }
            is SplitEvent.UpdateTipPercent -> _uiState.update { it.copy(tipPercent = event.tipPercent) }
            SplitEvent.Calculate -> calculate()
            SplitEvent.Reset -> reset()
        }
    }

    private fun calculate() {
        val total = _uiState.value.total.toDoubleOrNull() ?: return
        val people = _uiState.value.persons.toIntOrNull() ?: return
        val tipPercent = _uiState.value.tipPercent.toDoubleOrNull() ?: 15.0

        if (total <= 0 || people <= 0) return

        val calculation = Calculation(
            id = UUID.randomUUID().toString(),
            total = total,
            people = people,
            tipPercent = tipPercent
        )

        val newHistory = (_uiState.value.calculations + calculation).takeLast(5)
        repository.saveHistory(newHistory)

        _uiState.update {
            it.copy(
                calculations = newHistory,
                calculationIdForNavigation = calculation.id
            )
        }
    }

    fun getCalculationById(id: String): Calculation? {
        return _uiState.value.calculations.find { it.id == id }
    }

    private fun reset() {
        _uiState.update { 
            SplitUiState(calculations = it.calculations)
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(calculationIdForNavigation = null) }
    }

    companion object {
        fun provideFactory(
            repository: CalculationRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SplitViewModel(repository)
            }
        }
    }
}
