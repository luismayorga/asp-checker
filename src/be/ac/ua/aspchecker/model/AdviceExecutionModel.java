package be.ac.ua.aspchecker.model;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

public class AdviceExecutionModel {

	private HashSet<PriorityQueue<ExecutableNode>> executionSequences;
	private AdviceExecutionModel(){}

	public AdviceExecutionModel(AJRelationshipsModel model){
		executionSequences = new HashSet<>(model.getRelationships().size());
		transformFromRelationshipModel(model);
	}

	private void transformFromRelationshipModel(AJRelationshipsModel relModel){
		
		PriorityQueue<ExecutableNode> sequence;
		ExecutableNode acn;
		
		for (Entry<ExecutableNode, Set<ExecutableNode>> iter : relModel.getRelationships()) {
			
			sequence = new PriorityQueue<>(iter.getValue().size()+1);
			executionSequences.add(sequence);
			
			sequence.add(iter.getKey());
			
			for ( ExecutableNode en : iter.getValue()) {
				sequence.add(en);
			}
		}
	}

	public void evaluateExecutionRules() {
		// TODO Comparar orden de ejecucion
	}

}
