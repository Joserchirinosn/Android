package cleanpress.cleanpress;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEO on 10/16/15.
 */
public class Pricing extends Fragment {
    View myView;

    ArrayList<String> prices;
    ArrayList<Integer> backs;
    ArrayList<String> names;
    TextView PriceTitle,title_price,text_wash,dry_cl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.pricing, container, false);

        prices = new ArrayList<String>();
        names = new ArrayList<String>();
        backs = new ArrayList<Integer>();


        PriceTitle = (TextView) myView.findViewById(R.id.title);
        title_price = (TextView) myView.findViewById(R.id.price_title);
        text_wash = (TextView) myView.findViewById(R.id.textWash);
        dry_cl = (TextView) myView.findViewById(R.id.dry_clean);


        names.add("Jeans");
        names.add("Dress Pants");
        names.add("Jackets");
        names.add("Shirts");
        names.add("Blouses");
        names.add("Bras");
        names.add("Underwear");
        names.add("Dresses");
        names.add("Suits");

        backs.add(R.drawable.prices__dress_pants);
        backs.add(R.drawable.prices__dress_pants);
        backs.add(R.drawable.prices__dress_pants);
        backs.add(R.drawable.prices__dress_pants);
        backs.add(R.drawable.prices__blouses);
        backs.add(R.drawable.prices__bras);
        backs.add(R.drawable.prices__dress_pants);
        backs.add(R.drawable.prices__dresses);
        backs.add(R.drawable.prices__dress_pants);


        final ListView listView = (ListView) myView.findViewById(R.id.price_list);

        listView.setDividerHeight(0);

        ParseQuery<ParseObject> c_prices = new ParseQuery<ParseObject>("Prices");
        c_prices.orderByAscending("createdAt").setSkip(1).setLimit(9);
        c_prices.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject object : list) {
                        prices.add("$" + String.format("%.2f", object.getDouble("Price")));
                    }
                    AdapterPrices adapter = new AdapterPrices(getActivity(), prices, backs, names);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(2);
                    listView.setDivider(new ColorDrawable(Color.parseColor("#ffffff")));
                    final ScrollView vw = (ScrollView) myView.findViewById(R.id.scroll);
                    vw.post(new Runnable() {
                        public void run() {
                            vw.fullScroll(View.FOCUS_UP);
                        }
                    });
                }
            }
        });




        return myView;
    }
}
