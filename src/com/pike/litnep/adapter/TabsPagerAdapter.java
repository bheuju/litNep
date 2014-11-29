package com.pike.litnep.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pike.litnep.FragmentTab1;
import com.pike.litnep.FragmentTab2;
import com.pike.litnep.FragmentTab3;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		switch (index)
		{
		//case 0:
		//	return new FragmentTab1();
		case 0:
			return new FragmentTab2();
		//case 2:
		//	return new FragmentTab3();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

}
