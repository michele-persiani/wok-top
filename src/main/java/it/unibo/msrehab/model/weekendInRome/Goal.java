package it.unibo.msrehab.model.weekendInRome;

// This class represents a PDDL Goal for the Weekend in Rome exercise
// Creator: Margherita Donnici

public class Goal {
	
	int id;
	String pddlString;
	String userFriendlyString;
	
	public Goal(int id) {
		super();
		this.id = id;
		setPddlString();
		setUserFriendlyString();
	}

	public int getId() {
		return id;
	}

	public String getPddlString() {
		return pddlString;
	}
	
	private void setPddlString() {
		switch(this.id) {
			case 1:
				this.pddlString = "(slept)";
				break;
			case 2:
				this.pddlString = "(at-bologna)";
				break;
			case 3:
				this.pddlString = "(visited colosseo)";
				break;
			case 4:
				this.pddlString = "(visited trevi-fountain)";
				break;
			case 5:
				this.pddlString = "(visited san-pietro)";
				break;
			case 6:
				this.pddlString = "(visited ara-pacis)";
				break;
			case 7:
				this.pddlString = "(visited pantheon)";
				break;
			case 8:
				this.pddlString = "(visited trastevere)";
				break;
			case 9:
				this.pddlString = "(done-activity san-pietro)";
				break;
			case 10:
				this.pddlString = "(done-activity colosseo)";
				break;
			case 11:
				this.pddlString = "(done-activity trastevere)";
				break;
			case 12:
				this.pddlString = "(done-activity ara-pacis)";
				break;
			case 13:
				this.pddlString = "(done-activity pantheon)";
				break;
			case 14:
				this.pddlString = "(done-activity-timed trastevere hour12)";
				break;
			case 15:
				this.pddlString = "(done-activity-timed trastevere hour13)";
				break;
			case 16:
				this.pddlString = "(done-activity-timed trastevere hour36)";
				break;
			case 17:
				this.pddlString = "(done-activity-timed trastevere hour37)";
				break;
			case 18:
				this.pddlString = "(done-activity-timed trastevere hour20)";
				break;
			case 19:
				this.pddlString = "(done-activity-timed trastevere hour21)";
				break;
			case 20:
				this.pddlString = "(done-activity-timed trastevere hour18)";
				break;
			case 21:
				this.pddlString = "(done-activity-timed trastevere hour19)";
				break;
			case 22:
				this.pddlString = "(done-activity-timed trastevere hour34)";
				break;
			case 23:
				this.pddlString = "(done-activity-timed trastevere hour35)";
				break;
			case 24:
				this.pddlString = "(done-activity-timed stadio-olimpico hour15)";
				break;
			case 25:
				this.pddlString = "(done-activity-timed stadio-olimpico hour12)";
				break;
			case 26:
				this.pddlString = "(done-activity-timed stadio-olimpico hour39)";
				break;
			case 27:
				this.pddlString = "(done-activity-timed stadio-olimpico hour38)";
				break;
			case 28:
				this.pddlString = "(done-activity-timed auditorium hour20)";
				break;
			case 29:
				this.pddlString = "(done-activity-timed auditorium hour18)";
				break;
			case 30:
				this.pddlString = "(done-activity-timed auditorium hour42)";
				break;
			case 31:
				this.pddlString = "(done-activity-timed auditorium hour39)";
				break;
			case 32:
				this.pddlString = "(gone-running)";
				break;
			case 33:
				this.pddlString = "(had-breakfast)";
				break;
		}
	}
	public String getUserFriendlyString() {
		return userFriendlyString;
	}
	private void setUserFriendlyString() {
		String ufString = "";
  
		switch(this.id) {
			case 1:
				ufString = "";
				break;
			case 2:
				ufString = "";
				break;
			case 3:
				ufString = "Passa dal Colosseo";
				break;
			case 4:
				ufString = "Passa dalla Fontana di Trevi";
				break;
			case 5:
				ufString = "Passa da San Pietro";
				break;
			case 6:
				ufString = "Passa dall'Ara Pacis";
				break;
			case 7:
				ufString = "Passa dal Pantheon";
				break;
			case 8:
				ufString = "Passa da Trastevere";
				break;
			case 9:
				ufString = "Fai la visita guidata a San Pietro";
				break;
			case 10:
				ufString = "Fai la visita guidata al Colosseo";
				break;
			case 11:
				ufString = "Mangia a Trastevere";
				break;
			case 12:
				ufString = "Visita la mostra all'Ara Pacis";
				break;
			case 13:
				ufString = "Fai la visita guidata al Pantheon";
				break;
			case 14:
                               
				ufString = "Incontra i tuoi amici a trastevere sabato alle 12 ";
				break;
			case 15:
                               
				ufString = "Incontra i tuoi amici a trastevere sabato alle 13 ";
				break;
			case 16:
                                
				ufString = "Incontra i tuoi amici a trastevere domenica alle 12 ";
				break;
			case 17:
				ufString = "Incontra i tuoi amici a trastevere domenica alle 13 ";
				break;
			case 18:
				ufString = "Incontra i tuoi amici a trastevere sabato alle 20 ";
				break;
			case 19:
				ufString = "Incontra i tuoi amici a trastevere sabato alle 21 ";
				break;
			case 20:
				ufString = "Incontra i tuoi amici a trastevere sabato alle 18 ";
				break;
			case 21:
				ufString = "Incontra i tuoi amici a trastevere sabato alle 19 ";
				break;
			case 22:
				ufString = "Incontra i tuoi amici a trastevere domenica alle 10 ";
				break;
			case 23:
				ufString = "Incontra i tuoi amici a trastevere domenica alle 11 ";
				break;
			case 24:
				ufString = "Vai a vedere la partita della Roma allo Stadio Olimpico sabato alle 15 ";
				break;
			case 25:
				ufString = "Vai a vedere la partita della Roma allo Stadio Olimpico sabato alle 12 ";
				break;
			case 26:
				ufString = "Vai a vedere la partita della Roma allo Stadio Olimpico domenica alle 15 ";
				break;
			case 27:
				ufString = "Vai a vedere la partita della Roma allo Stadio Olimpico domenica alle 14 ";
				break;
			case 28:
				ufString = "Vai a vedere il concerto all'Auditorium sabato alle 20 ";
				break;
			case 29:
				ufString = "Vai a vedere il concerto all'Auditorium sabato alle 18 ";
				break;
			case 30:
				ufString = "Vai a vedere il concerto all'Auditorium domenica alle 18 ";
				break;
			case 31:
				ufString = "Vai a vedere il concerto all'Auditorium domenica alle 15 ";
				break;
			case 32:
				ufString = "Fai ginnastica domenica mattina prima di colazione";
				break;
			case 33:
				ufString = "Fai colazione in albergo domenica mattina";
				break;
		}
		this.userFriendlyString = ufString;
	}
	
}
