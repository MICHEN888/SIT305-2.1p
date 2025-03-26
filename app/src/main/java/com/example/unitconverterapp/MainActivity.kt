package com.example.unitconverterapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var inputValue: EditText
    private lateinit var convertButton: Button
    private lateinit var resultView: TextView

    // Unit categories
    private val lengthUnits = arrayOf("Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer")
    private val weightUnits = arrayOf("Pound", "Ounce", "Ton", "Kilogram", "Gram")
    private val tempUnits = arrayOf("Celsius", "Fahrenheit", "Kelvin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI Components
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        inputValue = findViewById(R.id.inputValue)
        convertButton = findViewById(R.id.convertButton)
        resultView = findViewById(R.id.resultView)

        // All units combined
        val allUnits = lengthUnits + weightUnits + tempUnits

        // Set up the Spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, allUnits)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Handle the conversion when button is clicked
        convertButton.setOnClickListener {
            convertUnits()
        }
    }

    private fun convertUnits() {
        val fromUnit = spinnerFrom.selectedItem.toString()
        val toUnit = spinnerTo.selectedItem.toString()
        val inputText = inputValue.text.toString()

        if (inputText.isEmpty()) {
            resultView.text = "Please enter a value"
            return
        }

        val inputValue = inputText.toDouble()
        val result = convertValue(inputValue, fromUnit, toUnit)
        resultView.text = "Result: $result $toUnit"
    }

    private fun convertValue(value: Double, fromUnit: String, toUnit: String): Double {
        // Length conversions (Convert everything to cm first)
        val lengthToCm = mapOf(
            "Inch" to 2.54, "Foot" to 30.48, "Yard" to 91.44, "Mile" to 160934.0,
            "Centimeter" to 1.0, "Kilometer" to 100000.0
        )

        // Weight conversions (Convert everything to kg first)
        val weightToKg = mapOf(
            "Pound" to 0.453592, "Ounce" to 0.0283495, "Ton" to 907.185,
            "Kilogram" to 1.0, "Gram" to 0.001
        )

        return when {
            // Length conversion
            lengthToCm.containsKey(fromUnit) && lengthToCm.containsKey(toUnit) -> {
                val cmValue = value * lengthToCm[fromUnit]!!
                cmValue / lengthToCm[toUnit]!!
            }

            // Weight conversion
            weightToKg.containsKey(fromUnit) && weightToKg.containsKey(toUnit) -> {
                val kgValue = value * weightToKg[fromUnit]!!
                kgValue / weightToKg[toUnit]!!
            }

            // Temperature conversions
            fromUnit == "Celsius" && toUnit == "Fahrenheit" -> (value * 1.8) + 32
            fromUnit == "Fahrenheit" && toUnit == "Celsius" -> (value - 32) / 1.8
            fromUnit == "Celsius" && toUnit == "Kelvin" -> value + 273.15
            fromUnit == "Kelvin" && toUnit == "Celsius" -> value - 273.15
            fromUnit == "Fahrenheit" && toUnit == "Kelvin" -> ((value - 32) / 1.8) + 273.15
            fromUnit == "Kelvin" && toUnit == "Fahrenheit" -> ((value - 273.15) * 1.8) + 32

            else -> 0.0 // Invalid conversion
        }
    }
}
