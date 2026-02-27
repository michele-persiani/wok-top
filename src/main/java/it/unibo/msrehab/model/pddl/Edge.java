package it.unibo.msrehab.model.pddl;

public class Edge
{
	private Node head;
	private Node tail;
	private String type;
	private String initialPredicate;
	
	public Edge(Node head, Node tail, String type, String init) {
		this.head = head;
		this.tail = tail;
		this.type = type;
		this.initialPredicate = init;
		
		return;
	}
	
	public String getInitialStatePDDL() {
		String syntax = "";
		
		// Type of the non oriented edge
		syntax += "(" + this.type + " " + head.getName() + " " + tail.getName() + ")";
		syntax += " (" + this.type + " " + tail.getName() + " " + head.getName() + ")";
		
		// Initial state predicate
		syntax += " (" + this.initialPredicate + " " + head.getName() + " " + tail.getName() + ")";
		syntax += " (" + this.initialPredicate + " " + tail.getName() + " " + head.getName() + ")";
		
		return syntax;
	}
}
