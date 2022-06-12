package ru.raptors.russian_museum.tests.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.tests.Test
import ru.raptors.russian_museum.tests.TestData
import ru.raptors.russian_museum.tests.fragments.TestSelectionFragment

class TestSelectionAdapter(fragmentActivity: FragmentActivity)
        : FragmentStateAdapter(fragmentActivity) {
    init {
        val count = fragmentActivity.resources.getInteger(R.integer.tests_amount)
        if (!TestData.isInitialized()) {
            TestData.initializeTests(count)
            val infoArray = fragmentActivity.resources.getStringArray(R.array.tests_info)
            val questionsArray = fragmentActivity.resources.getStringArray(R.array.tests_questions)
            val resultsArray = fragmentActivity.resources.getStringArray(R.array.tests_results)
            val typedArray = fragmentActivity.resources.obtainTypedArray(R.array
                    .tests_results_pictures)
            for (i in 0 until count) {
                TestData.addTest(Test(infoArray[i], questionsArray[i], resultsArray[i],
                        fragmentActivity.resources.obtainTypedArray(typedArray.getResourceId(
                            i, -1))))
            }
            typedArray.recycle()
        }
    }
    
    override fun createFragment(position: Int): Fragment {
        return TestSelectionFragment(TestData.getTest(position), position)
    }
    
    override fun getItemCount(): Int {
        return TestData.getSize()
    }
}