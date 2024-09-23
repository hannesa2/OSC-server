package info.hannesa2.osc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.illposed.osc.OSCMessage

class OSCSender : Fragment() {

    private lateinit var myView: View

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        myView = inflater.inflate(R.layout.osc_out, container, false)
        requireActivity().title = "OSC Out"

        val button = myView.findViewById<Button>(R.id.button)
        button.setOnTouchListener(OnTouchListener { v, event ->
            val buttonClick = OSCMessage("/button/1")
            if (event.action == MotionEvent.ACTION_DOWN) {
                buttonClick.addArgument(1.0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                buttonClick.addArgument(0.0)
            } else {
                return@OnTouchListener false
            }

            OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, buttonClick)
            true
        })

        val button2 = myView.findViewById<Button>(R.id.button2)
        button2.setOnTouchListener(OnTouchListener { v, event ->
            val oscMessage = OSCMessage("/button/2")
            if (event.action == MotionEvent.ACTION_DOWN) {
                oscMessage.addArgument(1.0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                oscMessage.addArgument(0.0)
            } else {
                return@OnTouchListener false
            }
            OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, oscMessage)
            true
        })

        val button3 = myView.findViewById<Button>(R.id.button3)
        button3.setOnTouchListener(OnTouchListener { v, event ->
            val oscMessage = OSCMessage("/button/3")
            if (event.action == MotionEvent.ACTION_DOWN) {
                oscMessage.addArgument(1.0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                oscMessage.addArgument(0.0)
            } else {
                return@OnTouchListener false
            }
            OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, oscMessage)
            true
        })

        //Setup custom send button
        val customEditText = myView.findViewById<EditText>(R.id.customText)
        val customSendButton = myView.findViewById<Button>(R.id.sendButton)
        customSendButton.setOnClickListener { //TODO: make this more robust: crashes if not the right format and always sends strings as arguments.
            val customText = customEditText.text.toString()
            val oscMessage = OSCMessage("/" + customText.substring(0, customText.indexOf('=', 0)))
            oscMessage.addArgument(customText.substring(customText.indexOf('=', 0), customText.length))
            OSCSendMessage().execute(MainActivity.outPort, MainActivity.OSCAddress, oscMessage)
        }

        return myView
    }

}
