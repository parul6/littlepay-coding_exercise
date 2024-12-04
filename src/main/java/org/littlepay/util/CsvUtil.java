package org.littlepay.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.littlepay.model.Tap;
import org.littlepay.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CsvUtil {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);
	private static final int EXPECTED_FIELD_COUNT = 7; // Define the expected number of fields


	public static List<Tap> readTapRecords(String inputFilePath) {
		// Logic to parse CSV and create TapRecord list
		// try with resource ensure stream is closed automatically at the end of block, even if an exception is thrown.
		try (Stream<String> lines = Files.lines(Paths.get(inputFilePath))) {
			List<Tap> taps = lines
					.skip(1)// Skip header
					.filter(CsvUtil::validateTapLine) // Validate each line
					.map(CsvUtil::parseTap) // Parse each line into a Tap object
					.toList();
			logger.info("Reading successfully completed with {} records.", taps.size());
			return taps;
		} catch (IOException e) {
			logger.error("Failed to read tap records from file: {}", inputFilePath, e);
			e.printStackTrace();
		}
		return List.of();
	}


	private static Tap parseTap(String line){
		String[] fields = line.split(",");
		return new Tap(
				Integer.parseInt(fields[0].trim()), // ID
				LocalDateTime.parse(fields[1].trim(), DATE_TIME_FORMATTER), // DateTimeUTC
				fields[2].trim(), // TapType
				fields[3].trim(), // StopId
				fields[4].trim(), // CompanyId
				fields[5].trim(), // BusID
				fields[6].trim()  // PAN
		);
	}

	private static boolean validateTapLine(String line) {
		if (line == null || line.trim().isEmpty()) {
			logger.warn("Skipping empty line");
			return false; // Indicate the line is invalid
		}

		String[] fields = line.split(",");
		if (fields.length != EXPECTED_FIELD_COUNT) {
			logger.warn("Skipping invalid line due to insufficient fields: {}", line);
			return false; // Indicate the line is invalid
		}
		return true; // Line is valid
	}

	public static void writeTripsToFile(List<Trip> trips, String outputFilePath) throws IOException {
		// Ensure trips is a mutable list before sorting
		List<Trip> mutableTrips = new ArrayList<>(trips); // Convert to a mutable list
		// Sort trips by the 'started' timestamp
		mutableTrips.sort(Comparator.comparing(Trip::getStarted)); // Sort by timestamp
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
			writer.write("Started,Finished,DurationSecs,FromStopId,ToStopId,ChargeAmount,CompanyId,BusID,PAN,Status\n");
			for (Trip trip : mutableTrips) {
				writer.write(trip.toCSV() + "\n");
			}
		}
	}
	}


