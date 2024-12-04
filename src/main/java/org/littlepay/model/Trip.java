package org.littlepay.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Trip {
	private LocalDateTime started;
	private LocalDateTime finished;
	private Long durationSecs;
	private String fromStopId;
	private String toStopId;
	private Double chargeAmount;
	private String companyId;
	private String busId;
	private String pan;
	private String status;

	public LocalDateTime getStarted() {
		return started;
	}
	public LocalDateTime getFinished() {
		return finished;
	}
	public Long getDurationSecs() {
		return durationSecs;
	}
	public String getFromStopId() {
		return fromStopId;
	}
	public String getToStopId() {
		return toStopId;
	}
	public Double getChargeAmount() {
		return chargeAmount;
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
	public String getStatus() {
		return status;
	}


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
				started != null ? started.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) :"",
				finished != null ? finished.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "",
				durationSecs,
				fromStopId!=null ? fromStopId :"", // Handle when fromStopId is null, then display empty instead of
				// null in output file
				toStopId!=null ? toStopId : "", // Handle when toStopId is null, then display empty instead of
				// null in output file
				chargeAmount,
				companyId,
				busId,
				pan,
				status);
	}
}
