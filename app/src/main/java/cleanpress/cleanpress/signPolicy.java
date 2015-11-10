package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by miguelprogrammer on 10/19/15.
 */
public class signPolicy extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_policy);
        addMenu();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String ref = getIntent().getExtras().getString("imgBack");

                Intent toSignup = new Intent(signPolicy.this,Signup.class);
                toSignup.putExtra("imgBack",ref);
                startActivity(toSignup);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        textView.setText("Privacy Policy");
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03e1ed")));
    }
}
