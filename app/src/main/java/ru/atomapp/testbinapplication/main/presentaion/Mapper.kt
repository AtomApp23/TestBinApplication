package ru.atomapp.testbinapplication.main.presentaion

interface Mapper<R, S> {

    fun map(source: S): R

    interface Unit<S> : Mapper<kotlin.Unit, S>
}