package myvisit.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListMenu extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmenu);
        
	}
	
	public void onClickFollowed(View v){
		startActivity(new Intent(this,FollowedList.class));
	}

	public void onClickNear(View v){
		startActivity(new Intent(this,NearList.class));
	}
}
