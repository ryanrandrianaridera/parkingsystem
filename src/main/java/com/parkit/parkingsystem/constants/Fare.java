package com.parkit.parkingsystem.constants;

/**
 * This class contains contain setup of fare and discount.
 * 
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class Fare {
	// fix error Utility classes should not have a public or default constructor.
	private Fare() {
	}

	/**
	 * Represent the fare for one hour parking per vehicle.
	 */
	public static final double BIKE_RATE_PER_HOUR = 1.0;
	public static final double CAR_RATE_PER_HOUR = 1.5;
	/**
	 * Represent the fare discount.
	 */
	public static final double DISCOUNT = 0.95;
}