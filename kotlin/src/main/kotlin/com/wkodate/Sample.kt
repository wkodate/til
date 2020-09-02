package com.wkodate

class Sample {

    fun sameMapWithEqualsMethod(map1: Map<*, *>, map2: Map<*, *>): Boolean {
        return map1.equals(map2)
    }

    fun sameMapWithEquals2Operator(map1: Map<*, *>, map2: Map<*, *>): Boolean {
        return map1 == map2
    }

    fun sameMapWithEquals3Operator(map1: Map<*, *>, map2: Map<*, *>): Boolean {
        return map1 === map2
    }

}