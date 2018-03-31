package com.box8.discountingEngine.util;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ConnectionUtil {

public static String getStringFromUrl(String urlx) throws IOException {
    URL url = new URL(urlx);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.connect();
    int responseCode = conn.getResponseCode();
    if (responseCode != 200) {
        throw new RuntimeException("HttpResponseCode: " + responseCode);
    } else {
        StringBuilder inline = new StringBuilder();
        Scanner sc = new Scanner(url.openStream());
        while (sc.hasNext()) {
            inline.append(sc.nextLine());
        }
        sc.close();
        return String.valueOf((inline));
    }
}
}
