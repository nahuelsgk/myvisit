package myvisit.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import com.google.android.maps.GeoPoint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SurpriseCreator extends Activity {
	
	private Surprise s;
	
	// these are for the camera
	private Button cameraButton;
	private Intent cameraIntent;
	private Intent audioIntent;
	private static Uri imageUri = Uri.EMPTY;
	private static Uri audioUri = Uri.EMPTY;
	private static Uri previousUri = Uri.EMPTY;
	private Calendar cal;
	private static final int CAMERA_PIC_REQUEST = 1;
	private static final int AUDIO_REC_REQUEST = 2;
	public static final String AUDIO_CODE = "AUDIO";

	private static final int IMAGE_MAX_SIZE = 300;
	public static final String PICTURE_DIR = Environment.DIRECTORY_PICTURES;
	public static final String AUDIO_DIR = Environment.DIRECTORY_MUSIC;
	
	private OnClickListener cameraListener;
	
	private RatingBar ratingBar;
	private EditText titleField;
	private EditText notesField;
	private ImageView photoView;
	private Button saveButton;
	private Button clearButton;
	private Button audioButton;
	private Button audioListenButton;
//	private Button TESTBUTTON; // TEST BUTTON ONLY
	private Bitmap bitmap;
	private ScrollView sv;
	private InputMethodManager imm;
	private int l = 200;
	private Context cont;
	private MediaPlayer mp;
	
	//private Mapper map;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creator);
        cont = this;
        // create a new Surprise
        s = new Surprise();
        
        //map = Mapper.getInstance();
        
        // empty uri
        //imageUri = Uri.EMPTY;
        
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        // the scrollview, if needed to scroll
        sv = (ScrollView) findViewById(R.id.scroll_view);
        
        cameraListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				previousUri = imageUri;
				
				// the intent for the built-in camera
			    cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			    
			    // Get current instance of the calendar
			    cal = Calendar.getInstance();
			    
			    // Own string presentation of the date/time
			    String datestr = String.format("%1$tY_%1$tm_%1$td_%1$tH_%1$tM_%1$tS", cal);

			    // file for the picture
//			    File photo = new File(Environment.getExternalStorageDirectory(), 
//			    		"pic_" + datestr + ".jpg");
			    
			    File photo = new File(getExternalFilesDir(PICTURE_DIR), "pic_" + datestr + ".jpg");
			    
			    // Set the uri for the camera intent
			    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			    
			    // Set also the local uri variable
			    imageUri = Uri.fromFile(photo);

			    // start the activity
			    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}
		};
        
        // imageview for the photo
        photoView = (ImageView) findViewById(R.id.photo_view);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        l = w < h ? w : h; // in landscape mode take the hight actually
        photoView.setLayoutParams(new LinearLayout.LayoutParams((int)(l*.8), (int)(l*.8)));
        photoView.setClickable(true);
        photoView.setOnClickListener(cameraListener);
        
        // show the current photo
        //loadPhoto();
        // try to load old photo
        loadOldPhoto();
        photoView.setImageBitmap(bitmap);
        
        // camera button, and the actual camera as it's listener
		cameraButton = (Button) findViewById(R.id.camera_button);
		cameraButton.setOnClickListener(cameraListener);
		
		// rating bar
		ratingBar = (RatingBar) findViewById(R.id.ratingbar);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		        //Toast.makeText(getApplicationContext(), "New Rating: " + rating, Toast.LENGTH_SHORT).show();
		        
		        // set rating
		        s.setRate((int)rating);
		    }
		});
		
		// title field
		titleField = (EditText) findViewById(R.id.surprise_title);
		titleField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
		        //Toast.makeText(getApplicationContext(), titleField.getText().toString(), Toast.LENGTH_SHORT).show();
		        s.setTitle(titleField.getText().toString());
				return false;
			}
		});
		
		// notes field
		notesField = (EditText) findViewById(R.id.surprise_notes);
		notesField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
		        //Toast.makeText(getApplicationContext(), notesField.getText().toString(), Toast.LENGTH_SHORT).show();
		        s.setNotes(notesField.getText().toString());
				return false;
			}
		});
		
		
        // save button
		saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// save the surprise to the database, and return!
				s.setId(Mapper.getInstance().findFreeId());
				updateSurprise();
				Mapper.getInstance().addVisitToMap(new Surprise(s));
				MyVisitMap.drawMarkers();
				
		        //Toast.makeText(getApplicationContext(), "Save it!", Toast.LENGTH_SHORT).show();

		        imm.hideSoftInputFromWindow(notesField.getWindowToken(), 0);
		        imm.hideSoftInputFromWindow(titleField.getWindowToken(), 0);
		        clearForm(false);
		        // switch to the main tab
		        ((TabActivity) getParent()).getTabHost().setCurrentTabByTag(TabMenu.TAG_MAP);
			}
		});
		
        // clear button
		clearButton = (Button) findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// save the surprise to a database, and return!
		        //Toast.makeText(getApplicationContext(), "Clear all!", Toast.LENGTH_SHORT).show();
		        clearForm(true);
		        imm.hideSoftInputFromWindow(notesField.getWindowToken(), 0);
		        imm.hideSoftInputFromWindow(titleField.getWindowToken(), 0);
			}
		});
		
