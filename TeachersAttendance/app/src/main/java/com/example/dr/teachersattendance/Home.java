package com.example.dr.teachersattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by DR on 1/10/2017.
 */
public class Home extends Fragment {
    String username;
public Home()
{

}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        username=getActivity().getIntent().getStringExtra("username");
        if (username!=null) {
            Intent i = new Intent(getActivity(),MainActivity.class);
        }
        else {
            getActivity().getFragmentManager().popBackStackImmediate();
            getActivity().finish();
            Toast.makeText(getActivity().getApplicationContext(), "Incorrect Username or password !!!\n or Check your Internet connection", Toast.LENGTH_LONG).show();
        }
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
