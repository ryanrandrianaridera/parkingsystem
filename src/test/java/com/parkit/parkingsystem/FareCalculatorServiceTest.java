package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void Setup() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void SetUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	@DisplayName("Calcul fare car price according to parking time spent")
	public void calculateFareCar() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// ACT
		fareCalculatorService.calculateFare(ticket);
		// ASSERT
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	@DisplayName("Calcul fare bike price according to parking time spent")
	public void calculateFareBike() {
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	@DisplayName("Send an Exception for trying to calculate fare price or unknow parking spot type")
	public void calculateFareUnkownType() {
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("Send an Exception for trying to calculate fare price based on an earlier outime than intime")
	public void calculateFareBikeWithFutureInTime() {
		LocalDateTime inTime = LocalDateTime.now().plusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("Calculate fare car price less than one hour")
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45); // 45 minutes parking time should give 3/4th
																		// parking fare
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	@DisplayName("Calculate fare bike price less than one hour")
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45); // 45 minutes parking time should give 3/4th
																		// parking fare
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	@DisplayName("Calculate fare car price less than thirty minutes and verify price for parking time free")
	public void calculateFareCarWithLessThanThirtyMinutesParkingTime() { // Less than30 minutes parking time should give
																			// a free
																			// parking fare
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(0, ticket.getPrice());
	}

	@Test
	@DisplayName("Calculate fare bike price less than thirty minutes and verify price for parking time free")
	public void calculateFareBikeWithLessThanThirtyMinutesParkingTime() { // Less than30 minutes parking time should
																			// give a free
																			// parking fare
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(0, ticket.getPrice());
	}

	@Test
	@DisplayName("Calculate fare car price more than a day")
	public void calculateFareCarWithMoreThanADayParkingTime() {
		LocalDateTime inTime = LocalDateTime.now().minusHours(25); // 24 hours parking time should give 24 *
																	// parking fare per hour
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((25 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	@DisplayName("Calculate fare car price mwith loyal customer and verify discount application price")
	public void calculateFareCarLoyalCustomer() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setStatusOfLoyalCustomer(true);
		fareCalculatorService.calculateFare(ticket);

		assertEquals((Fare.DISCOUNT * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	@DisplayName("Calculate fare bike price mwith loyal customer and verify discount application price")
	public void calculateFareLoyalCUstomer() {

		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setStatusOfLoyalCustomer(true);
		fareCalculatorService.calculateFare(ticket);

		assertEquals((Fare.DISCOUNT * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}
}
