package myvisit.android;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private static int indexCur;

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		  //super(defaultMarker);
		super(boundCenterBottom(defaultMarker));
		  mContext = context;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public void removeOverlay(int index) {
		mOverlays.remove(index);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		indexCur = index;
		//Toast.makeText(mContext, "index: " + index, Toast.LENGTH_SHORT).show();
		OverlayItem item = mOverlays.get(index);
//	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//	  dialog.setTitle(item.getTitle());
//	  dialog.setMessage(item.getSnippet());
//	  dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//	    	public void onClick(DialogInterface dialog, int id) {
//	    		Toast.makeText(mContext, "title: " + mOverlays.get(indexCur).getTitle(), Toast.LENGTH_SHORT).show();
//	    		//plist.remove(MyMenu.findPlayer(item.))
//	    //		removeOverlay(indexCur);
//	    	}
//	    });
//	  dialog.show();
		
		if(MyVisitMap.NO_ID.equals(item.getSnippet())) {
    		Toast.makeText(mContext, "That's you, dummy", Toast.LENGTH_SHORT).show();
			return false;
		}
		
	  //Dialog d = new Dialog(mContext);
	  Surprise s = Mapper.getInstance().findSurprise(Integer.parseInt(item.getSnippet()));
	  //SurpriseDialog d2 = new SurpriseDialog(mContext, new Surprise("new test", 2, null, null, null, "some notes"), 300);
	  SurpriseDialog d2 = new SurpriseDialog(mContext, s, 300);
	  d2.show();
	  return true;
	}

}