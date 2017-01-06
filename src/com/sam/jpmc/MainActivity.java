package com.sam.jpmc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	// Used to receive GPS Coordinates from the system.
	private LocationListener locationListener;
	private LocationManager locationManager;

	// The URL off of which the JSON data will be pulled from.
	private String base_url;

	// The list of banks.
	private ArrayList<Bank> banks = new ArrayList<Bank>();
	BankArrayAdapter bankAdapter;

	// The list to show the result on.
	ListView results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle("Acquiring GPS Signal");

		results = (ListView) findViewById(R.id.results);

		bankAdapter = new BankArrayAdapter(MainActivity.this,
				R.layout.information, banks);

		results.setAdapter(bankAdapter);

		// Setting the action to take when an item is clicked.
		results.setOnItemClickListener(itemClickListener);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Setting the locationListener to the location manager.
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {

				// updates the URL base off of which the JSON data will be
				// pulled.
				base_url = "https://m.chase.com/PSRWeb/location/list.action?lat="
						+ location.getLatitude()
						+ "&lng="
						+ location.getLongitude();

				getActionBar().setTitle("Please See the List Below:");
				getActionBar().setSubtitle("\"So You Can\"");

				// Run the task in background to parse JSON data.
				perform();

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

		};

		// To Preserver battery check for location every two minutes and every
		// 500 meters traveled.
															/* Changing this to different
															 * providers will resolve the
															 * issue on physical phones.
															 */
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				120000, 500, locationListener);

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Send the data for the selected bank and banks to the Mapping
			// class
			// to view the banks on an interactive Google map
			Intent intent = new Intent(MainActivity.this, Mapping.class);
			intent.putExtra("bank", banks.get(position));
			intent.putExtra("list", banks);

			startActivity(intent);
		}
	};

	public void perform() {
		// Clear the list in case a new location coordinates
		// is received from the system.
		banks.clear();
		// Run the thread to receive data for parsing.
		new JSONParserTask().execute(base_url);
	}

	// The method used to parse the given JSON data.
	private void parseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);

			JSONArray locations = reader.getJSONArray("locations");

			for (int i = 0; i < locations.length(); i++) {
				JSONObject jsonObject = locations.getJSONObject(i);
				Bank bank = new Bank();
				bank.setLocType(jsonObject.getString("locType"));
				bank.setDistance(jsonObject.getString("distance"));
				bank.setName(jsonObject.getString("name"));
				bank.setAddress(jsonObject.getString("address"));

				bank.setServices(jsonObject.getString("services"));
				bank.setState(jsonObject.getString("state"));
				bank.setLabel(jsonObject.getString("label"));
				bank.setCity(jsonObject.getString("city"));
				bank.setZip(jsonObject.getString("zip"));
				bank.setBank(jsonObject.getString("bank"));

				// Check to values that are not all existent for all banks and
				// ATMS
				// This is used to prevent exception being thrown.
				Iterator<String> iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();

					if (key.equals("atms"))
						bank.setAtms(jsonObject.getInt("atms"));

					else if (key.equals("type"))
						bank.setType(jsonObject.getString("type"));

					else if (key.equals("lobbyHrs"))
						bank.setLobbyHrs(jsonObject.getString("lobbyHrs"));

					else if (key.equals("driveUpHrs"))
						bank.setDriveUpHrs(jsonObject.getString("driveUpHrs"));

					else if (key.equals("phone"))
						bank.setPhone(jsonObject.getString("phone"));

					else if (key.equals("lat"))
						bank.setLat(jsonObject.getString("lat"));

					else if (key.endsWith("lng"))
						bank.setLng(jsonObject.getString("lng"));
				}
				banks.add(bank);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// The connection the server get s established here.
	private void fetchLocationData(String urlString) {

		try {
			URL url = new URL(base_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(150000);
			conn.connect();

			InputStream stream = conn.getInputStream();

			// Reading JSON data
			String data = convertStreamToString(stream);

			// Parse the JSON data into readable format.
			parseJSON(data);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Reading the text and returning the entire JSON Object
	// for parsing
	public String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner scanner = new java.util.Scanner(is)
				.useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	// The thread to fetch data to avoid
	// Android not Responding(ANR)
	private class JSONParserTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			fetchLocationData(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// Updating the adapter after a location change.
			bankAdapter.notifyDataSetChanged();

		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		// To Preserver battery check for location every two minutes and every
		// 500 meters traveled.
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				120000, 500, locationListener);
	}

	@Override
	protected void onPause() {
		// Releasing the resource for other Activities, and/or apps
		// to use.
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

	// The custom adapter to use for the listview.
	class BankArrayAdapter extends ArrayAdapter<Bank> {

		ArrayList<Bank> myBanks;

		private TextView locType;
		private TextView distance;
		private TextView name;
		private TextView address;

		public BankArrayAdapter(Context context, int resource, List<Bank> banks) {
			super(context, resource, banks);
			myBanks = (ArrayList<Bank>) banks;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// information is a custom layout built for the listview
			convertView = getLayoutInflater().inflate(R.layout.information,
					parent, false);

			locType = (TextView) convertView.findViewById(R.id.details);
			distance = (TextView) convertView.findViewById(R.id.distance);
			name = (TextView) convertView.findViewById(R.id.name);
			address = (TextView) convertView.findViewById(R.id.address);

			locType.setText(myBanks.get(position).getLocType());
			distance.setText(myBanks.get(position).getDistance());
			name.setText(myBanks.get(position).getName());
			address.setText(myBanks.get(position).getAddress());

			return convertView;
		}

	}

}
