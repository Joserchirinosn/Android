package cleanpress.cleanpress;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by AlejandroProgrammer on 10/16/15.
 */
public class About extends Fragment {
    TextView toTerms,toPolicy;
    WebView myVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.about, container, false);
        toTerms = (TextView) myView.findViewById(R.id.terms_btn);
        toPolicy = (TextView) myView.findViewById(R.id.wp_btn);
        myVideo = (WebView) myView.findViewById(R.id.webView);
        myVideo.getSettings().setJavaScriptEnabled(true);
        myVideo.getSettings().setPluginState(WebSettings.PluginState.ON);
        myVideo.getSettings().setBuiltInZoomControls(false);
        myVideo.getSettings().setSupportZoom(false);
        myVideo.loadUrl("https://www.youtube.com/embed/VbsZaa0ZZzA");
        myVideo.setWebChromeClient(new WebChromeClient());

        toTerms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container,new aboutTerms())
                        .commit();
            }
        });

        toPolicy.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container,new aboutPolicy())
                        .commit();
            }
        });

        return myView;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(myVideo, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {

        } catch(NoSuchMethodException nsme) {

        } catch(InvocationTargetException ite) {

        } catch (IllegalAccessException iae) {

        }
    }
}
