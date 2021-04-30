package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * This class contains methods that allow interaction.
 * 
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class TicketDAO {
	/**
	 * TicketDAO Logger.
	 */
	private static final Logger logger = LogManager.getLogger("TicketDAO");
	/**
	 * Instantiating DatabaseConfig.
	 */
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Save ticket to DB.
	 * 
	 * @param ticket current ticket (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE,
	 *               IN_TIME, OUT_TIME)
	 * @return boolean true if ticket was saved successfully false if the saving
	 *         process failed
	 */

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (Timestamp.valueOf(ticket.getOutTime())));
			return ps.execute();
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
		}
		return false;
	}

	/**
	 * Used to retrieve a ticket from DB.
	 *
	 * @param vehicleRegNumber Current vehicle registration number
	 * @return the latest ticket in DB (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER,
	 *         PRICE, IN_TIME, OUT_TIME)
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
				ticket.setOutTime(rs.getTimestamp(5) == null ? null : rs.getTimestamp(5).toLocalDateTime());
			}

		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return ticket;
	}

	/**
	 * Used to update given ticket with price and outTime.
	 *
	 * @param ticket should be update (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER,
	 *               PRICE,IN_TIME, OUT_TIME)
	 * @return boolean true if the ticket was updated successfully false if the
	 *         updating process failed
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
		}
		return false;
	}

	/**
	 * Used to check recurrent ticket with registration vehicle.
	 * 
	 * @param vehicleRegNumber recurrent vehicle
	 * @return return boolean loyalCustomer, true if registration number vehicle is
	 *         found false if the registration number vehicle is unknown
	 */
	public Boolean getCustomerTicketClosed(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean loyalCustomer = false;

		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_CUSTOMER_TICKET_CLOSED);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				loyalCustomer = true;
			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeResultSet(rs);
		}
		return loyalCustomer;
	}

}