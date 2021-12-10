package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * Methods for SQL requests to Ticket database
 * @author Silvio
 *
 */

public class TicketDAO
{

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();


	/**
	 * Add countTicket Method to check if a same reg number is already exist in database
	 * with the SQL request COUNT_TICKET
	 * @param vehicleRegNumber
	 * @return the count of the vehiculeRegNumber in database
	 */
	
	public int countTicket(String vehicleRegNumber)
	{
		Connection con = null;

		int count = 0;
		try
		{
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.COUNT_TICKET);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				count = rs.getInt(1);
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		}
		catch (SQLException | ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			dataBaseConfig.closeConnection(con);
		}
		return count;
	}
	
	/**
     * Save ticket object to Ticket table.
     * @param ticket object to save
     * @return true if operation succeeded
     */
	public boolean saveTicket(Ticket ticket)
	{
		Connection con = null;
		
		try{
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// ps.setInt(1,ticket.getId());
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			
			return ps.execute();
			
		} 
		
		catch (Exception ex)
		{
			logger.error("Error fetching next available slot", ex);
		} 
		
		
		finally
		{
			dataBaseConfig.closeConnection(con);
		}
		return false;
		
	}
	
	/**
     * Get ticket object from Ticket table.
     * @param vehicleRegNumber used to retrieve ticket
     * @return ticket object
     */
	public Ticket getTicket(String vehicleRegNumber)
	{
		Connection con = null;
		Ticket ticket = null;
		try
		{
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), rs.getBoolean(7));
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex)
		{
			logger.error("Error fetching next available slot", ex);
		} finally
		{
			dataBaseConfig.closeConnection(con);
			
		}
		return ticket;
	}
	/**
     * Modify ticket in Ticket table.
     * @param ticket object to modify
     * @return true if operation succeeded
     */
	public boolean updateTicket(Ticket ticket)
	{
		Connection con = null;
		try
		{
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			
			dataBaseConfig.closePreparedStatement(ps);
			return true;
		} catch (Exception ex)
		{
			logger.error("Error saving ticket info", ex);
		} finally
		{
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}
}
