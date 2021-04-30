package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Read both int and String input provided by user.
 *
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class InputReaderUtil {
	/**
	 * InputReaderUtil logger.
	 */
	private static final Logger logger = LogManager.getLogger("InputReaderUtil");
	/**
	 * Allows users provide their choices to ParkingSystem.
	 */
	private static Scanner scan = new Scanner(System.in, "UTF-8");

	/**
	 * Read the selection.
	 * 
	 * @return input in by the selection or -1 on error
	 */
	public int readSelection() {
		try {
			int input = Integer.parseInt(scan.nextLine());
			return input;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	/**
	 * Read the number registration vehicle.
	 * 
	 * @return the number registration vehicle
	 * @throws Exception
	 */
	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}

}