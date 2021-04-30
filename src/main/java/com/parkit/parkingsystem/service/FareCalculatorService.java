package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * This class calculates the price paid by user to exit parking.
 *
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class FareCalculatorService {

	/**
	 * @param ticket to which calculate fare ticket (ID, PARKING_NUMBER,
	 *               VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
	 */

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();

		Duration period = Duration.between(inHour, outHour);
		double duration = (double) period.toMinutes() / 60;
		double discount = 1;

		/**
		 * Get status of Customer Loyal.
		 * 
		 * @return discount with 5% if is true, no discount if is false
		 */
		if (ticket.isCustomerLoyal()) {
			discount = Fare.DISCOUNT;
		}

		// Add equation duration = 0, if duration parking less than 30 mn for any
		// vehicle
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
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

}