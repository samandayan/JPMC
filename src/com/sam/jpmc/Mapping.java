package com.sam.jpmc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapping extends Activity {

	// The GoogleMap Fragment
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapping);

		getActionBar().setTitle("Please see the Locations Below:");
		getActionBar().setSubtitle("\"So You Can\"");

		MapFragment mapFragment = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.fragment1));

		map = mapFragment.getMap();

		final ArrayList<Bank> bank = (ArrayList<Bank>) getIntent()
				.getSerializableExtra("list");

		// Setting the angle view of the camera to the first
		// found bank or ATM
		Double lattidude = Double.parseDouble(bank.get(0).getLat());
		Double longitude = Double.parseDouble(bank.get(0).getLng());

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lattidude,
				longitude), 12));
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// Adding the marker to the interactive Google Map
		// The default marker is red. That is changed to blue
		// instead to better resemble Chase.
		for (int i = 0; i < bank.size(); i++) {
			Double lat = Double.parseDouble(bank.get(i).getLat());
			Double lng = Double.parseDouble(bank.get(i).getLng());
			LatLng latLng = new LatLng(lat, lng);
			map.addMarker(new MarkerOptions().position(latLng).icon(
					BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		}

		// Setting the action to take when a marker is clicked.
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// This is the location of the marker
				// that gets clicked on the interactive
				// Google Map.
				LatLng where = marker.getPosition();

				// Find the bank associated with the marker
				// to send it to the DetailsPage for viewing
				// the details of the selected bank or ATM.
				for (int i = 0; i < bank.size(); i++) {
					if (bank.get(i).getLat().equals(where.latitude + "")
							&& bank.get(i).getLng()
									.equals(where.longitude + "")) {
						Intent intent = new Intent(Mapping.this,
								DetailsPage.class);
						intent.putExtra("bank", bank.get(i));
						startActivity(intent);
					}
				}

				return false;
			}
		});

	}
}
