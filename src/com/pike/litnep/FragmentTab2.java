package com.pike.litnep;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentTab2 extends Fragment {

	private ListView list;

	private ArrayList<String> mContentsList;
	private ArrayAdapter<String> dataAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment2, container, false);

		list = (ListView) v.findViewById(R.id.listPosts);

		// if data is not already received then receive data from server
		// create array adapter
		mContentsList = new ArrayList<String>();
		dataAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.list_contents_main, mContentsList);
		list.setAdapter(dataAdapter);

		// TODO: load data from local database
		// TODO: and add to mContentsList

		return v;
	}
}
