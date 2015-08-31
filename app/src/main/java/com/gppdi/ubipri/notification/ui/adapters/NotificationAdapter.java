package com.gppdi.ubipri.notification.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.data.models.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private int resource;

    private static class ViewHolder {
        ImageView icon;
        TextView message;
        TextView time;
    }

    public NotificationAdapter(Context context, int resource, List<Notification> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Notification notification = getItem(i);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, viewGroup, false);

            // Remember the layout views for faster access
            viewHolder.icon = (ImageView) view.findViewById(R.id.imgNotificationType);
            viewHolder.message = (TextView) view.findViewById(R.id.txtNotificationsMessage);
            viewHolder.time = (TextView) view.findViewById(R.id.txtNotificationsTime);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // Fill and configure view-items according to the notification
        HashMap<Integer,Integer> icons = new HashMap<>();
        icons.put(Notification.FORMAT_GCM, R.drawable.ic_notifications_black_24dp);
        icons.put(Notification.FORMAT_SMS, R.drawable.ic_sms_black_24dp);
        icons.put(Notification.FORMAT_EMAIL, R.drawable.ic_email_black_24dp);

        switch (notification.getState()) {
            case Notification.STATE_NEW:
                viewHolder.message.setTypeface(null, Typeface.BOLD);
                break;
            case Notification.STATE_READ:
                viewHolder.icon.setColorFilter(Color.LTGRAY);
                break;
        }

        viewHolder.icon.setImageResource(icons.get(notification.getFormat()));
        viewHolder.message.setText(notification.getMessage());
        viewHolder.time.setText(formatTime(notification.getTimestamp()));

        return view;
    }

    /**
     * Returns a formatted string with the difference between now and some timestamp, or the
     * formatted timestamp (e.g. "14:38", "5h", "6d", "Dec 12", "Jan 3, 2015"). Indicates when a
     * message was received.
     * @param timestamp    A timestamp (ms) to be formatted
     * @return String with the formatted timestamp
     */
    private String formatTime(long timestamp) {
        final long second = 1000;
        final long minute = 60 * second;
        final long hour = 60 * minute;
        final long day = 24 * hour;
        final long week = 7 * day;

        Calendar now = Calendar.getInstance();
        Calendar nDate = Calendar.getInstance();
        nDate.setTimeInMillis(timestamp);

        if(isSameYear(nDate,now)) {
            if (isSameMonth(nDate,now)) {
                long diff = now.getTimeInMillis() - nDate.getTimeInMillis();
                if(diff < hour) return SimpleDateFormat.getTimeInstance().format(nDate);
                if(diff < day) return String.valueOf(diff/hour) + "h";
                if(diff < week) return String.valueOf(diff/day) + "d";
            }
            return SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(nDate);
        }
        return SimpleDateFormat.getDateInstance().format(nDate);
    }

    private boolean isSameYear(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR);
    }
    private boolean isSameMonth(Calendar a, Calendar b) {
        return a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && isSameYear(a,b);
    }
    private boolean isSameDay(Calendar a, Calendar b) {
        return a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH) && isSameMonth(a,b);
    }
}