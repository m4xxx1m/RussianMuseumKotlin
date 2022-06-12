package ru.raptors.russian_museum.tests.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.ActivityTestSelectionBinding
import ru.raptors.russian_museum.tests.adapters.TestSelectionAdapter

class TestSelectionActivity : AppCompatActivity() {
    private var _binding: ActivityTestSelectionBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: TestSelectionAdapter
    private lateinit var tabLayout: TabLayout
    private var currentItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTestSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener { finish() }
        
        val ageItems = arrayOf(getString(R.string.age_6_8), getString(R.string.age_8_13),
                getString(R.string.age_14_and_more))
        val ageAdapter = ArrayAdapter(this, R.layout.test_age_selection_item, ageItems)
        binding.autoComplete.setAdapter(ageAdapter)
        
        viewPager = binding.pager
        (viewPager.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    override fun onStart() {
        super.onStart()
        pagerAdapter = TestSelectionAdapter(this)
        viewPager.adapter = pagerAdapter
        tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        viewPager.currentItem = currentItem
    }

    override fun onStop() {
        super.onStop()
        currentItem = viewPager.currentItem
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}