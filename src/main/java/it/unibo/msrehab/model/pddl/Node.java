package it.unibo.msrehab.model.pddl;

public class Node {
	private String name;
	private boolean initialNode;
	private boolean finalNode;
	
	public Node(String n) {
		this.name = n;
		this.initialNode = false;
		this.finalNode = false;
		
		return;
	}
	
	public Node(String n, boolean init, boolean fin) {
		this.name = n;
		this.initialNode = init;
		this.finalNode = fin;
		
		return;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isInitialNode() {
		return this.initialNode;
	}
	
	public boolean isFinalNode() {
		return this.finalNode;
	}
	
	public void setName(String n) {
		this.name = n;
		
		return;
	}
	
	public void setInitial(Boolean init) {
		this.initialNode = init;

		return;
	}
	
	public void setFinal(Boolean fin) {
		this.finalNode = fin;

		return;
	}
}
