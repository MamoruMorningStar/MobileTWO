package com.example.mobiletwo.data

data class Calculation(
    val id: String,
    val total: Double,
    val people: Int,
    val tipPercent: Double = 15.0
) {
    val tipAmount: Double
        get() = total * (tipPercent / 100)
    
    val totalWithTip: Double
        get() = total + tipAmount
    
    val perPerson: Double
        get() = totalWithTip / people
}
