package com.example.contactsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ContactDetailActivity extends AppCompatActivity {
    //creating variables for our image view and text view and string. .
    private String contactId, contactName, contactNumber;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        //on below line we are getting data which we passed in our adapter class with intent.
        contactId = getIntent().getStringExtra("id");
        contactName = getIntent().getStringExtra("userName");
        contactNumber = getIntent().getStringExtra("phoneNumber");
        //initializing our views.
        nameTV = findViewById(R.id.idTVName);
        contactIV = findViewById(R.id.idIVContact);
        contactTV = findViewById(R.id.idTVPhone);
        nameTV.setText(contactName);
        contactTV.setText(contactNumber);
        callIV = findViewById(R.id.idIVCall);
        messageIV = findViewById(R.id.idIVMessage);
        editButton = findViewById(R.id.idButtonEdit);
        deleteButton = findViewById(R.id.idButtonDelete);
        //on below line adding click listener for our calling image view
        callIV.setOnClickListener(v -> {
            //calling a method to make a call
            makeCall(contactNumber);
        });
        //on below line adding on click listener for our message image view
        messageIV.setOnClickListener(v -> {
            //calling a method to send message
            sendMessage(contactNumber);
        });
        //on below line adding on click listener for edit contact
        editButton.setOnClickListener(v -> {
            editContact();
        });
        //on below line adding on click listener for delete contact
        deleteButton.setOnClickListener(v -> {
            deleteContact();
        });
    }

    private void sendMessage(String contactNumber) {
        //in this method we are calling an intent to send sms.
        //on below line we are passing our contact number.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactNumber));
        intent.putExtra("sms_body", "Enter your message");
        startActivity(intent);
    }

    private void makeCall(String contactNumber) {
        //this method is called for making a call.
        //on below line we are calling an intent to make a call.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //on below line we are setting data to it.
        callIntent.setData(Uri.parse("tel:" + contactNumber));
        //on below line we are checking if the calling permissions are granted or not.
        if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //at last we are starting activity.
        startActivity(callIntent);
    }

    private void editContact() {
        // TODO: edit contact
    }

    private void deleteContact() {
        // Display a confirmation dialog for deleting the contact
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactDetailActivity.this);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Delete the contact
            // Implement your code to delete the contact from your data source or database
            // Create a URI for the contact to be deleted
            Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);

            // Perform the deletion
            int rowsDeleted = getContentResolver().delete(contactUri, null, null);

            if (rowsDeleted > 0) {
                // Contact deleted successfully
                Toast.makeText(ContactDetailActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                finish(); // Finish the activity or perform any other desired actions
            } else {
                // Failed to delete contact
                Toast.makeText(ContactDetailActivity.this, "Failed to delete contact", Toast.LENGTH_SHORT).show();
            }
            // Once the contact is deleted, you can navigate back to the previous activity or perform any other desired actions
            finish(); // Finish the current activity and return to the previous one
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
