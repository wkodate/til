package com.wkodate.learnkotlin.controlflow

fun main() {
    val authors = setOf("Shakespeare", "Hemingway", "Twain")
    val writers = setOf("Twain", "Shakespeare", "Hemingway")

    // true
    println(authors == writers)
    // false
    println(authors === writers)
}

