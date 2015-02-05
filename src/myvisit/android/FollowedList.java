package myvisit.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FollowedList extends ListActivity {

	private class Surprise{
		public String title, lat, lon, id;
	}
	
	Surprise[] surprises;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        String result = "";
	        //the year data to send
	        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        
	        //TODO this should be dynamic
	        nameValuePairs.add(new BasicNameValuePair("username","onemanclapping"));
	         
	        //http post
	        try{
	                HttpClient httpclient = new DefaultHttpClient();
	                HttpPost httppost = new HttpPost("http://www.android.nosubjec7.net/getSurprisesByUsername.php");
	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	                HttpResponse response = httpclient.execute(httppost);
	                HttpEntity entity = response.getEntity();
	                InputStream is = entity.getContent();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	                StringBuilder sb = new StringBuilder();
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	                        sb.append(line + "\n");
	                }
	                is.close();
	                result=sb.toString();
	        }catch(Exception e){
	                Log.d("log_tag", "Error converting result "+e.toString());
	        }
	         
	        //parse json data
	        try{
	                JSONArray jArray = new JSONArray(result);
	                surprises = new Surprise[jArray.length()];
	                for(int i=0;i<jArray.length();i++){
	                	Surprise s = new Surprise();
	                        JSONObject json_data = jArray.getJSONObject(i);
	                        s.title=json_data.getString("name");
	                        s.lat=json_data.getString("lat");
	                        s.lon=json_data.getString("lon");
	                        s.id=json_data.getString("id");
	                        surprises[i]=s;
	                }
	        }
	        catch(JSONException e){
	                Log.e("log_tag", "Error parsing data "+e.toString());
	        }
	        
	        setContentView(R.layout.surpriselist);
	        //set the adapter for the array of Tasks
	        setListAdapter(new SurpriseAdapter());
	   }
	 
	 public void onListItemClick(ListView parent, View v, int position, long id){
	    	//...we view that task in detail
	    	Intent i=new Intent(this,SurpriseDialog.class);
	    	Bundle b = new Bundle();
	    	i.putExtras(b);
			startActivity(i);
	    }
	 
	//adapting a task to be viewable as a list of instances of the layout Task
		class SurpriseAdapter extends ArrayAdapter<Surprise>{
			public SurpriseAdapter() {
				super(FollowedList.this,R.layout.surprise, surprises);
			}
		
			public View getView(int position, View contentView, ViewGroup parent) {
				LayoutInflater inflater=getLayoutInflater();
				View row = inflater.inflate(R.layout.surprise, parent, false);
				TextView title = (TextView) row.findViewById(R.id.title);
				title.setText(surprises[position].title);
				TextView lat = (TextView) row.findViewById(R.id.lat);
				lat.setText(lat.getText()+surprises[position].lat);
				TextView lon = (TextView) row.findViewById(R.id.lon);
				lon.setText(lon.getText()+surprises[position].lon);
				return row;				
			}
		}
}
