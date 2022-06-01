package com.brzstudio.tago;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindSearchingActivity extends AppCompatActivity {

    public class ListItem {
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

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_find_searching, parent, false);
                ;
            }

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
    EditText arrivalEditText;
    EditText departureEditText;
    JSONObject jObject;
    JSONArray titleArray;
    //어느 TextEdit에서 엔터 눌렀는지 값 저장
    String whereEnter = "Dep";

    //엔터 눌렀을 시 값 사라지지 않게 하는 boolean
    Boolean depEnter = false;
    Boolean arrEnter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_searching);

        //뒤로가기 눌렀을 경우
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DepartureArrivalData.setDoneDeparture(false);
                DepartureArrivalData.setDeparture("");
                DepartureArrivalData.setDepartureX(0);
                DepartureArrivalData.setDepartureY(0);

                DepartureArrivalData.setDoneArrival(false);
                DepartureArrivalData.setArrival("");
                DepartureArrivalData.setArrivalX(0);
                DepartureArrivalData.setArrivalY(0);

                setResult(201);
                finish();
            }
        });

        //리스트 값 설정
        listView = findViewById(R.id.list);


        departureEditText = findViewById(R.id.departureEditText);
        arrivalEditText = findViewById(R.id.arrivalEditText);
        departureEditText.setText(DepartureArrivalData.getDeparture());
        arrivalEditText.setText(DepartureArrivalData.getArrival());

        //출발지 리스너
        departureEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (KeyCode == keyEvent.KEYCODE_ENTER)) {
                    whereEnter = "Dep";
                    adapter = new ListItemAdapter();
                    InputMethodManager imm;
                    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(departureEditText.getWindowToken(), 0);
                    GetLocalData gld = new GetLocalData(departureEditText);
                    gld.start();
                    try {
                        gld.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String resultJson = gld.getResult();
                    System.out.println(resultJson);
                    gld.interrupt();

                    try {
                        jObject = new JSONObject(resultJson);
                        titleArray = jObject.getJSONArray("items");
                        for (int i = 0; i < titleArray.length(); i++) {
                            JSONObject obj = titleArray.getJSONObject(i);
                            String title = obj.getString("title").replace("<b>", "").replace("</b>", "");
                            String address = obj.getString("roadAddress").replace("<b>", "").replace("</b>", "");

                            adapter.addItem(new ListItem(title, address));
                            listView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    depEnter = true;
                    departureEditText.clearFocus();
                    arrivalEditText.clearFocus();
                    depEnter = false;

                    return true;
                }
                return false;
            }
        });
        //도착지 리스너
        arrivalEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (KeyCode == keyEvent.KEYCODE_ENTER)) {
                    whereEnter = "App";
                    adapter = new ListItemAdapter();
                    InputMethodManager imm;
                    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(arrivalEditText.getWindowToken(), 0);
                    GetLocalData gld = new GetLocalData(arrivalEditText);
                    gld.start();
                    try {
                        gld.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String resultJson = gld.getResult();
                    System.out.println(resultJson);
                    gld.interrupt();

                    try {
                        jObject = new JSONObject(resultJson);
                        titleArray = jObject.getJSONArray("items");
                        for (int i = 0; i < titleArray.length(); i++) {
                            JSONObject obj = titleArray.getJSONObject(i);
                            String title = obj.getString("title").replace("<b>", "").replace("</b>", "");
                            String address = obj.getString("roadAddress").replace("<b>", "").replace("</b>", "");

                            adapter.addItem(new ListItem(title, address));
                            listView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    arrEnter = true;
                    departureEditText.clearFocus();
                    arrivalEditText.clearFocus();
                    arrEnter = false;

                    return true;
                }
                return false;
            }
        });

        //EditText 포커스 이벤트
        departureEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    departureEditText.setText("");
                } else if (!depEnter) {
                    departureEditText.setText(DepartureArrivalData.getDeparture());
                }
            }
        });
        arrivalEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    arrivalEditText.setText("");
                } else if (!arrEnter) {
                    arrivalEditText.setText(DepartureArrivalData.getArrival());
                }
            }
        });

        //리스트뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                System.out.println("pos : " + position);
                String location = "";
                String address = "";
                String mapX = "";
                String mapY = "";
                try {
                    JSONObject obj = titleArray.getJSONObject(position);
                    location = obj.getString("title").replace("<b>", "").replace("</b>", "");
                    address = obj.getString("roadAddress").replace("<b>", "").replace("</b>", "");
                    mapX = obj.getString("mapx");
                    mapY = obj.getString("mapy");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), FindSearchingConfirmActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("address", address);
                intent.putExtra("mapX", mapX);
                intent.putExtra("mapY", mapY);
                intent.putExtra("isDepApp", whereEnter);

                startActivityIntent.launch(intent);

            }

            final ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == 100) {
                                departureEditText.setText(DepartureArrivalData.getDeparture());
                                arrivalEditText.setText(DepartureArrivalData.getArrival());

                                adapter = new ListItemAdapter();
                                listView.setAdapter(adapter);
                            }
                            if(DepartureArrivalData.getDoneDeparture() && DepartureArrivalData.getDoneArrival()){
                                setResult(200);
                                finish();
                            }
                        }
                    });
        });

    }


    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
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

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body) {
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
    public class GetLocalData extends Thread {

        EditText isDepAppEditText;
        public GetLocalData(EditText data) {
            isDepAppEditText = data;
        }
        private String result = "";

        public void run() {
            String clientId = "n9ZSMiTWguOqArGJ9XhO"; //애플리케이션 클라이언트 아이디값"
            String clientSecret = "A0ftsAo1jb"; //애플리케이션 클라이언트 시크릿값"


            String text = null;

            try {
                text = URLEncoder.encode(String.valueOf(isDepAppEditText.getText()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패", e);
            }


            String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text + "&display=5";    // json 결과


            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            String responseBody = get(apiURL, requestHeaders);
            result = responseBody;

        }

        public String getResult() {
            return this.result;
        }
    }
}

