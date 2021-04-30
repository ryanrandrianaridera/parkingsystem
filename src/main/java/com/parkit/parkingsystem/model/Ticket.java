package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * This class permits the storage and retrieving values to ticket table from
 * database.
 *
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class Ticket {
	/**
	 * Represents unique id of ticket.
	 */
	private int id;
	/**
	 * Number of parkingspot in which the vehicle will park.
	 */
	private ParkingSpot parkingSpot;
	/**
	 * Represents registration number vehicle.
	 */
	private String vehicleRegNumber;
	/**
	 * Price will paid by user customer to exit parking.
	 */
	private double price;
	/**
	 * Arrival time to the parking.
	 */
	private LocalDateTime inTime;
	/**
	 * Exit time to the parking.
	 */
	private LocalDateTime outTime;
	/**
	 * Status of loyal customer.
	 */
	private boolean isCustomerLoyal;

	/**
	 * Getter of ticket id.
	 *
	 * @return int ticket identify
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter of ticket id.
	 *
	 * @param id to ticket
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter of the ticket parkingspot.
	 *
	 * @return parking spot instance
	 */
	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	/**
	 * Setter of the ticket parking spot.
	 *
	 * @param parkingSpot instance
	 */
	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	/**
	 * Getter of the ticket vehicleRegNumber.
	 *
	 * @return vehicle registration number associated with ticket
	 */
	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	/**
	 * Setter of the ticket vehicleRegNumber.
	 *
	 * @param vehicleRegNumber instance
	 */
	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	/**
	 * Getter of ticket price.
	 *
	 * @return the user price paid to exit parking
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Setter of ticket price.
	 *
	 * @param price to be set on the ticket
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Getter of the ticket inTime.
	 *
	 * @return incoming time
	 */
	public LocalDateTime getInTime() {
		return inTime;
	}

	/**
	 * Setter of ticket inTime.
	 *
	 * @param inTime it's time to be set on the ticket
	 */
	public void setInTime(LocalDateTime inTime) {
		this.inTime = inTime;
	}

	/**
	 * Getter of the ticket outTime.
	 *
	 * @return time to user exit parking
	 */
	public LocalDateTime getOutTime() {
		return outTime;
	}

	/**
	 * Setter of the ticket outTime.
	 *
	 * @param outTime it's time to be set on the ticket
	 */
	public void setOutTime(LocalDateTime outTime) {
		this.outTime = outTime;
	}

	/**
	 * Getter status of loyal customer.
	 *
	 * @return boolean loyalCustomer, true if registration number vehicle is found
	 *         false if the registration number vehicle is unknown
	 */
	public boolean isCustomerLoyal() {
		return isCustomerLoyal;
	}

	/**
	 * Setter status of loyal customer.
	 *
	 * @param boolean customerLoyal
	 */
	public void setStatusOfLoyalCustomer(boolean customerLoyal) {
		this.isCustomerLoyal = customerLoyal;
	}
}
