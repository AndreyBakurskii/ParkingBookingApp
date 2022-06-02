package com.example.parking.presentation.utils


fun <T> ArrayList<T>.removeElementByIndex(index: Int) : ArrayList<T>{
    this.removeAt(index)
    return this
}