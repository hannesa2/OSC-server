package info.hannesa2.osc

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var fragment: Fragment? = null
    private var fragmentManager: FragmentManager? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        fragment = when (item.itemId) {
            R.id.navigation_oscOut -> {
                oscOutFragment
            }
            R.id.navigation_oscIn -> {
                oscInFragment
            }
            else -> return@OnNavigationItemSelectedListener false
        }
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.main_container, fragment!!).commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager = supportFragmentManager

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fragment = oscOutFragment
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.main_container, fragment!!).commit()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    companion object {
        var OSCAddress: String = "10.0.2.16"
        var outPort: Int = 5005
        var inPort: Int = 5005

        // The different pages, the in and out osc fragments are destroyed and recreated on settings changes
        var oscInFragment: Fragment = OSCReceiverFragment()
        var oscOutFragment: Fragment = OSCSenderFragment()
    }
}
