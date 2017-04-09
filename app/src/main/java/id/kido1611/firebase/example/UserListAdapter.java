package id.kido1611.firebase.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import id.kido1611.firebase.example.model.Users;

/**
 * Created by abdusy on 09/04/17.
 */

public class UserListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Users> mUsersList;

    public UserListAdapter(Context ctx, List<Users> user){
        mContext = ctx;
        mUsersList = user;
    }

    @Override
    public int getCount() {
        return mUsersList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null, false);
        TextView title = (TextView) rootView.findViewById(android.R.id.text1);
        Users currentUser = mUsersList.get(position);

        title.setText(currentUser.getUsername());
        return rootView;
    }
}
