package com.a1_1801040149.myteam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_profile#} factory method to
 * create an instance of this fragment.
 */

public class fragment_profile extends Fragment implements View.OnClickListener{
    private static TextView getName;
    private static TextView getMail;
    private static ImageView getAvatar;
    private ImageButton btnNext, btnBack;

    private int id=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        getName = view.findViewById(R.id.getName);
        getMail = view.findViewById(R.id.getMail);

        btnNext = view.findViewById(R.id.btnNext);
        btnBack = view.findViewById(R.id.btnBack);

        getAvatar = view.findViewById(R.id.avatar);

        String urlText = "https://jsonplaceholder.typicode.com/users/1";
        RestLoader restLoader = new RestLoader();
        restLoader.execute(urlText);
        //Somehow this took longer than ussua
        String urlImage = "https://robohash.org/1?set=set3";
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.execute(urlImage);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        String urlText, urlImage;
        ImageLoader imageLoader;
        RestLoader restLoader;

        switch (view.getId()){
            case R.id.btnBack:
                if(id >0 ){
                    id --;
                    urlText = "https://jsonplaceholder.typicode.com/users/" + id;
                    restLoader = new RestLoader();
                    restLoader.execute(urlText);

                    urlImage = "https://robohash.org/"+ id + "?set=set=2";
                    imageLoader = new ImageLoader();
                    imageLoader.execute(urlImage);

                    Log.i("ID-DECREASE", "" + id);
                }
                break;
            case R.id.btnNext:
                if(id < 5){
                    id ++;
                    Log.i("ID-INCREASE", "" + id);

                    urlText = "https://jsonplaceholder.typicode.com/users/" + id;
                    restLoader = new RestLoader();
                    restLoader.execute(urlText);

                    urlImage = "https://robohash.org/"+ id + "?set=set=2";
                    imageLoader = new ImageLoader();
                    imageLoader.execute(urlImage);
                }
                break;
        }
    }

    public static class RestLoader extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                Scanner sc = new Scanner(is);
                StringBuilder result = new StringBuilder();
                String line;
                while (sc.hasNextLine()) {
                    line = sc.nextLine();
                    result.append(line);
                }
                Log.i("RESULT", "" + result);
                return result.toString();
            }catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            try {
                JSONObject obj = new JSONObject(result);
                String name = obj.getString("name");

                String email = obj.getString("email");
                getName.setText(name);
                getMail.setText(email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static class ImageLoader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            getAvatar.setImageBitmap(bitmap);
        }
    }
}