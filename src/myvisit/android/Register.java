package myvisit.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
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

public class Register extends Activity {
    EditText username, email, password, passwordConfirm;
    Button register;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        username=(EditText) findViewById(R.id.username);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        passwordConfirm=(EditText) findViewById(R.id.passwordConfirm);
        
        register=(Button) findViewById(R.id.register);
        
        TextWatcher tw = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(username.getText().toString().length()>0 && 
						email.getText().toString().length()>0 &&
						password.getText().toString().length()>=6 &&
						passwordConfirm.getText().toString().length()>=6)
					register.setEnabled(true);
				else
					register.setEnabled(false);
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
		email.addTextChangedListener(tw);
		password.addTextChangedListener(tw);
		passwordConfirm.addTextChangedListener(tw);
		
	}
	
	public void onClickRegister(View v){
		//verify the equality of the passwords
		if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
			Toast.makeText(this, getString(R.string.passwordMismatch), Toast.LENGTH_SHORT).show();
		}else{
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
		            nameValuePairs.add(new BasicNameValuePair("email",email.getText().toString()));
		            
		            HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://www.android.nosubjec7.net/register.php");
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
		            Log.d("result",result);
		        	
		            if(result.trim().equals("REGISTER_OK")){
		            	SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("username", username.getText().toString());
						editor.putString("password", password.getText().toString());
					    editor.commit();
					    setResult(RESULT_OK);
					    this.finish();
		            }
		            else if(result.trim().equals("USERNAME_EXISTS")){
		    			Toast.makeText(this, getString(R.string.usernameExists), Toast.LENGTH_SHORT).show();
		            }
		            else if(result.trim().equals("EMAIL_EXISTS")){
		    			Toast.makeText(this, getString(R.string.emailExists), Toast.LENGTH_SHORT).show();
		            }
		            else
		            	Toast.makeText(this, getString(R.string.unknownError), Toast.LENGTH_LONG);
		        }catch(Exception e){
		        	Log.d("log_tag", "Error converting result "+e.toString());
		        }
		}
	}
}
