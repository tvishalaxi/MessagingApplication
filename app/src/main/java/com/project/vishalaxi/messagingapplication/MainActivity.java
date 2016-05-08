package com.project.vishalaxi.messagingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    /*Declare the fields to be bound to Widgets */
    protected EditText editText;
    protected Button submitBtn;

    //User defined request code for sub activity
    public static final int SUBACTIVITY_COMPOSE_MSG=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Bind the interface elements to the corresponding fields */
        editText=(EditText)findViewById(R.id.editText);
        submitBtn=(Button)findViewById(R.id.submit_btn);

        /*Setup listeners for the Button*/
        submitBtn.setOnClickListener(sendBtnListener);
    }

    // Listener for the send button
    public View.OnClickListener sendBtnListener = new View.OnClickListener() {

        // Called when up button is selected
        @Override
        public void onClick(View v) {

        String phoneNumberStr=getValidPhoneNumber(editText); //obtain the valid phone number from the text typed by user
        if(phoneNumberStr!=null)
             switchToComposeMessage(phoneNumberStr); // switch to message composing activity on valid phone number entry
        else
             Toast.makeText(MainActivity.this, "Enter valid Phone Number", Toast.LENGTH_LONG).show();
                                                     // display a message when text does not contain valid phone number
        }
    } ;



    /**
     * This method extracts the valid phone number from the text entered by
     * the user if it is present else it returns null.
     * @param editTextMsg This parameter contains the text entered by the user in the editview.
     * @return String This returns the valid phone number.
     * references: http://tutorials.jenkov.com/java-regex/matcher.html
    */
    public String getValidPhoneNumber(EditText editTextMsg) {

        String myText=editTextMsg.getText().toString().trim();
        String phoneNumberRegExp="\\(\\d{3}\\) ?\\d{3}-\\d{4}"; //regular expression for matching (xxx) yyy-zzzz or (xxx)yyy-zzzz
        String phoneNumberStr;

        Pattern pattern = Pattern.compile(phoneNumberRegExp);
        Matcher matcher = pattern.matcher(myText);

        if(matcher.find()) {
            phoneNumberStr = matcher.group(0); // get the string that matched the regex
            return phoneNumberStr;
        }
        else {
            return null;
        }
    }


    /**
     * This method is used to switch from the current activity
     * to Compose Message activity using intent .
     * @param phone_Number This paramter is the phone number in string format
     * @return Nothing.
    */
    public void switchToComposeMessage(String phone_Number) {

        Intent msgSendIntent = new Intent(Intent.ACTION_VIEW);
        msgSendIntent.setType("vnd.android-dir/mms-sms"); //specify explicit type of intent data for sms

        msgSendIntent.putExtra("address", phone_Number); //place the phone number to be addressed
        msgSendIntent.putExtra("sms_body", "Type your message here!"); // place the default message text

        startActivityForResult(msgSendIntent, SUBACTIVITY_COMPOSE_MSG);
    }



    /*Overridden function that is called when the MainActivity is resumed
    * It clears the initial text entered by the user and adds 'Returning from Compose Message...'*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditText editText = (EditText)findViewById(R.id.editText);

        if (requestCode == SUBACTIVITY_COMPOSE_MSG){ // check if the request code matches for the subactivity
            if(resultCode == RESULT_CANCELED) {
                editText.setText("Returning from Compose Message...");

            }
        }
    }
}
