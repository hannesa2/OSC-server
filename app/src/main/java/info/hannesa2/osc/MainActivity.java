package info.hannesa2.osc;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;

    // The OSC settings the app uses (set in settings class)
    public static String OSCAddress = "192.168.1.1";
    public static int outPort = 8001;
    public static int inPort = 8000;

    // The different pages, the in and out osc fragments are destroyed and recreated on settings changes
    public static Fragment oscInFragment = new OSCReceiver();
    public static Fragment oscOutFragment = new OSCSender();
    public static Fragment settingsFragment = new Settings();

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.navigation_oscOut) {
                fragment = oscOutFragment;
            } else if (item.getItemId() == R.id.navigation_oscIn) {
                fragment = oscInFragment;
            } else if (item.getItemId() == R.id.navigation_settings) {
                fragment = settingsFragment;
            } else
                return false;
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = oscOutFragment;
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
