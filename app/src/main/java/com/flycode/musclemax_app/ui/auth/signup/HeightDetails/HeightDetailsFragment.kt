package com.flycode.musclemax_app.ui.auth.signup.HeightDetails

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.databinding.HeightDetailsBinding
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.wx.wheelview.adapter.ArrayWheelAdapter
import com.wx.wheelview.widget.WheelView

import java.util.Arrays

import javax.inject.Inject

import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_height_details.*


class HeightDetailsFragment : DaggerFragment() {

    @Inject lateinit var superViewModel: SignUpViewModel

    private lateinit var binding: HeightDetailsBinding
    private var heightLimits: IntArray? = null

    companion object {

        fun newInstance(): HeightDetailsFragment {
            val fragment = HeightDetailsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_height_details, container, false)
        binding.superViewModel = superViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        btn_next.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.DetailsOverviewFragment)
        }
        btn_middle.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.DetailsOverviewFragment)
        }
        btn_prev.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.WeightDetailsFragment)
        }
    }
    private fun init() {
        setUpHeightUnitsSpinner()
    }

    private fun setUpHeightUnitsSpinner() {
        val data = Arrays.asList(*resources.getStringArray(R.array.height_units))
        heightLimits = resources.getIntArray(R.array.height_units_limits)

        binding.heightUnitsPicker.setWheelAdapter(ArrayWheelAdapter(context)) // text data source
        binding.heightUnitsPicker.setSkin(WheelView.Skin.None)
        binding.heightUnitsPicker.setLoop(false)
        binding.heightUnitsPicker.setWheelSize(data.size)
        binding.heightUnitsPicker.setSelection(0)
        binding.heightUnitsPicker.setWheelData(data)

        val style = WheelView.WheelViewStyle()
        style.selectedTextSize = 20
        style.textSize = 12
        style.selectedTextColor = Color.WHITE
        style.textColor = Color.LTGRAY
        style.backgroundColor = Color.TRANSPARENT
        binding.heightUnitsPicker.setStyle(style)

        binding.heightUnitsPicker.setOnWheelItemSelectedListener(WheelView.OnWheelItemSelectedListener<String> { position, o ->
            superViewModel.heightUnits = o
            binding.heightScalePicker.setMaximumAcceptedSize(heightLimits!![position])
        })
    }
}// Required empty public constructor
