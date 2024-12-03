package org.example.model;

import java.time.LocalDateTime;

public class Tap {
	private int id;
	private LocalDateTime dateTime;
	private String tapType;  //  should it be boolean or may be enum
	private String stopId;
	private String companyId;
	private String busId;
	private String pan;

	public Tap(int id,
			LocalDateTime dateTime,
			String tapType,
			String stopId,
			String companyId,
			String busId,
			String pan) {
		this.id = id;
		this.dateTime = dateTime;
		this.tapType = tapType;
		this.stopId = stopId;
		this.companyId = companyId;
		this.busId = busId;
		this.pan = pan;
	}

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getTapType() {
		return tapType;
	}

	public void setTapType(String tapType) {
		this.tapType = tapType;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}
}
