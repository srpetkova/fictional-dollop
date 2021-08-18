package com.example.fetchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// The app displays the items in a vertical list with three columns - listId, id, and nae
// The items are first sorted by list Id and then by id
// They are presented in a RecyclerView
public class MainActivity extends AppCompatActivity {

    final String TAG = "check";
    Map<Integer, Integer> hm = new TreeMap<>();
    ArrayList<Item> itemListSorted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity mContext = this;

        // Downloading the items asynchronously
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFile();
                // Tasks done after downloading is over (post-execute)
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (hm != null)
                            Toast.makeText(mContext, "Loaded " + hm.size() + " items", Toast.LENGTH_SHORT).show();

                            // Setting up the Recycler View and the Adapter
                            RecyclerView recyclerView;
                            ItemAdapter noteAdapter;
                            recyclerView = findViewById(R.id.rv);
                            noteAdapter = new ItemAdapter(itemListSorted, mContext);
                            recyclerView.setAdapter(noteAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                        setTitle("FetchList (" + hm.size() + ")");

                    }
                });
            }
        }).start();

    }

    // Method to download the items
    private void loadFile() {
        String DATA_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            return;
        }

        parseJSON(sb.toString());

    }

    // Method to parse JSON
    private void parseJSON(String s) {

        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                int id = jStock.getInt("id");
                int listId = jStock.getInt("listId");
                String name = jStock.getString("name");

                // Populating the Tree Map, which sorts the items by their ids (keys)
                // Filtering out the items without a name
                if (!name.equals("null")) {
                    if (!name.equals("")) {
                        hm.put(id, listId);
                    }
                }
            }

            List<Map.Entry<Integer, Integer> > list = new LinkedList<>(hm.entrySet());

            // Comparator to sort the items by values (their listIds)
            Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() {
                public int compare(Map.Entry<Integer, Integer> o1,
                                   Map.Entry<Integer, Integer> o2)
                {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            // HashMap with the items sorted first by keys than by values
            HashMap<Integer, Integer> temp = new LinkedHashMap<>();
            for (Map.Entry<Integer, Integer> entry : list) {
                temp.put(entry.getKey(), entry.getValue());
            }

            // List with items objects for the RecyclerView
            for (Map.Entry<Integer,Integer> entry : temp.entrySet()) {
                itemListSorted.add(new Item(entry.getValue(), entry.getKey(), "Item" + entry.getKey()));
            }

            Log.d(TAG, "test12: " + temp.toString());
            Log.d(TAG, "test12: " + temp.size());
            Log.d(TAG, "test12: " + itemListSorted);
            Log.d(TAG, "test12: " + itemListSorted.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
