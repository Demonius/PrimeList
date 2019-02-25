package by.lukashenko.primenumber

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt

private const val STATERUN = "state_run"
private const val MAXNUMBER = "max_number"
private const val STARTTIME = "start_time"
private const val LISTPRIME = "list_prime"
private const val ENDTIME = "end_time"
private const val NAMETHREAD = "name"
private const val INFOCALC = "info"


class MainActivity : AppCompatActivity() {

    private var stateRun: Boolean = false
    private var maxNumber: Int = 0
    private var listNumberPrime = ArrayList<Int>()
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            maxNumber = savedInstanceState.getInt(MAXNUMBER)
            listNumberPrime = savedInstanceState.getIntegerArrayList(LISTPRIME)
            startTime = savedInstanceState.getLong(STARTTIME)
            stateRun = savedInstanceState.getBoolean(STATERUN)
            endTime = savedInstanceState.getLong(ENDTIME)
            if (savedInstanceState.containsKey(INFOCALC))
                output.text = savedInstanceState.getString(INFOCALC)
        } else {
            stateRun = false
        }
        setViewByStateRun()
        start.setOnClickListener {
            startTime = System.currentTimeMillis()
            if (enterNumber.text.isNotEmpty()) {
                maxNumber = enterNumber.text.toString().toInt()
                startPrimeNumber()
            }
        }

        clearCache.setOnClickListener {
            listNumberPrime.clear()
            output?.text = ""
            Toast.makeText(this, "cache calculation clear", Toast.LENGTH_LONG).show()
        }
    }

    private fun startPrimeNumber() {
        stateRun = true
        setViewByStateRun()
        val callculate = CollectionStartPrimeNumber()
        callculate.name = NAMETHREAD
        callculate.start()

    }

    private fun setViewByStateRun() {
        enterNumber.isEnabled = !stateRun
        start.isEnabled = !stateRun
        progress.visibility = if (stateRun) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(STATERUN, stateRun)
        outState?.putInt(MAXNUMBER, maxNumber)
        outState?.putLong(STARTTIME, startTime)
        outState?.putIntegerArrayList(LISTPRIME, listNumberPrime)
        outState?.putLong(ENDTIME, endTime)
        if (output.text.isNotEmpty())
            outState?.putString(INFOCALC, output.text.toString())
    }

    private inner class CollectionStartPrimeNumber : Thread() {
        override fun run() {
            val startTimeCalculate = System.currentTimeMillis()
            val arrayOutput = getListPrimeArray(maxNumber)
            runOnUiThread {
                endTime = System.currentTimeMillis()
                if (output != null) {
                    stateRun = false
                    setViewByStateRun()
                    output?.text = StringBuilder()
                        .appendln("max number = $maxNumber")
                        .appendln("array: $arrayOutput")
                        .appendln("time calculate ${(endTime - startTimeCalculate)} msec")
                        .append("all time ${System.currentTimeMillis() - startTime} msec")
                        .toString()
                    currentThread().interrupt()
                } else {

                }
            }

        }

        private fun getListPrimeArray(max: Int): List<Int> {
            return if (listNumberPrime.any { it >= max }) {
                val index = listNumberPrime.indexOfFirst { it >= max }
                listNumberPrime.take(index)
//                listNumberPrime.subList(0, index)
            } else {
                val arrayPrime: MutableList<Int> = ArrayList()
                val start = if (listNumberPrime.isNotEmpty()) {
                    arrayPrime.addAll(listNumberPrime)
                    listNumberPrime.last()
                } else 1
                val newThread = Thread("prime")
                newThread.run {
                    (start + 1 until max).forEach { number ->
                        if (primeStatusNumber(number)) {
                            arrayPrime.add(number)
                            listNumberPrime.add(number)
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


