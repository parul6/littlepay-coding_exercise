package org.littlepay.service;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.littlepay.model.Tap;
import org.littlepay.model.Trip;
import org.littlepay.util.Utility;

public class FareCalculatorService {

	private static final Map<String, Double> FARE_MAP = Map.of(
			"Stop1-Stop2", 3.25,
			"Stop2-Stop3", 5.50,
			"Stop1-Stop3", 7.30
	);

	public static List<Trip> processTrips(List<Tap> taps) {
		return taps.stream()
				.filter(Objects::nonNull) // Filter out null elements
				.collect(Collectors.groupingBy(tap -> Map.entry(tap.getPan(), tap.getBusId()))) // Group by PAN and
				// BusID to make it unique id
				.values().stream()
				.flatMap(tapGroup -> processTapGroup(tapGroup).stream())
				.toList();
	}

	public static List<Trip> processTapGroup(List<Tap> tapGroup) {
		List<Trip> trips = new ArrayList<>();
		tapGroup.sort(Comparator.comparing(Tap::getDateTime)); // Sort by timestamp
		Deque<Tap> tapDeque = new ArrayDeque<>(tapGroup);
		while (!tapDeque.isEmpty()) {
			Tap tapOn = tapDeque.poll();
			if (!"ON".equals(tapOn.getTapType())) {
				continue;
			}
			// Skip consecutive "ON" taps
			while (!tapDeque.isEmpty() && "ON".equals(tapDeque.peek().getTapType())) {
				tapDeque.poll(); // Remove consecutive "ON" tap
			}

			Tap tapOff = tapDeque.peek();
			if (Optional.ofNullable(tapOff)
					.map(Tap::getStopId)
					.filter(tapOn.getStopId()::equals)
					.isPresent() && !tapOn.getTapType().equals(tapOff.getTapType())) {
				trips.add(createTrip(tapOn, tapOff, "CANCELLED")); // Cancelled trip
			}else if(tapOff != null && "OFF".equals(tapOff.getTapType())
					&& tapOff.getBusId().equals(tapOn.getBusId())) {
				tapDeque.poll();
				trips.add(createTrip(tapOn, tapOff, "COMPLETED"));
			} else {
				trips.add(createTrip(tapOn, null, "INCOMPLETE")); // Incomplete trip
			}
		}
		return trips;
	}

	public static Trip createTrip(Tap tapOn, Tap tapOff, String status) {
		String fromStop = tapOn != null ? tapOn.getStopId(): null;
		String toStop = tapOff != null ? tapOff.getStopId() : null;
		double charge = switch (status) {

			 case "COMPLETED" -> FARE_MAP.getOrDefault(Utility.normalizedKey(fromStop, toStop),  0.0);
			 case "INCOMPLETE" -> FARE_MAP.entrySet().stream()
					 .filter(e -> e.getKey().startsWith(fromStop + "-"))
					 .map(Map.Entry::getValue)
					 .max(Double::compare)
					 .orElse(0.0);
			 default -> 0.0;
		};

		long duration = (tapOn != null && tapOff != null)
				? Duration.between(tapOn.getDateTime(), tapOff.getDateTime()).getSeconds()
				: 0;

		return new Trip(
				tapOn != null ? tapOn.getDateTime() : null, // tapOn time
				tapOff != null ? tapOff.getDateTime() : null, // tapOff time
				duration,
				fromStop, // fromStop
				toStop,  // toStop
				charge,  // calculated fare
				tapOn != null ? tapOn.getCompanyId() : (tapOff != null ? tapOff.getCompanyId() : ""), // companyId
				tapOn != null ? tapOn.getBusId() : (tapOff != null ? tapOff.getBusId() : ""),         // busId
				tapOn != null ? tapOn.getPan() : (tapOff != null ? tapOff.getPan() : ""),             // PAN
				status
		);
	}

}
