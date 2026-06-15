package com.atharrrrrs99.multibluetooth.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atharrrrrs99.multibluetooth.BluetoothManager
import com.atharrrrrs99.multibluetooth.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var pairedAdapter: DeviceAdapter
    private lateinit var connectedAdapter: ConnectedDeviceAdapter
    
    private val PERMISSION_REQUEST_CODE = 100
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        bluetoothManager = BluetoothManager(this)
        
        setupRecyclerViews()
        setupListeners()
        observeStates()
        requestPermissions()
    }
    
    private fun setupRecyclerViews() {
        pairedAdapter = DeviceAdapter { device ->
            bluetoothManager.connectToDevice(device)
        }
        
        connectedAdapter = ConnectedDeviceAdapter(
            onDisconnect = { address -> bluetoothManager.disconnectDevice(address) },
            onVolumeChange = { address, volume -> bluetoothManager.setDeviceVolume(address, volume) }
        )
        
        binding.pairedDevicesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = pairedAdapter
        }
        
        binding.connectedDevicesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = connectedAdapter
        }
    }
    
    private fun setupListeners() {
        binding.enableBluetoothBtn.setOnClickListener {
            bluetoothManager.enableBluetooth()
        }
        
        binding.disableBluetoothBtn.setOnClickListener {
            bluetoothManager.disableBluetooth()
        }
        
        binding.disconnectAllBtn.setOnClickListener {
            bluetoothManager.disconnectAll()
        }
    }
    
    private fun observeStates() {
        lifecycleScope.launch {
            bluetoothManager.isBluetoothEnabled.collect { isEnabled ->
                updateBluetoothUI(isEnabled)
            }
        }
        
        lifecycleScope.launch {
            bluetoothManager.pairedDevices.collect { devices ->
                if (devices.isEmpty()) {
                    binding.pairedDevicesEmpty.visibility = View.VISIBLE
                    binding.pairedDevicesRv.visibility = View.GONE
                } else {
                    binding.pairedDevicesEmpty.visibility = View.GONE
                    binding.pairedDevicesRv.visibility = View.VISIBLE
                    pairedAdapter.submitList(devices)
                }
            }
        }
        
        lifecycleScope.launch {
            bluetoothManager.connectedDevices.collect { devices ->
                if (devices.isEmpty()) {
                    binding.connectedDevicesEmpty.visibility = View.VISIBLE
                    binding.connectedDevicesRv.visibility = View.GONE
                    binding.disconnectAllBtn.isEnabled = false
                } else {
                    binding.connectedDevicesEmpty.visibility = View.GONE
                    binding.connectedDevicesRv.visibility = View.VISIBLE
                    binding.disconnectAllBtn.isEnabled = true
                    connectedAdapter.submitList(devices)
                }
            }
        }
    }
    
    private fun updateBluetoothUI(isEnabled: Boolean) {
        binding.bluetoothStatus.text = if (isEnabled) "Bluetooth: ON" else "Bluetooth: OFF"
        binding.bluetoothStatus.setTextColor(
            if (isEnabled) ContextCompat.getColor(this, android.R.color.holo_green_light)
            else ContextCompat.getColor(this, android.R.color.holo_red_light)
        )
        binding.enableBluetoothBtn.isEnabled = !isEnabled
        binding.disableBluetoothBtn.isEnabled = isEnabled
    }
    
    private fun requestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        
        val needPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        
        if (needPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needPermissions, PERMISSION_REQUEST_CODE)
        } else {
            bluetoothManager.updatePairedDevices()
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            bluetoothManager.updatePairedDevices()
        }
    }
    
    override fun onResume() {
        super.onResume()
        bluetoothManager.updateBluetoothState()
        bluetoothManager.updatePairedDevices()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        bluetoothManager.disconnectAll()
    }
}