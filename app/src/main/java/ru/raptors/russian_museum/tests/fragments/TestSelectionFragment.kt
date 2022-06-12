package ru.raptors.russian_museum.tests.fragments

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.tests.Test
import ru.raptors.russian_museum.tests.TestData
import ru.raptors.russian_museum.tests.activities.TestActivity

class TestSelectionFragment constructor() : Fragment() {
    private var test: Test? = null
    private var testIndex = -1
    private lateinit var card: ConstraintLayout 
    
    constructor(test: Test, index: Int) : this() {
        this.test = test
        testIndex = index
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (test == null) {
            return inflater.inflate(R.layout.fragment_test_selection, container, false)
        }
        val rootView: ViewGroup = inflater.inflate(getLayoutResource(), container, false) 
                as ViewGroup
        rootView.findViewById<View>(R.id.button_enter_test).setOnClickListener { enterTest() }
        rootView.findViewById<View>(R.id.card_test).setOnClickListener { enterTest() }
        card = rootView.findViewById(R.id.card_test)
        rootView.findViewById<TextView>(R.id.test_name).text = test?.name
        if (test?.isTestSolved == true) {
            rootView.findViewById<TextView>(R.id.result_text).text = test?.getResult()
            setPicture()
        }
        return rootView
    }
    
    private fun getLayoutResource(): Int = 
        if (test?.isTestSolved == true) {
            R.layout.fragment_test_selection_solved
        } else {
            R.layout.fragment_test_selection
        }
    
    private fun setPicture() {
        val bitmap = BitmapFactory.decodeResource(resources, test!!.getResultRes())
        card.post { 
            val width = card.width
            val height = card.height
            val croppedBitmap = getCroppedBitmap(bitmap, width, height)
            val rbd = RoundedBitmapDrawableFactory.create(resources, croppedBitmap)
            rbd.cornerRadius = 15.0f
            rbd.colorFilter = PorterDuffColorFilter(Color.argb(100, 0, 0, 0),
                PorterDuff.Mode.DARKEN)
            card.background = rbd
        }
    }
    
    private fun getCroppedBitmap(image: Bitmap, width: Int, height: Int): Bitmap {
        val scaledBitmap = Bitmap.createScaledBitmap(image, height * image.width / 
                image.height, height, false)
        val redundantWidth = (scaledBitmap.width - width) / 2
        return Bitmap.createBitmap(scaledBitmap, redundantWidth, 0, width, height)
    }
    
    private fun enterTest() {
        val intent = Intent(activity, TestActivity::class.java)
        TestData.currentTest = testIndex
        startActivity(intent)
    }
}