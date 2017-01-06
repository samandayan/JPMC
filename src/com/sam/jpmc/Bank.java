package com.sam.jpmc;

import java.io.Serializable;

public class Bank implements Serializable {

	// This class represent a single bank found in a location

	String locType;
	String distance;
	String name;
	String address;
	String lat;
	String lng;
	String services;

	String state = "";
	String label = "";
	String city = "";
	String zip = "";
	String bank = "";
	// These are the default values in case the result is an ATM
	String type = "ATM";
	String lobbyHrs = "Never Close";
	String driveUpHrs = "24/7";
	String phone = "None";
	int atms = 1;

	// The getter and setters for
	// each bank properties are
	// as below.
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLobbyHrs() {
		return lobbyHrs;
	}

	public void setLobbyHrs(String lobbyHrs) {
		this.lobbyHrs = cleanUp(lobbyHrs);
	}

	public String getDriveUpHrs() {
		return driveUpHrs;
	}

	public void setDriveUpHrs(String driveUpHrs) {
		this.driveUpHrs = cleanUp(driveUpHrs);
	}

	public int getAtms() {
		return atms;
	}

	public void setAtms(int atms) {
		this.atms = atms;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = cleanUp(services);
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLocType() {
		return locType;
	}

	public void setLocType(String locType) {
		this.locType = locType;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String cleanUp(String input) {
		return input.replace("[", "").replace("]", "");
	}

}
