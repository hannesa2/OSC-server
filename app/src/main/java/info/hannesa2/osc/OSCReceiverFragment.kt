package info.hannesa2.osc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.illposed.osc.OSCMessage
import com.illposed.osc.OSCMessageListener
import com.illposed.osc.messageselector.JavaRegexAddressMessageSelector
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector
import com.illposed.osc.transport.OSCPortIn
import info.hannesa2.osc.databinding.OscInBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.text.DateFormat
import java.util.Date

class OSCReceiverFragment : Fragment() {

    private lateinit var myView: View
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var messageListIn: ArrayList<String> = ArrayList()

    private var _binding: OscInBinding? = null
    private val binding get() = _binding!!

    //OSC In port
    private var receiver: OSCPortIn? = null

    private var listener: OSCMessageListener = OSCMessageListener { message ->
        Timber.d("<= ${message.message.address} ${message.message.arguments[0]}")
        //Get the OSC message built, added Date/time for convenience
        val tempList = message.message.arguments
        var fullMessage = """
            ${DateFormat.getDateTimeInstance().format(Date())}
            ${message.message.address}, 
            """.trimIndent()
        for (argument in tempList) {
            fullMessage += argument.toString()
        }

        //Copy the string over to a final to add to messageListIn
        val temp = fullMessage

        //Needs to be added on the UI thread
        CoroutineScope(Dispatchers.Main).launch {
            messageListIn.add(0, temp)
            //Keep the list at 100 items
            if (messageListIn.size >= 100)
                messageListIn.removeAt(messageListIn.size - 1)

            arrayAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun doListen() {
        try {
            if (receiver == null)
                receiver = OSCPortIn(MainActivity.inPort)
            else {
                receiver!!.stopListening()
                Thread.sleep(300)
                receiver = OSCPortIn(MainActivity.inPort)
            }

            //Hook up the OSC Receiver to listen to messages. Right now
            //      it's just listening to all messages with /*/* format
            //TODO: listen to more OSC messages
            receiver!!.dispatcher.addListener(JavaRegexAddressMessageSelector(".*"), listener)
//            receiver!!.dispatcher.addListener(OSCPatternAddressMessageSelector("/*/*"), listener)
            receiver!!.startListening()
            binding.ipAddress.post { binding.ipAddress.text = getIpv4HostAddress() + ":" + MainActivity.inPort }
            Timber.d("Listening on ${MainActivity.inPort}")
        } catch (e: SocketException) {
            Timber.e("Socket creation failed! $e")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = OscInBinding.inflate(inflater, container, false)
        val myView = binding.root
        requireActivity().title = "OSC In"
        arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, messageListIn)
        binding.oscInList.adapter = arrayAdapter
        doListen()
        binding.ipAddress.text = getIpv4HostAddress()
        binding.listenPort.setText(MainActivity.inPort.toString())
        binding.listenPort.setOnEditorActionListener { textView, i, keyEvent ->
            try {
                MainActivity.inPort = binding.listenPort.text.toString().toInt()
                doListen()
            } catch (nfe: NumberFormatException) {
                //Todo: add message to user here saying it must be a number
            }
            false
        }
        return myView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getIpv4HostAddress(): String? {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses
                ?.toList()
                ?.find { !it.isLoopbackAddress && it is Inet4Address }
                ?.let { return it.hostAddress }
        }
        return ""
    }

}
