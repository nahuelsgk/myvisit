package myvisit.android;


//This is a singleton class now!
public class Pin {
	
	
	private double latitude;
	private double longitude;
	private String title;
	private String description;
	
	public Pin(double latitude, double longitude, String title, String description) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.description = description;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
}