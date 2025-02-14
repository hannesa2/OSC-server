package info.hannesa2.osc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.illposed.osc.OSCMessage
import info.hannesa2.osc.databinding.OscOutBinding

class OSCSenderFragment : Fragment() {

    private var _binding: OscOutBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = OscOutBinding.inflate(inflater, container, false)
        val myView = binding.root
        requireActivity().title = "OSC Out"

        binding.buttonFilter.setOnTouchListener(OnTouchListener { v, event ->
            val oscMessage: OSCMessage
            if (event.action == MotionEvent.ACTION_DOWN) {
                val args: MutableList<Int> = mutableListOf()
                args.add(1)
                oscMessage = OSCMessage("/filter", args)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                val args: MutableList<Int> = mutableListOf()
                args.add(0)
                oscMessage = OSCMessage("/filter", args)
            } else {
                return@OnTouchListener false
            }
            oscMessage.send(MainActivity.outPort, MainActivity.OSCAddress)
            true
        })

        //Setup custom send button
        binding.sendButton.setOnClickListener {
            val customText = binding.customText.text.toString()
            val args: MutableList<String> = mutableListOf()
            if (customText.isEmpty()) {
                Toast.makeText(requireContext(), "Custom command is empty", Toast.LENGTH_LONG).show()
            } else if (customText.indexOf('=', 0) == -1) {
                Toast.makeText(requireContext(), "Custom command miss an '='", Toast.LENGTH_LONG).show()
            } else {
                args.add(customText.substring(customText.indexOf('=', 0), customText.length))
                val oscMessage = OSCMessage("/" + customText.substring(0, customText.indexOf('=', 0)), args)
                oscMessage.send(MainActivity.outPort, MainActivity.OSCAddress)
            }
        }

        // Setup Ip Address Text Field
        // TODO: If the user just clicks elsewhere, the value isn't saved. Also catch onFocusChanged!!!!
        binding.ipAddress.setText(MainActivity.OSCAddress)
        binding.ipAddress.doOnTextChanged { text, start, before, count ->
            MainActivity.OSCAddress = binding.ipAddress.text.toString()
        }

        // Setup Out Port Text Field
        // TODO: If the user just clicks elsewhere, the value isn't saved. Also catch onFocusChanged!!!!
        binding.outPort.setText(MainActivity.outPort.toString())
        binding.outPort.doOnTextChanged { text, start, before, count ->
            try {
                MainActivity.outPort = binding.outPort.text.toString().toInt()
            } catch (nfe: NumberFormatException) {
                //Todo: add message to user here saying it must be a number
            }
        }
        return myView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
