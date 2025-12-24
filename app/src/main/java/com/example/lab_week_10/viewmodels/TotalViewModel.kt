package com.example.lab_week_10.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class TotalViewModel: ViewModel() {
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    init{
        _total.postValue(0)
    }
    fun incrementTotal(): Int{
        _total.postValue(_total.value?.plus(1))
        return _total.value!!
    }

    fun setTotal(newTotal: Int){
        _total.postValue(newTotal)
    }
}