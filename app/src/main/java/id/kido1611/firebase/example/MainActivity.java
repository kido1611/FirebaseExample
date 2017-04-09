package id.kido1611.firebase.example;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.kido1611.firebase.example.model.Users;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "Firebase";

    private Button mButtonTambah;
    private ListView mListView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private List<Users> userLists = new ArrayList<Users>();
    UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonTambah = (Button) findViewById(R.id.buttonTambah);
        mListView = (ListView) findViewById(R.id.list);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("CRUD");

        adapter = new UserListAdapter(this, userLists);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogTambahUser dialog_add = new DialogTambahUser();
                dialog_add.setDatabaseRef(mDatabaseReference);
                dialog_add.setUser(userLists.get(position));
                dialog_add.show(getSupportFragmentManager(), "TambahUser");
            }
        });

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("abdu", "jumlah: "+String.valueOf(dataSnapshot.child("users").getChildrenCount()));
                userLists.clear();
                for(DataSnapshot data : dataSnapshot.child("users").getChildren()){
                    userLists.add(data.getValue(Users.class));
                }
                adapter.notifyDataSetChanged();
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
        private Button mButtonHapus;

        private DatabaseReference mRef;
        private Users currentUsers;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public void setUser(Users user){
            currentUsers = user;
        }

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
            mButtonHapus = (Button) rootView.findViewById(R.id.button3);

            if(currentUsers!=null){
                mEditUsername.setText(currentUsers.getUsername());
                mEditPassword.setText(currentUsers.getPassword());
            }

            mButtonTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentUsers==null){
                        currentUsers = new Users();
                        currentUsers.setKey(null);
                    }
                    currentUsers.setUsername(mEditUsername.getText().toString());
                    currentUsers.setPassword(mEditPassword.getText().toString());

                    if(currentUsers.getKey()==null){
                        currentUsers.setKey(String.valueOf(currentUsers.hashCode()));
                    }
                    mRef.child("users").child(currentUsers.getKey()).setValue(currentUsers);
                    dismiss();
                }
            });
            mButtonHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef.child("users").child(currentUsers.getKey()).removeValue();
                    dismiss();
                }
            });

            getDialog().setTitle("Tambah User");

            return rootView;
        }
    }


}
