package com.droom.uniqueidentity

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.droom.uniqueidentity.UniqueDeviceID.getUniqueId
import kotlinx.android.synthetic.main.activity_main.*
import java.net.Inet4Address
import java.net.NetworkInterface


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUniqueId(getUniqueId() ?: "")
        val ipAddress = getLocalIpAddress()
        text_ip_address.text = if (!TextUtils.isEmpty(ipAddress))
            "IP Address:\n$ipAddress"
        else
            "IP Address:\n" + "Not Found"
    }

    private fun setUniqueId(id: String) {
        text_unique_identity.text = "Unique Id:\n${id}"
    }


    fun getLocalIpAddress(): String? {
        try {
//            https://stackoverflow.com/a/12449111/7406230
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

}