package cleanpress.cleanpress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Miguel on 10/16/15.
 */
public class Feedback extends Fragment {
    View myView;
    ListView feedback;
    List<feedItems> items;
    FeedbackAdapter adapter;
    Button showFeedHistory,submitFeed;
    EditText subjectFeed,messageFeed;
    String submitSubject,submitMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.feedback, container, false);
        feedback = (ListView) myView.findViewById(R.id.feed_history);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        messageFeed = (EditText) myView.findViewById(R.id.messageFeed);
        subjectFeed = (EditText) myView.findViewById(R.id.subjectFeed);

        items = new ArrayList<Feedback.feedItems>();
        showFeedHistory = (Button) myView.findViewById(R.id.showHistory);
        showFeedHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container,new FeedbackHistory())
                        .commit();
            }
        });
        submitFeed = (Button) myView.findViewById(R.id.submitFeed);
        submitFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (subjectFeed.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Please write a valid subject", Toast.LENGTH_SHORT).show();
                } else {

                    if (messageFeed.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Please write a valid review", Toast.LENGTH_SHORT).show();
                    } else {
                        sendReview();
                    }
                }

            }
        });

        ParseQuery<ParseObject> feedReviews = new ParseQuery<ParseObject>("FeedBack");
        feedReviews.whereEqualTo("username",ParseUser.getCurrentUser().getUsername())
                .orderByDescending("createdAt")
                .setLimit(5);
        feedReviews.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (final ParseObject result : list) {

                        items.add(new feedItems() {
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

        adapter = new FeedbackAdapter(getActivity(),items);
        feedback.setAdapter(adapter);
        return myView;
    }

    private void sendReview(){

        submitMessage = messageFeed.getText().toString();
        submitSubject = subjectFeed.getText().toString();

        ParseObject review = new ParseObject("FeedBack");
        review.put("username", ParseUser.getCurrentUser().getUsername());
        review.put("Subject", submitSubject);
        review.put("Review", submitMessage);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    items.add(0, new feedItems() {
                        {
                            message = submitMessage;
                            subject = submitSubject;
                            date = new SimpleDateFormat("EEEE, MMMM dd, yyyy - hh:mm aaa", Locale.ENGLISH).format(new Date());

                        }
                    });
                    feedback.invalidateViews();
                    subjectFeed.setText("");
                    messageFeed.setText("");

                }
            }
        });
    }

    class feedItems {
        public String subject;
        public String date;
        public String message;

    }

}
