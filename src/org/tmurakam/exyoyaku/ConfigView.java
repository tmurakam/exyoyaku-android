package org.tmurakam.exyoyaku;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.SharedPreferences;

public class ConfigView extends Activity {
	private EditText editUserId;
	private EditText editPassword;
	private SharedPreferences pref;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.config);
		editUserId = (EditText)findViewById(R.id.EditUserId);
		editPassword = (EditText)findViewById(R.id.EditPassword);
		
		pref = this.getSharedPreferences("userConfig", Activity.MODE_PRIVATE);
		editUserId.setText(pref.getString("UserId", ""));
		editPassword.setText(pref.getString("Password", ""));
		
		Button okButton = (Button)findViewById(R.id.ButtonOk);
		okButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences.Editor e;
				e = pref.edit();
				String s;
				
				s = editUserId.getText().toString();
				e.putString("UserId", s);
				s = editPassword.getText().toString();
				e.putString("Password", s);
				e.commit();
				
				finish();
			}
		});
	}

}
