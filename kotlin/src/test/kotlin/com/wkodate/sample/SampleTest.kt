package com.wkodate.sample

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class SampleTest {

    val s: Sample = Sample()

    @Test
    fun equalsTest() {
        val innerMap = mapOf("e" to "E", "f" to "F")
        val map1 = mapOf("a" to "A", "b" to "B", "c" to "C", "d" to "D", "ef" to innerMap)
        val map2 = mapOf("d" to "D", "b" to "B", "a" to "A", "ef" to innerMap, "c" to "C")
        assertThat(s.sameMapWithEqualsMethod(map1, map2), `is`(true))
        assertThat(s.sameMapWithEquals2Operator(map1, map2), `is`(true))
        assertThat(s.sameMapWithEquals3Operator(map1, map2), `is`(false))
    }

}