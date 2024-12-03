package org.example.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.model.Tap;
import org.example.model.Trip;

public class FareCalculatorService {

	/*
	MAX_FARES to be used for calculation of Incomplete trips
	For example, if a passenger taps on at Stop 2, but does not tap off,
	they could potentially have travelled to either stop 1 ($3.25) or stop 3 ($5.50),
	so they will be charged the higher value of $5.50.That's why Stop2 have max fare as 5.50 below.
	 */
	private static final Map<String, Double> maxFares = Map.of(
			"Stop1", 7.30,
			"Stop2", 5.50,
			"Stop3", 7.30
	);

	/*
	add documentation here
	 */
	// TODO Map<Map<String>, Double> : helps to do reference
	// TODO stop1+stop2 : concat then reference can be faster thn map of map , should be sorted my stop1, adn stop2
	// Stop 1 and stop2 in metrics form, matrixcalculation
	// hashmap , that join source desti together
	// if concat together, u will not use more memory, coz its sorted the value. source is always the smaller
	// comapred to desti Map<String, Double) : Map <"Stop1Stop2, 3.25>
	// assumption : time is not taken into consideration, in real sceanrio, it should be charged
	// highlight assumption, thught process
	// few way to apply charges , on and off within 10 mins to cancel , second is how much you shd b charged, how
	// much ditance travelled. or simpify saying I will charge max
	// dont implement, can propose potential solution
	// from bizz user perspective : usually want to avoid anything not in bizz logic its not wise to implement
	// can keep under readme file

	private static final Map<List<String>, Double> fares = Map.of(
			List.of("Stop1", "Stop2"), 3.25,
			List.of("Stop2", "Stop3"), 5.50,
			List.of("Stop1", "Stop3"), 7.30

			// TODO to check does list is required to be created for all combi like stop2 to stop1
	);

	//TODO : calculating the fare, input processing and conversion can keep it in seperate function or in utils ,
	// taps or trips in csvutils. In service can be done only the calculation
	//TODO using hashmap for unique key is good idea,
	// create trip object, how to group bizz logic, fare regards in fare calulcator
	// trip objects taps detail, processing can become trips.
	// bizz object to consolidate only bizz things
	// single place to change service
	// bus A tap on, off : completd : 9am - 10 am
	// bus A, tap on, no off  : should it be auto tap off , key should be pan in this case
	// bus B , tap on
	// bus A tap on, off : completd 5pm - 6pm
	// assuming no off without on
	// assumption :  tap records are sorted based on time
	// it does not carry over to next day, if its incomplete then charge the max
	// https://muthuishere.medium.com/java-if-else-removal-functional-polymorphism-673e25a6a5ae
	// Trip rules or object
	// cater for change or scalablity
	// may b not functional poly , create rule structure
	// how do u define interpret taps become trip
	// List <Taps> --> apply rule --> List<Trip>
	// dont need to go if else , instead go to rule list
	// Rule : ON, Off, Complete
	// assumption 2 : Fare and logic to determine the trip should be seperated : to make it more flexible to increase
	// the
	// price example
	// utility doesnot store bizz logic
	// conversions and determination of status bizz logic
	// apply the fare is another bizz logic
	// logic shpuld be in single logics
	//



	public List<Trip> calculateTrips(List<Tap> tapDetails) {
		List<Trip> trips = new ArrayList<>();
		Map<String, Tap> ongoingTaps = new HashMap<>();

		for (Tap tap : tapDetails) {
			String key = tap.getPan() + "-" + tap.getBusId(); // Unique identifier for a passenger on a specific bus
//TODO check if same pan can board bus twice in a day, think adding date or maybe time could be the good key ???
			if ("ON".equals(tap.getTapType())) {
				ongoingTaps.put(key, tap);
			} else if ("OFF".equals(tap.getTapType()) && ongoingTaps.containsKey(key)) {
				Tap tapOn = ongoingTaps.get(key);

				// Handle Cancelled Trip (tap on and off at the same stop)
				if (tapOn.getStopId().equals(tap.getStopId())) {
					// Create a cancelled trip with 0 duration and charge
					trips.add(new Trip(
							tapOn.getDateTime(),
							tap.getDateTime(),
							0L, // 0 duration for cancelled trip,
							//TODO  what if on and off at same station but duration is
							// more than 0 sec, may be can charge max fare and mark it as INCOMPLETE
							tapOn.getStopId(),
							tap.getStopId(),
							0.00, // No charge for cancelled trip
							tapOn.getCompanyId(),
							tapOn.getBusId(),
							tapOn.getPan(),
							"CANCELLED"
					));
				} else {
					// Create a key for fares
					List<String> fareKey = List.of(tapOn.getStopId(), tap.getStopId())
							.stream()
							.sorted()
							.collect(Collectors.toList());

					// Determine the fare
					Double fare = fares.getOrDefault(fareKey, 0.0);

					// Calculate trip duration in seconds
					long duration = Math.abs(Duration.between(tapOn.getDateTime(), tap.getDateTime()).getSeconds());

					// Add the completed trip
					trips.add(new Trip(
							tapOn.getDateTime(),
							tap.getDateTime(),
							duration,
							tapOn.getStopId(),
							tap.getStopId(),
							fare,
							tapOn.getCompanyId(),
							tapOn.getBusId(),
							tapOn.getPan(),
							"COMPLETED"
					));
				}

				// Remove the ongoing tap
				ongoingTaps.remove(key);
			}
		}

		// Handle incomplete trips
		for (Map.Entry<String, Tap> entry : ongoingTaps.entrySet()) {
			Tap tapOn = entry.getValue();
			Double maxFare = maxFares.getOrDefault(tapOn.getStopId(), 0.0);
//TODO if scaling needs to be done for maxfare, is this right way or can be improved
			trips.add(new Trip(
					tapOn.getDateTime(),
					null,
					null,
					tapOn.getStopId(),
					null,
					maxFare,
					tapOn.getCompanyId(),
					tapOn.getBusId(),
					tapOn.getPan(),
					"INCOMPLETE"
			));
		}

		return trips;
	}
}
