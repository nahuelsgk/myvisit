package myvisit.android;

/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 */

import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class AudioRecorder extends Activity
{
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private Button mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private Button   mPlayButton = null;
    private MediaPlayer   mPlayer = null;
    
    private Button mSaveButton = null;
    private Button mCancelButton = null;
    
    static boolean mStartRecording = true;
    static boolean mStartPlaying = true;
    
    private Calendar cal;
    private Intent retVal;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
	    // Own string presentation of the date/time
	    String datestr = String.format("%1$tY_%1$tm_%1$td_%1$tH_%1$tM_%1$tS", cal);
	    mFileName = getExternalFilesDir(SurpriseCreator.AUDIO_DIR) + "/audio_" + datestr + ".3gp";
    	
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

//    class RecordButton extends Button {
//        boolean mStartRecording = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onRecord(mStartRecording);
//                if (mStartRecording) {
//                    setText("Stop recording");
//                } else {
//                    setText("Start recording");
//                }
//                mStartRecording = !mStartRecording;
//            }
//        };
//
//        public RecordButton(Context ctx) {
//            super(ctx);
//            setText("Start recording");
//            setOnClickListener(clicker);
//        }
//    }

//    class PlayButton extends Button {
//        boolean mStartPlaying = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onPlay(mStartPlaying);
//                if (mStartPlaying) {
//                    setText("Stop playing");
//                } else {
//                    setText("Start playing");
//                }
//                mStartPlaying = !mStartPlaying;
//            }
//        };
//
//        public PlayButton(Context ctx) {
//            super(ctx);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//    }

    public AudioRecorder() {
//        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/audiorecordtest.3gp";
//	    File photo = new File(getExternalFilesDir(SurpriseCreator.PICTURE_DIR), "pic_" + datestr + ".jpg");

//	    mFileName = getExternalFilesDir(SurpriseCreator.AUDIO_DIR) + "/audio_" + datestr + ".3gp";
	    //mFileName.toString();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.recorder);
        
        mFileName = "";
        
	    // Get current instance of the calendar
	    cal = Calendar.getInstance();
	    
        retVal = new Intent();
        retVal.putExtra(SurpriseCreator.AUDIO_CODE, "");
        this.setResult(RESULT_OK, retVal);
        
        mRecordButton = (Button) findViewById(R.id.audio_record_button);
        mRecordButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                	mRecordButton.setText(R.string.stop_record);
                } else {
                    mRecordButton.setText(R.string.start_record);
                }
                mStartRecording = !mStartRecording;
            }
        });
        
        mPlayButton = (Button) findViewById(R.id.audio_listen_button);
        mPlayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    mPlayButton.setText(R.string.stop_listen);
                } else {
                    mPlayButton.setText(R.string.start_listen);
                }
                mStartPlaying = !mStartPlaying;
            }
        });
        
        mSaveButton = (Button) findViewById(R.id.audio_save_button);
        mSaveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	retVal.putExtra(SurpriseCreator.AUDIO_CODE, mFileName);
                finish();
            }
        });
        
        mCancelButton = (Button) findViewById(R.id.audio_cancel_button);
        mCancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	File f = new File(mFileName);
            	if(f != null) {
            		f.delete();
            	}
                finish();
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}