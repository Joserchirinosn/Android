package cleanpress.cleanpress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "vHjieX1nOgzeOy0hFcZVOecTMLSWBaubIkHW0a7X", "7W0EalICPRyz5ybOnCAm1nhUHYttZtkMv0vJKZ7U");
        ParseAnalytics.trackAppOpened(getIntent());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null){


                    ParseQuery<ParseObject> findUser = new ParseQuery<ParseObject>("User");
                    findUser.whereEqualTo("Username", currentUser.getUsername());
                    findUser.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (parseObject == null) {
                                ParseUser.logOut();
                                startActivity(new Intent(Splash.this, WelcomeScreen.class));
                                Splash.this.finish();

                            } else {
                                Intent mainintent = new Intent(Splash.this, PrincipalView.class);
                                Splash.this.startActivity(mainintent);
                                Splash.this.finish();
                            }
                        }
                    });
                } else {

                    startActivity(new Intent(Splash.this, WelcomeScreen.class));
                    Splash.this.finish();

                }
            }
        },2000);
    }

}
