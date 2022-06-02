package com.brzstudio.tago;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ListFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Button signoutButton = (Button) view.findViewById(R.id.signoutButton);

        Button testButton = (Button) view.findViewById(R.id.testButton);

        signoutButton.setOnClickListener(this);
        testButton.setOnClickListener(this);

        return view;
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signoutButton:
                FirebaseAuth.getInstance().signOut();
                FragmentActivity f = getActivity();
                Intent intent = new Intent(f, LoginActivity.class);
                startActivity(intent);
                f.finish();

            case R.id.testButton:
                new Thread (() -> {
                    String clientId = "xz03is1zp8"; //애플리케이션 클라이언트 아이디값"
                    String clientSecret = "Ypa9kuJHBBilpKhF0HhjPCT4zU3MPLclvuAy6EF2"; //애플리케이션 클라이언트 시크릿값"

                    String text = null;
                    try {
                        text = URLEncoder.encode("그린팩토리", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("검색어 인코딩 실패", e);
                    }

                    String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=127.0644347,37.2856370&sourcecrs=epsg:4326&orders=roadaddr&output=json";    // json 결과

                    Map<String, String> requestHeaders = new HashMap<>();
                    requestHeaders.put("X-NCP-APIGW-API-KEY-ID", clientId);
                    requestHeaders.put("X-NCP-APIGW-API-KEY", clientSecret);
                    String responseBody = get(apiURL, requestHeaders);

                    System.out.println(responseBody);
                }).start();
        }
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
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

}