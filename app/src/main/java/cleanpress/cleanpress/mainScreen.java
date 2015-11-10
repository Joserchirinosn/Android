package cleanpress.cleanpress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by miguelprogrammer on 10/30/15.
 */
public class mainScreen extends Fragment {


    Button toPickup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_welcome_screen,container,false);

        toPickup = (Button) view.findViewById(R.id.toPickupRequest);


        toPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new RequestPickup()).commit();
            }
        });

        return view;
    }
}
