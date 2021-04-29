package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();

		// TODO: Some tests are failing here. Need to check if this logic is correct

		Duration period = Duration.between(inHour, outHour);
		double duration = (double) period.toMinutes() / 60;
		double discount = 1;

		if (ticket.isCustomerLoyal()) {
			discount = Fare.DISCOUNT;
			// System.out.println("Get Status Of Loyal Customer : " +
			// ticket.isCustomerLoyal());
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
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

}