package fr.geniustom.monpotagerconnecter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "jdbc:mysql://51.38.48.43/s73_test";
    private static final String USER = "u73_owHuLUCsWN";
    private static final String PASSWORD = "vVF5oIy.udopPzh.8Upo!2y2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                new InfoAsyncTask().execute();
            }
        },0,5000);

    }

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            Map<String, String> info = new HashMap<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT * FROM infos ";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    info.put("temp", resultSet.getString("temp"));
                    info.put("humidity", resultSet.getString("humidity"));
                    info.put("lux", resultSet.getString("lux"));
                    info.put("wind", resultSet.getString("wind"));
                    info.put("moove", resultSet.getString("moove"));
                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return info;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (!result.isEmpty()) {

                TextView textViewtemp = findViewById(R.id.texttemp);
                textViewtemp.setText(result.get("temp"));

                TextView textViewhum = findViewById(R.id.texthum);
                textViewhum.setText(result.get("humidity"));

                TextView textViewlux = findViewById(R.id.textlux);
                textViewlux.setText(result.get("lux"));

                TextView textViewwind = findViewById(R.id.textwind);
                textViewwind.setText(result.get("wind"));
            }
        }
    }
}