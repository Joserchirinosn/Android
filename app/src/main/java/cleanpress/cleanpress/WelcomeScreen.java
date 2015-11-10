package cleanpress.cleanpress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Vector;


public class WelcomeScreen extends AppCompatActivity {
    Bitmap oriBit,oriBit1,oriBit2,oriBit3;
    ViewPager viewPager;
    PageAdapter adapter;
    List<Fragment> fragmentList;
    RadioButton p1,p2,p3,p4;
    ImageView imgChange;
    LinearLayout backToChange;
    BitmapDrawable back1,back2,back3,back4;
    Bitmap blurredbitMap,blurredbitMap1,blurredbitMap2,blurredbitMap3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        imgChange = (ImageView) findViewById(R.id.imgToChange);

        backToChange = (LinearLayout) findViewById(R.id.backToChange);

        p1 = (RadioButton) findViewById(R.id.page1);
        p2 = (RadioButton) findViewById(R.id.page2);
        p3 = (RadioButton) findViewById(R.id.page3);
        p4 = (RadioButton) findViewById(R.id.page4);

        fragmentList = new Vector<Fragment>();
        viewPager = (ViewPager) findViewById(R.id.principalContent);

        fragmentList.add(Fragment.instantiate(this, contFragment1.class.getName()));
        fragmentList.add(Fragment.instantiate(this, contFragment2.class.getName()));
        fragmentList.add(Fragment.instantiate(this, contFragment3.class.getName()));
        fragmentList.add(Fragment.instantiate(this, contFragment4.class.getName()));

        adapter = new PageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);


        oriBit = BitmapFactory.decodeResource(getResources(), R.drawable.prices__blouses);
        oriBit1 = BitmapFactory.decodeResource(getResources(), R.drawable.prices__bras);
        oriBit2 = BitmapFactory.decodeResource(getResources(), R.drawable.prices__dress_pants);
        oriBit3 = BitmapFactory.decodeResource(getResources(), R.drawable.prices__dresses);


        blurredbitMap = BlurBuilder.blur(getApplicationContext(), oriBit);
        blurredbitMap1 = BlurBuilder.blur(getApplicationContext(), oriBit1);
        blurredbitMap2 = BlurBuilder.blur(getApplicationContext(), oriBit2);
        blurredbitMap3 = BlurBuilder.blur(getApplicationContext(), oriBit3);


        back1 = new BitmapDrawable(getResources(),blurredbitMap);
        back2 = new BitmapDrawable(getResources(),blurredbitMap1);
        back3 = new BitmapDrawable(getResources(),blurredbitMap2);
        back4 = new BitmapDrawable(getResources(),blurredbitMap3);
        backToChange.setBackground(back1);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
             public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        imgChange.setImageResource(R.drawable.prices__blouses);
                        p1.setChecked(true);
                        backToChange.setBackground(back1);
                        break;
                    case 1:
                        imgChange.setImageResource(R.drawable.prices__bras);
                        p2.setChecked(true);
                        backToChange.setBackground(back2);
                        break;
                    case 2:
                        imgChange.setImageResource(R.drawable.prices__dress_pants);
                        p3.setChecked(true);
                        backToChange.setBackground(back3);
                        break;
                    case 3:
                        imgChange.setImageResource(R.drawable.prices__dresses);
                        p4.setChecked(true);
                        backToChange.setBackground(back4);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


            }

    public void showPage(View view){
        if (p1.isChecked()){
            imgChange.setImageResource(R.drawable.prices__blouses);
            viewPager.setCurrentItem(0);
            backToChange.setBackground(back1);
            }
        if (p2.isChecked()){
            imgChange.setImageResource(R.drawable.prices__bras);
            viewPager.setCurrentItem(1);
            backToChange.setBackground(back2);
        }
        if (p3.isChecked()){
            imgChange.setImageResource(R.drawable.prices__dress_pants);
            viewPager.setCurrentItem(2);
            backToChange.setBackground(back3);
        }
        if (p4.isChecked()) {
            imgChange.setImageResource(R.drawable.prices__dresses);
            viewPager.setCurrentItem(3);
            backToChange.setBackground(back4);
        }
    }

    public void toSign (View view){
        switch (viewPager.getCurrentItem()){
            case 0:
                Intent passData = new Intent(WelcomeScreen.this,Signup.class);
                passData.putExtra("imgBack", "background1");
                startActivity(passData);
                break;
            case 1:
                Intent passData1 = new Intent(WelcomeScreen.this,Signup.class);
                passData1.putExtra("imgBack", "background2");
                startActivity(passData1);
                break;
            case 2:
                Intent passData2 = new Intent(WelcomeScreen.this,Signup.class);
                passData2.putExtra("imgBack", "background3");
                startActivity(passData2);
                break;
            case 3:
                Intent passData3 = new Intent(WelcomeScreen.this,Signup.class);
                passData3.putExtra("imgBack", "background4");
                startActivity(passData3);
                break;
        }
    }

    public void toLogin (View view){
        switch (viewPager.getCurrentItem()){
            case 0:
                Intent passData = new Intent(WelcomeScreen.this,Login.class);
                passData.putExtra("imgBack", "background1");
                startActivity(passData);
                break;
            case 1:
                Intent passData1 = new Intent(WelcomeScreen.this,Login.class);
                passData1.putExtra("imgBack", "background2");
                startActivity(passData1);

                break;
            case 2:
                Intent passData2 = new Intent(WelcomeScreen.this,Login.class);
                passData2.putExtra("imgBack", "background3");
                startActivity(passData2);
                break;
            case 3:
                Intent passData3 = new Intent(WelcomeScreen.this,Login.class);
                passData3.putExtra("imgBack", "background4");
                startActivity(passData3);
                break;
        }
    }

    public void logFB(View view){
        Toast.makeText(getApplicationContext(),"Still not working",Toast.LENGTH_SHORT).show();

    }

}

