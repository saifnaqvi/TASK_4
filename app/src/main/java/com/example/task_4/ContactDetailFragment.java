package com.example.task_4;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ContactDetailFragment extends Fragment {

    private static final String ARG_CONTACT_ID = "contact_id";
    private static final String TAG = "ContactDetailFragment";

    private String contactId;

    public static ContactDetailFragment newInstance(String contactId) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactId = getArguments().getString(ARG_CONTACT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        TextView contactName = view.findViewById(R.id.contact_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView contactPhone = view.findViewById(R.id.contact_phone);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView contactEmail = view.findViewById(R.id.contact_email);

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts._ID + " = ?";
        String[] selectionArgs = new String[]{contactId};
        Cursor cursor = getActivity().getContentResolver().query(contactUri, projection, selection, selectionArgs, null);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            if (nameIndex != -1) {
                if (cursor.moveToFirst()) {
                    String displayName = cursor.getString(nameIndex);
                    contactName.setText(displayName);
                }
            } else {
                Log.e(TAG, "Name column index not found.");
            }
            cursor.close();
        } else {
            Log.e(TAG, "Cursor is null.");
        }

        // Query phone numbers
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] phoneProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] phoneSelectionArgs = new String[]{contactId};
        Cursor phoneCursor = getActivity().getContentResolver().query(phoneUri, phoneProjection, phoneSelection, phoneSelectionArgs, null);

        StringBuilder phoneNumbers = new StringBuilder();
        if (phoneCursor != null) {
            int phoneNumberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (phoneNumberIndex != -1) {
                while (phoneCursor.moveToNext()) {
                    phoneNumbers.append(phoneCursor.getString(phoneNumberIndex)).append("\n");
                }
            } else {
                Log.e(TAG, "Phone number column index not found.");
            }
            phoneCursor.close();
        } else {
            Log.e(TAG, "Phone cursor is null.");
        }
        contactPhone.setText(phoneNumbers.toString().trim());

        // Query email addresses
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] emailProjection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
        String emailSelection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        String[] emailSelectionArgs = new String[]{contactId};
        Cursor emailCursor = getActivity().getContentResolver().query(emailUri, emailProjection, emailSelection, emailSelectionArgs, null);

        StringBuilder emailAddresses = new StringBuilder();
        if (emailCursor != null) {
            int emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);

            if (emailIndex != -1) {
                while (emailCursor.moveToNext()) {
                    emailAddresses.append(emailCursor.getString(emailIndex)).append("\n");
                }
            } else {
                Log.e(TAG, "Email column index not found.");
            }
            emailCursor.close();
        } else {
            Log.e(TAG, "Email cursor is null.");
        }
        contactEmail.setText(emailAddresses.toString().trim());

        return view;
    }
}
