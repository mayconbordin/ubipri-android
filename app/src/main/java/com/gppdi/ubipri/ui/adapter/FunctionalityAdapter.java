package com.gppdi.ubipri.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.functionality.FnListItem;

import java.util.List;

/**
 * @author mayconbordin
 */
public class FunctionalityAdapter extends ArrayAdapter<FnListItem> {
    public FunctionalityAdapter(Context context, List<FnListItem> objects) {
        super(context, R.layout.functionality_row, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        FunctionalityHolder holder = null;

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.functionality_row, parent, false);
            holder = new FunctionalityHolder(view);
            view.setTag(holder);
        } else {
            holder = (FunctionalityHolder) view.getTag();
        }

        FnListItem item = getItem(position);

        if (item != null) {
            holder.getName().setText(item.getName());
            holder.getState().setChecked(item.getFunctionality().isEnabled());
        }

        return view;
    }

    private class FunctionalityHolder {
        View base;
        TextView name = null;
        Switch state  = null;

        public FunctionalityHolder(View base) {
            this.base = base;
        }

        public TextView getName() {
            if (name == null) {
                name = (TextView) base.findViewById(R.id.fnName);
            }
            return name;
        }

        public Switch getState() {
            if (state == null) {
                state = (Switch) base.findViewById(R.id.fnState);
            }
            return state;
        }
    }
}
