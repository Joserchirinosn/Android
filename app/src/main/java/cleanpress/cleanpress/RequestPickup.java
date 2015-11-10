package cleanpress.cleanpress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by LEO on 10/16/15.
 */
public class RequestPickup extends Fragment {
    View myView;
    Button toNow,toSchedule;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.pickup_request, container, false);

        toNow = (Button) myView.findViewById(R.id.toPickup);

        toSchedule = (Button) myView.findViewById(R.id.toSchedule);

        toNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new PickupNow()).commit();
            }
        });
        toSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new PickupLater()).commit();
            }
        });
        return myView;
    }
}
