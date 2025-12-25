package com.example.map_lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.map_lab_week_10.database.Total
import com.example.map_lab_week_10.database.TotalDatabase
import com.example.map_lab_week_10.database.TotalObject
import com.example.map_lab_week_10.viewmodels.TotalViewModel
import java.util.Date // Penting: Import Date

class MainActivity : AppCompatActivity() {

    companion object {
        const val ID: Long = 1
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    private val db by lazy { prepareDatabase() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    // BONUS: Simpan data + Tanggal saat ini ketika aplikasi Pause
    override fun onPause() {
        super.onPause()
        val currentTotalValue = viewModel.total.value ?: 0
        val currentDate = Date().toString() // Ambil waktu sekarang

        // Simpan sebagai TotalObject
        val dataToSave = Total(
            id = ID,
            total = TotalObject(currentTotalValue, currentDate)
        )

        db.totalDao().update(dataToSave)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration() // PENTING: Hapus DB lama karena struktur tabel berubah
            .build()
    }

    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)

        if (totalList.isEmpty()) {
            val initialData = Total(
                id = ID,
                total = TotalObject(0, Date().toString())
            )
            db.totalDao().insert(initialData)
        } else {
            // Data lama ada: Ambil value-nya
            val savedData = totalList.first()
            viewModel.setTotal(savedData.total.value) // Perhatikan akses .total.value

            Toast.makeText(this, "Last update: ${savedData.total.date}", Toast.LENGTH_LONG).show()
        }
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total ->
            updateText(total)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text = getString(R.string.text_total, total)
    }
}