package com.example.newcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.newcalculator.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View) {
        if(view is Button){
            if(canAddDecimal){
                binding.input.append(view.text)
                canAddDecimal = false
            }
            else
            binding.input.append(view.text)
            canAddOperation = true
        }
    }
    fun operationAction(view: View) {
        if(view is Button && canAddOperation){
            binding.input.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }


    fun allClearAction(view: View)
    {
        binding.input.text = ""
        binding.result.text = ""
    }
    fun backSpaceAction(view: View) {
        val length = binding.input.length()
        if(length > 0)
            binding.input.text = binding.input.text.subSequence(0, length - 1)
    }
    fun equalsAction(view: View) {
        binding.result.text = calculateResult()
    }

    private fun calculateResult(): String {

        val digitsOperation = digitsOperation()
        if(digitsOperation.isEmpty()) return ""

        val timesDivision = timeDivisionCalculate(digitsOperation)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>{
        var list = passedList
        while (list.contains('x') || list.contains('/')){
            list = calculateDivision(list)
        }
        return list
    }

    private fun calculateDivision(passedList: MutableList<Any>): MutableList<Any>{
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigits = passedList[i - 1] as Float
                val nextDigits = passedList[i + 1] as Float
                when(operator){
                    'x' ->
                    {
                        newList.add(prevDigits * nextDigits)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigits / nextDigits)
                        restartIndex = i + 1
                    }else -> {
                        newList.add(prevDigits)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }
    private fun digitsOperation(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.input.text){
            if(character.isDigit() || character =='.')
                currentDigit += character
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
        }
    }
