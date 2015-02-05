package myvisit.android;

import java.util.ArrayList;

public class Mapper {
	private double currentLocationLatitude;
	private double currentLocationLongitude;
	private static Mapper instance;
//	private ArrayList<Pin> plist=null;
	private ArrayList<Surprise> plist = null;
	
	public Mapper(double currentLatitude, double currentLongitud){
//		plist = new ArrayList<Pin>();
		plist = new ArrayList<Surprise>();
		setCurrentLocationLatitude(currentLatitude);
		setCurrentLocationLongitude(currentLongitud);
		//Load all the surprise from the database
	}
	
//	public void addVisitToMap(double latitude, double longitude, String title, int id){	
//	    Pin pp = new Pin(latitude ,longitude ,title,String.valueOf(id));
//	    plist.add(pp);
//    }
	public void addVisitToMap(Surprise s){
	    plist.add(s);
	    plist.get(0);
    }
	
//	public ArrayList<Pin> getPlist(){
//		return this.plist;
//	}
	public ArrayList<Surprise> getPlist(){
		return plist;
	}
	
	// finder method
	public Surprise findSurprise(int id) {
		for(int i = 0; i < plist.size(); i++) {
			if(plist.get(i).getId() == id) {
				return plist.get(i);
			}
		}
		return null;
	}
	
	// free id method
	public int findFreeId() {
		int ret = 0;
		boolean found = true;
		if(plist != null && plist.size() > 0) {
			ret = plist.get(plist.size()-1).getId()+1;
			while(found) {
				found = false;
				for(int i = plist.size()-1; i >= 0; i--) {
					if(ret == plist.get(i).getId()) {
						found = true;
						break;
					}
				}
				if(found) {
					ret++;
				}
			}
		}
		return ret;
	}
	
	public static Mapper getInstance(){
		if(instance==null){
			instance = new Mapper(0,0);
		}
		return instance;
	}

	public void setCurrentLocationLatitude(double currentLocationLatitude) {
		this.currentLocationLatitude = currentLocationLatitude;
	}

	public double getCurrentLocationLatitude() {
		return currentLocationLatitude;
	}

	public void setCurrentLocationLongitude(double currentLocationLongitude) {
		this.currentLocationLongitude = currentLocationLongitude;
	}

	public double getCurrentLocationLongitude() {
		return currentLocationLongitude;
	}
}
