package com.example.task_4;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ContactListFragment extends Fragment {
    private static final String TAG = "ContactListFragment";
    private OnContactSelectedListener listener;
    public interface OnContactSelectedListener {
        void onContactSelected(String contactId);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactSelectedListener) {
            listener = (OnContactSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnContactSelectedListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ListView listView = view.findViewById(R.id.contact_list);

        ArrayList<String> contacts = new ArrayList<>();
        final ArrayList<String> contactIds = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            if (idIndex != -1 && nameIndex != -1) {
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(idIndex);
                    String displayName = cursor.getString(nameIndex);
                    contacts.add(displayName);
                    contactIds.add(contactId);
                }
            } else {
                Log.e(TAG, "Column indices not found.");
            }
            cursor.close();
        } else {
            Log.e(TAG, "Cursor is null.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onContactSelected(contactIds.get(position));
                }
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
