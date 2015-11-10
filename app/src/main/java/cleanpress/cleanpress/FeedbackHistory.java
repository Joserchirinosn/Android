package cleanpress.cleanpress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by miguelprogrammer on 10/21/15.
 */
public class FeedbackHistory extends Fragment {

    ListView feedback;
    List<feedItemsHistory> items;
    FeedbackHistoryAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.feedback_history,container,false);
        feedback = (ListView) myView.findViewById(R.id.feedHistory);
        items = new ArrayList<feedItemsHistory>();

        ParseQuery<ParseObject> feedReviews = new ParseQuery<ParseObject>("FeedBack");
        feedReviews.whereEqualTo("username", ParseUser.getCurrentUser().getUsername())
                .orderByDescending("createdAt");
        feedReviews.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (final ParseObject result : list) {

                        items.add(new feedItemsHistory() {
                            {
                                date = new SimpleDateFormat("EEEE, MMMM dd, yyyy - hh:mm aaa", Locale.ENGLISH).format(result.getCreatedAt());
                                message = result.getString("Review");
                                subject = "Subject: " + result.getString("Subject");
                            }
                        });
                        feedback.invalidateViews();
                    }
                }
            }
        });

        adapter = new FeedbackHistoryAdapter(getActivity(),items);
        feedback.setAdapter(adapter);

        return myView;
    }

    class feedItemsHistory {
        public String subject;
        public String date;
        public String message;

    }

}
