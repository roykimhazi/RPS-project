package com.example.rps;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        listView = (ListView)findViewById(R.id.leaderboard);
        listView.setAdapter(new RankPlayerAdapter(this, 0, 0, getListFromServer()));
    }

    public ArrayList<JSONArray> getListFromServer()
    {
        JSONObject getLeaderboard = new JSONObject();
        try
        {
            getLeaderboard.put("request", "leaderboard"); // creates json object and sends it to the server
            Client client = new Client(getLeaderboard); // gets the leaderboard from the server
            JSONObject received = client.execute().get(); // does the act above
            String response = received.getString("response"); // gets OK from the server
            System.out.println("received data: " + response);
            JSONArray jArray = received.getJSONArray("leaderboard");
            ArrayList<JSONArray> jlist = new ArrayList<JSONArray>(); // you have to work with json array
            System.out.println("json array len = " + jArray.length());

            for (int i = 0; i < jArray.length(); i++)
            {
                try
                {
                    jlist.add(jArray.getJSONArray(i));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return jlist;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
