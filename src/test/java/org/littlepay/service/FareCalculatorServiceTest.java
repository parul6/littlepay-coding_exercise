package org.littlepay.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.littlepay.model.Tap;
import org.littlepay.model.Trip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareCalculatorServiceTest {
	// Example fare map to be used in the test
@Test
 void testProcessTrips() {
	// Arrange: Create sample Tap objects
	List<Tap> taps = Arrays.asList(
	new Tap(1, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus37", "122000000000003"),
			new Tap(2, LocalDateTime.now(), "OFF", "Stop2", "Company1", "Bus37", "122000000000003"),
			new Tap(3, LocalDateTime.now().plusHours(1), "ON", "Stop1", "Company1", "Bus38", "5019717010103742"),
			new Tap(4, LocalDateTime.now().plusHours(1).plusMinutes(10), "OFF", "Stop1", "Company1", "Bus38",
					"5019717010103742"),
	        new Tap(5, LocalDateTime.now().plusHours(1).plusMinutes(15), "ON", "Stop1", "Company1", "Bus38",
			"3528000700000000")
	);
	// Act: Call the method to test
	List<Trip> result = FareCalculatorService.processTrips(taps);

	// Assert: Verify the result
	assertNotNull(result, "The result should not be null");
	assertEquals(3, result.size(), "The number of trips should be 3");

	for (Trip trip : result) {
		if ("Stop1".equals(trip.getFromStopId()) && "Stop2".equals(trip.getToStopId())) {
			assertEquals("COMPLETED", trip.getStatus(), "Trip from Stop1 to Stop2 should be COMPLETED");
		} else if ("Stop1".equals(trip.getFromStopId()) && "Stop1".equals(trip.getToStopId())) {
			assertEquals("CANCELLED", trip.getStatus(), "Trip from Stop1 to Stop1 should be CANCELLED");
		}else if ("Stop1".equals(trip.getToStopId())){
			assertEquals("INCOMPLETE", trip.getStatus(), "Trip of Stop1 should be INCOMPLETE");

		}
	}
}

	@Test
	 void testProcessTrips_eitherDirection() {
		// Arrange: Create sample Tap objects
		List<Tap> taps = Arrays.asList(
				new Tap(1, LocalDateTime.now(), "ON", "Stop3", "Company1", "Bus37", "122000000000003"),
				new Tap(2, LocalDateTime.now().plusMinutes(20), "OFF", "Stop1", "Company1", "Bus37", "122000000000003")
		);
		// Act: Call the method to test
		List<Trip> result = FareCalculatorService.processTrips(taps);

		// Assert: Verify the result
		assertNotNull(result, "The result should not be null");
		assertEquals(1, result.size(), "The number of trips should be 1");


		for (Trip trip : result) {
			if ("Stop3".equals(trip.getFromStopId()) && "Stop1".equals(trip.getToStopId())) {
				assertEquals("COMPLETED", trip.getStatus(), "Trip from Stop3 to Stop1 should be COMPLETED");
			}

			assertNotNull(trip);
			assertEquals("Stop3", trip.getFromStopId());
			assertEquals("Stop1", trip.getToStopId());
			assertEquals(1200, trip.getDurationSecs()); // Duration is 10 minutes (600 seconds)
			assertEquals(7.30, trip.getChargeAmount()); // Fare should match the one from the fare map
			assertEquals("Company1", trip.getCompanyId());
			assertEquals("Bus37", trip.getBusId());
			assertEquals("122000000000003", trip.getPan());
			assertEquals("COMPLETED", trip.getStatus());
		}
	}

@Test
void testCreateTrip_Completed() {
	// Given
	Tap tapOn = new Tap(1, LocalDateTime.now(), "ON", "Stop2", "Company1", "Bus37", "122000000000003");
	Tap tapOff = new Tap(2, LocalDateTime.now().plusMinutes(10), "OFF", "Stop1", "Company1", "Bus37",
			"122000000000003");

	// When
	Trip trip = FareCalculatorService.createTrip(tapOn, tapOff, "COMPLETED");

	// Then
	assertNotNull(trip);
	assertEquals("Stop2", trip.getFromStopId());
	assertEquals("Stop1", trip.getToStopId());
	assertEquals(600, trip.getDurationSecs()); // Duration is 10 minutes (600 seconds)
	assertEquals(3.25, trip.getChargeAmount()); // Fare should match the one from the fare map
	assertEquals("Company1", trip.getCompanyId());
	assertEquals("Bus37", trip.getBusId());
	assertEquals("122000000000003", trip.getPan());
	assertEquals("COMPLETED", trip.getStatus());
}

	@Test
	void testCreateTrip_Incomplete() {
		// Given
		Tap tapOn = new Tap(1, LocalDateTime.now().plusMinutes(10), "ON", "Stop1", "Company1", "Bus37",
				"4484070000000000");

		// When
		Trip trip = FareCalculatorService.createTrip(tapOn, null, "INCOMPLETE");

		// Then
		assertNotNull(trip);
		assertNull(trip.getToStopId());
		assertEquals("Stop1", trip.getFromStopId());
		assertEquals(7.30, trip.getChargeAmount()); // maximum fare for incomplete trip
		assertEquals(0, trip.getDurationSecs()); // Duration should be 0
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("4484070000000000", trip.getPan());
		assertEquals("INCOMPLETE", trip.getStatus());
	}


	@Test
	void testCreateTrip_Canceled() {
		// Given
		Tap tapOn = new Tap(1, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus37", "6011000400000000");
		Tap tapOff = new Tap(2, LocalDateTime.now().plusMinutes(1), "OFF", "Stop1", "Company1", "Bus37",
				"6011000400000000");

		// When
		Trip trip = FareCalculatorService.createTrip(tapOn, tapOff, "CANCELLED");

		// Then
		assertNotNull(trip);
		assertEquals("Stop1", trip.getToStopId());
		assertEquals("Stop1", trip.getFromStopId());
		assertEquals(0.0, trip.getChargeAmount()); // No charges for cancelled trip
		assertEquals(60, trip.getDurationSecs()); // Duration should be 0
		assertEquals("Company1", trip.getCompanyId());
		assertEquals("Bus37", trip.getBusId());
		assertEquals("6011000400000000", trip.getPan());
		assertEquals("CANCELLED", trip.getStatus());
	}



	@Test
	 void testProcessTapGroup() {
		// Setup test data
		List<Tap> tapGroup = Arrays.asList(
				new Tap(1, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus1", "1234567890"),
				new Tap(2, LocalDateTime.now().plusMinutes(5), "OFF", "Stop1", "Company1", "Bus1", "1234567890"),
				new Tap(3, LocalDateTime.now().plusMinutes(10), "ON", "Stop2", "Company1", "Bus1", "1234567891"),
				new Tap(4, LocalDateTime.now().plusMinutes(15), "OFF", "Stop3", "Company1", "Bus1", "1234567891"),
				new Tap(5, LocalDateTime.now().plusMinutes(20), "ON", "Stop3", "Company1", "Bus2", "122000000000003")
		);

		// Act
		List<Trip> trips = FareCalculatorService.processTapGroup(tapGroup);

		// Assert
		assertNotNull(trips, "The result should not be null");
		assertEquals(3, trips.size(), "The number of trips should be 2");

		for (Trip trip : trips) {
			switch (trip.getStatus()){
				case "COMPLETED" -> {
					assertEquals("COMPLETED", trip.getStatus());
					assertEquals("Bus1", trip.getBusId());
					assertEquals("Stop2", trip.getFromStopId());
					assertEquals("Stop3", trip.getToStopId());
				}
				case "CANCELLED" -> {
					assertEquals("CANCELLED", trip.getStatus());
					assertEquals("Bus1", trip.getBusId());
					assertEquals("Stop1", trip.getFromStopId());
					assertEquals("Stop1", trip.getToStopId());
				}
				case "INCOMPLETE" -> {
					assertEquals("INCOMPLETE", trip.getStatus());
					assertEquals("Bus2", trip.getBusId());
					assertNull(trip.getToStopId(), "Incomplete trip not having toStopId");
				}
			}
		}
	}

	@Test
	void testProcessTapGroupEmpty() {
		// Setup: Empty tap group
		List<Tap> tapGroup = new ArrayList<>();

		// Act
		List<Trip> trips = FareCalculatorService.processTapGroup(tapGroup);

		// Assert
		assertNotNull(trips, "The result should not be null");
		assertTrue(trips.isEmpty(), "The trips list should be empty");
	}

	@Test
	void testProcessTapGroupWithIncomplete() {
		// Setup: "ON" taps without corresponding "OFF" taps
		List<Tap> tapGroup = Arrays.asList(
				new Tap(1, LocalDateTime.now().plusMinutes(5), "ON", "Stop1", "Company1", "Bus1", "1234567890")
		);

		// Act
		List<Trip> trips = FareCalculatorService.processTapGroup(tapGroup);

		// Assert
		assertNotNull(trips, "The result should not be null");
		assertEquals(1, trips.size(), "The number of trips should be 2");

		// Check that the second trip is incomplete
		Trip incompleteTrip = trips.get(0);
		assertEquals("INCOMPLETE", incompleteTrip.getStatus());
		assertNull(incompleteTrip.getToStopId(), "Incomplete trip should have no 'toStop'");
	}

	@Test
	void testProcessTapGroupWithCancel() {
		// Setup: "ON" and "OFF" taps with different stop IDs
		List<Tap> tapGroup = Arrays.asList(
				new Tap(1, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus1", "1234567890"),
				new Tap(2, LocalDateTime.now().plusMinutes(5), "OFF", "Stop1", "Company1", "Bus1", "1234567890")
		);

		// Act
		List<Trip> trips = FareCalculatorService.processTapGroup(tapGroup);

		// Assert
		assertNotNull(trips, "The result should not be null");
		assertEquals(1, trips.size(), "The number of trips should be 1");

		// Check that the trip is cancelled
		Trip cancelledTrip = trips.get(0);
		assertEquals("CANCELLED", cancelledTrip.getStatus());
		assertEquals("Bus1", cancelledTrip.getBusId());
		assertEquals("Stop1", cancelledTrip.getFromStopId());
		assertEquals("Stop1", cancelledTrip.getToStopId());
	}


	@Test
	void testProcessTapGroupConsecutiveOnTap() {
		// Setup: "ON" taps without corresponding "OFF" taps
		List<Tap> tapGroup = Arrays.asList(
				new Tap(1, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus1", "1234567890"),
				new Tap(2, LocalDateTime.now().plusMinutes(5), "ON", "Stop1", "Company1", "Bus1", "1234567890"),
				new Tap(3, LocalDateTime.now().plusMinutes(5), "OFF", "Stop2", "Company1", "Bus1", "1234567890")
		);

		// Act
		List<Trip> trips = FareCalculatorService.processTapGroup(tapGroup);

		// Assert
		assertNotNull(trips, "The result should not be null");
		assertEquals(1, trips.size(), "The number of trips should be 2");

		// Check that the second trip is incomplete
		Trip completedTrip = trips.get(0);
		assertEquals("COMPLETED", completedTrip.getStatus());
	}

}