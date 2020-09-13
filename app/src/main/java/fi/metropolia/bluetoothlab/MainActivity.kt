package fi.metropolia.bluetoothlab

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.device_cell.*


class MainActivity : AppCompatActivity() {
    private  var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanResults: HashMap<String, ScanResult>? = null
    private var mScanCallback: ScanCallback? = null
    private var mBluetoothLeScanner : BluetoothLeScanner? = null
    private var mScanning: Boolean? = null
    private var mHandler: Handler? = null
    companion object {
        const val SCAN_PERIOD: Long = 3000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter

        hasPermissions()

        button.setOnClickListener{
            startScan()
        }
    }





    private fun startScan() {
        Log.d("DBG", "Scan start")
        mScanResults = HashMap()
         mScanCallback = BtleScanCallback()
         mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner

        val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build()

        val filter: List<ScanFilter>? = null

        // Stops scanning after a pre-defined scan period.

        mHandler = Handler()
        mHandler!!.postDelayed({ stopScan() }, SCAN_PERIOD)

/*        mHandler = Handler(Looper.getMainLooper()).postDelayed({
            stopScan()
        }, 3000)*/

        mScanning = true
        mBluetoothLeScanner!!.startScan(filter, settings, mScanCallback)

    }

    private fun stopScan() {
        mBluetoothLeScanner!!.stopScan(mScanCallback)

        devicesListView.adapter = DeviceCustomAdapter(this, GlobalModel.devices )
    }





    private inner class BtleScanCallback: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("DBG", "BLE Scan Failed with code $errorCode")
        }

        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceAddress = device.address
            val deviceName = result.scanRecord?.deviceName
            val signalStrength  = result.rssi


            mScanResults!![deviceAddress] = result
            Log.d("DBG", "RESULT OBJECT : $result")
            Log.d("DBG", "Device address: $deviceAddress (${result.isConnectable})")

            if (deviceName == null){
                GlobalModel.devices.add(Device("Unnammed", deviceAddress, signalStrength, result.isConnectable))
            } else {
                GlobalModel.devices.add(Device(deviceName, deviceAddress, signalStrength, result.isConnectable))
            }

        }

    }



    private fun hasPermissions(): Boolean {
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            Log.d("DBG", "No bluetooth LE capability")
            return false
        } else if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DBG", "No fine location access")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);
            return true // assuming that the user grants permission
        }
        return true
    }
}






