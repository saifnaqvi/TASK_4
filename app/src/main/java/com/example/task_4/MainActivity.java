package com.example.task_4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements ContactListFragment.OnContactSelectedListener {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            initFragment();
        }
    }
    private void initFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.contact_list_container) == null) {
            ContactListFragment fragment = new ContactListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contact_list_container, fragment)
                    .commit();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initFragment();
            } else {
            }
        }
    }
    @Override
    public void onContactSelected(String contactId) {
        ContactDetailFragment detailFragment = ContactDetailFragment.newInstance(contactId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contact_detail_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
