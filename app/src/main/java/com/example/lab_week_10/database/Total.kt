package com.example.map_lab_week_10.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Buat Data Class baru untuk menampung Value + Date
data class TotalObject(
    @ColumnInfo(name = "value")
    val value: Int,

    @ColumnInfo(name = "date")
    val date: String
)

// 2. Update Entity Total untuk menggunakan @Embedded
@Entity(tableName = "total")
data class Total(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    // @Embedded akan membongkar TotalObject menjadi kolom 'value' dan 'date' di tabel
    @Embedded
    val total: TotalObject
)