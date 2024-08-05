package com.skymilk.composecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel() : ViewModel() {

    var uiState by mutableStateOf(UiState())

    fun onInfixChange(infix: String) {
        uiState = uiState.copy(infix = uiState.infix + infix)
    }

    fun clearInfixExpression() {
        uiState = uiState.copy(infix = "", result = "")
    }

    private fun onResultChange(result: String) {
        uiState = uiState.copy(result = result)
    }

    fun evaluateExpression() {
        if (uiState.infix.isNotBlank()) {
            onResultChange(Model().getResult(uiState.infix))
        }
    }

}