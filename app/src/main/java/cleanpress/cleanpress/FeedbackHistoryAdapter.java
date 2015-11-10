package cleanpress.cleanpress;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by miguelprogrammer on 10/21/15.
 */
public class FeedbackHistoryAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<FeedbackHistory.feedItemsHistory> items;

    public FeedbackHistoryAdapter(Activity context, List<FeedbackHistory.feedItemsHistory> items){
        super();
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FeedbackHistory.feedItemsHistory item = items.get(position);

        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.feedback_rows,null);

        TextView feed_msg,feed_date,feed_subj;

        feed_msg = (TextView) view.findViewById(R.id.feed_msg);
        feed_date = (TextView) view.findViewById(R.id.feed_date);
        feed_subj = (TextView) view.findViewById(R.id.feed_subject);

        feed_msg.setText(item.message);
        feed_date.setText(item.date);
        feed_subj.setText(item.subject);


        return view;
    }
}
