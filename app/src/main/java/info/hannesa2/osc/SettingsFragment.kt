package info.hannesa2.osc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText

class SettingsFragment : Fragment() {

    private lateinit var myView: View

    // Create and setup view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        myView = inflater.inflate(R.layout.settings, container, false)
        requireActivity().title = "Settings"

        // Setup Ip Address Text Field
        // TODO: If the user just clicks elsewhere, the value isn't saved. Also catch onFocusChanged!!!!
        val ipAddressEditText = myView.findViewById<EditText>(R.id.ipAddress)
        ipAddressEditText.setText(MainActivity.OSCAddress)
        ipAddressEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                MainActivity.OSCAddress = ipAddressEditText.text.toString()
                MainActivity.oscOutFragment = OSCSenderFragment()
            }
            false
        }

        // Setup Out Port Text Field
        // TODO: If the user just clicks elsewhere, the value isn't saved. Also catch onFocusChanged!!!!
        val outPortEditText = myView.findViewById<EditText>(R.id.outPort)
        outPortEditText.setText(MainActivity.outPort.toString())
        outPortEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                try {
                    MainActivity.outPort = outPortEditText.text.toString().toInt()
                    MainActivity.oscOutFragment = OSCSenderFragment()
                } catch (nfe: NumberFormatException) {
                    //Todo: add message to user here saying it must be a number
                }
            }
            false
        }

        // Setup In Port Text Field
        // TODO: If the user just clicks elsewhere, the value isn't saved. Also catch onFocusChanged!!!!
        val inPortEditText = myView.findViewById<EditText>(R.id.inPort)
        inPortEditText.setText(MainActivity.inPort.toString())
        inPortEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                try {
                    MainActivity.inPort = inPortEditText.text.toString().toInt()
                    MainActivity.oscInFragment = OSCReceiverFragment()
                } catch (nfe: NumberFormatException) {
                    //Todo: add message to user here saying it must be a number
                }
            }
            false
        }

        return myView
    }
}
