package com.brzstudio.tago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindSearchingActivity extends AppCompatActivity {

    public class ListItem{
        private String name;
        private String phone;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        ListItem(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }
    public class ListItemAdapter extends BaseAdapter {
        ArrayList<ListItem> items = new ArrayList<ListItem>();
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
            ListItem listItem = items.get(position);

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_find_searching, parent, false);
                ;            }

            TextView nameText = convertView.findViewById(R.id.name);
            TextView phoneText = convertView.findViewById(R.id.phone);

            nameText.setText(listItem.getName());
            phoneText.setText(listItem.getPhone());
            return convertView;
        }

        public void addItem(ListItem item) {
            items.add(item);
        }
    }

    ListView listView;
    ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_searching);

        GetLocalData gld = new GetLocalData();
        gld.start();
        try {
            gld.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //뒤로가기 눌렀을 경우
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //출발지 입력 후 엔터키 눌렀을 경우
        EditText departureEditText = findViewById(R.id.departureEditText);
        EditText arrivalEditText = findViewById(R.id.arrivalEditText);
        departureEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == keyEvent.KEYCODE_ENTER)) {
                    departureEditText.clearFocus();
                    arrivalEditText.clearFocus();
                    InputMethodManager imm;
                    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(departureEditText.getWindowToken(), 0);
                    System.out.println("asdfasdufljdashfkjadshvkasdhbf");
                    return true;
                }
                return false;
            }
        });

        listView = findViewById(R.id.list);
        adapter = new ListItemAdapter();

        adapter.addItem(new ListItem("김김김", "010-111-111"));
        adapter.addItem(new ListItem("님님님", "010-222-222"));
        adapter.addItem(new ListItem("딤딤딤", "010-333-333"));
        listView.setAdapter(adapter);


        //리스트뷰 테스트

    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    //네이버 API 데이터 받아오는 쓰레드
    public static class GetLocalData extends Thread {
        public void run() {
            String clientId = "n9ZSMiTWguOqArGJ9XhO"; //애플리케이션 클라이언트 아이디값"
            String clientSecret = "A0ftsAo1jb"; //애플리케이션 클라이언트 시크릿값"


            String text = null;
            try {
                text = URLEncoder.encode("상명대학교", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패",e);
            }


            String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text + "&display=5";    // json 결과


            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            String responseBody = get(apiURL,requestHeaders);


            System.out.println(responseBody);
            interrupt();
        }
    }
}

