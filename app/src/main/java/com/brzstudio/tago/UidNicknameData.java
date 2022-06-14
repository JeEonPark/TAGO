package com.brzstudio.tago;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UidNicknameData {

    private static Map<String, String> uidNicknameMap = new HashMap<>();

    public static Map<String, String> getUidNicknameMap() {
        return uidNicknameMap;
    }

    public static void updateUid(final isTaskDoneCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference userInformation = firestore.collection("UserInformation");

        userInformation.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String uid = document.getId();
                    String nickname = (String) document.getData().get("nickname");

                    System.out.println(uid + " : " + nickname);

                    uidNicknameMap.put(uid, nickname);

                }
            } else {
                System.out.println("task0 : Error getting documents: " + task.getException());
            }
            callback.onCallback(true);

        });
    }

    public interface isTaskDoneCallback {
        void onCallback(boolean inIsTaskDone);
    }

}
