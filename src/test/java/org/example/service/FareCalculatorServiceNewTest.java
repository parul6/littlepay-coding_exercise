package org.example.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.model.Tap;
import org.example.model.Trip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareCalculatorServiceNewTest {

@Test
public void testProcessTrips() {
	// Arrange: Create sample Tap objects
	List<Tap> taps = Arrays.asList(
	new Tap(1, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus37", "5500005555555559"),
			new Tap(2, LocalDateTime.now(), "OFF", "Stop2", "Company1", "Bus37", "5500005555555559"),
			new Tap(3, LocalDateTime.now().plusHours(1), "ON", "Stop1", "Company1", "Bus37", "5500005555555559"),
			new Tap(4, LocalDateTime.now(), "ON", "Stop1", "Company1", "Bus37", "5500005555555559")
	);
	// Act: Call the method to test
	List<Trip> result = FareCalculatorServiceNew.processTrips(taps);

	// Assert: Verify the result
	assertNotNull(result, "The result should not be null");
	assertEquals(3, result.size(), "The number of trips should be 3");

	// You can add more specific assertions based on what `processTrips` returns
	// For example, verify a trip was created for a specific PAN/BusID pair:
	Optional<Trip> trip = result.stream()
			.filter(t -> "5500005555555559".equals(t.getPan()) && "Bus37".equals(t.getBusId()))
			.findFirst();

	assertTrue(trip.isPresent(), "Trip for PAN 5500005555555559 and Bus37 should exist");

	// Verify that the trip was correctly processed. (This assumes Trip has relevant methods like getFromStop, getToStop, etc.)
	Trip firstTrip = trip.get();
	assertEquals("Stop1", firstTrip.getFromStopId(), "The first stop should be Stop1");
	assertEquals("Stop2", firstTrip.getToStopId(), "The second stop should be Stop2");

}

}