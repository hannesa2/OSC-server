package info.hannesa2.osc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.illposed.osc.OSCListener
import com.illposed.osc.OSCPortIn
import java.net.SocketException
import java.text.DateFormat
import java.util.Date

class OSCReceiver : Fragment() {
    //UI Elements
    private lateinit var myView: View
    private lateinit var myArrayAdaptor: ArrayAdapter<String>
    private lateinit var oscInListView: ListView

    //Array that holds recieves OSC Messages
    private var messageListIn: ArrayList<String> = ArrayList()

    //OSC In port
    private lateinit var receiver: OSCPortIn

    var listener: OSCListener = OSCListener { time, message ->
        //Get the OSC message built, added Date/time for convenience
        val tempList = message.arguments
        var fullMessage = """
            ${DateFormat.getDateTimeInstance().format(Date())}
            ${message.address}, 
            """.trimIndent()
        for (argument in tempList) {
            fullMessage = fullMessage + argument.toString()
        }

        //Copy the string over to a final to add to messageListIn
        val temp = fullMessage

        //Needs to be added on the UI thread
        activity!!.runOnUiThread {
            messageListIn.add(0, temp)
            //Keep the list at 100 items
            if (messageListIn.size >= 100) messageListIn.removeAt(messageListIn.size - 1)

            //Tell the ArrayAdaptor something changed
            myArrayAdaptor.notifyDataSetChanged()
        }
        //System.out.println("Message received! " + fullMessage);
    }

    init {
        //Create the OSCPort
        try {
            receiver = OSCPortIn(MainActivity.inPort)

            //Hook up the OSC Receiver to listen to messages. Right now
            //      it's just listening to all messages with /*/* format
            //TODO: listen to more OSC messages
            receiver.addListener("/*/*", listener)
            receiver.startListening()
        } catch (e: SocketException) {
            Log.e("AAA", "Socket creation failed!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Set up the ListView to show the MessageListIn
        myView = inflater.inflate(R.layout.osc_in, container, false)
        oscInListView = myView.findViewById(R.id.oscInList)
        activity!!.title = "OSC In"
        myArrayAdaptor = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, messageListIn)
        oscInListView.adapter = myArrayAdaptor
        return myView
    }

}
