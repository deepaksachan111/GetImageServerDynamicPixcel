package com.example.qserver.getimageserverdynamicpixcel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qserver.getimageserverdynamicpixcel.Model.Data;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private ListView listView;
private ArrayList<Data> dataArrayList = new ArrayList<>();
private ArrayAdapter adapter ;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =(ListView)findViewById(R.id.listview);

    Button button =(Button)findViewById(R.id.btn_next);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),GetImageFromVolly.class));
        }
    });




    /*    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        String url ="http://www.aklearningsolutions.com/dpvideostore/user/webservices/dp-new/favouriteVideoList.php?user_id=1&device_id=f71d3aeb-8f1c-3d12-9eb4-21941a4ae3db";

        new MyAsynckTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);

        }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

private  void parseData(){


        }

private class MyAsynckTask extends AsyncTask<String,Void,String>{

    private    String res;
    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();


        try {
            HttpGet httpGet = new HttpGet(params[0]);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity= httpResponse.getEntity();
            res = EntityUtils.toString(httpEntity);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            //JSONObject jsonObjectdata = (JSONObject) jsonObject.get("data");
            JSONArray jsonArray = jsonObject.getJSONArray("video_details");
            for(int i = 0;i<=jsonArray.length()-1;i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String id = jsonObject1.getString("id");
                String video_id = jsonObject1.getString("video_id");
                String video_title = jsonObject1.getString("video_title");
                String video_duration = jsonObject1.getString("video_duration");
                String video_desc = jsonObject1.getString("video_desc");

                dataArrayList.add(new Data(id,video_id,video_duration,video_title,video_desc));

            }
            adapter = new MyAdapter(MainActivity.this,R.layout.adapter_getdata_server,dataArrayList);
            listView.setAdapter(adapter);




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

private  class MyAdapter extends ArrayAdapter {
    ArrayList<Data> data;
    Activity context;

    public MyAdapter(Activity context, int resource,ArrayList<Data> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    class Viewholder {
        ImageView imageView;
        TextView tv_duration;
        TextView tv_tittle;
        TextView tv_desc;
        public int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //super.getView(position, convertView, parent);
        Viewholder viewholder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_getdata_server, null);

            viewholder = new Viewholder();
            viewholder.imageView = (ImageView)convertView.findViewById(R.id.iv_image);
            viewholder.tv_duration =(TextView)convertView.findViewById(R.id.tv_duration);
            viewholder.tv_tittle =(TextView)convertView.findViewById(R.id.tv_tittle);
            viewholder.tv_desc =(TextView)convertView.findViewById(R.id.tv_description);

            convertView.setTag(viewholder);
        }else{

            viewholder = (Viewholder)convertView.getTag();
        }
       // imageView = (ImageView)convertView.findViewById(R.id.iv_image);
        Data datas = data.get(position);
        viewholder.tv_duration.setText(datas.getVideo_duration());
        viewholder.tv_tittle.setText(datas.getVideo_title());
        viewholder.tv_desc.setText(datas.getVideo_desc());

        if(!datas.getVideo_id().equals(null)) {

            String image_url= "http://img.youtube.com/vi/";
            new ImageAsynckTask(viewholder).execute(image_url + datas.getVideo_id() + "/mqdefault.jpg");
           // Picasso.with(context).load(image_url+datas.getVideo_id()+ "/mqdefault.jpg").resize(240, 150).into(viewholder.imageView);


            // integerAsyncTaskHashMap.put(position, myDisplayAsynctask);

        }
        return convertView;
    }

    private class  ImageAsynckTask extends  AsyncTask<String,Void,Bitmap>{
        //private int pos;
        private Viewholder viewHolder;
        Bitmap bitmap;

        public ImageAsynckTask(Viewholder viewHolder) {
            this.viewHolder = viewHolder;
           // this.pos = viewHolder.position;
        }
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                InputStream in  = new java.net.URL(params[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);


            if (bitmap != null) {
                viewHolder.imageView.setImageBitmap(bitmap);
            }
            else {

                viewHolder.imageView.setBackgroundResource(R.drawable.hi);
            }
        }
    }


}



}
