package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService
{
	
	TicketDAO td = new TicketDAO();

	public void calculateFare(Ticket ticket)
	{

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())))
		{
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		double inHour = ticket.getInTime().getTime();
		double outHour = ticket.getOutTime().getTime();
		double duration = (outHour - inHour) / 3600000;
		
		if (duration <= 0.5)
		{
			ticket.setPrice(0);
		}
		
		else
		{
			switch (ticket.getParkingSpot().getParkingType())
			{
			case CAR:
			{
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				break;
			}
			case BIKE:
			{
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

		if (td.countTicket(ticket.getVehicleRegNumber()) > 1)
		{
			ticket.setPrice(ticket.getPrice() * 0.95);
		}
	}

}