//        // TESTBUTTON
//		TESTBUTTON = (Button) findViewById(R.id.TESTBUTTON);
//		TESTBUTTON.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//showDialog(SurpriseDialog.DIALOG_SURPRISE_ID);
//				updateSurprise();
//				SurpriseDialog d = new SurpriseDialog(cont, s, l);
//				d.show();
//			}
//		});
		
		audioIntent = new Intent(this, AudioRecorder.class);
		
        // audio record button
		audioButton = (Button) findViewById(R.id.audio_button);
		audioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(audioIntent, AUDIO_REC_REQUEST);
			}
		});
		
		if(s.getAudio() != null) {
			mp = MediaPlayer.create(this, Uri.fromFile(s.getAudio()));
		} else {
			mp = null;
		}
		
        // audio listen button
		audioListenButton = (Button) findViewById(R.id.audio_listen_button);
		audioListenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mp != null) {
					mp.start();
				}
			}
		});
		
    }
    
	private void clearForm(boolean deleteFoto) {
		titleField.setText("");
		notesField.setText("");
		bitmap = null;
		photoView.setImageBitmap(null);
		ratingBar.setRating(0);
		if(deleteFoto) {
			deletePhoto(imageUri);
		}
		s = new Surprise();
		
        //Toast.makeText(getApplicationContext(), "All cleared", Toast.LENGTH_SHORT).show();
        
        // scroll at top
        sv.scrollTo(0, 0);
        titleField.requestFocus();
	}
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case CAMERA_PIC_REQUEST:
			
	        if (resultCode == Activity.RESULT_OK) {
	        	loadNewPhoto();
	        	deletePhoto(previousUri);
	        }

	        break;
		case AUDIO_REC_REQUEST:
			if( resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra(AUDIO_CODE);
				if(result.length() > 0) {
					audioUri = Uri.parse(result);
					s.setAudio(new File(result));
					mp = MediaPlayer.create(this, Uri.fromFile(s.getAudio()));
				}
			}
			break;
		default:
			break;
	    }
	}
	
	private void loadNewPhoto() {
		if(imageUri == Uri.EMPTY) return;
		
    	// uri to the captured image
        Uri selectedImage = imageUri;
        
        File f = new File(imageUri.getPath());
        s.setImage(f);
        bitmap = decodeFile(f, IMAGE_MAX_SIZE);
        photoView.setImageBitmap(bitmap);
        //Toast.makeText(this, selectedImage.toString(),Toast.LENGTH_LONG).show();
        
//        getContentResolver().notifyChange(selectedImage, null);
//        ContentResolver cr = getContentResolver();
//        try {
//             bitmap = android.provider.MediaStore.Images.Media
//             .getBitmap(cr, selectedImage);
//             
//             // save the image etc..
//
//            photoView.setImageBitmap(bitmap);
//            Toast.makeText(this, selectedImage.toString(),
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
//                    .show();
//        }
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    //final Bitmap bm = bitmap;
	    //keepPhotos(list);
	    return bitmap;
	}
	
	private void loadOldPhoto() {
		final Object data = getLastNonConfigurationInstance();
		if(data != null) {
			bitmap = (Bitmap) data;
		}
	}
	
	private void deletePhoto(Uri uri) {
		if(uri == Uri.EMPTY) return;
		File f = new File(uri.getPath());
		f.delete();
		uri = Uri.EMPTY;
	}
	
	//decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f, int max_size){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        int scale = 1;
	        if (o.outHeight > max_size || o.outWidth > max_size) {
	            scale = (int) Math.pow(2, (int) Math.round(Math.log(max_size / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } catch (FileNotFoundException e) {
	    }
	    return b;
	}
	
//	protected Dialog onCreateDialog(int id) {
//	    SurpriseDialog dialog;
//	    switch(id) {
//	    case SurpriseDialog.DIALOG_SURPRISE_ID:
//	        dialog = new SurpriseDialog(this, s, l);
//	        break;
//	    default:
//	        dialog = null;
//	    }
//	    return dialog;
//	}
//	
//	protected void onPrepareDialog (int id, Dialog dialog, Bundle args) {
//		dialog.setTitle(s.getTitle());
//		//dialog.updateDialog(s);
//		SurpriseDialog dialog2 = new SurpriseDialog(this, s, l);
//		dialog = (Dialog)dialog2;
//	}
	
	private void updateSurprise() {
		s.setTitle(titleField.getText().toString());
		s.setNotes(notesField.getText().toString());
		s.setRate((int)ratingBar.getRating());
		s.setImage(new File(imageUri.getPath()));
		s.setAudio(new File(audioUri.getPath()));
		s.setCoordinate(new GeoPoint( (int)(Mapper.getInstance().getCurrentLocationLatitude()*1000000),
				(int)(Mapper.getInstance().getCurrentLocationLongitude()*1000000)));
		
	}
	
	

}
