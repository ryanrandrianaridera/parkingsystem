package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * This class processes both vehicle come in and come out to parking.
 *
 * @author Ryan RANDRIA
 * @version 1.0
 *
 */
public class ParkingService {
	/**
	 * Parking service logger.
	 */
	private static final Logger logger = LogManager.getLogger("ParkingService");
	/**
	 * Instantiation fare CalculatorService.
	 */
	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();
	/**
	 * InputReaderUtil object.
	 */
	private InputReaderUtil inputReaderUtil;
	/**
	 * ParkingSpotDAO object.
	 */
	private ParkingSpotDAO parkingSpotDAO;
	/**
	 * TicketDAO object.
	 */
	private TicketDAO ticketDAO;

	/**
	 * Class constructor.
	 *
	 * @param inputReaderUtil
	 * @param parkingSpotDAO
	 * @param ticketDAO
	 */
	public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}

	/**
	 * Method to process incoming vehicle. Generated and save ticket ind DB.
	 */
	public void processIncomingVehicle() {
		try {
			ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
			if (parkingSpot != null && parkingSpot.getId() > 0) {
				String vehicleRegNumber = getVehicleNumberReg();

				parkingSpot.setAvailable(false);

				parkingSpotDAO.updateParking(parkingSpot);// allot this parking space and mark it's availability as
															// false
				LocalDateTime inTime = LocalDateTime.now();
				Ticket ticket = new Ticket();
				// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
				// ticket.setId(ticketID);

				ticket.setParkingSpot(parkingSpot);
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(0);
				ticket.setInTime(inTime);
				ticket.setOutTime(null);
				ticketDAO.saveTicket(ticket);

				// Add a console message for reccurent user

				Boolean isCustomerLoyal = ticketDAO.getCustomerTicketClosed(vehicleRegNumber);
				if (isCustomerLoyal) {
					System.out.println("Welcome back! As a recurring user of our parking lot,"
							+ "you'll benefit from a 5% discount.");
				}
				// set status of isCustomerLoyal on ticket
				ticket.setStatusOfLoyalCustomer(isCustomerLoyal);

				System.out.println("Generated Ticket and saved in DB");
				System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
				System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);

			}
		} catch (Exception e) {
			logger.error("Unable to process incoming vehicle", e);
		}
	}

	/**
	 * Method to call readVehicleRegistrationNumber to inputReaderUtil class.
	 *
	 * @return String: registration vehicle number
	 * @throws Exception
	 */
	private String getVehicleNumberReg() throws Exception {
		System.out.println("Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();
	}

	/**
	 * Method to check if there parking spot is available.
	 *
	 * @return the available parkingSpot (parkingnumber, parkingtype, isAvailable)
	 * @throws IllegalArgumentException, Exception if user's input is incorrectif or
	 *                                   there is no available parking spot
	 */
	public ParkingSpot getNextParkingNumberIfAvailable() {
		int parkingNumber = 0;
		ParkingSpot parkingSpot = null;
		try {
			ParkingType parkingType = getTypeVehicle();
			parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
			if (parkingNumber > 0) {
				parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
			} else {
				throw new Exception("Error fetching parking number from DB. Parking slots might be full");
			}
		} catch (IllegalArgumentException ie) {
			logger.error("Error parsing user input for type of vehicle", ie);
		} catch (Exception e) {
			logger.error("Error fetching next available parking slot", e);
		}
		return parkingSpot;
	}

	/**
	 * Get incoming vehicle type.
	 *
	 * @return the select type parking
	 * @throws IllegalArgumentException if the type is incorrect
	 */
	private ParkingType getTypeVehicle() {
		System.out.println("Please select vehicle type from menu");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
		case 1: {
			return ParkingType.CAR;
		}
		case 2: {
			return ParkingType.BIKE;
		}
		default: {
			System.out.println("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
		}
		}
	}

	/**
	 * Method to process exiting vehicle.
	 */
	public void processExitingVehicle() {
		try {
			String vehicleRegNumber = getVehicleNumberReg();
			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			// System.out.println(ticket.getId());
			LocalDateTime outTime = LocalDateTime.now();
			ticket.setOutTime(outTime);
			fareCalculatorService.calculateFare(ticket);

			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);
				System.out.println("Please pay the parking fare:" + ticket.getPrice());
				System.out.println(
						"Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
			} else {
				System.out.println("Unable to update ticket information. Error occurred");
			}
		} catch (Exception e) {
			logger.error("Unable to process exiting vehicle", e);
		}
	}

}
