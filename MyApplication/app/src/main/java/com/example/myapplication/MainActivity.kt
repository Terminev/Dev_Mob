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
    private var zero: Boolean = false
    private  var decim: Boolean = false
    private var previousDecim: Int = 0
    private var previousSymbol: Float? = null
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
                if(decim == true){
                    input = (value.toFloat() / 10) + previousInput!!
                    updateDisplayContainer(input.toString())
                    decim = false
                    previousDecim = 1
                }else if(previousDecim != 0) {
                    input = value.toFloat()
                    updateDisplayContainer( value!!)
                    previousDecim!! + 1
                }else{
                    input = value.toFloat()
                    updateDisplayContainer(input.toString())
                }
            }
            value == "Ce"->{
                onCeClicked()
            }
            value == "." -> {
                onPointClicked()
            }
            value == "C"->{
                onCclicked()
            }
            value == "=" -> onEqualsClicked()
            listOf("/", "*", "-", "+").contains(value) -> onSymbolClicked(value)
        }
    }
    private fun onCclicked() {
        if(decim == true){
            decim = false
            input = previousInput
            previousInput = null
            updateDisplayContainer(input.toString())
        }else if (symbol != null && input != null && previousDecim == 0 ){
            input = null
            updateDisplayContainer("symbole")
        }else if(symbol != null && input != null && previousDecim == 0){
        }else if(symbol != null && input == null  && previousDecim == 0 ){
            symbol = null
            input = previousInput
            previousInput = null
            updateDisplayContainer(input.toString())
        }else if(symbol == null && input != null && previousDecim == 0){
            input = null
            symbol = null
            previousInput = null
            updateDisplayContainer("")
        }else if(symbol == null && input != null && previousDecim != 0){
            if(previousDecim ==  1 ){
                previousDecim!! - 1
                input = null
                decim = true
                updateDisplayContainer(".")
            }else{
                previousDecim!! - 1
                input = previousInput
                previousInput = null
                updateDisplayContainer(input.toString())
            }
        }
    }

    private fun onCeClicked(){
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
    private fun onPointClicked(){
        updateDisplayContainer(".")
        decim = true
        previousInput = input
        input = null
    }
    private fun String.isNum(): Boolean {
        return length == 1 && isDigitsOnly()
    }

    private fun onSymbolClicked(symbol: String) {
        this.symbol = symbol
        previousSymbol = input
        previousInput = input
        input = null
    }

    private fun onEqualsClicked() {
        if (input == null || previousInput == null || symbol == null) {
            return
        }

        updateDisplayContainer(when (symbol) {
            "+" ->   input!! +  previousSymbol!!
            "-" ->  previousSymbol!! - input!!
            "*" ->  input!! *  previousSymbol!!
            "/" ->
                if ( previousSymbol == 0.0f || input == 0.0f){
                    zero = true
                } else{
                    previousSymbol!! / input!!
                }
            else -> "ERROR"
        })
        when (symbol) {
            "-" -> input = previousSymbol!! - input!!
            "*" -> input = input!! * previousSymbol!!
            "+" -> input =  input!! + previousSymbol!!
            "/" ->
                if (previousSymbol == 0.0f || input == 0.0f){
                    input = input
                } else{
                    input =  previousSymbol!! / input!!
                }
        }
        previousInput = null
        symbol = null
        previousSymbol = null
    }

}