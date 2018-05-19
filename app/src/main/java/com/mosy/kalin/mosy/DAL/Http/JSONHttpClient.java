package com.mosy.kalin.mosy.DAL.Http;

import com.google.gson.GsonBuilder;
import com.mosy.kalin.mosy.BuildConfig;
import com.mosy.kalin.mosy.DTOs.Enums.TokenResultStatus;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Services.AccountService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class JSONHttpClient {

    private HttpURLConnection Connection;

    public <T> T Get(String url, HttpParams params, final Type objectType, String dateFormat, String authToken) { //final Class<T> objectClass
        String query = paramsToQuery(params);

        try {
            this.Connection = (HttpURLConnection) new URL(url + query).openConnection();
            this.Connection.setRequestMethod("GET");
            this.Connection.setReadTimeout(15000);
            this.Connection.setConnectTimeout(15000);
            this.Connection.setRequestProperty("Accept", "application/json");
            this.Connection.setRequestProperty("Content-Type", "application/json");
            if (StringHelper.isNotNullOrEmpty(authToken))
                this.Connection.setRequestProperty("authorization", authToken);

            long execStart = 0;
            long elapsed = 0;

            if (BuildConfig.DEBUG) execStart = System.currentTimeMillis();
            int httpStatusCode = this.Connection.getResponseCode();

            if (BuildConfig.DEBUG) elapsed = System.currentTimeMillis() - execStart;
            if (BuildConfig.DEBUG) System.out.println("MOSYLOGS : REST CALL - " + url + " with params: " + query + " TOOK: " + elapsed + "ms;");

            if (httpStatusCode == HttpURLConnection.HTTP_OK) {
                String jsonString = convertStreamToString(this.Connection.getInputStream());

                GsonBuilder builder  = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, new JsonDateDeserializer());
                builder.registerTypeAdapter(Date.class, new JsonDateDeserializer2());
                builder.registerTypeAdapter(Time.class, new JsonTimeDeserializer());

                System.out.println("StatusCode: " + httpStatusCode + ". " + this.Connection.getResponseMessage());
                return builder.create().fromJson(jsonString, objectType);
            } else if (httpStatusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                System.out.println("StatusCode: " + httpStatusCode + ". " + this.Connection.getResponseMessage());
            } else {
                System.out.println("StatusCode: " + httpStatusCode + ". " + this.Connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.Connection.disconnect();
        }

        return null;
    }

    public <T> T PostObject(final String url, final Object bodyObject, final Type objectType, String dateFormat, String authToken) { //final Class<T> objectClass
        try {
            URL theUrl = new URL(url);
            this.Connection = (HttpURLConnection) theUrl.openConnection();
            this.Connection.setRequestMethod("POST");
            this.Connection.setReadTimeout(15000);
            this.Connection.setConnectTimeout(15000);
            this.Connection.setDoInput(true);
            this.Connection.setDoOutput(true);
            this.Connection.setRequestProperty("Accept", "application/json");
            this.Connection.setRequestProperty("Content-Type", "application/json");
            if (StringHelper.isNotNullOrEmpty(authToken))
                this.Connection.setRequestProperty("authorization", authToken);

            long execStart = 0;
            long elapsed = 0;

            String requestBodyJson = new GsonBuilder().create().toJson(bodyObject);
            byte[] requestBodyBytes = requestBodyJson.getBytes("UTF-8");
            OutputStream os = this.Connection.getOutputStream();
            os.write( requestBodyBytes );
            os.close();

            if (BuildConfig.DEBUG) execStart = System.currentTimeMillis();
            int httpResult = this.Connection.getResponseCode();
            if (BuildConfig.DEBUG) elapsed = System.currentTimeMillis() - execStart;
            if (BuildConfig.DEBUG) System.out.println("MOSYLOGS : REST CALL - " + url + " TOOK: " + elapsed + "ms;");


            if (httpResult == HttpURLConnection.HTTP_OK) {
                String jsonString = convertStreamToString(this.Connection.getInputStream());

                GsonBuilder builder  = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, new JsonDateDeserializer());
                builder.registerTypeAdapter(Date.class, new JsonDateDeserializer2());
                builder.registerTypeAdapter(Time.class, new JsonTimeDeserializer());

                return builder.create().fromJson(jsonString, objectType);
            } else {
                System.out.println(this.Connection.getResponseMessage());
                throw new Exception(this.Connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.Connection.disconnect();
        }

        return null;
    }

    public TokenResult GetToken(final String url, String username, String password) {
        TokenResult accessToken = new TokenResult();
        accessToken.Status = TokenResultStatus.Fail;

        // Encode the body of your request, including your clientID and clientSecret values.
        String encoded = String.format("grant_type=password&username=%s&password=%s", username, password);
        // Create a new URL object with the base URL for the access token request.
        try {
            URL authUrl = new URL(url);

            // Generate the HTTPS connection. You cannot make a connection over HTTP.
            HttpURLConnection con = (HttpURLConnection) authUrl.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod( "POST" );

            // Set the Content-Type header.
            con.setRequestProperty( "Content-Type" , "application/x-www-form-urlencoded" );
            con.setRequestProperty( "Charset" , "UTF-8" );
            // Send the encoded parameters on the connection.
            OutputStream os = con.getOutputStream();
            os.write(encoded.getBytes( "UTF-8" ));
            os.flush();
            con.connect();

            // Convert the response into a String object.
            String resultString = convertStreamToString(con.getInputStream());

            //TODO: Handle the case when no Internet connection
            //TODO: Handle the case when Server does not respond
            if (resultString.contains("invalid_grant") ||
                    (resultString.contains("error") && (resultString.contains("username") || resultString.contains("password")))){
                accessToken.Status = TokenResultStatus.Unauthorized;
            }
            else if(resultString.contains("access_token")) {
                accessToken = new GsonBuilder().create().fromJson(resultString, TokenResult.class);
                accessToken.Status = TokenResultStatus.Success;
            }
            else if(resultString.contains("Invalid Hostname")){
                accessToken.Status = TokenResultStatus.InvalidHosName;
            }
            else {
                accessToken.Status = TokenResultStatus.Fail;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return accessToken;
    }

//    public <T> T PostParams(String url, final List<NameValuePair> params, final Type objectType, String dateFormat){ // final Class<T> responseObjectClass)
//        String paramString = URLEncodedUtils.format(params, "utf-8");
//        url += "?" + paramString;
//        return PostObject(url, null, objectType, dateFormat);
//    }

//    public boolean Delete(String url, final List<NameValuePair> params) {
//        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
//        String paramString = URLEncodedUtils.format(params, "utf-8");
//        url += "?" + paramString;
//        HttpDelete httpDelete = new HttpDelete(url);
//
//        HttpResponse httpResponse = null;
//        try {
//            httpResponse = defaultHttpClient.execute(httpDelete);
//            return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT;
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//        return false;
//    }

    private String paramsToQuery(HttpParams params) {
        List<String> paramStrings = new ArrayList<>();
        if (params != null) {
            for (HttpParam param : params.get()) {
                try {
                    paramStrings.add(StringHelper.join("=", Arrays.asList(param.getName(), URLEncoder.encode(param.getValue(), "UTF-8"))));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return paramStrings.size() > 0 ? "?" + StringHelper.join("&", paramStrings) : StringHelper.empty();
    }

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder
                        .append(line)
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return stringBuilder.toString();
    }

}
