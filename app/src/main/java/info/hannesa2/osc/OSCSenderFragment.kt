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
import info.hannesa2.osc.databinding.OscInBinding
import info.hannesa2.osc.databinding.OscOutBinding

class OSCSenderFragment : Fragment() {

    private var _binding: OscOutBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = OscOutBinding.inflate(inflater, container, false)
        val myView = binding.root
        requireActivity().title = "OSC Out"

        binding.button.setOnTouchListener(OnTouchListener { v, event ->
            val oscMessage = OSCMessage("/button/1")
            if (event.action == MotionEvent.ACTION_DOWN) {
                oscMessage.addArgument(1.0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                oscMessage.addArgument(0.0)
            } else {
                return@OnTouchListener false
            }

            oscMessage.send(MainActivity.outPort, MainActivity.OSCAddress)
            true
        })

        binding.button2.setOnTouchListener(OnTouchListener { v, event ->
            val oscMessage = OSCMessage("/button/2")
            if (event.action == MotionEvent.ACTION_DOWN) {
                oscMessage.addArgument(1.0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                oscMessage.addArgument(0.0)
            } else {
                return@OnTouchListener false
            }
            oscMessage.send(MainActivity.outPort, MainActivity.OSCAddress)
            true
        })

        binding.buttonFilter.setOnTouchListener(OnTouchListener { v, event ->
            val oscMessage = OSCMessage("/filter")
            if (event.action == MotionEvent.ACTION_DOWN) {
                oscMessage.addArgument(1.0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                oscMessage.addArgument(0.0)
            } else {
                return@OnTouchListener false
            }
            oscMessage.send(MainActivity.outPort, MainActivity.OSCAddress)
            true
        })

        //Setup custom send button
        binding.sendButton.setOnClickListener { //TODO: make this more robust: crashes if not the right format and always sends strings as arguments.
            val customText = binding.customText.text.toString()
            val oscMessage = OSCMessage("/" + customText.substring(0, customText.indexOf('=', 0)))
            oscMessage.addArgument(customText.substring(customText.indexOf('=', 0), customText.length))
            oscMessage.send(MainActivity.outPort, MainActivity.OSCAddress)
        }

        return myView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
