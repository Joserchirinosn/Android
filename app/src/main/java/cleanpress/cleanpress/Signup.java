package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Signup extends AppCompatActivity {

    LinearLayout back;
    Bitmap oriBit,oriBit1,oriBit2,oriBit3;
    Bitmap blurredbitMap,blurredbitMap1,blurredbitMap2,blurredbitMap3;
    BitmapDrawable back1,back2,back3,back4;
    Button register;
    EditText name, lastname, phone, email, pass;
    String sName, sLastname, sPhone, sEmail,sPass;


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
        setContentView(R.layout.signup);

        back = (LinearLayout) findViewById(R.id.backTochangeSignUp);
        addMenu();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        /**
         * Initialize UI
         * */

        name = (EditText) findViewById(R.id.Name);
        lastname = (EditText) findViewById(R.id.Lastname);
        phone = (EditText) findViewById(R.id.Phone);
        email = (EditText) findViewById(R.id.Email);
        pass = (EditText) findViewById(R.id.Pass);
        register = (Button) findViewById(R.id.registerUserAcc);

        /**
         * Save data from UI into Strings
         * */

        name.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Perform action on Enter key press
                    name.clearFocus();
                    lastname.requestFocus();
                    return true;
                }
                return false;
            }
        });
        lastname.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Perform action on Enter key press
                    lastname.clearFocus();
                    phone.requestFocus();
                    return true;
                }
                return false;
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                register.setEnabled(
                        !name.getText().toString().trim().isEmpty()
                        && !lastname.getText().toString().trim().isEmpty()
                        && !email.getText().toString().trim().isEmpty()
                        && !phone.getText().toString().trim().isEmpty()
                        && !pass.getText().toString().trim().isEmpty()
                );

            }

            @Override
            public void afterTextChanged(Editable s) {

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
        textView.setText("New Account");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }

    public void registerUser (View view){

        sName = name.getText().toString();
        sLastname = lastname.getText().toString();
        sPhone = phone.getText().toString();
        sEmail = email.getText().toString();
        sPass = pass.getText().toString();

        if (sEmail.contains("@")){
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(sEmail);
            parseUser.setEmail(sEmail);
            parseUser.setPassword(sPass);
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        ParseObject userData = new ParseObject("User");
                        userData.put("Username",sEmail);
                        userData.put("FirstName",sName);
                        userData.put("LastName",sLastname);
                        userData.put("Phone",Long.valueOf(sPhone));
                        userData.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    startActivity(new Intent(Signup.this,PrincipalView.class));
                                    Signup.this.finish();
                                    finish();
                                } else {
                                    Log.e("error Obj:",e.getLocalizedMessage());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),"The email is already taken.",Toast.LENGTH_SHORT).show();
                        Log.e("error Obj:",e.getLocalizedMessage());
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(),"Please, enter a valid email",Toast.LENGTH_SHORT).show();
        }
    }

    public void regTerms (View view){
        String ref = getIntent().getExtras().getString("imgBack");

        Intent toTerms = new Intent(Signup.this,signTerms.class);
        toTerms.putExtra("imgBack",ref);
        startActivity(toTerms);
        Signup.this.finish();
    }

    public void regPolicy (View view){
        String ref = getIntent().getExtras().getString("imgBack");

        Intent toPolicy = new Intent(Signup.this,signPolicy.class);
        toPolicy.putExtra("imgBack",ref);
        startActivity(toPolicy);
        Signup.this.finish();
    }
}
