package com.konifar.nearbychat.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.konifar.nearbychat.R;
import com.konifar.nearbychat.models.pojo.MessageModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessagesArrayAdapter extends ArrayAdapter<MessageModel> {

    public MessagesArrayAdapter(Context context) {
        super(context, R.layout.item_message, new ArrayList<>());
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        view.setTag(holder);

        bindData(holder, getItem(pos));

        return view;
    }

    private void bindData(ViewHolder holder, MessageModel phrase) {
        holder.txtMessage.setText(phrase.getText());
    }

    static class ViewHolder {
        @Bind(R.id.txt_message)
        TextView txtMessage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
