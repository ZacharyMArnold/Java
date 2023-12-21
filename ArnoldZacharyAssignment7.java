import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

//Zachary Arnold 
//CS1450 
//Section 001 (M/W)
//Due Date: 28 October 2021
//Assignment #7
//This assignments purpose is to learn to use Queues and Priority Queues.
//This program will open a file to read values from and create a railroad objects.
//It continue reading and create train objects while filing them into specific railroad tracks.
//It will also open a file to read values from and create RailCar objects while adding them to a Queue.
//It will then begin a simulation where it will move the RailCars to the appropriate trains. Following that it will move the Train to the Departure Track and clear them for departure.
//SortyingYard status milestones will be displayed: After reading and creating all objects, after moving Railcars to trains, and after all trains have departed.


public class ArnoldZacharyAssignment7 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int trackNumber = 0; 
		
		//Open file for reading trains as input into program. Utilizes scanner to read assignment file.
		File fileTrains = new File("Trains7.txt"); 
		Scanner inputTrains = new Scanner(fileTrains);
		
		//Create a Railroad object and begin reading file to create Train objects and place them into the sortingYard on the corresponding track.
		Railroad myRailroad = new Railroad (inputTrains.nextInt());
		
		//The while loop continues until there is no more data to read from file.
		//For creating and adding a Train object each input is read directly into the constructor. *Utilized nextLine to allow for multiple word cities.
		while(inputTrains.hasNext()) {
			trackNumber = inputTrains.nextInt();
			myRailroad.addTrainToSortingYard(trackNumber, new Train (inputTrains.nextInt(),inputTrains.next(), inputTrains.nextInt(), inputTrains.next(), inputTrains.nextLine().trim()));
		}
		
		//close input file
		inputTrains.close(); 
		
		//call the Railroad method to display the sortingYard without rail cars.
		System.out.println("Loading trains onto tracks in sorting yard...");
		myRailroad.displaySortingYard();
		
		//Open file for reading rail cars as input into program. Utilizes scanner to read assignment file.
		File fileRailCars = new File("RailCars7.txt"); 
		Scanner inputRailCars = new Scanner(fileRailCars);
		
		//The while loop continues until there is no more data to read from file.
		//For creating and adding a RailCar object to the recevingTrack each input is read directly into the constructor.
		while (inputRailCars.hasNext()) {	
			myRailroad.addRailCarToReceivingTrack(new RailCar(inputRailCars.nextInt(), inputRailCars.next(), inputRailCars.next().trim()));
		}
		
		//Create a new RailroadController object.
		RailroadController railroadController = new RailroadController();
		
		//close input file
		inputRailCars.close();
		
		//Display the ReceivingTrack queue contents.
		System.out.println("Loading rail cars on receiving track...");
		myRailroad.displayReceivingTrack();
		
		//Start the railroad simulation by moving RailCars to applicable Trains.
		System.out.println("\n\nStart railroad yard simulation...\n\n");
		
		System.out.println("Controller: Moving rail cars from receiving track to correct sorting yard track:");
		for (int i =0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		railroadController.moveRailCarsToTrains(myRailroad);
		
		
		//call the Railroad method to display the sortingYard with rail cars.
		System.out.println("Showing sorting yard with trains and rail cars...");
		myRailroad.displaySortingYard();
		
		//Move the Trains from the sortingYard to the departureTrack PriorityQueue.
		System.out.println("Controller: Moving trains from sorting yard to departure track:");
		for (int i =0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		railroadController.moveTrainsToDepartureTrack(myRailroad);
		
		//Clear the Trains for departure and remove them from the departureTrack.
		System.out.println("Controller: Moving trains from departure track to main line - longer trains go first:");
		for (int i =0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		railroadController.clearedForDeparture(myRailroad);
		
		//call the Railroad method to display the empty sortyingYard
		System.out.println("Showing sorting yard with no trains...");
		myRailroad.displaySortingYard();
		
		System.out.println("\n\nEnd railroad yard simulation...");
	}
	
}

//Railroad Class
class Railroad {
	private int numberTracks;
	private Train[] sortingYard; //Array of objects inside of an object.
	private Queue<RailCar> receivingTrack; //Queue of RailCars.
	private PriorityQueue<Train> departureTrack; //Priority 

	//Overloaded constructor that saves size of railroad and allocates memory for an array of Train objects, a Queue of RailCar Objects, and a PriorityQueue of Train Objects.
	public Railroad (int numberTracks) {
		this.numberTracks = numberTracks;
		sortingYard = new Train[numberTracks];
		receivingTrack = new LinkedList<>();
		departureTrack = new PriorityQueue<>();
	}
	
	//Getter for the number of tracks within the railroad.
	public int getNumberTracks() {
		return numberTracks;
	}

	//Getter for the Train object at a specific track.
	public Train getTrainInSortingYard (int trackNumber) {
		return sortingYard[trackNumber];
	}
	
	//Setter for adding Train to SortingYard
	public void addTrainToSortingYard (int trackNumber, Train train) {
		sortingYard[trackNumber] = train;
	}
	
	//Returns whether the Queue, receivingTrack is empty.
	public boolean isReceivingTrackEmpty () {
		return receivingTrack.isEmpty();
	}
	
	//method to remove and return a RailCar from the receivingTrack Queue.
	public RailCar removeRailCarFromReceivingTrack () {
		return receivingTrack.remove();
	}
	
	//method to add a RailCar to the receivingTrack Queue.
	public void addRailCarToReceivingTrack (RailCar railcar) {
		receivingTrack.offer(railcar);
	}
	
	//Returns whether the PriorityQueue, departureTrack is empty.
	public boolean isDepartureTrackEmpty () {
		return departureTrack.isEmpty();
	}
	
	//method to remove and return a Train from the departureTrack PriorityQueue.
	public Train removeTrainFromDepartureTrack () {
		return departureTrack.remove();
	}
	
	//method to add a Train to the departureTrack PriorityQueue.
	public void addTrainToDepartureTrack (Train train) {
			departureTrack.offer(train);
	}
	
	//method to return the track of the Train the RailCar is to be added to.
	public int findTrain(RailCar railcar) {
		int trackNumber = 0; //Index for the sortingYard track.
		boolean foundTrain = false; //boolean to exit while loop once the train is found to add the RailCar to.
		
		//While loop that compares RailCar destination and type to the Train destination and type. When both compares are a match (both are a 0), the Train is found and exits the loop.
		while (!foundTrain) {
			if (Objects.isNull(sortingYard[trackNumber])) {
				trackNumber++;
			}
			else if (sortingYard[trackNumber].getDestinationCity().compareTo(railcar.getRailCarDestination()) != 0 || sortingYard[trackNumber].getTrainType().compareTo(railcar.getRailCarType()) != 0) {
				trackNumber++;
			}
			else {
				foundTrain = true;
			}
		}
		//returns the sortingYard track that the RailCar is to be added to.
		return trackNumber;
	}
	
	//method to add a RailCar to the Train on the specified Track.
	public void addRailCartoTrainInSortingYard(RailCar railcar, int trackNumber) {
		sortingYard[trackNumber].addRailCar(railcar);
	}
	
	//method to remove the Train on the specified Track.
	public void removeTrainFromSortingYard(int trackNumber) {
		sortingYard[trackNumber] = null;
	}
	
	//method to display the contents of receivingTrack Queue.
	public void displayReceivingTrack() {
		//Prints Header
		for (int i = 0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		System.out.println(String.format("%s\t%-15s%s", "Number", "Type", "Destination"));
		for (int i = 0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		
		//For loop to look at the element at the head of the Queue and print it. Then it removes it from the Head and adds it to the back of the queue.
		//I am able to utilize the .size() because I not altering the size but only cycling through the Queue contents.
		for (int i = 0; i < receivingTrack.size(); i++) {
			System.out.println(receivingTrack.element().toString());
			receivingTrack.offer(receivingTrack.remove());
		}
		System.out.println();
	}
	
	//Method for displaying the sortingYard layout.
	public void displaySortingYard() {
		
		//Prints header
		for (int i =0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		System.out.println(String.format("%s\t%s\t\t%s\t\t%s\t%-15s\t%s", "Track", "Engine", "Company", "Rail Cars", "Type", "Destination"));
		for (int i =0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
		
		//Prints data about the tracks at the specific track. If the value saved is null, then it prints dashes for the corresponding data.
		for (int i = 0; i < numberTracks; i++) {
			if (Objects.isNull(sortingYard[i])) {
				System.out.println(String.format("%d\t%s\t\t%s\t\t%s\t\t%-15s\t%s", i , "----", "----", "----", "----", "----"));
			}
			else {
				System.out.println( i + "\t" + sortingYard[i].toString());
			}
		}
		System.out.println();
	}
	
} //End Railroad Class

//Train Class
class Train implements Comparable<Train> {
	
	//Instance Variables for each component that makes up a Train.
	private int engineNumber;
	private int numberRailCars;
	private String company;
	private String type;
	private String destinationCity;
	private Queue<RailCar> railCars;
	
	
	//Overloaded constructor to create Train object and fill instance variables. Also allocates memory to the railCars Queue.
	public Train(int engineNumber, String company, int numberRailCars, String type, String destinationCity) {
		this.engineNumber = engineNumber;
		this.company = company;
		this.numberRailCars = numberRailCars;
		this.type = type;
		this.destinationCity = destinationCity;
		railCars = new LinkedList<>();
	}
	
	//method to add a RailCar to the Train.
	public void addRailCar (RailCar railcar) {
		railCars.offer(railcar);
		numberRailCars = numberRailCars + 1;
	}
	
	//Getter for each instance variable.
	public int getEngineNumber() {
		return engineNumber;
	}
	
	public int getNumberRailCars() {	
		return numberRailCars;
		}
		
	public String getCompany() {
		return company;
	}
		
	public String getTrainType() {
		return type;
	}

	public String getDestinationCity() {
		return destinationCity;
	}
	
	//Override method from String class to format Train object for specific output.
	@Override
	public String toString() {
		return String.format("%d\t\t%s\t\t%d\t\t%-15s\t%s", engineNumber, company, numberRailCars, type, destinationCity);
	}
	
	//Compares two train objects by number of rail cars they have.
	@Override
	public int compareTo(Train train) {
		if (numberRailCars > train.getNumberRailCars()) {
			return -1;
		}
		else if (numberRailCars < train.getNumberRailCars()) {
			return 1;
		}
		else {
			return 0;
		}
	}
} //End Train Class


//RailCar Class
class RailCar {
	
	//Instance Variables
	private int number;
	private String type;
	private String destination;
	
	//Overloaded constructor to create RailCar object and fill instance variables.
	public RailCar(int number, String type, String destination) {
		this.number = number;
		this.type = type;
		this.destination = destination;
	}
	
	//Getter for each instance variable.
	public int getRailCarNumber() {	
		return number;
		}
		
	public String getRailCarType() {
		return type;
	}
		
	public String getRailCarDestination() {
		return destination;
	}
	
	//Override method from String class to format RailCar object for specific output.
	@Override
	public String toString() {
		return String.format("%d\t%-15s%s", number, type, destination);
	}
	
}//End RailCar Class

//RailroadController Class
class RailroadController {
	
	//Method to find move RailCars to the appropriate Train.
	public void moveRailCarsToTrains (Railroad railroad) {
		
		//While loop is used to loop through until the receivingTrack has no more RailCars in it.
		while(!railroad.isReceivingTrackEmpty()) {
			
			//Displays the RailCar that is being moved. It also find the Track that the RailCar is to be moved to.
			RailCar aRailCar = railroad.removeRailCarFromReceivingTrack();
			System.out.format("Moved to sorting track #%d: rail car %d going to %s (%s)", railroad.findTrain(aRailCar), aRailCar.getRailCarNumber(), aRailCar.getRailCarDestination(), aRailCar.getRailCarType());
			railroad.addRailCartoTrainInSortingYard(aRailCar, railroad.findTrain(aRailCar));
			System.out.println();
		}
		System.out.println();
	}
	
	//Method to move the Trains to the DepartureTrack PriorityQueue.
	public void moveTrainsToDepartureTrack (Railroad railroad) {
		
		//For loop to remove the Trains in order of TrackNumber and move them into the PriorityQueue.
		for (int i = 0; i < railroad.getNumberTracks(); i++) {
			
			//If statement to make sure there is a Train and then move and displays the Train being moved.
			if (!Objects.isNull(railroad.getTrainInSortingYard(i))) {
				System.out.format("Moved to depature track: Train %d going to %s (%s)", railroad.getTrainInSortingYard(i).getEngineNumber(), railroad.getTrainInSortingYard(i).getDestinationCity(), railroad.getTrainInSortingYard(i).getTrainType());
				railroad.addTrainToDepartureTrack(railroad.getTrainInSortingYard(i));
				railroad.removeTrainFromSortingYard(i);
				System.out.println();
			}
		}
		System.out.println();
	}
	
	//Method to move the remove the departing Train from the departureTrack PriorityQueue.
	public void clearedForDeparture (Railroad railroad) {
		Train departingTrain;
		//While loop that removes and displays the departing Trains on the departureTrack.
		while (!railroad.isDepartureTrackEmpty()) {
			departingTrain = railroad.removeTrainFromDepartureTrack();
			System.out.format("Train %d with %d rail cars is departing to %s (%s)" , departingTrain.getEngineNumber(), departingTrain.getNumberRailCars(), departingTrain.getDestinationCity(), departingTrain.getTrainType());
			System.out.println();
		}
		System.out.println();
	}
	
}//End RailroadController Class
