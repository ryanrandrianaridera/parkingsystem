package com.parkit.parkingsystem.constants;

/**
 * This class contains different SQL queries.
 *
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class DBConstants {
	// fix error Utility classes should not have a public or default constructor.
	private DBConstants() {
	}

	/**
	 * Sql query to used for available parking spots.
	 */
	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	/**
	 * Sql query to used to update parking spot.
	 */
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
	/**
	 * Sql query to used to save tickets into DB.
	 */
	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	/**
	 * Sql query to used to update ticket with price and outTime.
	 */
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	/**
	 * Sql query to used to retrieve a ticket from DB in order DESC.
	 */
	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
	/**
	 * Sql query to used to list customer with ticket closed.
	 */
	public static final String GET_CUSTOMER_TICKET_CLOSED = "select ID from ticket where VEHICLE_REG_NUMBER = ? AND OUT_TIME IS NOT NULL LIMIT 1";
}