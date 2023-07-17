package com.example.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> {
    // Creating variables for context and array list
    private Context context;
    private ArrayList<Contact> contactList;

    public ContactRVAdapter(Context context, ArrayList<Contact> contactsList) {
        this.context = context;
        this.contactList = contactsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Passing layout file for displaying our card item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_rv_item, parent, false);
        return new ViewHolder(view);
    }

    public void filter(ArrayList<Contact> filterList) {
        // Passing filtered array list in original array list
        contactList = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Getting data from array list
        Contact contact = contactList.get(position);
        // Setting data to text view
        holder.contactTV.setText(contact.getUserName());
        // Generate random color
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        // Create a TextDrawable
        // As we are building a circular drawable we are calling a build round method.
        // In that method we are passing our text and color.
        TextDrawable drawable = new TextDrawable.Builder()
                .setWidth(100)
                .setHeight(100)
                .setShape(TextDrawable.SHAPE_ROUND)
                .setText(contact.getUserName().substring(0, 1))
                .setColor(color)
                .build();
        holder.contactIV.setImageDrawable(drawable);

        // Adding onClickListener
        holder.itemView.setOnClickListener(view -> {
            // Opening new activity and passing data
            Intent intent = new Intent(context, ContactDetailActivity.class);
            intent.putExtra("userName", contact.getUserName());
            intent.putExtra("phoneNumber", contact.getPhoneNumber());
            intent.putExtra("emailAddress", contact.getEmailAddress());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Creating variable for image view and text view
        private ImageView contactIV;
        private TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing image view and text view
            contactIV = itemView.findViewById(R.id.idIVContact);
            contactTV = itemView.findViewById(R.id.idTVContactName);
        }
    }
}
