
import java.util.*;

public class BattleShip_MP {

	     /*|--------------------|
	       |By: Angel A. Robles |
	       |--------------------|*/

	// |---------------------------------------------------------READ ME -------------------------------------------------------------|
	// | To play the battle ship WITHOUT the server and client uncomment BOTH the " ChangePlayerTurn(); " at the end of each "for"    |
	// | inside the  SettingShips method. 																						      |
	// | Why? so it can switch between player 1 and player 2 while setting the ships when playing offline (without server-client).    |
	// |------------------------------------------------------------------------------------------------------------------------------|


			Scanner keyboard = new Scanner(System.in) ;
	        String row, revenge,letter,username,P1,P2 ;
	        int p1ShipsLeft, p2ShipsLeft,ROW,COL,rows,columm,direction, shipselect,shipleft;
	        int shipsleft = 0;
	        boolean Players,win,gameover,enemy;
	        int[][] tableTEMP;
	        String[][] tableP2, tableP1, p1ShipsPos, p2ShipsPos;
	        Random random;
	    	BattleShip_DB database = new BattleShip_DB();
	        //char[] letters = {'A','B','C','D','E','F','G','H','I','J'};

			public BattleShip_MP()
			{
			}
	    	public static void main(String[] arg)
	    	 {
	            BattleShip_MP BSMP = new BattleShip_MP();
	            BSMP.BattShMPStart();
	        }// Main

	        public void BattShMPStart() {
	            int r,c;
	            Init();							//Initialize
	            database.ConnectDB();
	            username = database.RegisterLogin();
	            P1 = database.GetInfo(username);
	            ChangePlayerTurn();
	            username = database.RegisterLogin();
	            P2 = database.GetInfo(username);
	            ChangePlayerTurn();
	            SettingShipsPos();					// Set Ships
	            shipsleft =	ShipsLeft();
	            ShipL(shipsleft);

             	 while(!IsGameOver()){				//Check If game is over; check if there's a winner or loser
	             		 displayBoard();				// Display Board
	             		 r = getUserMoveRow();		    //Get user row input
	             		 c = getUserMoveCol();			//Get user column input
	             		 Jugada(r,c);					// process moves
	             		 ChangePlayerTurn();			// change player's turn
	             		 shipsleft =ShipsLeft();
		               	 ShipL(shipsleft);				// check for if there are ships left
	             }
	        }//BattShMP

	        public void Init() {
	        	random = new Random();
	            COL = 10;
	            ROW = 10;
	            tableP1 = new String[ROW][ COL];
	            tableP2 = new String[ROW][ COL];
	            tableTEMP = new int[ROW][ COL];
	            p1ShipsPos = new String[10][10];
	            p2ShipsPos = new String[10][10];
	            Players = true;
	            // initialize board with nothing on, blank "" in each space.
	            for(int r=0;r<ROW;r++)
	            {
	            	for(int c=0;c<COL;c++)
	            	{
	            		tableP1[r][c]="";
	            		tableP2[r][c]="";
	            	}
	            }
	            //SettingShipsPos();

	         }//Init

