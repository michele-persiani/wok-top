package it.unibo.msrehab.util;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<ExerciseCategory> exerciseCategory;
	private String groupName;
	
	public CategoryGroup(String groupName) {
		super();
		this.exerciseCategory = new ArrayList<ExerciseCategory>();
		this.groupName = groupName;
	}

	public ArrayList<ExerciseCategory> getExerciseCategory() {
		return exerciseCategory;
	}

	public void setExerciseCategory(ArrayList<ExerciseCategory> exerciseCategory) {
		this.exerciseCategory = exerciseCategory;
	}

	public String getGroupName() {
		switch (groupName) {
			case "ATTENTION_FIG": return "Attenzione: figure";
			case "ATTENTION_FAC": return "Attenzione: volti";
			case "ATTENTION_ORI": return "Attenzione: orientamento";
			case "ATTENTION_RFLXS": return "Attenzione: riflessi";
			case "MEMORY_FIG": return "Memoria: figure";
			case "MEMORY_FAC": return "Memoria: volti";
			case "MEMORY_ORI": return "Memoria: orientamento";
			case "EX_FUNC": return "Funzioni esecutive";
			default: return "Senza nome";
		}
	}
	

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public void addExerciseCategory(ExerciseCategory exerciseCategory) {
		this.exerciseCategory.add(exerciseCategory);
	}

}
