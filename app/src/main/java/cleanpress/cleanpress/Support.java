package cleanpress.cleanpress;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by AlejandroProgrammer on 10/16/15.
 */
public class Support extends Fragment {
    View myView;
    Button twitterSupport,callSupport,fbSupport,cleanWeb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.support, container, false);
        callSupport = (Button) myView.findViewById(R.id.call_support);
        twitterSupport = (Button) myView.findViewById(R.id.twitter_support);
        fbSupport = (Button) myView.findViewById(R.id.fb_support);
        cleanWeb = (Button) myView.findViewById(R.id.clean_web);


        callSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "tel:9105862450";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(number));
                startActivity(intent);
            }
        });
        fbSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String face = "https://www.facebook.com/pages/Clean-Press-Inc/1465514210413452?fref=ts";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(face));
                startActivity(intent);
            }
        });
        twitterSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twit = "https://twitter.com/cleanpressinc";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twit));
                startActivity(intent);
            }
        });
        cleanWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clweb = "http://www.getcleanpress.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(clweb));
                startActivity(intent);
            }
        });

        return myView;
    }

}
