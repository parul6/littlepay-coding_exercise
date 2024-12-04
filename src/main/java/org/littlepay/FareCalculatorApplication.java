package org.littlepay;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.littlepay.model.Tap;
import org.littlepay.model.Trip;
import org.littlepay.service.FareCalculatorService;
import org.littlepay.util.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FareCalculatorApplication {
	private static final Logger logger = LoggerFactory.getLogger(FareCalculatorApplication.class);

	public static void main(String[] args) throws IOException {
		String inputFile = "src/main/resources/taps.csv";
		File outputDir = new File("build/output");
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		String outputFile = "build/output/trips.csv";
		logger.info("Reading input file");
		List<Tap> tapRecords = CsvUtil.readTapRecords(inputFile);

		logger.info("Processing input file");
		List<Trip> trips = FareCalculatorService.processTrips(tapRecords);
		logger.info("Processing input file completed");

		logger.info("Writing Trip details to output file");
		CsvUtil.writeTripsToFile(trips, outputFile );
		logger.info("Writing Trip details to output file is completed");


	}
}