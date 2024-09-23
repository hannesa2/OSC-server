package info.hannesa2.osc

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import com.illposed.osc.OSCMessage
import com.illposed.osc.OSCPortOut
import java.io.IOException
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException

class OSCSendMessage : AsyncTask<Any?, Void?, Boolean>() {
    //parm 0 = port (int)
    //parm 1 = address (String)
    //parm 2 = message (OSCMessage)
    override fun doInBackground(vararg parms: Any?): Boolean {
        try {
            val address = InetAddress.getByName(parms[1] as String)
            val sender = OSCPortOut(address, parms[0] as Int)
            sender.send(parms[2] as OSCMessage)
        } catch (e: SocketException) {
            Log.d(ContentValues.TAG, "doInBackground: socket exception")
            //TODO: tell user osc send port failed
        } catch (e: UnknownHostException) {
            Log.d(ContentValues.TAG, "doInBackground: unknown host exception")
            //TODO: tell user osc send port failed
        } catch (e: IOException) {
            Log.d(ContentValues.TAG, "doInBackground: IO exception")
            //TODO: tell user osc send port failed
        }
        return true
    }

}