	        public void SettingShipsPos() {

	            if (Players) {
	                for (int i = 0; i < 4; i++) // # que aparese es la cantidad de barcos que podra poner el jugador, EX: 4 .
	                 {
	                    displayBoard();
	                    // select which ship user want to pisition (ex: ship wt 3 space)
	                    System.out.println(" --------------------");
	                    System.out.println("| Ships: \t    |\n|   2 space ship | \n|   3 space ship | \n|   4 space ship |\n|   6 space ship |");
	                    System.out.println(" --------------------");
	                    System.out.print(""+P1+", choose ship to position 2,3,4 or 6: ");
	                    String shipselct = keyboard.next() ;
	                    this.shipselect = Integer.parseInt(shipselct);

	                    //select where the ship will be position
	                    System.out.print("Choose where you want your ship to start position at row A-J: ");
	                    row = keyboard.next() ;
	                    ProcessUsarInput_row(row); // convierte la letra a numero ex: A=0, B=1, etc.
	                    System.out.print("Choose where you want your ship to start position at colunm  0-9: ");
	                    String colum = keyboard.next() ;
	                    this.columm = Integer.parseInt(colum);

	                    // select where the rest of ship will go; ex: if user choose left, ship will go from
	                    //  the previous position(ex: F7) to the left(F6,F5 ... until all the ship is position)
	                    System.out.println("Choose where you want your ship to start position from "+ row.toString().toUpperCase() + colum + " :");
	                    System.out.println("\t 1- Up  \n\t 2- Down  \n\t 3- Left  \n\t 4- Right ");
	                    String directn = keyboard.next() ;
	                    this.direction = Integer.parseInt(directn);

	                    do // while the ship spaces is not "empty"(0) it will continue putting them on the board
	                    {
	                    	p1ShipsLeft =p1ShipsLeft +1;

	                        if (tableP1[rows][columm] == "\bS")
	                        {
	                            System.out.println("You chose that one already ... try again!");
	                            SettingShipsPos();
	                        }
	                        else
	                        {
	                            p1ShipsPos[rows][columm] = "\bS";
	                            tableP1[rows][columm] = p1ShipsPos[rows][columm];

	                        }

	                        // can be vertical or horizontal
	                        if (direction == 1)//Up
	                        {
	                            rows = rows - 1;
	                        }
	                        else if (direction == 2)//Down
	                        {
	                            rows = rows + 1;
	                        }
	                        else if (direction == 3)//Left
	                        {
	                            columm = columm - 1;
	                        }
	                        else if (direction == 4)//Right
	                        {
	                            columm = columm + 1;
	                        }
	                        shipselect = shipselect - 1;
	                    } while (shipselect != 0 );
	                 //   displayBoard();
	                }
	                // uncomment this to play without server and client
	               //ChangePlayerTurn();
	            }
	             if (!Players) {
	                 for (int i = 0; i < 4; i++)  // # que aparese es la cantidad de barcos que podra poner el jugador, EX: 4 .
	                 {
		                    displayBoard();
		                    // select which ship user want to pisition (ex: ship with 3 space)
		                    System.out.println(" --------------------");
		                    System.out.println("| Ships: \t    |\n|   2 space ship  | \n|   3 space ship  | \n|   4 space ship  |\n|   6 space ship  |");
		                    System.out.println(" --------------------");
		                    System.out.print(""+P2+", choose ship to position 2,3,4 or 6: ");
		                    String shipselct = keyboard.next() ;
		                    this.shipselect = Integer.parseInt(shipselct);

		                    //select where the ship will be position
		                    System.out.print("Choose where you want your ship to start position at row A-J: ");
		                    row = keyboard.next() ;
		                    ProcessUsarInput_row(row); // convert letter to number ex: A=0, B=1, etc.
		                    System.out.print("Choose where you want your ship to start position at colunm  0-9: ");
		                    String colum = keyboard.next() ;
		                    this.columm = Integer.parseInt(colum);

		                    // select where the rest of ship will go; ex: if user choose left, ship will go from
		                    //  the previous position(ex: F7) to the left(F6,F5 ... until all the ship is position)
		                    System.out.println("Choose where you want your ship to start position from "+ row.toString().toUpperCase() + colum + " :");
		                    System.out.println("\t 1- Up  \n\t 2- Down  \n\t 3- Left  \n\t 4- Right ");
		                    String directn = keyboard.next() ;
		                    this.direction = Integer.parseInt(directn);

		                    do // while the ship spaces is not "empty"(0) it will continue putting them on the board
		                    {
		                    	p2ShipsLeft = p2ShipsLeft +1;
		                        if (tableP2[rows][columm] == "\bS")
		                        {
		                            System.out.println("You chose that one already ... try again!");
		                            SettingShipsPos();
		                        }
		                        else
		                        {
		                            p2ShipsPos[rows][columm] = "\bS";
		                            tableP2[rows][columm] = p2ShipsPos[rows][columm];
		                        }

		                        // can be vertical or horizontal
		                        if (direction == 1)//Up
		                        {
		                            rows = rows - 1;
		                        }
		                        else if (direction == 2)//Down
		                        {
		                            rows = rows + 1;
		                        }
		                        else if (direction == 3)//Left
		                        {
		                            columm = columm - 1;
		                        }
		                        else if (direction == 4)//Right
		                        {
		                            columm = columm + 1;
		                        }
		                        shipselect = shipselect - 1;
		                    } while (shipselect != 0 );
		                        displayBoard();
		                }
	                 //uncomment this to play without server and client
	               // ChangePlayerTurn();
	            }

	        }//SettingShipsPos

