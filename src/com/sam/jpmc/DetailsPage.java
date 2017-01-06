package com.sam.jpmc;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsPage extends Activity {

	TextView details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailspage);

		// The TextView below gives the result in a textual format
		details = (TextView) findViewById(R.id.details);

		// This is the bank whose details are to be displayed
		Bank bank = (Bank) getIntent().getSerializableExtra("bank");

		getActionBar().setTitle(bank.getName());
		getActionBar().setSubtitle("\"So You Can\"");

		// Storing the data in a StringBuffer to put on a text
		StringBuffer information = new StringBuffer();

		information.append("Name:\t" + bank.getName() + "\n");
		information.append("LocType:\t" + bank.getLocType() + "\n");
		information.append("Distance:\t" + bank.getDistance() + "\n");
		information.append("Address:\t" + bank.getAddress() + "\n");
		information.append("Services:\t" + bank.getServices() + "\n");
		information.append("State:\t" + bank.getState() + "\n");
		information.append("Label:\t" + bank.getCity() + "\n");
		information.append("Zip:\t" + bank.getZip() + "\n");
		information.append("Distance:\t" + bank.getDistance() + "\n");
		information.append("Bank:\t" + bank.getBank() + "\n");
		information.append("ATMS:\t" + bank.getAtms() + "\n");
		information.append("City:\t" + bank.getCity() + "\n");
		information.append("Type:\t" + bank.getType() + "\n");
		if (bank.getLobbyHrs().equals("Never Close"))
			information.append("Lobby Hours: " + bank.getLobbyHrs() + "\n");
		else
			information.append("Lobby Hours:\n" + bank.getLobbyHrs() + "\n");
		information.append("DriveUpHours:\t" + bank.getDriveUpHrs() + "\n");
		information.append("Phone:\t" + bank.getPhone());

		// Adding the information from the bank to the TextView
		details.setText(information.toString());
	}

}
