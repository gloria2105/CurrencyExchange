package com.tu.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tu.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CurrencyConverter(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CurrencyConverter(modifier: Modifier = Modifier) {
    val fromCurrency by remember { mutableStateOf("USD") }
    val toCurrency by remember { mutableStateOf("EUR") }
    var amount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf<Double?>(null) }

    val currencies = listOf("USD", "EUR", "JPY", "GBP")

    Column(modifier = modifier.padding(16.dp)) {
        Text("Conversor de Divisas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.example_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )


        CurrencyDropdown(label = "De", selectedCurrency = fromCurrency, currencies = currencies)
        Spacer(modifier = Modifier.height(8.dp))

        CurrencyDropdown(label = "A", selectedCurrency = toCurrency, currencies = currencies)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val rate = getExchangeRate(fromCurrency, toCurrency)
            convertedAmount = if (rate != null && amount.isNotEmpty()) {
                amount.toDoubleOrNull()?.let { it * rate }
            } else {
                null
            }
        }) {
            Text("Convertir")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (convertedAmount != null) {
                "$amount $fromCurrency es equivalente a $convertedAmount $toCurrency"
            } else {
                "No se pudo obtener la tasa de cambio o la cantidad es inválida"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CurrencyDropdown(label: String, selectedCurrency: String, currencies: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(selectedCurrency)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                currencies.forEach { _ ->
                    dropdownMenuItem()
                }
            }
        }
    }
}

fun dropdownMenuItem() {
    TODO("Not yet implemented")
}


fun getExchangeRate(fromCurrency: String, toCurrency: String): Double? {

    val exchangeRates = mapOf(
        "USD" to mapOf(
            "EUR" to 0.85,
            "JPY" to 110.0,
            "GBP" to 0.75
        ),
        "EUR" to mapOf(
            "USD" to 1.18,
            "JPY" to 129.0,
            "GBP" to 0.88
        ),
        "JPY" to mapOf(
            "USD" to 0.0091,
            "EUR" to 0.0078,
            "GBP" to 0.0069
        ),
        "GBP" to mapOf(
            "USD" to 1.33,
            "EUR" to 1.14,
            "JPY" to 151.0
        )

    )

    return exchangeRates[fromCurrency]?.get(toCurrency)
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterPreview() {
    MyApplicationTheme {
        CurrencyConverter()
    }
}