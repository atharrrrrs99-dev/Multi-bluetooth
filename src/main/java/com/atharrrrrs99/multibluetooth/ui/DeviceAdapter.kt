package com.atharrrrrs99.multibluetooth.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atharrrrrs99.multibluetooth.BluetoothDeviceInfo
import com.atharrrrrs99.multibluetooth.databinding.ItemDeviceBinding

class DeviceAdapter(
    private val onConnect: (BluetoothDeviceInfo) -> Unit
) : ListAdapter<BluetoothDeviceInfo, DeviceAdapter.ViewHolder>(DeviceDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onConnect
        )
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(
        private val binding: ItemDeviceBinding,
        private val onConnect: (BluetoothDeviceInfo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(device: BluetoothDeviceInfo) {
            binding.deviceName.text = device.name
            binding.deviceAddress.text = device.address
            binding.connectBtn.setOnClickListener {
                onConnect(device)
            }
        }
    }
    
    class DeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDeviceInfo>() {
        override fun areItemsTheSame(oldItem: BluetoothDeviceInfo, newItem: BluetoothDeviceInfo): Boolean {
            return oldItem.address == newItem.address
        }
        
        override fun areContentsTheSame(oldItem: BluetoothDeviceInfo, newItem: BluetoothDeviceInfo): Boolean {
            return oldItem == newItem
        }
    }
}