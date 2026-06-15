package com.atharrrrrs99.multibluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

data class BluetoothDeviceInfo(
    val device: BluetoothDevice,
    val name: String,
    val address: String,
    val isConnected: Boolean = false,
    val volume: Int = 50,
    val isPaired: Boolean = false
)

class BluetoothManager(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    
    private val _connectedDevices = MutableStateFlow<List<BluetoothDeviceInfo>>(emptyList())
    val connectedDevices: StateFlow<List<BluetoothDeviceInfo>> = _connectedDevices
    
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceInfo>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDeviceInfo>> = _pairedDevices
    
    private val _isBluetoothEnabled = MutableStateFlow(false)
    val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled
    
    private val connectedSockets = mutableMapOf<String, BluetoothSocket>()
    
    init {
        updateBluetoothState()
        updatePairedDevices()
    }
    
    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
    
    fun isBluetoothSupported(): Boolean = bluetoothAdapter != null
    
    fun enableBluetooth() {
        if (hasPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
            bluetoothAdapter?.enable()
        }
    }
    
    fun disableBluetooth() {
        if (hasPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
            bluetoothAdapter?.disable()
        }
    }
    
    fun updateBluetoothState() {
        _isBluetoothEnabled.value = bluetoothAdapter?.isEnabled == true
    }
    
    fun updatePairedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return
        
        val devices = bluetoothAdapter?.bondedDevices?.map { device ->
            BluetoothDeviceInfo(
                device = device,
                name = device.name ?: "Unknown",
                address = device.address,
                isPaired = true
            )
        } ?: emptyList()
        
        _pairedDevices.value = devices
    }
    
    fun connectToDevice(deviceInfo: BluetoothDeviceInfo) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return
        
        try {
            val socket = deviceInfo.device.createRfcommSocketToServiceRecord(
                java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            )
            socket.connect()
            connectedSockets[deviceInfo.address] = socket
            
            val updated = _connectedDevices.value.toMutableList()
            updated.add(deviceInfo.copy(isConnected = true))
            _connectedDevices.value = updated
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    fun disconnectDevice(address: String) {
        try {
            connectedSockets[address]?.close()
            connectedSockets.remove(address)
            
            val updated = _connectedDevices.value.filter { it.address != address }
            _connectedDevices.value = updated
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    fun setDeviceVolume(address: String, volume: Int) {
        val updated = _connectedDevices.value.map { device ->
            if (device.address == address) {
                device.copy(volume = volume.coerceIn(0, 100))
            } else {
                device
            }
        }
        _connectedDevices.value = updated
    }
    
    fun getMasterVolume(): Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    
    fun setMasterVolume(volume: Int) {
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volume.coerceIn(0, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
            0
        )
    }
    
    fun getMaxVolume(): Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    
    fun disconnectAll() {
        connectedSockets.forEach { (_, socket) ->
            try {
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        connectedSockets.clear()
        _connectedDevices.value = emptyList()
    }
}