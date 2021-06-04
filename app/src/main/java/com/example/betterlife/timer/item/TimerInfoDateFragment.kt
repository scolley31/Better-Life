package com.example.betterlife.timer.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.betterlife.R
import com.example.betterlife.data.Plan
import com.example.betterlife.databinding.FragmentTimerInfoBinding
import com.example.betterlife.databinding.FragmentTimerInfoDateBinding
import com.example.betterlife.ext.getVmFactory
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class TimerInfoDateFragment (private val plan: Plan): Fragment() {

    lateinit var binding: FragmentTimerInfoDateBinding
    private val viewModel by viewModels<TimerInfoDateViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel._info.value = plan
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerInfoDateBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.info.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "info = ${viewModel.info.value}")
            viewModel.getRank()
        })

        viewModel.completedTest.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("test", "completedTest = ${viewModel.completedTest.value}")
        })

        viewModel.leaveTimer.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().popBackStack()
            }
        })

        viewModel.forPrintChat.observe(viewLifecycleOwner, Observer {
            it?.let {
                setBarChart()
            }
        })

        viewModel.rank.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("test", "rank = ${viewModel.rank.value}")
            }
        })

        return binding.root
    }

    fun setBarChart() {

//        val xvalues = ArrayList<String>()
//        xvalues.add("1")
//        xvalues.add("2")
//
//        val barentries = ArrayList<BarEntry>()
//        barentries.add(BarEntry(1f,0f))
//        barentries.add(BarEntry(2f,4f))
////
//        val bardateset = BarDataSet(barentries,"First")
//        val data = BarData(bardateset)
//        binding.BarChartDaily.data = data
//        binding.BarChartDaily.setBackgroundColor(resources.getColor(R.color.colorAccent))

        val dataSet = BarDataSet(viewModel.entries, "每日執行時間")
        Log.d("test","dataSet = $dataSet")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black_3f3a3a)

        // Controlling X axis
        val xAxis = binding.BarChartDaily.xAxis
        // Set the xAxis position to bottom. Default is top
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //Customizing x axis value
        xAxis.granularity = 1f // minimum axis-step (interval) is 1

        // Controlling right side of y axis
        val yAxisRight = binding.BarChartDaily.axisRight
        yAxisRight.isEnabled = false

        // Controlling left side of y axis
        val yAxisLeft = binding.BarChartDaily.axisLeft
        yAxisLeft.granularity = 1f

        // setup limit line
        val dailyTarget = viewModel.info.value?.dailyTarget
        val LimitLine = LimitLine(dailyTarget!!.toFloat(),"")

        // Setting Data
        val data = BarData(dataSet)
        binding.BarChartDaily.data = data
        binding.BarChartDaily.axisLeft.addLimitLine(LimitLine)
        binding.BarChartDaily.invalidate()
        binding.BarChartDaily.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(viewModel.label)
            labelCount = viewModel.xTitle.size
            setDrawLabels(true)
            setDrawGridLines(false)
        }
        binding.BarChartDaily.description.text = ""
        binding.BarChartDaily.setDrawGridBackground(false)
    }
}