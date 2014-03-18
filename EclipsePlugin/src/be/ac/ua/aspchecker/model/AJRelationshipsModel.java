package be.ac.ua.aspchecker.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.aspectj.asm.IRelationship;
import org.eclipse.ajdt.core.model.AJProjectModelFacade;
import org.eclipse.ajdt.core.model.AJRelationshipManager;
import org.eclipse.ajdt.core.model.AJRelationshipType;
import org.eclipse.jdt.core.IJavaElement;

public class AJRelationshipsModel {

	private Map<ExecutableNode, Set<ExecutableNode>> relationships;

	private AJRelationshipsModel(){}
	
	private AJRelationshipsModel(AJProjectModelFacade model) {
		super();
		relationships = new HashMap<>();
		buildModel(model);
	}

	public static AJRelationshipsModel getInstanceForModel(AJProjectModelFacade model){
		if(model.hasModel()){
			return new AJRelationshipsModel(model);
		}else{
			return null;
		}
	}

	private void buildModel(AJProjectModelFacade model){
		//  join point shadow       advised by      advice
		//  (source handler)						(targets)
		AJRelationshipType[] types = {AJRelationshipManager.ADVISED_BY};
		List<IRelationship> list = model.getRelationshipsForProject(types);

		//Iterate through relationships
		for (IRelationship rel : list) {
			IJavaElement JavaElement = model.programElementToJavaElement(rel.getSourceHandle());
			
			//And store them
			if(!relationships.containsKey(JavaElement)){
				relationships.put(new ExecutableNode(JavaElement), new HashSet<ExecutableNode>());
			}
			addTargets(rel, model);
		}
	}

	private void addTargets(IRelationship rel, AJProjectModelFacade model){
		Set<ExecutableNode> existingRels = relationships.get(model.programElementToJavaElement(rel.getSourceHandle()));
		for( String target : rel.getTargets()) {
			existingRels.add(new ExecutableNode(model.programElementToJavaElement(target)));
		}
	}
	
	public Set<Entry<ExecutableNode, Set<ExecutableNode>>> getRelationships(){
		return relationships.entrySet();
	}

	public AdviceExecutionModel getAdviceExecutionModel(){
		return new AdviceExecutionModel(this);
	}
	
	public void evaluateASP(){
		for ( Entry<ExecutableNode, Set<ExecutableNode>> rels : relationships.entrySet()) {
			ExecutableNode method = rels.getKey();
			for ( ExecutableNode advice : rels.getValue()) {
				//TODO STUB; completely implement
				method.getContract().compareTo(advice.getContract());
			}
		}
	}
	
}