			public void ProcessUsername(String P){
	        	if (Players){
	        		P1 = P;
	        	}else{
	        		P2 = P;
	        	}
	        }//process username

	        public void displayBoard()
	        {
	            String tbl = "0   1   2   3   4   5   6   7   8   9\n";

	            System.out.println("\n       ------------");
	            System.out.println("      |Battle Ship!|");
	            System.out.println("       ------------");

	            if (Players)
	            {
	            	 System.out.println("         ---------");
	 	            System.out.println("      |"+P1+"|");
	 	            System.out.println("       ---------");
	                for (int r = 0; r < ROW; r++)
	                {
	                    for (int c = 0; c < COL; c++)
	                    {
	                       tbl +=" " + tableP1[r][c] + " | ";
	                    }
	                    tbl += "\n---------------------------------------\n";
	                }
	                System.out.println(tbl);
	            }
	            else if (!Players )
	            {
	            	 System.out.println("         ---------");
		 	            System.out.println("      |"+P2+"|");
		 	            System.out.println("       ---------");

	                for (int r = 0; r < ROW; r++)
	                {
	                    for (int c = 0; c < COL; c++)
	                    {
	                        tbl += " " + tableP2[r][c] + " | ";
	                    }
	                    tbl += "\n---------------------------------------\n";
	                }
	                System.out.println(tbl);
	            }

	        }//display board

	        public void Jugada(int r, int c ) // process player's move
	        {
	        	 rows = r ;
	        	 columm = c ;
	            if (Players)
	            {
	                // X = miss | S = ships | * = your ship hit | E = enemy ship hit
	                 if (tableP2[rows][columm] == "\bS")
	                {
	                    p2ShipsLeft= p2ShipsLeft - 1;
	                    tableP1[rows][columm] = "\bE";
	                    tableP2[rows][columm] = "\b*";
	                    displayBoard();
	                }

	                else if (tableP2[rows][columm] == "X" || tableP2[rows][columm] == "*")
	                {
	                    System.out.println("You chose that one already ... try again!");
	                    getUserMoveRow();
	        	        getUserMoveCol();
	                }
	                else if (tableP2[rows][columm] != "X" || tableP2[rows][columm] != "\bS" || tableP2[rows][columm] != "*")
	                {
	                    tableP1[rows][columm] = "\bX";
	                    //Players = false;
	                }
	            }
	            else if (!Players)
	            {
	                // X = miss | S = ships | * = ship hit | E = enemy ship hit
	                if (tableP1[rows][columm] == "X" || tableP1[rows][columm] == "*")
	                {
	                    System.out.println("You chose that one already ... try again!");
	                    getUserMoveRow();
	        	        getUserMoveCol();
	                }

	                else if (tableP1[rows][columm] == "\bS")
	                {
	                    p1ShipsLeft = p1ShipsLeft - 1;
	                    tableP2[rows][columm] = "\bE";
	                    tableP1[rows][columm] = "\b*";
	                    displayBoard();
	                }
	                else if (tableP1[rows][columm] != "X" || tableP1[rows][columm] != "\bS" || tableP1[rows][columm] != "*")
	                {
	                    tableP2[rows][columm] = "\bX";
	                }
	            }
	        }// Judaga

