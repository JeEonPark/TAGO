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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public class ChatListItem{
//        private String oldProfilePic;
        private String oldDepartureText;
        private String oldDestinationText;
        private String oldLastChatText;
        private String oldLivePeople;
        private String oldTotalPeople;

//        public String getOldProfilePic() { return oldProfilePic; }
//        public void setOldProfilePic(String oldProfilePic) { this.oldProfilePic = oldProfilePic; }
        public String getOldDepartureText() { return oldDepartureText; }
        public void setOldDepartureText(String oldDepartureText) { this.oldDepartureText = oldDepartureText; }
        public String getOldDestinationText() { return oldDestinationText; }
        public void setOldDestinationText(String oldDestinationText) { this.oldDestinationText = oldDestinationText; }
        public String getOldLastChatText() { return oldLastChatText; }
        public void setOldLastChatText(String oldLastChatText) { this.oldLastChatText = oldLastChatText; }
        public String getOldLivePeople() { return oldLivePeople; }
        public void setOldLivePeople(String oldLivePeople) { this.oldLivePeople = oldLivePeople; }
        public String getOldTotalPeople() { return oldTotalPeople; }
        public void setOldTotalPeople(String oldTotalPeople) { this.oldTotalPeople = oldTotalPeople; }

        ChatListItem(String oldDepartureText, String oldDestinationText, String oldLastChatText, String oldLivePeople, String oldTotalPeople) {
//            this.oldProfilePic = oldProfilePic;
            this.oldDepartureText = oldDepartureText;
            this.oldDestinationText = oldDestinationText;
            this.oldLastChatText = oldLastChatText;
            this.oldLivePeople = oldLivePeople;
            this.oldTotalPeople = oldTotalPeople;
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

            TextView oldDepartureTextText = convertView.findViewById(R.id.oldDepartureText);
            TextView oldDestinationTextText = convertView.findViewById(R.id.oldDestinationText);
            TextView oldLastChatTextText = convertView.findViewById(R.id.oldLastChatText);
            TextView oldLivePeopleTextText = convertView.findViewById(R.id.oldLivePeople);
            TextView oldTotalPeopleTextText = convertView.findViewById(R.id.oldTotalPeople);

            oldDepartureTextText.setText(chatListItem.getOldDepartureText());
            oldDestinationTextText.setText(chatListItem.getOldDestinationText());
            oldLastChatTextText.setText(chatListItem.getOldLastChatText());
            oldLivePeopleTextText.setText(chatListItem.getOldLivePeople());
            oldTotalPeopleTextText.setText(chatListItem.getOldTotalPeople());

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

        listView = v.findViewById(R.id.list);

        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "탐스 피씨 카페", "피방 가자", "2", "3"));
        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "천안역", "야우리 가쉴?", "1", "2"));
        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "힐스테이트 광교", "집 가자", "2", "2"));
        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "탐스 피씨 카페", "피방 가자", "2", "3"));
        adapter.addItem(new ChatListItem("상명대학교 천안캠퍼스", "탐스 피씨 카페", "피방 가자", "2", "3"));
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }
}