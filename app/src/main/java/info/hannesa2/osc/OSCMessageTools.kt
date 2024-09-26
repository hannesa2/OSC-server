package info.hannesa2.osc

import com.illposed.osc.OSCMessage
import com.illposed.osc.OSCPortOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException

fun OSCMessage.send(outPort: Int, oscAddress: String) {
    val message = this
    CoroutineScope(Dispatchers.IO).launch {
        val address = InetAddress.getByName(oscAddress)
        val sender = OSCPortOut(address, outPort)
        try {
            Timber.d("=> ${message.arguments.firstOrNull()} $address:${outPort}")
            sender.send(message)
        } catch (e: SocketException) {
            Timber.e("$address:${outPort} $e")
            //TODO: tell user osc send port failed
        } catch (e: UnknownHostException) {
            Timber.e("$address:${outPort} $e")
            //TODO: tell user osc send port failed
        } catch (e: IOException) {
            Timber.e("$address:${outPort} $e")
            //TODO: tell user osc send port failed
        }
    }
}