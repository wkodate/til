package com.wkodate.learnkotlin.collections

fun main() {
    val numbers1 = listOf(1, -2, 3, -4, 5, -6)
    val anyNegative = numbers1.any { it < 0 }
    val anyGT6 = numbers1.any { it > 6 }
    println(anyNegative)
    println(anyGT6)

    val numbers = listOf(1, -2, 3, -4, 5, -6)
    val allEven = numbers.all { it % 2 == 0 }
    val allLess6 = numbers.all { it < 6 }
    // false
    println(allEven)
    // true
    println(allLess6)

    val numbers3 = listOf(1, -2, 3, -4, 5, -6)
    val noneEven = numbers3.none { it % 2 == 1 }
    val noneLess6 = numbers3.none { it > 6 }
    // false
    println(noneEven)
    // true
    println(noneLess6)
}

