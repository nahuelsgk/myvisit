package myvisit.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class PhotoDialog extends Dialog {
	
	public static final int DIALOG_SURPRISE_ID = 0;
	public static final int DIALOG_GAMEOVER_ID = 1; // not needed
	private static final int IMAGE_MAX_SIZE = 800;
	
	private ImageView photoView;
	private Bitmap bitmap;

	public PhotoDialog(Context context, Surprise s, int l) {
		super(context);
		// TODO create the dialog
		//Dialog dialog = new Dialog(context);

		this.setContentView(R.layout.photo_dialog);
		this.setTitle(s.getTitle());
		

		
        // imageview for the photo
        photoView = (ImageView) findViewById(R.id.dialog_full_photo_view);
//        int w = getWindowManager().getDefaultDisplay().getWidth();
//        int h = getWindowManager().getDefaultDisplay().getHeight();
//        int l = w < h ? w : h; // in landscape mode take the hight actually
        //photoView.setLayoutParams(new LinearLayout.LayoutParams((int)(l*.6), (int)(l*.6)));
        
        if(s.getImage() != null) {
        	bitmap = SurpriseCreator.decodeFile(s.getImage(), IMAGE_MAX_SIZE);
        } else {
        	bitmap = null;
        }
        photoView.setImageBitmap(bitmap);
        
        
        // try to load old photo
//        loadOldPhoto();
//        photoView.setImageBitmap(bitmap);

//		title = (TextView) findViewById(R.id.dialog_surprise_title);
//		title.setText(s.getTitle());


	}
	
	public void updateDialog(Surprise s) {
	}


}


