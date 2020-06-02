import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class BattleShip_Cliet
{
  /*|--------------------|
    |By: Angel A. Robles |
    |--------------------|*/

   public static final int PORT = 32323 ;
   public BattleShip_MP bsgame ;
   public BattleShip_DB database;
   public Socket clientSK ;
   public DataInputStream din ;
   public DataOutputStream dout ;

   public static void main(String[] arg)
   {
	   BattleShip_Cliet BSMP = new BattleShip_Cliet() ;
   }

   public BattleShip_Cliet()
   {
	   bsgame = new BattleShip_MP() ;
	   database = new BattleShip_DB();

      try
      {
         System.out.print("Connecting to server...") ;
         clientSK = new Socket("localhost", PORT) ;
         din = new DataInputStream(clientSK.getInputStream()) ;
         dout = new DataOutputStream(clientSK.getOutputStream()) ;
         System.out.println("done!") ;

         int r = 0;
         int c = 0;
         int shipsleft;
         String BoardState,username,P2;

    	 bsgame.Init();
    	 bsgame.ChangePlayerTurn(); // change turn to player 2

	     username = database.RegisterLogin();
	     sendInfo(username);// send the user to the server so it get the "Profile"
	     P2 = receiveInfo();
	     bsgame.ProcessUsername(P2);

		 bsgame.SettingShipsPos();
    	 dout.writeBoolean(true);  // tell opponent i'm done setting ships
    	 ready();
    	 shipsleft =	bsgame.ShipsLeft();
        	dout.writeInt(shipsleft);
        	shipsleft =	din.readInt();
        	bsgame. ShipL(shipsleft);

    	 while(!bsgame.IsGameOver()){
    		 r = receiveMoveR();
    		 c = receiveMoveC();
    		 bsgame.ReceiveJugada(r,c);

    		 if(!bsgame.IsGameOver() ){
    			  bsgame.displayBoard();
    		 }
    		 bsgame.SuggestedMoveAI();
    		 r = bsgame.getUserMoveRow();	//Get user input
     		 c = bsgame.getUserMoveCol();
     		 bsgame.Jugada(r,c);   	// Process input
     		 sendMoveR(r);
     		 sendMoveC(c);
     			 if( bsgame.Mark()){
            	     bsgame.MarkEnemy(r,c);
            	     }
     		 shipsleft =	bsgame.ShipsLeft();
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
   public void sendInfo(String input) throws Exception
   {
      dout.writeUTF(input);
   }

   public String receiveInfo() throws Exception
   {
 	  String input = din.readUTF();
       return input;
   }
   public boolean ready() throws Exception{
    	System.out.println("Waiting for oponent to finsih setting up his ships .....\n") ;
    	boolean ready = din.readBoolean();
    	System.out.println("Let the game begin!");
    	return ready;
 }
}
