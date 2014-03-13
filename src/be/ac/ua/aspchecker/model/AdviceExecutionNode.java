package be.ac.ua.aspchecker.model;

import org.eclipse.jdt.core.IJavaElement;

public class AdviceExecutionNode implements Comparable<AdviceExecutionNode> {	

	private enum ContractKind {BEFORE, AFTER, AROUND_PRE, AROUND_POST};

	private AdviceExecutionNode() {
	}

	private AdviceExecutionNode lower;

	public AdviceExecutionNode(IJavaElement ije){
		//TODO get information from element, including contracts annotations
		System.out.println(ije.getElementName());
	}

	public AdviceExecutionNode getLower() {
		return lower;
	}

	public boolean equals(AdviceExecutionNode o){
		return this.compareTo(o)==0;
	}

	@Override
	public int compareTo(AdviceExecutionNode o) {
		int ret = 0;
		if(o==null){
			throw new NullPointerException();
		}
		//TODO compare contracts
		return ret;	
	}

}
