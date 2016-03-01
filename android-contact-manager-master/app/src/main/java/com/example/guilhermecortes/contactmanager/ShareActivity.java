package com.example.guilhermecortes.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Matt on 2/29/2016.
 */
public class ShareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String type = intent.getType();
        String action = intent.getAction();

        if(Intent.ACTION_SEND.equals(action) && type!=null){
            if(type.equals("text/plain")){
                Intent next = new Intent(this,MainActivity.class);
                next.putExtra("address", intent.getStringExtra(Intent.EXTRA_TEXT));
                startActivity(next);
            }
        }
    }
    //if(Intent.ACTION_SEND.equals(action) && type!=null){
    //if(type.equals("text/plain")){
    //    String message = intent.getStringExtra(Intent.EXTRA_TEXT);
    //    getIntent().getStringExtra("key")
    //    TextView textView = (TextView) findViewById(R.id.txtAddress);
     //   textView.setText(message);

    //}
//}
}
