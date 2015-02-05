package myvisit.android;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyVisitMap extends MapActivity {
    /** Called when the activity is first created. */
	public static final String NO_ID = "NO_ID";
	private MapController myMapController;
	private LocationManager locationManager=null;
	private LocationListener mlocListener;
//	private TextView gpsCoordinatesLon;
//	private TextView gpsCoordinatesLat;
//	private TextView cityFromCoordinates;
	private MapView myMapView;
	private Geocoder myGeoCoder;
	private List<Address> myAdresses = null;
	//private ArrayList<Pin> plist;
	//private ArrayList<OverlayItem> items;
	private static MyItemizedOverlay itemizedoverlay;
	private MyItemizedOverlay currentOverlay;
	private static List<Overlay> mapOverlays;
	private Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapgui);
        
        /*Inits for GUIS*/
        //Labels for writing Latituds
//        gpsCoordinatesLat = (TextView)findViewById(R.id.lat);
//        gpsCoordinatesLat.setText("Starting latitudes...");
//        //Labels for writing Longitudes
//        gpsCoordinatesLon = (TextView)findViewById(R.id.lon);
//        gpsCoordinatesLon.setText("Starting longitudes...");
//        cityFromCoordinates = (TextView)findViewById(R.id.cityLabel);
//        cityFromCoordinates.setText("Starting searching city...");
        res=this.getResources();
        
        //Init map items
        myMapView = (MapView)findViewById(R.id.mapView);
        myMapView.setSatellite(true); //Set satellite view
        myMapController = myMapView.getController();
        myMapController.setZoom(17); //Fixed Zoom Level
        myMapView.setBuiltInZoomControls(true);
        myMapView.displayZoomControls(true);
        
        //Layout init
        mapOverlays = myMapView.getOverlays();
	    Drawable drawable = res.getDrawable(R.drawable.icon2);
	    itemizedoverlay = new MyItemizedOverlay(drawable, this);
	    
//	    //Exemple init
//	    Mapper.getInstance().addVisitToMap((double) 41.41462353, (double) 2.20412373, "Nahuel's Home", 1);
//	    Mapper.getInstance().addVisitToMap((double)41.38875534,(double) 2.11772212,"UPC",2);
	    Mapper.getInstance().addVisitToMap(new Surprise(1, "Nahuel's home", 3,
	    		new GeoPoint((int) (41.41462353*1000000), (int) (2.20412373*1000000)),
	    		null, null, "Home sweet home"));
	    Mapper.getInstance().addVisitToMap(new Surprise(2, "UPC", 3,
	    		new GeoPoint((int) (41.38875534*1000000), (int) (2.11772212*1000000)),
	    		null, null, "Booooriiiing"));

	    //Draw markers for the visits
	    drawMarkers();
    	
        /*GPS Inits*/
        //Acces to system gps
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Server for the manager
        mlocListener = new MyLocationListener();
        //Connect manager to server
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,6000,50.0f,mlocListener);
        myGeoCoder = new Geocoder(getBaseContext(),Locale.getDefault());
//		Toast.makeText(this, "Welcome to my visit.",  5000).show();
    }

    private void CenterLocatio(GeoPoint centerGeoPoint){
    	myMapController.animateTo(centerGeoPoint);
    };
    
    public static void drawMarkers() {
	    if(Mapper.getInstance().getPlist() != null && Mapper.getInstance().getPlist().size() > 0) {
	    	for(int i = 0; i < Mapper.getInstance().getPlist().size(); i++) {
	    		//Pin p = Mapper.getInstance().getPlist().get(i);
	    		//GeoPoint myGeoPoint = new GeoPoint( (int)(p.getLatitude()*1000000),(int)(p.getLongitude()*1000000));
	    		Surprise s = Mapper.getInstance().getPlist().get(i);
	    		OverlayItem oi = new OverlayItem(s.getCoordinate(),s.getTitle(), Integer.toString(s.getId())); 
	    		itemizedoverlay.addOverlay(oi);
	    		mapOverlays.add(itemizedoverlay);
	    	}
	    }
    }
    
    private class MyLocationListener implements LocationListener{

    	public void onLocationChanged(Location argLocation) {
//    		Toast.makeText(getApplicationContext(), "Welcome to planet Earth.", 5000).show();
    		
    		GeoPoint myGeoPoint = new GeoPoint( (int)(argLocation.getLatitude()*1000000),(int)(argLocation.getLongitude()*1000000));
    		CenterLocatio(myGeoPoint);
    		
//	    	gpsCoordinatesLat.setText(String.valueOf(argLocation.getLatitude()));
//	    	gpsCoordinatesLon.setText(String.valueOf(argLocation.getLongitude()));
	    	
	    	myGeoPoint = new GeoPoint( (int)(argLocation.getLatitude()*1000000),(int)(argLocation.getLongitude()*1000000));
	
		    Drawable drawable = res.getDrawable(R.drawable.icon3);
		    currentOverlay = new MyItemizedOverlay(drawable, getApplicationContext());
			OverlayItem oi = new OverlayItem(myGeoPoint,"Current Location", NO_ID); 
    		currentOverlay.addOverlay(oi);
    		mapOverlays.add(currentOverlay);
    		Mapper.getInstance().setCurrentLocationLongitude(argLocation.getLongitude());
    		Mapper.getInstance().setCurrentLocationLatitude(argLocation.getLatitude());
    		
	    	try {
				myAdresses = myGeoCoder.getFromLocation(argLocation.getLatitude() , argLocation.getLongitude(), 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Failed during fetching the city...", 1000).show();
			}
			
			if(myAdresses.size()>0){
//				Toast.makeText(getApplicationContext(), "Welcome to " + myAdresses.get(0).getCountryName(), 1000).show();
				Toast.makeText(getApplicationContext(), "You have arrived to " + myAdresses.get(0).getLocality() +
						" " + myAdresses.get(0).getCountryName(), 1000).show();
//				cityFromCoordinates.setText(myAdresses.get(0).getLocality());
			}
			else Toast.makeText(getApplicationContext(), "Location Updated but has found 0 addresses.", 1000).show();
			
    	}

    	public void onProviderDisabled(String provider) {
    	// TODO Auto-generated method stub
    		Toast.makeText( getApplicationContext(),"MyVisit: Gps Disabled",	Toast.LENGTH_SHORT ).show();
    	}

    	public void onProviderEnabled(String provider) {
    	// TODO Auto-generated method stub
    		Toast.makeText( getApplicationContext(),"MyVisit: Provider Enable",Toast.LENGTH_SHORT ).show();
    	}

    	public void onStatusChanged(String provider,
    	 int status, Bundle extras) {
    	// TODO Auto-generated method stub
    		Toast.makeText(getApplicationContext(), "Ha cambiado de estado el proveedor" + provider + "a " + status, 5000);
    		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000L,500.0f,mlocListener);
    	}
    	};
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
}