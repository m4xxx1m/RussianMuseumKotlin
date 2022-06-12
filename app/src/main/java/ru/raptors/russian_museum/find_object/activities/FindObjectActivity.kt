package ru.raptors.russian_museum.find_object.activities

import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.ActivityFindObjectBinding
import ru.raptors.russian_museum.find_object.FindObjectPicture
import ru.raptors.russian_museum.find_object.FindObjectView
import ru.raptors.russian_museum.fragments.DialogGameFinished
import java.util.*

private const val BUNDLE_TASK_NUM_KEY = "taskNum"

class FindObjectActivity : AppCompatActivity() {
    private var taskNum = -1
    private var _binding: ActivityFindObjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            taskNum = savedInstanceState.getInt(BUNDLE_TASK_NUM_KEY, -1)
        }
        if (taskNum < 0) {
            taskNum = getTask()
        }
        _binding = ActivityFindObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inputData = resources.getStringArray(R.array.find_object_values)[taskNum]
        val findObjectData = FindObjectPicture(inputData)
        binding.title.text = findObjectData.title
        val fov = FindObjectView(this, findObjectData, taskNum)
        binding.placeHolder.addView(fov)
        binding.cross.setOnClickListener { finish() }
    }

    private fun getTask(): Int {
        val totalTask = resources.getInteger(R.integer.find_object_total)
        return Random().nextInt(totalTask)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BUNDLE_TASK_NUM_KEY, taskNum)
        super.onSaveInstanceState(outState)
    }

    fun showDialog() {
        val dialog = DialogGameFinished.newInstance(getString(R.string.you_found_object))
        dialog.show(supportFragmentManager, "dialog")
    }

    fun wrongAnswer() {
        val container: LinearLayout = binding.placeHolder
        container.setBackgroundColor(resources.getColor(R.color.red_wrong))
        val handler = Handler()
        handler.postDelayed(2000) {
            container.setBackgroundColor(resources.getColor(R.color.my_white_dark_grey))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}