package by.lukashenko.primenumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_main.*

private const val STATERUN = "state_run"
private const val MAXNUMBER = "max_number"
private const val STARTTIME = "start_time"
private const val LISTPRIME = "list_prime"


class MainActivity : AppCompatActivity() {

    private var stateRun: Boolean = false
    private var maxNumber: Int = 0
    private var listNumberPrime = ArrayList<Int>()
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stateRun = savedInstanceState != null
        if (savedInstanceState != null) {
            maxNumber = savedInstanceState.getInt(MAXNUMBER)
        }
        setViewByStateRun()
        start.setOnClickListener {
            if (enterNumber.text.isNotEmpty()) {
                maxNumber = enterNumber.text.toString().toInt()
                startPrimeNumber()

            }
        }


    }

    private fun startPrimeNumber() {

    }

    private fun setViewByStateRun() {
        enterNumber.isEnabled = !stateRun
        start.isEnabled = !stateRun
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(STATERUN, stateRun)
        outState?.putInt(MAXNUMBER, maxNumber)
        outState?.putLong(STARTTIME, startTime)
        outState?.putIntegerArrayList(LISTPRIME, listNumberPrime)
    }
}
