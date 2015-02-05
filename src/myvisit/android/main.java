package myvisit.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class main extends Activity {
    /** Called when the activity is first created. */
	LocationManager locationManager;
	TextView gpsCoordinates;
	
	// These are for the SurpriseCreator
	private Button button;
	private Intent creatorIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Labels for testings
        gpsCoordinates = (TextView)findViewById(R.id.lat);
        gpsCoordinates.setText("He conseguido arrancar!!!");
        
        //Generate the location manager for read the coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Server for waiting the response
        LocationListener mlocListener = new MyLocationListener();
        
        //Search for current coordinates phone
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        
        // intent for the SurpriseCreator
		creatorIntent = new Intent(this, SurpriseCreator.class);
		
        // camera button, and the actual camera as it's listener
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    // start the activity
			    startActivity(creatorIntent);
			}
		});
    }
    
    //Implementation for the Interface
    public class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			location.getLatitude(); 

			location.getLongitude(); 

			String Text = "My current location is: Latitud = " + location.getLatitude() + "Longitud = " + location.getLongitude(); 

			Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "The Provider "+provider+" Enabled", 2000);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "The status has changed into " + Integer.toString(status), 2000);
		}
    }
    
}