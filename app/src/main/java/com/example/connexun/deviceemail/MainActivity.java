package com.example.connexun.deviceemail;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_CODE = 1111;
    TextView helloo,name, email;
    String Name,Email;
    LinearLayout parentLL;
    CheckBox send_SMS;
    String sms="";
    @SuppressLint("MissingPermission")
    ListView listview;
    ArrayList<String> names ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLL=(LinearLayout)findViewById(R.id.parent_ll);
        email = (TextView) findViewById(R.id.email_id);
        listview=(ListView) findViewById(R.id.listview);
        names = new ArrayList<String>();

        getpermissions();
        String User_EmailId = null;
        String fullName = null;
        try {
            User_EmailId = getEmailID(getApplicationContext());
            System.out.println("Accounts=="+User_EmailId);
            fullName = User_EmailId.substring(0,User_EmailId.lastIndexOf("@"));
            System.out.println("Account name=="+fullName);
            if (checkPermission()) {
                email.setText(User_EmailId);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // listview.setAdapter(arrayAdapter);

    }

    private String getEmailID(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }

    }

    public static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }


    //Requesting device permissions and Handling
    private void getpermissions() {
        if (!checkPermission()) {
            requestPermission();
        } else {
        }
    }

    //To check mobile device permissions
    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{GET_ACCOUNTS,READ_CONTACTS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean account = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (account ){
                        Toast.makeText(this, "Permission Granted, Now you can access all the features of app.", Toast.LENGTH_SHORT).show();
                        email.setText(getEmailID(getApplicationContext()));

                    }else {
                        email.setText("--");
                        Toast.makeText(this, "Permission Denied,Please restart the app", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                                showMessageOKCancel("You need to allow all permissions to use features of app",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{GET_ACCOUNTS,READ_CONTACTS},
                                                    PERMISSION_REQUEST_CODE);
                                        }
                                    }});return; } } } }
                break; } }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNeutralButton("Cancel", null)
                .setCancelable(false)
                // .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}