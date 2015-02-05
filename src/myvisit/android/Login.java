package myvisit.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	final int REQUEST_REGISTER = 1;
	final int REQUEST_TABMENU = 2;
	
	EditText username, password;
	Button login;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);
        
        TextWatcher tw = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(username.getText().length()>0 && password.getText().length()>0)
					login.setEnabled(true);
				else
					login.setEnabled(false);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
		};
        
		username.addTextChangedListener(tw);
		
		password.addTextChangedListener(tw);
		
		SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
	    if(sp.contains("username") && sp.contains("password") && sp.contains("password_md5"))
	    	startActivityForResult(new Intent(this, TabMenu.class),REQUEST_TABMENU);
	}
	
	public void onClickLogin(View v){
		
        try{
        	String result = "";
            
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            
            nameValuePairs.add(new BasicNameValuePair("username",username.getText().toString()));
            
            String password_md5 = "";
            MessageDigest m = null;

            try {
                    m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            }

            m.update(password.getText().toString().getBytes(),0,password.getText().toString().length());
            password_md5 = new BigInteger(1, m.digest()).toString(16);

            nameValuePairs.add(new BasicNameValuePair("password_md5",password_md5));
            
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.android.nosubjec7.net/login.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
            	sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
        	
            if(result.trim().equals("LOGIN_APPROVED")){
            	SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("username", username.getText().toString());
			    editor.putString("password", password.getText().toString());
			    editor.putString("password_md5", password_md5);
			    editor.commit();
			    Toast.makeText(this,getString(R.string.successfulLogin)+username.getText().toString(),Toast.LENGTH_SHORT).show();
			    startActivityForResult(new Intent(this, TabMenu.class),REQUEST_TABMENU);
            }else if(result.trim().equals("INVALID_USERNAME")){
            	Toast.makeText(this,getString(R.string.invalidUsername),Toast.LENGTH_LONG).show();
            }else if(result.trim().equals("LOGIN_FAILED")){
            	Toast.makeText(this,getString(R.string.loginFailed),Toast.LENGTH_SHORT).show();
            }else
            	Toast.makeText(this, getString(R.string.unknownError), Toast.LENGTH_LONG);



        }catch(Exception e){
        	Log.d("log_tag", "Error converting result "+e.toString());
        }
	}
	
	public void onActivityResult (int requestCode, int resultCode, Intent i){
    	if(requestCode==REQUEST_REGISTER && resultCode==RESULT_OK){
    		SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
    		username.setText(sp.getString("username", null));
    		password.setText(sp.getString("password", null));
    	}
    	else if(requestCode==REQUEST_TABMENU && resultCode==RESULT_CANCELED){
    		this.finish();
    	}
    }

	public void onClickRegister(View v){
	    startActivityForResult(new Intent(this,Register.class),REQUEST_REGISTER);
	}
}
