package info.hannesa2.osc;

import android.os.AsyncTask;
import android.util.Log;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.content.ContentValues.TAG;

public class OSCSendMessage extends AsyncTask<Object, Void, Boolean> {
    //parm 0 = port (int)
    //parm 1 = address (String)
    //parm 2 = message (OSCMessage)
    protected Boolean doInBackground(Object... parms){
        try {
            InetAddress addr = InetAddress.getByName((String)parms[1]);
            OSCPortOut sender = new OSCPortOut(addr, (int)parms[0]);
            sender.send((OSCMessage)parms[2]);
        }
        catch (SocketException e) {
            Log.d(TAG, "doInBackground: socket exception");
            //TODO: tell user osc send port failed
        }
        catch (UnknownHostException e)
        {
            Log.d(TAG, "doInBackground: unknown host exception");
            //TODO: tell user osc send port failed
        }
        catch (IOException e)
        {
            Log.d(TAG, "doInBackground: IO exception");
            //TODO: tell user osc send port failed
        }
        return true;
    }
}
