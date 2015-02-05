package myvisit.android;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class Surprise {
	
	private int id;
	private String title;
	private int rate; // 0-5
	private GeoPoint coordinate;
	private File image; // filename, or File or image?
	private File audio; // filename, or File or audio?
	private String notes;


	public Surprise() {}
	
	public Surprise(Surprise s) {
		id = s.id;
		title = s.title;
		rate = s.rate;
		coordinate = s.coordinate;
		image = s.image;
		audio = s.audio;
		notes = s.notes;
	}
	
	public Surprise(int i, String t, int r, GeoPoint p, File im, File a, String n) {
		id = i;
		title = t;
		rate = r;
		coordinate = p;
		image = im;
		audio = a;
		notes = n;
	}
	
	public void downloadSurpriseContent(int id){
		this.id=id;
		 
		String result = "";
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		nameValuePairs.add(new BasicNameValuePair("id",new Integer(id).toString()));
        
        //http post
        try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.android.nosubjec7.net/getSurprisesById.php");
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
                for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        this.title=json_data.getString("name");
                        String lat=json_data.getString("lat");
                        double latE6 = new Double(lat)*1000000;
                        String lon=json_data.getString("lon");
                        double lonE6 = new Double(lon)*1000000;
                        this.coordinate = new GeoPoint( (int)latE6, (int)lonE6);
                        this.notes=json_data.getString("description");
                        this.rate=json_data.getInt("rating");
                        
                        if(json_data.getInt("has_image")==1){
                        	String filename = new Integer(id).toString() + ".jpg";
                        	String localFilename = new File(filename).getName();

                            File img = new File("/sdcard/app/tmp/" + localFilename);

                            // Create directories
                            new File("/sdcard/app/tmp/").mkdirs();

                            // only download new images
                            if (!img.exists()) {
                                try {
                                    URL imageUrl = new URL("http://www.android.nosubjec7.net/images/" + filename);
                                    InputStream in = imageUrl.openStream();
                                    OutputStream out = new BufferedOutputStream(new FileOutputStream(img));

                                    for (int b; (b = in.read()) != -1;) {
                                        out.write(b);
                                    }
                                    out.close();
                                    in.close();
                                } catch (MalformedURLException e) {
                                    img = null;
                                } catch (IOException e) {
                                    img = null;
                                }
                            }
                            this.image=img;

                        }

                }
        }
        catch(JSONException e){
                Log.e("log_tag", "Error parsing data "+e.toString());
        }
		
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public GeoPoint getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(GeoPoint coordinate) {
		this.coordinate = coordinate;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public File getAudio() {
		return audio;
	}

	public void setAudio(File audio) {
		this.audio = audio;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
