package sis.pewpew.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sis.pewpew.R;
import sis.pewpew.connections.GoogleAuthActivity;

public class NetIntegrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login";
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String username = user.getDisplayName();
    public String email = user.getEmail();
    public Uri photo = user.getPhotoUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

        if (user == null) {
            Intent intent = new Intent(this, GoogleAuthActivity.class);
            startActivity(intent);
            finish();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    if (user.getDisplayName() != null) {
                        TextView navUserName = (TextView) headerView.findViewById(R.id.user_display_name);
                        navUserName.setText(username);
                    } else {
                        TextView navUserName = (TextView) headerView.findViewById(R.id.user_display_name);
                        navUserName.setText("Имя пользователя");
                    }
                    if (user.getEmail() != null) {
                        TextView navUserEmail = (TextView) headerView.findViewById(R.id.user_email);
                        navUserEmail.setText(email);
                    } else {
                        TextView navUserEmail = (TextView) headerView.findViewById(R.id.user_email);
                        navUserEmail.setText("Адрес электронной почты");
                    }
                    if (user.getPhotoUrl() != null) {
                        ImageView navUserPhoto = (ImageView) headerView.findViewById(R.id.user_icon);
                        navUserPhoto.setImageURI(photo);
                    }
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
