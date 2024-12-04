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

	public int getId() {
		return id;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String getTapType() {
		return tapType;
	}
	public String getStopId() {
		return stopId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public String getBusId() {
		return busId;
	}
	public String getPan() {
		return pan;
	}
}
