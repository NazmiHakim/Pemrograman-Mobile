package com.example.tipcalculatorcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TipCalculatorApp() }
    }
}

@Composable
fun TipCalculatorApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        TipCalculatorLayout()
    }
}

@Composable
fun TipCalculatorLayout() {
    var serviceAmount by remember { mutableStateOf("") }
    var selectedTip by remember { mutableStateOf("20%") }
    var roundUp by remember { mutableStateOf(false) }

    val tipOptions = listOf("20%", "18%", "15%")
    val tipPercent = selectedTip.dropLast(1).toDoubleOrNull() ?: 0.0
    val amount = serviceAmount.toDoubleOrNull() ?: 0.0
    val formattedTip = NumberFormat.getCurrencyInstance().format(
        calculateTip(amount, tipPercent, roundUp)
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        with(MaterialTheme.typography) {
            Text("Tip Calculator", style = headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = serviceAmount,
                onValueChange = { serviceAmount = it },
                label = { Text("Service Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TipDropdown(
                options = tipOptions,
                selected = selectedTip,
                onSelect = { selectedTip = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Round up tip?")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = roundUp, onCheckedChange = { roundUp = it })
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Tip Amount: $formattedTip", style = bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipDropdown(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tip Percentage") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun calculateTip(amount: Double, tipPercent: Double, roundUp: Boolean): Double {
    var tip = amount * (tipPercent / 100)
    if (roundUp) tip = ceil(tip)
    return tip
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculatorApp()
}