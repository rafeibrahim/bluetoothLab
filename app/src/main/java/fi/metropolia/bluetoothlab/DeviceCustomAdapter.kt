package fi.metropolia.bluetoothlab

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.w3c.dom.Text

class DeviceCustomAdapter(context: Context, val devices: MutableList<Device>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return devices.size
    }

    override fun getItem(p0: Int): Any {
        return devices[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val row: View = inflater.inflate(R.layout.device_cell, p2, false)

        val currentDevice: Device = devices[p0]

        var deviceTextView = row.findViewById(R.id.textView2) as TextView
        deviceTextView.text = currentDevice.toString()


        if(!currentDevice.isConnetable){
            deviceTextView.setTextColor(Color.LTGRAY)
        }

        return row
    }
}






