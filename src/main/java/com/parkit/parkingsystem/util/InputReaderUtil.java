package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

/**
 * Class use for exception handling when user does a choice.
 * @author Silvio
 *
 */

public class InputReaderUtil {
	
	private final Logger logger = LogManager.getLogger("InputReaderUtil");
    private Scanner scan = new Scanner(System.in);
    
    
    /**
     * Get integer from scan member attribute nextLine().
     * @return user shell entry as Integer
     */
    
    public int readSelection() {
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }
    
    /**
     * Get vehicleRegistrationNumber from scan member attribute nextLine().
     * @return validated user registration number
     * @throws Exception IllegalArgumentException for wrong registration number format
     */
    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
