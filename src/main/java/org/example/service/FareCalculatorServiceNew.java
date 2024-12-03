package org.example.service;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.model.Tap;
import org.example.model.Trip;

public class FareCalculatorServiceNew {

	private static final Map<String, Double> FARE_MAP = Map.of(
			"Stop1-Stop2", 3.25,
			"Stop2-Stop3", 5.50,
			"Stop1-Stop3", 7.30
	);

	public static List<Trip> processTrips(List<Tap> taps) {
		return taps.stream()
				.collect(Collectors.groupingBy(tap -> Map.entry(tap.getPan(), tap.getBusId()))) // Group by PAN and BusID
				.values().stream()
				.flatMap(tapGroup -> processTapGroup(tapGroup).stream())
				.collect(Collectors.toList());
	}

	private static List<Trip> processTapGroup(List<Tap> tapGroup) {
		List<Trip> trips = new ArrayList<>();
		tapGroup.sort(Comparator.comparing(Tap::getDateTime)); // Sort by timestamp

		Deque<Tap> tapDeque = new ArrayDeque<>(tapGroup);

		while (!tapDeque.isEmpty()) {
			Tap tapOn = tapDeque.poll();
			if (!"ON".equals(tapOn.getTapType())) {
				continue;
			}

			Tap tapOff = tapDeque.peek();
			if (tapOff != null && "OFF".equals(tapOff.getTapType()) && tapOff.getBusId().equals(tapOn.getBusId())) {
				tapDeque.poll(); // Match ON with OFF
				System.out.println("Logic for completed");
				trips.add(createTrip(tapOn, tapOff, "COMPLETED"));
			} else if (tapOn.getStopId().equals(tapOff != null ? tapOff.getStopId() : null)) {
				System.out.println("Logic for cancelled");

				trips.add(createTrip(tapOn, tapOn, "CANCELLED")); // Cancelled trip
			} else {
				System.out.println("Logic for incomplete");

				trips.add(createTrip(tapOn, null, "INCOMPLETE")); // Incomplete trip
			}
		}

		return trips;
	}

	private static Trip createTrip(Tap tapOn, Tap tapOff, String status) {
		String fromStop = tapOn.getStopId();
		String toStop = tapOff != null ? tapOff.getStopId() : null;
		double charge = switch (status) {
			 case "COMPLETED" -> FARE_MAP.getOrDefault(fromStop + "-" + toStop, 0.0);
			 case "INCOMPLETE" -> FARE_MAP.entrySet().stream()
					 .filter(e -> e.getKey().startsWith(fromStop + "-"))
					 .map(Map.Entry::getValue)
					 .max(Double::compare)
					 .orElse(0.0);
			 default -> 0.0;
		};

		long duration = tapOff != null
				? Duration.between(tapOn.getDateTime(), tapOff.getDateTime()).getSeconds()
				: 0;

		return new Trip(
				tapOn.getDateTime(),
				tapOff != null ? tapOff.getDateTime() : null,
				duration,
				fromStop,
				toStop,
				charge,
				tapOn.getCompanyId(),
				tapOn.getBusId(),
				tapOn.getPan(),
				status
		);
	}


}
