package org.littlepay.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilTest {
	@Test
	void testValidateTapLine() {
		// Valid line with expected field count
		String validLine = "1, 22-01-2023 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559";
		assertTrue(CsvUtil.validateTapLine(validLine), "Valid line should return true");

		// Empty line
		String emptyLine = "";
		assertFalse(CsvUtil.validateTapLine(emptyLine), "Empty line should return false");

		// Null line
		String nullLine = null;
		assertFalse(CsvUtil.validateTapLine(nullLine), "Null line should return false");

		// Line with insufficient fields
		String insufficientFields = "ON,Stop1,Company1";
		assertFalse(CsvUtil.validateTapLine(insufficientFields), "Line with insufficient fields should return false");

		// Line with extra fields
		String extraFields = "ON,Stop1,Company1,Bus37,122000000000003,2023-12-01T08:30:00,ExtraField, ExtraField1";
		assertFalse(CsvUtil.validateTapLine(extraFields), "Line with extra fields should return false");
	}

}