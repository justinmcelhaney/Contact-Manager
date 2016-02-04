package com.example.guilhermecortes.contactmanager;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.text.Spanned;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.style.StyleSpan;
import  android.graphics.Typeface;
public class MainActivity extends Activity {

    private EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    ImageView contactImageImgView;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    Uri imageURI = null;
    public String[] choices = {
            "Phone Number",
            "Email",
            "Address",
    };
    final ArrayList<Integer>  userchoices  = new ArrayList();
    SharedPreferences sharedpreferences;
    int privacy = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedpreferences = sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);


        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contacts.add(new Contact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(), imageURI));
                populateList();
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() +  " has been added to your Contacts!", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            //habilitar o botao se o valor do campo for diferente de vazio
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty()); //trim para "cortar os espaços em branco"
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });

    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if (resCode == RESULT_OK){
            if (reqCode == 1){
                imageURI = data.getData();
                contactImageImgView.setImageURI(data.getData());
            }
        }
    }

    private void populateList(){
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    //add contact
//    private void addContact(String name, String phone, String email, String address){
//        Contacts.add(new Contact(name, phone, email, address));
//    }

    private class ContactListAdapter extends ArrayAdapter<Contact>{
        public ContactListAdapter(){
            super(MainActivity.this, R.layout.listview_item, Contacts);
        }

        //criar função para retornar o emelento do array
        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = Contacts.get(position);

            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.get_name());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.get_phone());
            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.get_email());
            TextView address = (TextView) view.findViewById(R.id.cAddress);
            sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            if(sharedpreferences.contains("Choice 3")) {
                SpannableString ss = new SpannableString(currentContact.get_address());
                ss.setSpan(new StyleSpan(Typeface.ITALIC), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new URLSpan("http://google.com"), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                address.setText(ss);
                address.setMovementMethod(LinkMovementMethod.getInstance());
            } else{
                address.setText(currentContact.get_address());
            }

            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContact.get_imageURI());
            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final List<Integer>  userchoices  = new ArrayList();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Privacy Settings");
            builder.setMultiChoiceItems(choices, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                userchoices.add(which);
                            } else if (userchoices.contains(which)) {
                                userchoices.remove(Integer.valueOf(which));
                            }
                        }
                    }
            );
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK, so save the mSelectedItems results somewhere
                    // or return them to the component that opened the dialog
                    sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    for(int i = 0; i < userchoices.size(); i++) {
                        if ((userchoices.get(i)).equals(0)  && i == 0) {
                            editor.putInt("Choice 1", userchoices.get(i) + 1);
                        } else if (userchoices.get(i).equals(1) && i == 0) {
                            editor.putInt("Choice 2", userchoices.get(i));
                        } else if (userchoices.get(i).equals(2) && i == 0) {
                            editor.putInt("Choice 3", userchoices.get(i) - 1);
                        } else if (userchoices.get(i).equals(1) && i == 1) {
                            editor.putInt("Choice 2", userchoices.get(i));
                        } else if (userchoices.get(i).equals(2) && i == 1) {
                            editor.putInt("Choice 3", userchoices.get(i) - 1);
                        } else if (userchoices.get(i).equals(2) && i == 2) {
                            editor.putInt("Choice 3", userchoices.get(i) - 1);
                        }
                    }
                    editor.commit();
                    populateList();
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
