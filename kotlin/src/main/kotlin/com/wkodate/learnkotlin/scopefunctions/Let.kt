package com.wkodate.learnkotlin.scopefunctions

fun main() {
    val empty = "test".let {
        it.isEmpty()
    }
    println(" is empty: $empty")

    fun printNonNull(str: String?) {
        println("Printing \"$str\":")

        str?.let {
            print("\t")
            println()
        }
    }
    printNonNull(null)
    printNonNull("my string")
}