			public void ReceiveJugada(int r, int c ){
			// method for server - client only.
			// Recibe las jugadas, verifica en los tableros del que la recive y dice si le dio.

	            if (Players){
	                 if (tableP1[r][c].equals("\bS")){

	                	tableP1[r][c] = "\b*";
	                    p1ShipsLeft= p1ShipsLeft - 1;
	                    enemy =true ;
	                    }
	            }else if (!Players){
	                 if (tableP2[r][c].equals("\bS")){

	                	tableP2[r][c] = "\b*";
	                    p2ShipsLeft= p2ShipsLeft - 1;
	                    enemy =true;
	                    }
	             }
	             else {
	             	enemy =false;
	             }
	        }// receive jugadas

			public boolean Mark(){
				//return true so it can mark on the board that there was a hit; mark as "E"
			boolean mark;
				if(this.enemy == true)
				{
					mark = true;
				}
				else {
					mark = false;
				}
				return mark;
				}// mark enemy

		    public void MarkEnemy(int r, int c ){
			 	 if (Players){
	                 tableP1[r][c] = "\bE";
			 	 }
	             else if (!Players){
	                 tableP2[r][c] = "\bE";
	             }
			 }// mark enemy

	        public int GetUserMove(){
	        	System.out.print("Player choose between rows A-J: ");
	            row = keyboard.next() ;
	            ProcessUsarInput_row(row);
	            System.out.print("Player choose between columns 0-9: ");
	            String colum = keyboard.next() ;
	            this.columm = Integer.parseInt(colum);

	            return tableTEMP[rows][columm];

	        }// get uset move

	        public int getUserMoveRow(){
	        	//SuggestedMoveAI();
	        	System.out.print("Player choose between rows A-J: ");
	            row = keyboard.next() ;
	            ProcessUsarInput_row(row);

	           return rows;
	        }//get user move

	        public int getUserMoveCol(){
	        	System.out.print("Player choose between columns 0-9: ");
	            String colum = keyboard.next() ;
	            this.columm = Integer.parseInt(colum);
	        	/*if (columm > 9 &&  columm <0)
	        	{
	        		System.out.println("Choose between 0 - 9! ");
	        	    System.out.print("Player choose between columns 0-9: ");
	 	            colum = keyboard.next() ;
	                this.columm = Integer.parseInt(colum);
	        	}*/

	        	return columm;
	        }//get user move

	        public int ShipsLeft(){
	        	// get how many ships are left
	        	if (Players )
	        		{ shipsleft = this.p1ShipsLeft;}
	        	else if (!Players )
        	    	{ shipsleft = this.p2ShipsLeft;}
	        	return shipsleft;

	        }//ships left

	        public void ShipL(int shpleft){
	        	 shipleft = shpleft;
	        	if (Players )
        		{ shipleft = this.p1ShipsLeft;}
            	else if (!Players )
    	    	{ shipleft = this.p2ShipsLeft;}
	        }//shiplt

	        public boolean WinLose() {

	            if(this.shipleft == 0)
	            {return true;}
	            else
	            {return false; }
	        }// WinLose

	        public boolean IsGameOver()
	        {
	            if (WinLose() == true && !Players){
	                System.out.println("Player 1 WIN!");
	                database.UpdateInfo(P1,true,false);
	                return true;
	             }
	            else if (WinLose() && Players){
	                 System.out.println("Player 2 WIN!");
	                 database.UpdateInfo(P2,true,false);
	                 return  true;
	             }
	            else
	            {return false;}

	        }// isGameover

	        public void ChangePlayerTurn(){
	        	 Players=!Players;
	        }// change turn

