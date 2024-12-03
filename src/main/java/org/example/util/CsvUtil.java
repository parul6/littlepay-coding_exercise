package org.example.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.example.FareCalculatorApplication;
import org.example.model.Tap;
import org.example.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CsvUtil { //TODO CsvUtil or Csvhelper whats the right naming convention
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);


	public static List<Tap> readTapRecords(String inputFilePath) {
		// Logic to parse CSV and create TapRecord list
		// try with resource ensure stream is closed automatically at the end of block, even if an exception is thrown.
		try (Stream<String> lines = Files.lines(Paths.get(inputFilePath))) {
			List<Tap> taps = lines
					.skip(1)// Skip header
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

	public static void writeTripsToFile(List<Trip> trips, String outputFilePath) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
			writer.write("Started,Finished,DurationSecs,FromStopId,ToStopId,ChargeAmount,CompanyId,BusID,PAN,Status\n");
			for (Trip trip : trips) {
				writer.write(trip.toCSV() + "\n");
			}
		}
	}



	/*public static void writeTrips(String filePath, List<Trip> trips) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			// Write the header
			//TODO will this be good idea to add header in property file and read the header here
			writer.write("Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status");
			writer.newLine();

			// Write each trip
			for (Trip trip : trips) {
				String started = trip.getStarted().format(formatter);
				String finished = trip.getFinished() != null ? trip.getFinished().format(formatter) : "";
				String durationSecs = trip.getDurationSecs() != null ? trip.getDurationSecs().toString() : "";
				String toStopId = trip.getToStopId() != null ? trip.getToStopId() : "";

				String line = String.join(", ",
						started,
						finished,
						durationSecs,
						trip.getFromStopId(),
						toStopId,
						trip.getChargeAmount(),
						trip.getCompanyId(),
						trip.getBusId(),
						trip.getPan(),
						trip.getStatus()
				);

				writer.write(line);
				writer.newLine();
			}

		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}*/
		// Logic to write Trip list to CSV
	}


