package com.martdev.android.devjobs.utils;

import android.text.TextUtils;
import android.util.Log;

import com.martdev.android.devjobs.DevJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<DevJob> getJobsData(String requestURL) {
        URL url = createURL(requestURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "getJobsData: Problem making the HTTP request. ", e);
        }

        return extractJsonData(jsonResponse);
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createURL: Problem building the URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                in = connection.getInputStream();
                jsonResponse = readFromStream(in);
            } else {
                Log.e(TAG, "makeHttpRequest: Error response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: Problem retrieving jobs data", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (in != null) {
                in.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream in) throws IOException {
        StringBuilder output = new StringBuilder();
        if (in != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<DevJob> extractJsonData(String json) {
        if (TextUtils.isEmpty(json))
            return null;

        List<DevJob> devJobList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                String companyLogo = object.getString("company_logo");
                String jobTitle = object.getString("title");
                String company = object.getString("company");
                String location = object.getString("location");
                String type = object.getString("type");
                String url = object.getString("url");

                DevJob devJob = new DevJob(companyLogo, jobTitle, company, location, type, url);

                devJobList.add(devJob);
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractJsonData: Problem parsing the JSON results. ", e);
        }

        return devJobList;
    }
}
