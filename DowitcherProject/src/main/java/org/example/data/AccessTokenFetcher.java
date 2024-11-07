package org.example.data;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class AccessTokenFetcher {

    public static String getAccessToken(String serviceAccountKeyPath) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath))
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        AccessToken accessToken = credentials.refreshAccessToken();
        return accessToken.getTokenValue();
    }

    public static void main(String[] args) {
        try {
            String token = getAccessToken("C:\\SoftwareDev\\dowitchercwbot-5d84a82c5f0c.json"); // Provide path here
            System.out.println("Access Token: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
