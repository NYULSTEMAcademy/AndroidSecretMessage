package com.nyul.secretmessage;


import org.joda.time.DateTime;

import com.framentos.hellonfc.R;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity 
	implements CreateNdefMessageCallback, View.OnClickListener {
    NfcAdapter mNfcAdapter;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView textView = (TextView) findViewById(R.id.textView1);
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
    	String message = ((EditText) findViewById(R.id.edit_message)).getText().toString();
    	
        String text = (message);
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "application/vnd.com.framentos.hellonfc;", text.getBytes())
         /**
          * The Android Application Record (AAR) is commented out. When a device
          * receives a push with an AAR in it, the application specified in the AAR
          * is guaranteed to run. The AAR overrides the tag dispatch system.
          * You can add it back in to guarantee that this
          * activity starts when receiving a beamed message. For now, this code
          * uses the tag dispatch system.
          */
          //,NdefRecord.createApplicationRecord("com.example.android.beam")
        });
        return msg;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        textView = (TextView) findViewById(R.id.textView1);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        textView.setText(new String(msg.getRecords()[0].getPayload()));
        
        TextView timeSignature = (TextView) findViewById(R.id.time_signature);

        timeSignature.setText("\n\n\n Beam Received Time: " + DateTime.now());
       
        ((Button)findViewById(R.id.decrypt_button)).setVisibility(View.VISIBLE);
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
        case R.id.encrypt_button:
            onClickEncrypt( (Button) v);
            break;
        case R.id.decrypt_button:
        	onClickDecrypt( (Button) v );
        	break;
        }
    }
    private void onClickDecrypt(Button v) {
        textView = (TextView) findViewById(R.id.textView1);
    	String receivedMessage = textView.getText().toString();
    	String decryptedText = Cipher.decrypt(receivedMessage);
    	textView.setText(decryptedText);
	}

	public void onClickEncrypt(Button b) {
    	EditText editView = (EditText) findViewById(R.id.edit_message);
    	String message = editView.getText().toString();
    	String encryptedMessage = Cipher.encrypt(message);
    	
    	editView.setText(encryptedMessage);
    	
    }
}