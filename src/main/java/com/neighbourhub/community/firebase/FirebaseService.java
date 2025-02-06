package com.neighbourhub.community.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class FirebaseService {

    private Firestore firestore;

    public FirebaseService() {
        initFirestore();
    }

    public void initFirestore() {
        try (FileInputStream serviceAccount
                     = new FileInputStream("neighbourhub-ff684-firebase-adminsdk-fbsvc-652c8a0ddf.json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
            this.firestore = FirestoreClient.getFirestore(firebaseApp);
        } catch (IOException ignored) {
        }
    }

    public Optional<Firestore> getFirestore() {
        if (firestore == null) {
            initFirestore();
        }
        return Optional.ofNullable(firestore);
    }
}
