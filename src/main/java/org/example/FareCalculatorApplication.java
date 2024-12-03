package org.example;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.example.model.Tap;
import org.example.model.Trip;
import org.example.service.FareCalculatorService;
import org.example.service.FareCalculatorServiceNew;
import org.example.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FareCalculatorApplication {
	private static final Logger logger = LoggerFactory.getLogger(FareCalculatorApplication.class);

	public static void main(String[] args) throws IOException {
		String inputFile = "src/main/resources/taps.csv";
//		String outputFile = "trips.csv";
		//TODO should keep under configurable output dir of build/output directory
		File outputDir = new File("build/output");
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		String outputFile = "build/output/trips.csv";
		logger.info("Reading input file");
		List<Tap> tapRecords = CsvUtil.readTapRecords(inputFile);
		logger.info("Processing input file");
		List<Trip> trips = FareCalculatorServiceNew.processTrips(tapRecords);
		logger.info("Processing input file completed"); //TODO should add timestamp here

		logger.info("Writing Trip details to output file");
		CsvUtil.writeTripsToFile(trips, outputFile );
		logger.info("Writing Trip details to output file is completed");


	}
}