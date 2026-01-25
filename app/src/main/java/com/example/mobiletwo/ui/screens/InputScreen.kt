package com.example.mobiletwo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiletwo.viewmodel.SplitViewModel

@Composable
fun InputScreen(
    viewModel: SplitViewModel,
    onCalculateClick: (String) -> Unit
) {
    val total by viewModel.currentTotal.collectAsState()
    val people by viewModel.currentPeople.collectAsState()
    val isValid by viewModel.isInputValid.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Введите данные",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = total,
            onValueChange = { viewModel.updateTotal(it) },
            label = { Text("Total (сумма счёта)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )
        
        OutlinedTextField(
            value = people,
            onValueChange = { viewModel.updatePeople(it) },
            label = { Text("People (количество людей)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                val calcId = viewModel.calculate()
                if (calcId.isNotEmpty()) {
                    onCalculateClick(calcId)
                }
            },
            enabled = isValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Calculate",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
