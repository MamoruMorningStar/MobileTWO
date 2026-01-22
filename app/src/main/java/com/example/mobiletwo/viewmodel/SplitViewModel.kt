package com.example.mobiletwo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiletwo.data.Calculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import java.util.UUID

class SplitViewModel : ViewModel() {
    private val _calculations = MutableStateFlow<List<Calculation>>(emptyList())
    val calculations: StateFlow<List<Calculation>> = _calculations.asStateFlow()
    
    private val _currentTotal = MutableStateFlow<String>("")
    val currentTotal: StateFlow<String> = _currentTotal.asStateFlow()
    
    private val _currentPeople = MutableStateFlow<String>("")
    val currentPeople: StateFlow<String> = _currentPeople.asStateFlow()
    
    private val _currentTipPercent = MutableStateFlow<Double>(15.0)
    val currentTipPercent: StateFlow<Double> = _currentTipPercent.asStateFlow()
    
    val isInputValid: StateFlow<Boolean> = combine(
        _currentTotal,
        _currentPeople
    ) { total, people ->
        val totalValue = total.toDoubleOrNull() ?: return@combine false
        val peopleValue = people.toIntOrNull() ?: return@combine false
        totalValue > 0 && peopleValue > 0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    
    fun updateTotal(total: String) {
        _currentTotal.value = total
    }
    
    fun updatePeople(people: String) {
        _currentPeople.value = people
    }
    
    fun updateTipPercent(tipPercent: Double) {
        _currentTipPercent.value = tipPercent
    }
    
    fun calculate(): String {
        val total = _currentTotal.value.toDoubleOrNull() ?: return ""
        val people = _currentPeople.value.toIntOrNull() ?: return ""
        val tipPercent = _currentTipPercent.value
        
        if (total <= 0 || people <= 0) return ""
        
        val calculation = Calculation(
            id = UUID.randomUUID().toString(),
            total = total,
            people = people,
            tipPercent = tipPercent
        )
        
        val updatedList = (_calculations.value + calculation).takeLast(5)
        _calculations.value = updatedList
        
        return calculation.id
    }
    
    fun getCalculationById(id: String): Calculation? {
        return _calculations.value.find { it.id == id }
    }
    
    fun reset() {
        _currentTotal.value = ""
        _currentPeople.value = ""
        _currentTipPercent.value = 15.0
    }
}
