package com.atharrrrrs99.multibluetooth.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atharrrrrs99.multibluetooth.BluetoothDeviceInfo
import com.atharrrrrs99.multibluetooth.databinding.ItemConnectedDeviceBinding

class ConnectedDeviceAdapter(
    private val onDisconnect: (String) -> Unit,
    private val onVolumeChange: (String, Int) -> Unit
) : ListAdapter<BluetoothDeviceInfo, ConnectedDeviceAdapter.ViewHolder>(ConnectedDeviceDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemConnectedDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onDisconnect,
            onVolumeChange
        )
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(
        private val binding: ItemConnectedDeviceBinding,
        private val onDisconnect: (String) -> Unit,
        private val onVolumeChange: (String, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(device: BluetoothDeviceInfo) {
            binding.deviceName.text = device.name
            binding.deviceAddress.text = device.address
            binding.volumeSlider.progress = device.volume
            binding.volumeValue.text = "${device.volume}%"
            
            binding.volumeSlider.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            binding.volumeValue.text = "$progress%"
                            onVolumeChange(device.address, progress)
                        }
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )
            
            binding.disconnectBtn.setOnClickListener {
                onDisconnect(device.address)
            }
        }
    }
    
    class ConnectedDeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDeviceInfo>() {
        override fun areItemsTheSame(oldItem: BluetoothDeviceInfo, newItem: BluetoothDeviceInfo): Boolean {
            return oldItem.address == newItem.address
        }
        
        override fun areContentsTheSame(oldItem: BluetoothDeviceInfo, newItem: BluetoothDeviceInfo): Boolean {
            return oldItem == newItem
        }
    }
}