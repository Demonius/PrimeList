package by.lukashenko.primenumber

import android.view.View
import android.widget.Button
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

class MainViewModel : ViewModel() {
    val maxNumber = ObservableField<String>("")
    val arrayListPrime = ObservableArrayList<Int>()
    val arrayListHistory = ObservableField<String>("")
    val runCalculate = ObservableField<Boolean>(false)
    private var startTime: Long = 0

    fun onClickListener(view: View) {
        if (view is Button) {
            when (view.id) {
                R.id.start -> {
                    if (maxNumber.get()!!.isNotEmpty()) {
                        startTime = System.currentTimeMillis()
                        startCalculationPrime()
                    }
                }
                R.id.clearCache -> {
                    arrayListPrime.clear()
                }
            }
        }
    }

    private fun startCalculationPrime() {
        val calculate = CollectionStartPrimeNumber()
        runCalculate.set(true)
        calculate.start()
    }

    private inner class CollectionStartPrimeNumber : Thread() {
        override fun run() {
            val startTimeCalculate = System.currentTimeMillis()
            val arrayOutput = getListPrimeArray(maxNumber.get()!!.toInt())
            val endTime = System.currentTimeMillis()
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            val output = StringBuilder()
                .appendln("time start: ${sdf.format(Date(startTime))}")
                .appendln("max number = ${maxNumber.get()}")
                .appendln("array: $arrayOutput")
                .appendln("time calculate ${(endTime - startTimeCalculate)} msec")
                .append("all time ${System.currentTimeMillis() - startTime} msec")
                .toString()
            arrayListHistory.set(output)
            currentThread().interrupt()
            runCalculate.set(false)
        }

        private fun getListPrimeArray(max: Int): List<Int> {
            return if (arrayListPrime.any { it >= max }) {
                val index = arrayListPrime.indexOfFirst { it >= max }
                arrayListPrime.take(index)
            } else {
                val arrayPrime: MutableList<Int> = ArrayList()
                val start = if (arrayListPrime.isNotEmpty()) {
                    arrayPrime.addAll(arrayListPrime)
                    arrayListPrime.last()
                } else 1
                val newThread = Thread("prime")
                newThread.run {
                    (start + 1 until max).forEach { number ->
                        if (primeStatusNumber(number)) {
                            arrayPrime.add(number)
                            arrayListPrime.add(number)
                        }
                    }
                    start()
                }
                arrayPrime

            }
        }

        private fun primeStatusNumber(number: Int): Boolean {
            var state = true
            val newThread = Thread("state")
            newThread.run {
                (2..sqrt(number.toFloat()).toInt()).forEach { del ->
                    if (number % del == 0) {
                        state = false
                        newThread.interrupt()
                        return@forEach

                    }
                }
                start()
            }
            return state
        }
    }

}