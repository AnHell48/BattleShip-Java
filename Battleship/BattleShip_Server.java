import java.io.* ;
import java.net.* ;


public class BattleShip_Server
{
  /*|--------------------|
    |By: Angel A. Robles |
    |--------------------|*/

   public static final int PORT = 32323 ;
   public ServerSocket serverSK ;

   public static void main(String[] arg)
   {
	   BattleShip_Server BSMP = new BattleShip_Server() ;
   }//main

   public BattleShip_Server()
   {
      try
      {
         System.out.print("Creating server socket...") ;
         serverSK = new ServerSocket(PORT) ;
         System.out.println("done!") ;

         while(true)
         {
            System.out.print("Waiting for opponent to connect...") ;
            Socket clientSK = serverSK.accept() ;
            System.out.println("done!") ;

            ClientServicingThread cst = new ClientServicingThread(clientSK) ;
            cst.start() ;
         }
      }
      catch(Exception e)
      {
         e.printStackTrace() ;
      }
   }

   private class ClientServicingThread extends Thread
   {

	  public BattleShip_MP bsgame ;
      public BattleShip_DB database;
      public Socket clientSK ;
      public DataInputStream din ;
      public DataOutputStream dout ;


      public ClientServicingThread(Socket clientSK) throws Exception
      {
         this.clientSK = clientSK ;
         bsgame = new BattleShip_MP() ;
	     database = new BattleShip_DB();
         din = new DataInputStream(clientSK.getInputStream()) ;
         dout = new DataOutputStream(clientSK.getOutputStream()) ;
      }

      public void run()
      {
         try
         {
        	int r,c,shipsleft;
			String BoardState, username,P1,P2 ;

           	bsgame.Init();							   //Initialize

           	database.ConnectDB();					// connect to data base
	        username = database.RegisterLogin();    	// ask the user to log in or register
	        P1 = database.GetInfo(username);		//get the user "profile"
      	    bsgame.ChangePlayerTurn();
	        username = receiveInfo();
	        P2 = database.GetInfo(username);		//get the user "profile"
	        sendInfo(P2);
	        bsgame.ChangePlayerTurn();

	        bsgame.ProcessUsername(P1);
           	bsgame.SettingShipsPos();					// Set Ships
           	dout.writeBoolean(true);				// tell opponent im ready
           	ready();                                    // wait till opponent finish setting ships
           	shipsleft =	bsgame.ShipsLeft();			//send ships left ( how many ships are on the board)
           	dout.writeInt(shipsleft);
           	shipsleft =	din.readInt();
           	bsgame. ShipL(shipsleft);

            	 while(!bsgame.IsGameOver()){				//Check If game is over; check if there's a winner or loser
            		 bsgame.displayBoard();				    		// Display Board
            		 r = bsgame.getUserMoveRow();					//Get user input
             		 c = bsgame.getUserMoveCol();
             		 bsgame.Jugada(r,c);   							// Process input
            	     sendMoveR(r); 									// send move to client
            	     sendMoveC(c);
            	     if( bsgame.Mark()){
            	     	bsgame.MarkEnemy(r,c);
            	     }

            		 if(!bsgame.IsGameOver()){
            			 bsgame.displayBoard();
            		 }
            			r = receiveMoveR();
            			c = receiveMoveC();
            	        bsgame.ReceiveJugada(r,c);			// process client move
            		    shipsleft = bsgame.ShipsLeft();
                    	dout.writeInt(shipsleft);
                    	shipsleft =	din.readInt();
                    	bsgame. ShipL(shipsleft);
            	 }
            din.close() ;
            dout.close() ;
            clientSK.close() ;
         }
         catch(Exception e)
         {
            e.printStackTrace() ;
         }
      }
  	  public void sendInfo(String input) throws Exception{
  	     dout.writeUTF(input);
 	  }

 	  public String receiveInfo() throws Exception{
 		 String input = din.readUTF();
  	     return input;
  	  }

      public void sendMoveR(int r) throws Exception
      {
    	  //send the board miss or hit.
         System.out.print("Sending move...") ;
         dout.writeInt(r) ;
         System.out.println("done!") ;
      }
      public void sendMoveC(int c) throws Exception
      {
    	  //send the board miss or hit.
         System.out.print("Sending move...") ;
         dout.writeInt(c) ;
         System.out.println("done!") ;
      }

      public int receiveMoveR() throws Exception
      {
         System.out.print("Receiving move...") ;
         int r = din.readInt() ;
         System.out.println("done!") ;

         return r ;
      }
      public int receiveMoveC() throws Exception
      {
         System.out.print("Receiving move...") ;
         int c = din.readInt() ;
         System.out.println("done!") ;

         return c ;
      }
      public boolean ready() throws Exception{
         	System.out.println("Waiting for oponent to finsih setting up his ships .....\n") ;
         	boolean ready = din.readBoolean();
         	System.out.println("Let the game begin!");
         	return ready;
      }

   }
}
