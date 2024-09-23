package info.hannesa2.osc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.illposed.osc.*;

import java.net.SocketException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OSCReceiver extends Fragment {

    //UI Elements
    View myView;
    Activity myActivity;
    ArrayAdapter<String> myArrayAdaptor;
    ListView oscInListView;

    //Array that holds recieves OSC Messages
    ArrayList<String> messageListIn = new ArrayList<>();

    //OSC In port
    OSCPortIn receiver;

    public OSCReceiver() {
        //Create the OSCPort
        try {
            receiver = new OSCPortIn(MainActivity.inPort);
        } catch (SocketException e) {
            //Todo: Tell user that it failed
            System.out.println("Socket creation failed!");
            return;
        }

        //Hook up the OSC Receiver to listen to messages. Right now
        //      it's just listening to all messages with /*/* format
        //TODO: listen to more OSC messages
        receiver.addListener("/*/*", listener);
        receiver.startListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set up the ListView to show the MessageListIn
        myView = inflater.inflate(R.layout.osc_in, container, false);
        oscInListView = myView.findViewById(R.id.oscInList);
        myActivity = (Activity) myView.getContext();
        myActivity.setTitle("OSC In");
        myArrayAdaptor = new ArrayAdapter<String>(myActivity, android.R.layout.simple_list_item_1, messageListIn);
        oscInListView.setAdapter(myArrayAdaptor);
        return myView;
    }

    OSCListener listener = new OSCListener() {
        public void acceptMessage(java.util.Date time, OSCMessage message) {
            //Get the OSC message built, added Date/time for convenience
            List tempList = message.getArguments();
            String fullMessage = DateFormat.getDateTimeInstance().format(new Date()) + "\n" + message.getAddress() + ", ";
            for (final Object argument : tempList) {
                fullMessage = fullMessage.concat(String.valueOf(argument));
            }

            //Copy the string over to a final to add to messageListIn
            final String temp = fullMessage;

            //Needs to be added on the UI thread
            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageListIn.add(0, temp);

                    //Keep the list at 100 items
                    if (messageListIn.size() >= 100)
                        messageListIn.remove(messageListIn.size() - 1);

                    //Tell the ArrayAdaptor something changed
                    myArrayAdaptor.notifyDataSetChanged();
                }
            });
            //System.out.println("Message received! " + fullMessage);
        }
    };
}
