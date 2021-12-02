package com.droom.uniqueidentity

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdvertisingTask(this).execute()
        val ipAddress = getLocalIpAddress()
        text_ip_address.text = if (!TextUtils.isEmpty(ipAddress))
            "IP Address:\n$ipAddress"
        else
            "IP Address:\n" + "Not Found"
    }

    private fun setUniqueId(s: String?) {
        text_unique_identity.text = if (!TextUtils.isEmpty(s)) {
            "Unique Id:\n$s"
        } else {
            "Unique Id:\n" + "Not Found"
        }
    }

    private

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
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }


    inner class AdvertisingTask(val context: Context) :
        AsyncTask<Void?, Void?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(s: String?) {
            setUniqueId(s)
        }


        override fun doInBackground(vararg params: Void?): String? {
            var idInfo: AdvertisingIdClient.Info? = null
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var advertId: String? = null
            try {
                advertId = idInfo?.id
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return advertId
        }
    }

}