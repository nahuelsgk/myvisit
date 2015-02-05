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

public class SurpriseDialog extends Dialog {
	
	public static final int DIALOG_SURPRISE_ID = 0;
	public static final int DIALOG_GAMEOVER_ID = 1; // not needed
	private static final int IMAGE_MAX_SIZE = 200;
	
//	private TextView title;
	private TextView notes;
	private RatingBar rBar;
	private ImageView photoView;
	private Bitmap bitmap;
	private Button listenButton;
	private static MediaPlayer mp;
	private static Context c;
	private static Surprise ss;

	public SurpriseDialog(Context context, Surprise s, int l) {
		super(context);
		c = context;
		ss = s;
		// TODO create the dialog
		//Dialog dialog = new Dialog(context);

		this.setContentView(R.layout.surprise_dialog);
		this.setTitle(s.getTitle());
		
//        int w = getOwnerActivity().getWindowManager().getDefaultDisplay().getWidth();
//        int h = getOwnerActivity().getWindowManager().getDefaultDisplay().getHeight();
//        l = w < h ? w : h; // in landscape mode take the hight actually

		
        // imageview for the photo
        photoView = (ImageView) findViewById(R.id.dialog_photo_view);
//        int w = getWindowManager().getDefaultDisplay().getWidth();
//        int h = getWindowManager().getDefaultDisplay().getHeight();
//        int l = w < h ? w : h; // in landscape mode take the hight actually
        photoView.setLayoutParams(new LinearLayout.LayoutParams((int)(l*.6), (int)(l*.6)));
        
        if(s.getImage() != null) {
        	bitmap = SurpriseCreator.decodeFile(s.getImage(), IMAGE_MAX_SIZE);
        } else {
        	bitmap = null;
        }
        photoView.setImageBitmap(bitmap);
        
        photoView.setClickable(true);
        photoView.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bitmap != null) {
					  PhotoDialog d = new PhotoDialog(c, ss, 300);
					  d.show();
				}
			}
        });
        
        // try to load old photo
//        loadOldPhoto();
//        photoView.setImageBitmap(bitmap);

//		title = (TextView) findViewById(R.id.dialog_surprise_title);
//		title.setText(s.getTitle());

		
		notes = (TextView) findViewById(R.id.dialog_surprise_notes);
		notes.setText(s.getNotes());
		
		rBar = (RatingBar) findViewById(R.id.dialog_ratingbar);
		rBar.setRating(s.getRate());
		

		if(s.getAudio() != null) {
			mp = MediaPlayer.create(context, Uri.fromFile(s.getAudio()));
		} else {
			mp = null;
		}
		
		listenButton = (Button) findViewById(R.id.dialog_audio_button);
		listenButton.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mp != null) {
					mp.start();
				}
			}
		});
	}
	
//	public void updateDialog(Surprise s) {
//		this.setTitle(s.getTitle());
//		notes.setText(s.getNotes());
//		rBar.setRating(s.getRate());
////		photoView.setImageBitmap(bm);
//	}


}


