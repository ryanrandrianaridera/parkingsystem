package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.util.InputReaderUtil;

public class InputReaderUtilTest {

	private static InputReaderUtil inputReaderUtil;

	@BeforeEach
	public void setUpPerTest() {
		inputReaderUtil = new InputReaderUtil();
	}

	@Test
	@DisplayName("When a user input is an int, readSelection() should return" + " the input")
	public void readSelectionTest() {
		// ARRANGE
		String input = "1";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		// ASSERT
		assertEquals(1, inputReaderUtil.readSelection());

	}

	@Test
	@DisplayName("When a user input is different than an int, readSelection()" + " should return -1")
	public void readSelectionWhenInputIsInvalidTest() {
		// ARRANGE
		String input = "r";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		// ASSERT
		assertEquals(-1, inputReaderUtil.readSelection());
	}

	@Test
	@DisplayName("When a user provides an input, readVehicleRegistrationNumber() " + "should return the input")
	public void readVehicleRegNumberTest() throws Exception {
		// ARRANGE
		String input = "ABCDEF";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		// ASSERT
		System.out.println("test3");
		assertEquals("ABCDEF", inputReaderUtil.readVehicleRegistrationNumber());
	}

	@Test
	@DisplayName("When a user input is blank, readVehicleRegistrationNumber"
			+ "() should throws an IllegalArgumentException")
	public void readVehicleRegNumberWhenInputIsInvalidTest() {
		// ARRANGE
		String input = "\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		// ASSERT
		assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
	}
}