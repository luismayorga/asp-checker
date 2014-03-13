package be.ac.ua.aspchecker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.asm.IRelationship;
import org.eclipse.ajdt.core.model.AJProjectModelFacade;
import org.eclipse.ajdt.core.model.AJRelationshipManager;
import org.eclipse.ajdt.core.model.AJRelationshipType;
import org.eclipse.jdt.core.IJavaElement;

public class AJRelationshipsModel {

	private Map<IJavaElement, List<IJavaElement>> relationships;

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
				relationships.put(JavaElement, new ArrayList<IJavaElement>());
			}
			addTargets(rel, model);
		}
	}

	private void addTargets(IRelationship rel, AJProjectModelFacade model){
		List<IJavaElement> existingRels = relationships.get(rel.getSourceHandle());
		for( String target : rel.getTargets()) {
			existingRels.add(model.programElementToJavaElement(target));
		}
	}
	
	public AdviceExecutionModel toExecutionModel(){
		return new AdviceExecutionModel(this);
	}

}
