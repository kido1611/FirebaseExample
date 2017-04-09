package id.kido1611.firebase.example;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.kido1611.firebase.example.model.Users;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "Firebase";

    private Button mButtonTambah;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonTambah = (Button) findViewById(R.id.buttonTambah);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("CRUD");


        Users userBaru = new Users("Ahmad", "tes");
        mDatabaseReference.child("users").child(String.valueOf(userBaru.hashCode())).setValue(userBaru);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mButtonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTambahUser dialog_add = new DialogTambahUser();
                dialog_add.setDatabaseRef(mDatabaseReference);
                dialog_add.show(getSupportFragmentManager(), "TambahUser");
            }
        });
    }

    public static class DialogTambahUser extends DialogFragment{

        private EditText mEditUsername;
        private EditText mEditPassword;
        private Button mButtonTambah;

        private DatabaseReference mRef;

        public void setDatabaseRef(DatabaseReference ref){
            this.mRef = ref;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog_add, container, false);

            mEditUsername = (EditText) rootView.findViewById(R.id.editText);
            mEditPassword = (EditText) rootView.findViewById(R.id.editText2);
            mButtonTambah = (Button) rootView.findViewById(R.id.button2);

            mButtonTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Users newUsers = new Users();
                    newUsers.setUsername(mEditUsername.getText().toString());
                    newUsers.setPassword(mEditPassword.getText().toString());

                    mRef.child("users").child(String.valueOf(newUsers.hashCode())).setValue(newUsers);

                    dismiss();

                }
            });

            getDialog().setTitle("Tambah User");

            return rootView;
        }
    }


}
