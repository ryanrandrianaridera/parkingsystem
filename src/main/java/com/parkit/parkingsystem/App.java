package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

/**
 * This class is a Main class.
 * 
 * @author Ryan RANDRIA
 * @version 1.0
 */
public class App {
	/**
	 * Parameters of Logger.
	 */
	private static final Logger logger = LogManager.getLogger("App");

	public static void main(final String[] args) {
		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface();
	}
}