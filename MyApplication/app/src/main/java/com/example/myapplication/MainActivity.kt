package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isDigitsOnly
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {

    private var input: Float? = null
    private var previousInput: Float? = null
    private var symbol: String? = null
    private var zero: Boolean? = false
    private var virgule: Boolean = false


    companion object {
        private val INPUT_BUTTONS = listOf(
                listOf("Ce", "C"),
                listOf("1", "2", "3", "/"),
                listOf("4", "5", "6", "*"),
                listOf("7", "8", "9", "-"),
                listOf("0", ".", "=", "+")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCells(findViewById(R.id.calculator_input_container_line1), 0)
        addCells(findViewById(R.id.calculator_input_container_line2), 1)
        addCells(findViewById(R.id.calculator_input_container_line3), 2)
        addCells(findViewById(R.id.calculator_input_container_line4), 3)
        addCells(findViewById(R.id.calculator_input_container_line5), 4)
    }

    private fun addCells(linearLayout: LinearLayout, position: Int) {
        for (x in INPUT_BUTTONS[position].indices) {
            linearLayout.addView(
                    TextView(
                            ContextThemeWrapper(this, R.style.CalculatorInputButton)
                    ).apply {
                        text = INPUT_BUTTONS[position][x]
                        setOnClickListener { onCellClicked(this.text.toString()) }
                    },
                    LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    )
            )
        }
    }

    private fun onCellClicked(value: String) {
        when {
            value.isNum() -> {
                if(virgule){

                    input = (value.toFloat() / 10) + previousInput!!
                    updateDisplayContainer(input.toString())
                    virgule = false
                }else{
                    input = value.toFloat()
                    updateDisplayContainer(value)
                }
            }
            value == "."-> onPointClicked()
            value == "Ce"-> onCeClicked()
            value == "C" -> onCClicked()
            value == "=" -> onEqualsClicked()
            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)
        }


    }

    private fun onPointClicked() {
        updateDisplayContainer(".")
        virgule = true
        previousInput = input
    }

    private fun onCClicked() {
        if(symbol != null && input != null){
            input = null
            updateDisplayContainer("")
        }
        else if(symbol != null && input == null){
            symbol = null
            input = previousInput
            previousInput = null
            updateDisplayContainer(input.toString())
        }
        else if(symbol == null && input != null) {
            input = null
            previousInput = null
            symbol = null
            updateDisplayContainer("")
        }

    }

    private fun onCeClicked() {
        input = null
        previousInput = null
        symbol = null
        updateDisplayContainer("")
    }

    private fun updateDisplayContainer(value: Any) {
        if(zero == true){
            findViewById<TextView>(R.id.calculator_display_container).text = "ERROR"
            zero = false
        }else{
            findViewById<TextView>(R.id.calculator_display_container).text = value.toString()

        }

    }

    fun String.isNum(): Boolean {
        return length == 1 && isDigitsOnly()
    }

    private fun onSymbolClicked(symbol: String) {
        this.symbol = symbol
        previousInput = input
        input = null
    }

    private fun onEqualsClicked() {
        if (input == null || previousInput == null || symbol == null) {
            return
        }

        updateDisplayContainer(when (symbol) {
            "+" -> {
                input!! + previousInput!!

            }
            "-" -> {

                previousInput!! - input!!
            }
            "*" -> {
                input!! * previousInput!!

            }
            "/" ->
                if (previousInput == 0.0f || input == 0.0f) {
                    zero = true

                } else {
                    previousInput!! / input!!

                }
            else -> "ERROR"
        })
        when (symbol) {
            "+" -> {
                input = input!! + previousInput!!

            }
            "-" -> {

                input = previousInput!! - input!!
            }
            "*" -> {

                input = input!! * previousInput!!
            }
            "/" ->
                if (previousInput == 0.0f || input == 0.0f) {
                    zero = true

                } else {

                    input = previousInput!! / input!!
                }
            else -> "ERROR"
        }
        previousInput = null
        symbol = null
    }

}