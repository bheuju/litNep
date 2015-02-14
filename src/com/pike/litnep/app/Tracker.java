package com.pike.litnep.app;

import java.util.HashMap;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.GoogleAnalytics;

import android.app.Application;

public class Tracker extends Application {
	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 * 
	 * A single tracker is usually enough for most purposes. In case you do need
	 * multiple trackers, storing them all in Application object helps ensure
	 * that they are created only once per application instance.
	 */
	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app
		GLOBAL_TRACKER, // Tracker used by all the apps from a company
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company
	}

	synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {
			
			//GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// Tracker t = (trackerId == TrackerName.APP_TRACKER) ?
			// analytics.newTracker() :
			//EasyTracker.getInstance(ctx)
		}
		return mTrackers.get(trackerId);
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

}
