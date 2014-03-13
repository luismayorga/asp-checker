package be.ac.ua.aspchecker.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.eclipse.ajdt.core.model.AJProjectModelFacade;
import org.eclipse.jdt.core.IJavaElement;

public class AdviceExecutionModel {

	private HashSet<PriorityQueue<AdviceExecutionNode>> executionSequences;
	private AdviceExecutionModel(){}

	public AdviceExecutionModel(AJProjectModelFacade model){
		
		AJRelationshipsModel relModel = AJRelationshipsModel.getInstanceForModel(model);
		executionSequences = new HashSet<>(relModel.getRelationships().size());
		
		nodify(relModel);
	}

	private void nodify(AJRelationshipsModel relModel){
		PriorityQueue<AdviceExecutionNode> sequence;
		AdviceExecutionNode acn;
		
		for ( Entry<IJavaElement, List<IJavaElement>> iter : relModel.getRelationships()) {
			
			sequence = new PriorityQueue<>(iter.getValue().size()+1);
			executionSequences.add(sequence);
			
			acn = new AdviceExecutionNode(iter.getKey());
			sequence.add(acn);
			
			for (IJavaElement advice : iter.getValue()) {
				acn = new AdviceExecutionNode(advice);
				sequence.add(acn);
			}
		}
	}

	public void evaluate() {
		// TODO Comparar todos los contratos
	}

}
