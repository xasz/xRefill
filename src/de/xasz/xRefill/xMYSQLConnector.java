package de.xasz.xRefill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;




public class xMYSQLConnector {
    Logger log = Logger.getLogger("Minecraft");
    Connection con = null;
    ResultSet rs = null;
    protected final xRefill x;
    boolean status = false;
    
    
    /** 
     * create database
     * @param player
     * @param itemid
     */
    public xMYSQLConnector(final xRefill instance){
            x = instance;
    }
    
    public boolean connect(){
            try {
                    con = DriverManager.getConnection("jdbc:mysql://"+x.mysqlServer+":"+x.mysqlPort+"/"+x.mysqlDatabase,x.mysqlUser,x.mysqlPassword);
                    con.setAutoCommit(false);
                    status = true;
                    Statement stmt = this.con.createStatement();
                    stmt.executeUpdate(
                                    "CREATE TABLE IF NOT EXISTS refill"+
                                    "(" +
                                    "id INTEGER auto_increment PRIMARY KEY," +
                                    "worlduid char(100),"+
                                    "X INTEGER, "+
                                    "Y INTEGER, "+
                                    "Z INTEGER "+
                                    ");"
                            );
                    con.commit();
                    stmt.close();
                   
                    
                    
            } catch (SQLException e) {
                    writeError(e.getMessage(), true);
            }
            return status;
    }
    
    
    public void disconnect(){
            try {
                    con.commit();
                    con.setAutoCommit(true);
                    this.con.close();
                    status = false;
            } catch (SQLException e) {                      
                   writeError(e.getMessage(), true);
            }
    }
    /** 
     * error output
     * @param toWrite
     * @param severe
     */
    public void writeError(String toWrite, boolean severe) {
            if (toWrite != null) {
                    if (severe) {
                            this.log.severe("[xRefill]" +  toWrite);
                    } else {
                            this.log.warning("[xRefill]" + toWrite);
                    }
            }
    }
    /**
     * 
     * @return true = connected || false = not connected
     */
    public boolean checkConnection(){
            return status;
    }

	public boolean watchBlock(String world, int x, int y, int z) {
		connect();
		Statement state = null;
		if(!isBlockWatched(world,x,y,z)){
			try{
				state = con.createStatement();
				state.executeUpdate("INSERT INTO refill (world,x,y,z) VALUES ('"+world+"',"+x+","+y+","+z+");");
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				return false;
			}
			finally{
				try{
					con.commit();				
				}catch(Exception e){}
				try{
					state.close();
				}catch(Exception e){}
			}
			disconnect();			
			return true;
		}
		disconnect();
		return false;
	}
	public boolean isBlockWatched(String world, int x, int y, int z) {
		connect();
		Statement state = null;
		ResultSet res = null;
		boolean success = false;
		try {
		    state = con.createStatement();
			res = state.executeQuery("Select * from refill Where world = '"+world+"' and x = "+x+" and y = "+y+" and z = "+z+";");
			//getcount
			res.last();
			int count = res.getRow();
			res.beforeFirst();
			
			if(count >= 1){
			 success = true;
			}
			
		} catch (Exception e) {

		} finally{
			try{
				con.commit();				
			}catch(Exception e){}
			try{
				res.close();				
			}catch(Exception e){}
			try{
				state.close();
			}catch(Exception e){}
		}
		disconnect();
		return success;
	}
	public void disBlock(String world, int x, int y, int z) {
		connect();
		Statement state = null;
		try {
		    state = con.createStatement();
			state.executeUpdate("DELETE from refill Where world = '"+world+"' and x = "+x+" and y = "+y+" and z = "+z+";");
			//getcount
		} catch (Exception e) {

		} finally{
			try{
				con.commit();				
			}catch(Exception e){}
			try{
				state.close();
			}catch(Exception e){}
		}
		disconnect();
	}
}
