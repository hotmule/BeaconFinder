package ru.hotmule.beaconfinder.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.findbeacon.R
import com.example.findbeacon.databinding.FragmentDevicesBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class DevicesFragment : Fragment() {

    private val viewModel by viewModel<DevicesViewModel>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentDevicesBinding>(
            inflater, R.layout.fragment_devices, container,false)

        val adapter = DevicesAdapter(
            DeviceClickListener { mac, isBeacon -> viewModel.onDeviceClick(mac, isBeacon) })

        val context = this@DevicesFragment

        binding.apply {
            lifecycleOwner = context
            viewModel = context.viewModel
            rvDevices.adapter = adapter
            rvDevices.addItemDecoration(MarginItemDecoration(
                resources.getDimension(R.dimen.recycler_view_padding).toInt()))
        }

        viewModel.apply {

            devices.observe(context, Observer {
                adapter.submitList(it)
            })

            beaconsOnly.observe(context, Observer {
                adapter.isBeaconsOnly = it
                binding.rvDevices.swapAdapter(adapter, false)
            })

            clickedDeviceMac.observe(context, Observer {
                it?.let {
                    findNavController().navigate(
                        DevicesFragmentDirections.actionDevicesFragmentToBeaconFragment(it))
                    viewModel.onDeviceNavigated()
                }
            })

            clickedDeviceIsNotBeacon.observe(context, Observer {
                it?.let {
                    Snackbar
                        .make(view!!, R.string.device_is_not_beacon, Snackbar.LENGTH_SHORT)
                        .apply { setAction(R.string.ok) { dismiss() } }
                        .show()
                    viewModel.onIsNotBeaconMessageShown()
                }
            })
        }

        return binding.root
    }
}