package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
/**
 * Created by miguelprogrammer on 10/15/15.
 */
public class PrincipalView extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public FragmentManager fragmentManager;

    LinearLayout principal_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_view);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "Welcome";

        fragmentManager = getSupportFragmentManager();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new mainScreen()).commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment myFragment = null;
        switch (position){
            case 0 : myFragment = null;
                break;
            case 1 : myFragment = new RequestPickup();
                addMenu("Request Pickup");
                mTitle = "Request Pickup";
                break;
            case 2 : myFragment = new Orders();
                addMenu("My Orders");
                mTitle = "My Orders";
                break;
            case 3 : myFragment = new Profile();
                addMenu("Profile");
                mTitle = "Profile";
                break;
            case 4 : myFragment = new Pricing();
                addMenu("Prices");
                mTitle = "Prices";
                break;
            case 5 : myFragment = new Support();
                addMenu("Support Center");
                mTitle = "Support Center";
                break;
            case 6 : myFragment = new Feedback();
                addMenu("Send Feedback");
                mTitle = "Send Feedback";
                break;
            case 7 : myFragment = new About();
                addMenu("About Us");
                mTitle = "About Us";
                break;
            case 8 : ParseUser.logOut();
                startActivity(new Intent(this, WelcomeScreen.class));
                PrincipalView.this.finish();
                break;

        }

        if(myFragment == null){

        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, myFragment)
                    .commit();
        }

    }

    public void newRequest (View view){

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new RequestPickup()).commit();

    }

    public void restoreActionBar() {
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);*/
        addMenu(String.valueOf(mTitle));
    }

    private void addMenu(String string) {

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.titles, null);
        TextView textView = (TextView) view.findViewById(R.id.mytext);
        textView.setWidth(getWindow().getWindowManager().getDefaultDisplay().getWidth());
        textView.setPadding(0, 0, 50, 0);
        textView.setGravity(Gravity.CENTER);

        Typeface segoeui = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        textView.setTypeface(segoeui);
        textView.setText(string);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

/**
 * Para ma*ana:
 * 1) Hacer el actionBar del Nav Drawer basado en el switch case del onNavigationDrawerItemSelected().
 * 2) Crear los layouts con los titulos.
 * 3) agregar los iconos del menu junto al intercambio del UpCareet.
 * 4) Unir el About, Support y Profile.
 * 5) Intentar hacer la lista del Nav Drawer con un adapter Propio.
 * */

}
