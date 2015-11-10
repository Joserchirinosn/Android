package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class Login extends AppCompatActivity {

    LinearLayout back;
    Bitmap oriBit,oriBit1,oriBit2,oriBit3;
    Bitmap blurredbitMap,blurredbitMap1,blurredbitMap2,blurredbitMap3;
    BitmapDrawable back1,back2,back3,back4;
    FrameLayout frameLayout;
    EditText username,password;
    String user,pass;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        back = (LinearLayout) findViewById(R.id.backTochangeLogin);
        addMenu();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        frameLayout = (FrameLayout) findViewById(R.id.popuplayoutPw);
        frameLayout.getForeground().setAlpha(0);

       String ref = getIntent().getExtras().getString("imgBack");


        if (ref.equals("background1")){
            oriBit = BitmapFactory.decodeResource(getResources(), R.drawable.prices__blouses);
            blurredbitMap = BlurBuilder.blur(getApplicationContext(), oriBit);
            back1 = new BitmapDrawable(getResources(),blurredbitMap);
            back.setBackground(back1);
        } else if(ref.equals("background2")){
            oriBit1 = BitmapFactory.decodeResource(getResources(), R.drawable.prices__bras);
            blurredbitMap1 = BlurBuilder.blur(getApplicationContext(), oriBit1);
            back2 = new BitmapDrawable(getResources(),blurredbitMap1);
            back.setBackground(back2);
        } else if(ref.equals("background3")){
            oriBit2 = BitmapFactory.decodeResource(getResources(), R.drawable.prices__dress_pants);
            blurredbitMap2 = BlurBuilder.blur(getApplicationContext(), oriBit2);
            back3 = new BitmapDrawable(getResources(),blurredbitMap2);
            back.setBackground(back3);
        } else if(ref.equals("background4")){
            oriBit3 = BitmapFactory.decodeResource(getResources(), R.drawable.prices__dresses);
            blurredbitMap3 = BlurBuilder.blur(getApplicationContext(), oriBit3);
            back4 = new BitmapDrawable(getResources(),blurredbitMap3);
            back.setBackground(back4);
        }

        username = (EditText) findViewById(R.id.logEmail);
        password = (EditText) findViewById(R.id.logPass);

    }

    public void showPopupForgot (View view) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog = inflater.inflate(R.layout.popup_forgotpw, null);
        final AlertDialog infoDialog = new AlertDialog.Builder(Login.this)
                .setView(dialog)
                .create();
        infoDialog.show();
        TextView message = (TextView) dialog.findViewById(R.id.Message);
        final EditText pwForgot = (EditText) dialog.findViewById(R.id.pwForgot);
        Button SendPw = (Button) dialog.findViewById(R.id.AcceptSend);
        Button cancelPw = (Button) dialog.findViewById(R.id.CancelSend);

        frameLayout.getForeground().setAlpha(220);

        cancelPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
                frameLayout.getForeground().setAlpha(0);
                pwForgot.setText("");
            }
        });


        SendPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = pwForgot.getText().toString();
                ParseUser.requestPasswordResetInBackground(sEmail, new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            infoDialog.dismiss();
                            frameLayout.getForeground().setAlpha(0);
                            Toast.makeText(getApplicationContext(), "You will receive an email soon", Toast.LENGTH_LONG).show();

                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            Toast.makeText(getApplicationContext(), "The email is invalid or not registered", Toast.LENGTH_LONG).show();
                            infoDialog.dismiss();
                            frameLayout.getForeground().setAlpha(0);
                        }
                    }
                });
            }
        });






    }

    private void addMenu() {

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.titles, null);
        TextView textView = (TextView) view.findViewById(R.id.mytext);
        textView.setWidth(getWindow().getWindowManager().getDefaultDisplay().getWidth());
        textView.setPadding(0, 0, 50, 0);
        textView.setGravity(Gravity.CENTER);

        //Typeface segoeui = Typeface.createFromAsset(getAssets(),"fonts/segoeui.ttf");
        //textView.setTypeface(segoeui);
        textView.setText("Login");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    public void logInApp(View view){
        if (username.getText().toString().trim().isEmpty()){
            if (password.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(),"Please enter an email",Toast.LENGTH_SHORT).show();

        } else {
            user = username.getText().toString();
            pass = password.getText().toString();

            ParseUser.logInInBackground(user, pass, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null){
                        startActivity(new Intent(Login.this,PrincipalView.class));
                        Login.this.finish();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


}