package com.example.contactsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Creating variables
    private ArrayList<Contact> contactArrayList;
    private RecyclerView contactRV;
    private ContactRVAdapter contactRVAdapter;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views to variables
        contactArrayList = new ArrayList<>();
        contactRV = findViewById(R.id.idRVContacts);
        FloatingActionButton addNewContactFAB = findViewById(R.id.idFABAdd);
        FloatingActionButton githubFAB = findViewById(R.id.idFABGithub);
        loadingPB = findViewById(R.id.idPBLoading);

        // Calling method to prepare recycler view
        prepareContactRV();

        // Calling method to request permissions
        requestPermissions();

        // Adding onClickListener for fab add
        addNewContactFAB.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateNewContactActivity.class);
            startActivity(intent);
        });

        // Adding onClickListener for fab github
        githubFAB.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nerdya/ContactsApp"));
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Calling a menu inflater and inflating menu file
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        // Getting menu item as search view item
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        // Creating a variable for search view
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        // Setting on query text listener for search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Clearing the focus for search view.
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // On changing the text in our search view, call a filter method to filter our array list.
                filter(newText.toLowerCase());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {
        // Creating a new filtered array list
        ArrayList<Contact> filteredlist = new ArrayList<>();
        // Running a loop for checking if the item is present in array list
        for (Contact item : contactArrayList) {
            if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
                // Adding item to filtered array list.
                filteredlist.add(item);
            }
        }
        // Passing this filtered list to adapter with filter method.
        contactRVAdapter.filter(filteredlist);
    }

    private void prepareContactRV() {
        // Preparing recycler view with adapter.
        contactRVAdapter = new ContactRVAdapter(this, contactArrayList);
        // Setting layout manager
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        // Setting adapter to recycler view
        contactRV.setAdapter(contactRVAdapter);
    }

    private void requestPermissions() {
        // Request permission in the current activity
        // this method is use to handle error
        // in runtime permissions
        Dexter.withActivity(this)
                // Request the number of permissions which are required in our app.
                .withPermissions(
                        // List of permissions
                        android.Manifest.permission.READ_CONTACTS,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_CONTACTS
                )
                // Calling a with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // When all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            getContacts();
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // Check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // Permission is denied permanently, show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // When user grants some permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                })
                // Handle error in runtime permissions
                .withErrorListener(error -> {
                    // Displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                // Run the permissions on same thread and to check the permissions
                .onSameThread().check();
    }

    // Display a dialogue message
    private void showSettingsDialog() {
        // Displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Title for alert dialog
        builder.setTitle("Need Permissions");

        // Message for dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            // this method is called on click on positive
            // button and on clicking shit button we
            // are redirecting our user from our app to the
            // settings page of our app.
            dialog.cancel();
            // below is the intent from which we
            // are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // When user click on negative button
            dialog.cancel();
        });
        // Display dialog
        builder.show();
    }

    @SuppressLint("Range")
    private void getContacts() {
        //this method is use to read contact from users device.
        //on below line we are creating a string variables for our contact id and display name.
        String contactId = "";
        String displayName = "";
        //on below line we are calling our content resolver for getting contacts
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        //on blow line we are checking the count for our cursor.
        if (cursor.getCount() > 0) {
            //if the count is greater than 0 then we are running a loop to move our cursor to next.
            while (cursor.moveToNext()) {
                //on below line we are getting the phone number.
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    //we are checking if the has phone number is >0
                    //on below line we are getting our contact id and user name for that contact
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //on below line we are calling a content resolver and making a query
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);
                    //on below line we are moving our cursor to next position.
                    if (phoneCursor.moveToNext()) {
                        //on below line we are getting the phone number for our users and then adding the name along with phone number in array list.
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactArrayList.add(new Contact(contactId, displayName, phoneNumber, ""));
                    }
                    //on below line we are closing our phone cursor.
                    phoneCursor.close();
                }
            }
        }
        //on below line we are closing our cursor.
        cursor.close();
        //on below line we are hiding our progress bar and notifying our adapter class.
        loadingPB.setVisibility(View.GONE);
        contactRVAdapter.notifyDataSetChanged();
    }
}