	        public String ProcessUsarInput_row(String m)
	        {
	        	switch (row.toUpperCase())
	            {
	                case "A":
	                    //   Convert.ToInt32(movida);
	                    rows = 0;
	                    break;
	                case "B":
	                    rows = 1;
	                    break;
	                case "C":
	                    rows = 2;
	                    break;
	                case "D":
	                    rows = 3;
	                    break;
	                case "E":
	                    rows = 4;
	                    break;
	                case "F":
	                    rows = 5;
	                    break;
	                case "G":
	                    rows = 6;
	                    break;
	                case "H":
	                    rows = 7;
	                    break;
	                case "I":
	                    rows = 8;
	                    break;
	                case "J":
	                    rows = 9;
	                    break;

	                default:
	                    System.out.println("Choose between A-E");
	                    getUserMoveRow();
	                    break;
	            }
	            return Integer.toString(rows);
	        }//ProcessUsarInput

	        public boolean ProcessUsarInput_colum(int columm)
	        {
	        	if(columm >= 0 &&  columm < 10)
	        	{
	        		return true;
	        	}
	        	else{
	        		System.out.println("Choose between 0 to 9");
        			return false;
	        	}


	        }//ProcessUsarInput

	        public void SuggestedMoveAI(){
	        	int fila,clmna;

	        	fila = rows=random.nextInt(9);
	        	clmna= random.nextInt(9);

	        	// if the suggested move finds a ship check the aroud to find for the rest for the ship.
	        	if (tableP1[fila][clmna] == "\bS"){
	        		while (tableP1[fila][clmna] == "\bS"){

                        fila = fila - 1;
                        if (tableP1[fila][clmna] == "\bS"){
            	        	System.out.println("Suggested move: "+letter+ clmna);
                            while (tableP1[fila][clmna] == "\bS"){
                        	    fila = fila -1;
                	        	System.out.println("Suggested move: "+letter+ clmna);
                            }
                        }

                        fila = fila + 1;
                        if (tableP1[fila][clmna] == "\bS"){
            	        	System.out.println("Suggested move: "+letter+ clmna);
                            while (tableP1[fila][clmna] == "\bS"){
                        	    fila = fila +1;
                	        	System.out.println("Suggested move: "+letter+ clmna);
                            }
                        }

                        clmna = clmna - 1;
                        if (tableP1[fila][clmna] == "\bS"){
            	        	System.out.println("Suggested move: "+letter+ clmna);
                            while (tableP1[fila][clmna] == "\bS"){
                            	clmna = clmna - 1;
                	        	System.out.println("Suggested move: "+letter+ clmna);
                            }
                        }

                        clmna = clmna + 1;
                        if (tableP1[fila][clmna] == "\bS"){
            	        	System.out.println("Suggested move: "+letter+ clmna);
                            while (tableP1[fila][clmna] == "\bS"){
                            	clmna = clmna + 1;
                	        	System.out.println("Suggested move: "+letter+ clmna);
                            }
                        }
	        		}
	        	}
	        	else{
		        	System.out.println("Suggested move: "+letter+ clmna);
		        	}
	        	switch (fila)
	            {
	                case 0:
	                	letter = "A";
	                    break;
	                case 1:
	                	letter = "B";
	                    break;
	                case 2:
	                	letter = "C";
	                    break;
	                case 3:
	                	letter = "D";
	                    break;
	                case 4:
	                	letter = "E";
	                    break;
	                case 5:
	                	letter = "F";
	                    break;
	                case 6:
	                	letter = "G";
	                    break;
	                case 7:
	                	letter = "H";
	                	break;
	                case 8:
	                	letter = "I";
	                    break;
	                case 9:
	                	letter = "J";
	                    break;
	                default:
	                    System.out.println("Choose between A-E");
	                    getUserMoveRow();
	                    break;
	            }
	        	//System.out.println("Suggested move: "+letter+ clmna);

	        }// Suggested Move

 }// BattleS class
