package com.example.lab_week_10

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel
import androidx.room.Room
import com.example.lab_week_10.database.Total
import java.util.Date
import com.example.lab_week_10.database.TotalObject


class MainActivity : AppCompatActivity() {
    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy{
        ViewModelProvider(this)[TotalViewModel::class.java]
    }
    private var lastUpdateDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart(){
        super.onStart()
        lastUpdateDate?.let { date ->
            if(date.isNotEmpty()){
                Toast.makeText(this, date, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val currentTotalValue = viewModel.total.value ?: 0
        val currentDateString = Date().toString()
        val totalObjectToSave = TotalObject(value = currentTotalValue, date = currentDateString)

        db.totalDao().update(Total(ID, total = totalObjectToSave))
    }

    private fun updateText(total: Int){
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel(){
        viewModel.total.observe(this, {
            updateText(it)
        })

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase{
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total_database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase(){
        val total = db.totalDao().getTotal(ID)
        if(total.isEmpty()){
            val initialObject = TotalObject(value = 0, date = "Never")
            db.totalDao().insert(Total(id = ID, total = initialObject))
            viewModel.setTotal(0)
        } else {
            val firstRecord = total.first()
            viewModel.setTotal(firstRecord.total.value)
            lastUpdateDate = firstRecord.total.date
        }
    }



    companion object{
        const val ID: Long = 1
    }
}