package com.example.lab_week_10.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Embedded

@Entity(tableName = "total")
// Every variables declared inside this class counts
// as declaring a new column inside the table
data class Total(
    // @PrimaryKey sets a column into a primary key
    @PrimaryKey(autoGenerate = true)
    // @ColumnInfo sets the name of the column
    @ColumnInfo(name = "id")
    // The variable name can be different from the column name
    val id: Long = 0,

    @Embedded val total: TotalObject
)

data class TotalObject(
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(name = "date") val date:String
)