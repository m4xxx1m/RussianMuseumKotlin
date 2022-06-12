package ru.raptors.russian_museum.tests

object TestData {
    private var tests: ArrayList<Test>? = null
    var currentTest = -1
    
    fun getCurrentTest() = tests!![currentTest]
    
    fun getTest(index: Int) = tests!![index]
    
    fun addTest(test: Test) {
        tests?.add(test)
    }
    
    fun getSize() = tests!!.size
    
    fun initializeTests(capacity: Int) {
        tests = ArrayList(capacity)
    }
    
    fun isInitialized() = tests != null
}