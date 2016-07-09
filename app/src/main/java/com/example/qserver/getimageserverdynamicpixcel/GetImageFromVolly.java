package com.example.qserver.getimageserverdynamicpixcel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.qserver.getimageserverdynamicpixcel.Model.ImageDataVolly;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetImageFromVolly extends AppCompatActivity {
    private String TAG = GetImageFromVolly.class.getSimpleName();
    private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private ArrayList<ImageDataVolly> images;
    private ProgressDialog pDialog;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image_from_volly);
        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        fetchImages();

         recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);



    }
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.indexOfChild(v);
              ImageDataVolly imageDataVolly = images.get(itemPosition);
            Toast.makeText(getApplicationContext(),itemPosition +"//"+ imageDataVolly.getMedium(),Toast.LENGTH_LONG).show();
            Log.e("Clicked and Position is ",String.valueOf(itemPosition));
        }
    }
    private void fetchImages() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                ImageDataVolly image = new ImageDataVolly();
                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                images.add(image);

                                adapter = new ImageAdapter(getApplicationContext(),images);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                      //  mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
       // AppController.getInstance().addToRequestQueue(req);
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

        int color  = Color.parseColor("#7C1419");
        int color2  = Color.parseColor("#3B6C00");

        int getColor[] = new int[]{color,color2};
        private List<ImageDataVolly> moviesList;
        private Context context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.iv_volly_image);

            }
        }


        public ImageAdapter(Context context,List<ImageDataVolly> moviesList) {
            this.context = context;
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_list_row_adapter, parent, false);
             //int colorpos = recyclerView.indexOfChild(parent) % getColor.length;
             //itemView.setBackgroundColor(getColor[colorpos]);





            itemView.setOnClickListener(new MyOnClickListener());
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ImageDataVolly imageDataVollylist = moviesList.get(position);
          //  viewHolder.tv_android.setText(android_versions.get(i).getAndroid_version_name());
            Picasso.with(context).load(imageDataVollylist.getMedium()).resize(200, 120).into(holder.imageView);
           // holder.imageView.setImageBitmap(movie.getTitle());

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }
}
