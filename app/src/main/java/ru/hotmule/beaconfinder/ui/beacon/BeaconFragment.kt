package ru.hotmule.beaconfinder.ui.beacon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.findbeacon.R
import com.example.findbeacon.databinding.FragmentBeaconBinding
import org.koin.android.viewmodel.ext.android.viewModel

class BeaconFragment : Fragment() {

    private val viewModel by viewModel<BeaconViewModel>()
    private val args: BeaconFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel.mac = args.mac

        val binding = DataBindingUtil.inflate<FragmentBeaconBinding>(
            inflater, R.layout.fragment_beacon, container,false)

        val context = this@BeaconFragment

        binding.apply {
            lifecycleOwner = context
            viewModel = context.viewModel
        }

        viewModel.apply {

            rssiData.observe(context, Observer {
                binding.chartRssi.data = it
            })

            distanceData.observe(context, Observer {
                binding.chartDistance.data = it
            })

            beaconSync.observe(context, Observer {
                binding.syncDate = it.date
            })
        }

        return binding.root
    }
}