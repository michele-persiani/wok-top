package it.unibo.msrehab.model.weekendInRome;

public class TrainTrip {

	String station;
	int outwardDeparture;
	int returnDeparture;
	String pddlString;
	
	public TrainTrip(String station, int outwardDeparture, int returnDeparture) {
		super();
		this.station = station;
		this.outwardDeparture = outwardDeparture;
		this.returnDeparture = returnDeparture;
	}
	
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public int getOutwardDeparture() {
		return outwardDeparture;
	}
	public void setOutwardDeparture(int outwardDeparture) {
		this.outwardDeparture = outwardDeparture;
	}
	public int getReturnDeparture() {
		return returnDeparture;
	}
	public void setReturnDeparture(int returnDeparture) {
		this.returnDeparture = returnDeparture;
	}
	public String getPddlString() {
		return pddlString;
	}
	
	public void generatePddlString() {
		// Example string: (round-trip termini hour8 hour38)"
		pddlString = "(round-trip " + station.toLowerCase() + " hour" + String.valueOf(outwardDeparture) + " hour" + String.valueOf(returnDeparture) + ")";
	}
	
	
	
}
