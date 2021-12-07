package com.parkit.parkingsystem.constants;


/**
 * Class which regroup all SQL requests
 * @author Silvio
 *
 */

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    
  /*
   *  Modify GET_TICKET : add "p.AVAILABLE" to be able to access to this data in database
   */
    
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE, p.AVAILABLE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    
 /*
  * Add COUNT_TICKET request
  */
    
    public static final String COUNT_TICKET = "select count(*) FROM prod.ticket where VEHICLE_REG_NUMBER = ?";
}
