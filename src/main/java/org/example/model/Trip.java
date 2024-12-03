package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Trip {
	private LocalDateTime started;

	public LocalDateTime getStarted() {
		return started;
	}

	public void setStarted(LocalDateTime started) {
		this.started = started;
	}

	public LocalDateTime getFinished() {
		return finished;
	}

	public void setFinished(LocalDateTime finished) {
		this.finished = finished;
	}

	public Long getDurationSecs() {
		return durationSecs;
	}

	public void setDurationSecs(Long durationSecs) {
		this.durationSecs = durationSecs;
	}

	public String getFromStopId() {
		return fromStopId;
	}

	public void setFromStopId(String fromStopId) {
		this.fromStopId = fromStopId;
	}

	public String getToStopId() {
		return toStopId;
	}

	public void setToStopId(String toStopId) {
		this.toStopId = toStopId;
	}

	public Double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private LocalDateTime finished;
	private Long durationSecs;
	private String fromStopId;
	private String toStopId;
	private Double chargeAmount;
	private String companyId;
	private String busId;
	private String pan;
	private String status;

	public Trip(LocalDateTime started, LocalDateTime finished, Long durationSecs, String fromStopId, String toStopId,
			double chargeAmount, String companyId, String busId, String pan, String status) {
		this.started = started;
		this.finished = finished;
		this.durationSecs = durationSecs;
		this.fromStopId = fromStopId;
		this.toStopId = toStopId;
		this.chargeAmount = chargeAmount;
		this.companyId = companyId;
		this.busId = busId;
		this.pan = pan;
		this.status = status;
	}

	public String toCSV() {
		return String.format("%s,%s,%d,%s,%s,$%.2f,%s,%s,%s,%s",
				started.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
				finished != null ? finished.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "",
				durationSecs,
				fromStopId,
				toStopId,
				chargeAmount,
				companyId,
				busId,
				pan,
				status);
	}
}
