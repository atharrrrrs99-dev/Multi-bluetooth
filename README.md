# Multi Bluetooth Audio Controller

## Overview
Multi Bluetooth Audio is a completely offline Android application that allows users to connect and control multiple Bluetooth headsets or speakers simultaneously on a single mobile device.

## ✨ Features
- 🎵 **Connect Multiple Bluetooth Devices**: Pair and connect multiple Bluetooth audio devices at once
- 🔊 **Individual Volume Control**: Adjust volume for each connected device independently
- 🎯 **Easy Device Management**: Connect, disconnect, or manage all devices with a simple interface
- 📱 **Offline Functionality**: Works completely offline without internet connection
- 🚀 **Lightweight**: Minimal resource usage, fast performance
- 🌙 **Dark Theme UI**: Modern, eye-friendly GitHub-inspired interface

## 📋 System Requirements
- Android 5.0 (API Level 21) or higher
- Bluetooth 4.0 or later
- At least 50MB free storage

## 🚀 Installation
1. Clone the repository
2. Open in Android Studio
3. Build and run on your Android device or emulator

## 🔐 Permissions Required
- `BLUETOOTH`
- `BLUETOOTH_ADMIN`
- `BLUETOOTH_CONNECT` (Android 12+)
- `BLUETOOTH_SCAN` (Android 12+)
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `MODIFY_AUDIO_SETTINGS`

## 📖 Usage
1. **Enable Bluetooth**: Tap the "Enable" button to turn on Bluetooth
2. **View Paired Devices**: All paired devices appear under "Available Devices"
3. **Connect Device**: Tap "Connect" on any device to establish connection
4. **Manage Volume**: Use individual sliders to control volume per device
5. **Disconnect**: Tap ✕ button to disconnect a specific device, or "Disconnect All" for all

## 🏗️ Architecture
- **MVVM Pattern**: Clean separation of concerns
- **Kotlin Coroutines**: Efficient asynchronous operations
- **StateFlow**: Reactive state management
- **Native Bluetooth API**: Android's standard Bluetooth connectivity

## 📁 Project Structure
```
src/main/
├── java/com/atharrrrrs99/multibluetooth/
│   ├── BluetoothManager.kt           # Core Bluetooth logic
│   └── ui/
│       ├── MainActivity.kt           # Main activity
│       ├── DeviceAdapter.kt          # Paired devices list
│       └── ConnectedDeviceAdapter.kt # Connected devices list
└── res/
    ├── layout/                       # UI layouts
    ├── values/                       # Colors, themes, strings
    └── drawable/                     # Button shapes & backgrounds
```

## 🎨 UI Design Highlights
- **Dark Theme**: GitHub-inspired color scheme (#0d1117 background)
- **Header Section**: App title + Bluetooth status indicator
- **Control Buttons**: Enable/Disable/Disconnect All with color-coded actions
- **Connected Devices Section**: Shows active connections with individual volume controls
- **Available Devices Section**: Lists paired devices ready to connect
- **Real-time Updates**: Status changes reflected instantly
- **Rounded Cards**: Modern card-based layout with borders

## ⚙️ Technical Stack
- **Language**: Kotlin
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Build System**: Gradle
- **Architecture**: MVVM
- **Async**: Kotlin Coroutines + StateFlow
- **UI Framework**: AndroidX AppCompat

## 🔄 Key Components

### BluetoothManager.kt
- Manages Bluetooth connections
- Handles device discovery
- Controls volume for each device
- Manages connected sockets
- Exposes reactive StateFlow for UI updates

### MainActivity.kt
- Requests runtime permissions
- Observes Bluetooth state changes
- Manages RecyclerView adapters
- Handles UI button interactions

### Adapters
- **DeviceAdapter**: Displays paired devices with Connect button
- **ConnectedDeviceAdapter**: Shows active devices with volume sliders and disconnect button

## 📊 State Management
- `isBluetoothEnabled`: Tracks Bluetooth on/off status
- `pairedDevices`: List of paired Bluetooth devices
- `connectedDevices`: List of currently connected devices with their volumes

## 🚫 Known Limitations
- Audio routing to multiple devices simultaneously depends on device OS limitations
- Some devices may not support proper multi-device audio streaming
- Connection stability depends on device hardware and Bluetooth version
- Requires Android 5.0 or higher

## 🔮 Future Enhancements
- Equalizer controls per device
- Audio profile management
- Device grouping/naming
- Connection history
- Battery indicator for devices
- A2DP Sink support
- Device preferences saving
- Connection timeout configuration
- Audio routing strategies
- Device reconnection logic

## 📄 License
MIT License - Open source for personal and commercial use

## 🤝 Contributing
Feel free to fork, modify, and submit pull requests

## 💬 Support
For issues and feature requests, please open an issue in the repository

## 👨‍💻 Author
**atharrrrrs99**

---

**Made with ❤️ for seamless multi-device audio control**