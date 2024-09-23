package info.hannesa2.osc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.illposed.osc.*;

public class OSCSender extends Fragment {

    //UI Elements
    View myView;
    Activity myActivity;

    public OSCSender() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.osc_out, container, false);
        myActivity = (Activity) myView.getContext();
        myActivity.setTitle("OSC Out");

        //Setup Button 1
        final Button button = myView.findViewById(R.id.button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OSCMessage buttonClick = new OSCMessage("/button/1");
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonClick.addArgument(1.0);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    buttonClick.addArgument(0.0);
                }
                else {
                    return false;
                }

                new OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, buttonClick);
                return true;
            }
        });

        //Setup Button 2
        final Button button2 = myView.findViewById(R.id.button2);
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OSCMessage buttonClick = new OSCMessage("/button/2");
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonClick.addArgument(1.0);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    buttonClick.addArgument(0.0);
                }
                else {
                    return false;
                }
                new OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, buttonClick);
                return true;
            }
        });

        //Setup Button 3
        final Button button3 = myView.findViewById(R.id.button3);
        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OSCMessage buttonClick = new OSCMessage("/button/3");
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonClick.addArgument(1.0);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    buttonClick.addArgument(0.0);
                }
                else {
                    return false;
                }
                new OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, buttonClick);
                return true;
            }
        });

        //Setup custom send button
        final EditText customEditText = myView.findViewById(R.id.customText);
        Button customSendButton = myView.findViewById(R.id.sendButton);
        customSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: make this more robust: crashes if not the right format and always sends strings as arguments.
                String customText = customEditText.getText().toString();
                OSCMessage sendButtonClick = new OSCMessage("/" + customText.substring(0, customText.indexOf('=', 0)));
                sendButtonClick.addArgument(customText.substring(customText.indexOf('=', 0), customText.length()));
                new OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, sendButtonClick);
            }
        });

        return myView;
    }
}
