import java.util.*;
import java.sql.* ;

public class BattleShip_DB {
  /*|--------------------|
    |By: Angel A. Robles |
    |--------------------|*/

	Connection connectDB;
	int wins, answer, loses;
	String username;
	Scanner keyboard = new Scanner(System.in) ;

	public static void main(String[] arg){
		BattleShip_DB bsdb = new BattleShip_DB() ;
		bsdb.ConnectDB();	// connect to the data base
		String name = bsdb.CheckforUser(); // enter the user you want to check
		bsdb.GetInfo(name);// check for that user and show it's ''profile''
	//	bsdb.GetInfoOfAllPlayers();
	}

	public void ConnectDB(){
		try{
			connectDB = DriverManager.getConnection("jdbc:derby:testdb");
			if (connectDB != null){
				System.out.println("Connection Success!");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}// connect db

	public String RegisterLogin(){
		System.out.println("Welcome to BattleShip: \n \t1- Register \n\t2- Log in");
		answer = Integer.parseInt(keyboard.next());
		if (answer == 1){
			System.out.println("Enter the username you want: ");
	   	    username = keyboard.next();
			InsertInfo(username,0,0);
		}
		else if(answer == 2)
		{
			System.out.println("Enter your username: ");
			username = keyboard.next();

		}
		 return username;
	}

	 public String GetInfo(String name)  {
		 try {
			 Statement stmt = connectDB.createStatement() ;

			 ResultSet rs = stmt.executeQuery ("SELECT * FROM PLAYER WHERE player_id='"+name+"'") ;
			 while(rs.next()){
				 wins  = (int) rs.getObject(2);
				 loses  = (int) rs.getObject(3);
			 }
			  System.out.println("Player  Wins   Loses \n"+name+"\t"+wins+"\t"+loses+"\t\n");
		 }
		 catch (SQLException e){
			 e.printStackTrace();
		 }
		 return name;
	 }// GetInfo

	 public void GetInfoOfAllPlayers()  {
		 try {
			 Statement stmt = connectDB.createStatement() ;

			 ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYER") ;
			Vector<String>usernameList = new Vector<String>();
			Vector<Integer>winsList = new Vector<Integer>();
			Vector<Integer>losesList = new Vector<Integer>();
			//leaderboard+="Leaderboard:";
			 while(rs.next())
			 {
				 usernameList.add(rs.getObject(1).toString());
				 winsList.add((int) rs.getObject(2));
				 losesList.add((int) rs.getObject(3));
				 System.out.println("Leaderboard:\nPlayer  Wins   Loses \n"+usernameList+"\t"+winsList+"\t"+losesList+"\t\n");
			 }

		 }
		 catch (SQLException e){
			 e.printStackTrace();
		 }
	 }

	 public void UpdateInfo(String username, boolean win, boolean lost) {
		try{
			Statement stmt = connectDB.createStatement() ;
	 		stmt.executeQuery ("SELECT * FROM PLAYER WHERE player_id='"+username+"'") ; // get the user scores
	 		if (win){// if player won, this will be true, so it add 1 more to his win score
	 			wins = wins + 1;
	 		}else{// else it add 0
	 			wins = wins + 0;
	 		}
	 		if (lost){
	 			loses = loses + 1;
	 		}else{
	 			loses = loses + 0;
	 		}
        	 stmt.execute("UPDATE PLAYER SET win = "+wins+",lose = "+loses+" WHERE player_id='"+username+"'") ;
		}
		catch (SQLException e){
			 e.printStackTrace();
		 }
	 }//update table

	 public String InsertInfo(String username, int wins, int loses) {
		try{
		    Statement stmt = connectDB.createStatement() ;

             stmt.execute("INSERT INTO PLAYER (player_id, win, lose) VALUES ('"+username+"',0,0)") ;
           }
		 catch (SQLException e){
			 e.printStackTrace();
		 }
		 return username;
	 }//insert into table

   	 public void DiconnectDB(){
		try{
			if(connectDB!=null){
				connectDB.close();
				connectDB = null;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			}
		}// disconnect db

	 public String CheckforUser() {

		System.out.println("Enter your username: ");
		username = keyboard.next();
		return username;
	}// Check for user

}// get info db
