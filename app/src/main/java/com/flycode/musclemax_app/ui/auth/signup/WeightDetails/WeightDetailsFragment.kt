package com.flycode.musclemax_app.ui.auth.signup.WeightDetails

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.databinding.WeightDetailsBinding
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.wx.wheelview.adapter.ArrayWheelAdapter
import com.wx.wheelview.widget.WheelView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_weight_details.*
import java.util.*
import javax.inject.Inject

class WeightDetailsFragment : DaggerFragment() {

    @Inject lateinit var superViewModel: SignUpViewModel
    private lateinit var binding: WeightDetailsBinding
    private var weightLimits: IntArray? = null

    companion object {

        fun newInstance(): WeightDetailsFragment {
            val fragment = WeightDetailsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_weight_details, container, false)
        binding.superViewModel = superViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        btn_next.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.HeightDetailsFragment)
        }
        btn_middle.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.HeightDetailsFragment)
        }
        btn_prev.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.LocationDetailsFragment)
        }
    }

    private fun init() {
        //CONFIGURE UI
        setUpWeightUnitsSpinner()
    }

    private fun setUpWeightUnitsSpinner() {
        val data = Arrays.asList(*resources.getStringArray(R.array.weight_units))
        weightLimits = resources.getIntArray(R.array.weight_units_limits)


        binding.weightUnitsPicker.setWheelAdapter(ArrayWheelAdapter(context)) // text data source
        binding.weightUnitsPicker.setSkin(WheelView.Skin.None)
        binding.weightUnitsPicker.setLoop(false)
        binding.weightUnitsPicker.setWheelSize(data.size)
        binding.weightUnitsPicker.setSelection(0)
        binding.weightUnitsPicker.setWheelData(data)

        val style = WheelView.WheelViewStyle()
        style.selectedTextSize = 20
        style.textSize = 12
        style.selectedTextColor = Color.WHITE
        style.textColor = Color.LTGRAY
        style.backgroundColor = Color.TRANSPARENT
        binding.weightUnitsPicker.setStyle(style)

        binding.weightUnitsPicker.setOnWheelItemSelectedListener(object : WheelView.OnWheelItemSelectedListener<String> {
            override fun onItemSelected(position: Int, o: String) {
                superViewModel.weightUnits = o
                binding.weightScalePicker.setMaximumAcceptedSize(weightLimits!![position])

            }
        })
    }


}
