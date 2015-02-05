package myvisit.android;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class TabMenu extends TabActivity {
	
	public static final int LOGOUT_ID = Menu.FIRST+1;
	
	public static final String TAG_MAP = "MAP";
	public static final String TAG_LIST = "LIST";
	public static final String TAG_CREATOR = "CREATOR";
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_layout);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    
//	    ImageView mapIcon = new ImageView(this);
//	    mapIcon.setImageResource(R.drawable.ic_tab_artists);
//	    ImageView creatorIcon = new ImageView(this);
//	    creatorIcon.setImageResource(R.drawable.ic_tab_artists);
//	    mapIcon.setBackgroundDrawable(res.getDrawable(R.drawable.tab_background));
//	    creatorIcon.setBackgroundDrawable(res.getDrawable(R.drawable.tab_background));

	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, MyVisitMap.class);
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec(TAG_MAP)
	    .setIndicator(getString(R.string.map), res.getDrawable(R.drawable.tab_globe))
	                  .setContent(intent);
	    //spec = tabHost.newTabSpec("map").setIndicator(mapIcon).setContent(intent);
	    
	    tabHost.addTab(spec);
	    
	    // creator
	    intent = new Intent().setClass(this, SurpriseCreator.class);
	    spec = tabHost.newTabSpec(TAG_CREATOR)
	    .setIndicator(getString(R.string.new_surprise), res.getDrawable(R.drawable.tab_new))
	                  .setContent(intent);
	    //spec = tabHost.newTabSpec("creator").setIndicator(creatorIcon).setContent(intent);
	    tabHost.addTab(spec);
	    
	    // list
	    intent = new Intent().setClass(this, ListMenu.class);
	    spec = tabHost.newTabSpec(TAG_LIST)
	    .setIndicator(getString(R.string.list),res.getDrawable(R.drawable.tab_list))
	    .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTabByTag(TAG_MAP);
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(Menu.NONE, LOGOUT_ID, Menu.NONE, R.string.logout);
    	return(super.onCreateOptionsMenu(menu));
    }
    
  
	public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case LOGOUT_ID:
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage(getString(R.string.logoutConfirmation))
        	       .setCancelable(false)
        	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        	        	   Editor editor = sp.edit();
        	               editor.clear();
        	               editor.commit();
        	               setResult(RESULT_OK);
        	               TabMenu.this.finish();
        	           }
        	       })
        	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   dialog.cancel();
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
        	return true;
    	}
    	return(super.onOptionsItemSelected(item));
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }
        return true;
    }

    public void onBackPressed() {
    	setResult(RESULT_CANCELED);
    }
}

