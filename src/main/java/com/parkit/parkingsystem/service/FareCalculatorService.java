package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		/*
		 * int inHour = ticket.getInTime().getHours(); int outHour =
		 * ticket.getOutTime().getHours();
		 */

		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		// int duration = outHour - inHour;

		Duration period = Duration.between(inHour, outHour);
		double duration = (double) period.toMinutes() / 60;
		double discount;

		/*
		 * if (duration < 0.5) { // System.out.println("here"); ticket.setPrice(0); }
		 * 
		 * else {
		 */

		TicketDAO ticketDAO = new TicketDAO();
		Boolean statutOfLoyalCustomer = ticketDAO.getCustomerTicketClosed(ticket.getVehicleRegNumber());
		// System.out.println("ticket.getParkingSpot().getParkingType(): " +
		// ticket.getParkingSpot().getParkingType());
		if (statutOfLoyalCustomer) {
			discount = 0.95;
		} else {
			discount = 1;
		}
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			double price = (duration < 0.5) ? 0 : duration * Fare.CAR_RATE_PER_HOUR * discount;
			ticket.setPrice(price);
		}
			break;

		case BIKE: {
			{
				double price = (duration < 0.5) ? 0 : duration * Fare.BIKE_RATE_PER_HOUR * discount;
				ticket.setPrice(price);
			}
			break;
		}
		default:
			// System.out.println("ou");
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

}