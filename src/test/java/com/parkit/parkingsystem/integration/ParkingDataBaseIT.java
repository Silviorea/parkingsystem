package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


    

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
   
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
       
    }

    @BeforeEach
    // Avant chaque test on retourne une voiture et une plaque
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
    	
    	// GIVEN
    
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        

        // WHEN
        parkingService.processIncomingVehicle();
        
        
        // THEN
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        // test si methode get.ticket ne retourne pas résultat null => bien inscrit en BDD
        assertNotNull(ticket);
        // test si getParkingSpot retourne bien false => la place donnée n'est plus disponible lors de l'entrée au parking
        assertEquals(ticket.getParkingSpot().isAvailable(), false);
  
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
   
    
    }

    @Test
    public void testParkingLotExit() throws InterruptedException{
        
    	// GIVEN
    	
    	testParkingACar();
    	Thread.sleep(2000);
    	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	
        
        // WHEN
    	
        parkingService.processExitingVehicle();
        
        // THEN
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        //test gestOutTIme n'est pas null
        assertNotNull(ticket.getOutTime());
        //test getPrice n'est pas null
        assertNotNull(ticket.getPrice());
        // test si getParkingSpot retourne bien True une fois le véhicule sortie => place à nouveau libre
        assertEquals(ticket.getParkingSpot().isAvailable(), true);
        
        
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

}
