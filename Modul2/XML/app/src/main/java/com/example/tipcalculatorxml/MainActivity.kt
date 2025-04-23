package com.example.tipcalculatorxml

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    lateinit var costEditText: EditText
    lateinit var tipSpinner: Spinner
    lateinit var roundUpSwitch: SwitchCompat
    lateinit var tipResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        costEditText = findViewById(R.id.costOfService)
        tipSpinner = findViewById(R.id.tipOptions)
        roundUpSwitch = findViewById(R.id.roundUpSwitch)
        tipResultTextView = findViewById(R.id.tipResult)

        val tipOptions = listOf("15%", "18%", "20%")
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tipOptions
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipSpinner.adapter = it
        }

        costEditText.setOnEditorActionListener { _, _, _ ->
            calculateTip()
            false
        }

        tipSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                calculateTip()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        roundUpSwitch.setOnCheckedChangeListener { _, _ ->
            calculateTip()
        }
    }

    private fun calculateTip() {
        val cost = costEditText.text.toString().toDoubleOrNull()

        if (cost == null) {
            tipResultTextView.setText(R.string.tip_result_placeholder)
            return
        }

        val tipPercentage = when (tipSpinner.selectedItem.toString()) {
            "20%" -> 0.20
            "18%" -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage
        if (roundUpSwitch.isChecked) {
            tip = ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        tipResultTextView.text = getString(R.string.tip_result, formattedTip)
    }
}