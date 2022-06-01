package com.brzstudio.tago;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public class ChatListItem{
        private String departureText;
        private String lastChatText;

        public String getDepartureText() { return departureText; }
        public void setDepartureText(String departureText) { this.departureText = departureText; }
        public String getLastChatText() { return lastChatText; }
        public void setLastChatText(String lastChatText) { this.lastChatText = lastChatText; }
        ChatListItem(String departureText, String lastChatText) {
            this.departureText = departureText;
            this.lastChatText = lastChatText;
        }
    }
    public class ChatListItemAdapter extends BaseAdapter {
        ArrayList<ChatListItem> items = new ArrayList<ChatListItem>();
        Context context;

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            context = parent.getContext();
            ChatListItem chatListItem = items.get(position);

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_fragment_chat, parent, false);
            }
            TextView departureTextText = convertView.findViewById(R.id.departureText);
            TextView lastChatTextText = (TextView) convertView.findViewById(R.id.lastChatText);

            departureTextText.setText(chatListItem.getDepartureText());
            lastChatTextText.setText(chatListItem.getLastChatText());
            return convertView;
        }
        public void addItem(ChatListItem item) {
            items.add(item);
        }
    }

    ListView listView;
    ChatListItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat,container,false);
        adapter = new ChatListItemAdapter();

        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "충남 천안시 동남구 상명대길 31"));
        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "충남 천안시 동남구 상명대길 31"));
        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "충남 천안시 동남구 상명대길 31"));

        // Inflate the layout for this fragment
        return v;
    }
}