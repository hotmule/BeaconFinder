package ru.hotmule.beaconfinder.ui.devices

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findbeacon.databinding.ItemDeviceBinding
import ru.hotmule.beaconfinder.data.BleDevice

class DevicesAdapter(private val clickListener: DeviceClickListener)
    : ListAdapter<BleDevice, DevicesAdapter.ViewHolder>(DevicesDiffCallback()) {

    var isBeaconsOnly = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        holder.apply {
            bind(item, clickListener, isBeaconsOnly)
            itemView.tag = item
        }
    }

    class ViewHolder private constructor(private val binding: ItemDeviceBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) = ViewHolder(
                    ItemDeviceBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
        }

        fun bind(item: BleDevice,
                 listener: DeviceClickListener,
                 isBeaconsOnly: Boolean) {

            binding.apply {
                bleDevice = item
                clickListener = listener
                beaconsOnly = isBeaconsOnly
                executePendingBindings()
            }
        }
    }
}

class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0)
                top = spaceHeight
            left =  spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}

class DeviceClickListener(val clickListener: (mac: String, isBeacon: Boolean) -> Unit) {
    fun onClick(device: BleDevice) = clickListener(device.mac, device.isBeacon)
}

private class DevicesDiffCallback : DiffUtil.ItemCallback<BleDevice>() {

    override fun areItemsTheSame(oldItem: BleDevice, newItem: BleDevice) = oldItem.mac == newItem.mac

    override fun areContentsTheSame(oldItem: BleDevice, newItem: BleDevice) = oldItem == newItem
}