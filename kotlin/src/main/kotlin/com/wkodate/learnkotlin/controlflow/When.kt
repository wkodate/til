package com.wkodate.learnkotlin.controlflow

fun cases(obj: Any) {
    when (obj) {
        1 -> println("One")
        "Hello" -> println("Greeting")
        is Long -> println("Long")
        !is String -> println("Not a string")
        else -> println("Unknown")
    }
}

fun main() {
    // Greeting
    cases("Hello")
    // One
    cases(1)
    // Long
    cases(0L)
    // Unknown
    cases("hello")
}

