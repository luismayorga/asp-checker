package be.ac.ua.aspchecker.model;

import org.eclipse.jdt.core.IJavaElement;

public class ExecutableNode implements Comparable<ExecutableNode> {	

	private enum ContractKind {BEFORE, AFTER, AROUND_PRE, AROUND_POST};
	private Contract contract;

	private ExecutableNode() {
	}

	private ExecutableNode lower;

	public ExecutableNode(IJavaElement ije){
		//TODO get information from element, including contracts annotations
	}

	public Contract getContract() {
		return contract;
	}

	public ExecutableNode getLower() {
		return lower;
	}

	//TODO should be changed, though it would not respect x.equals(y) == x.compareTo(y)=0
	public boolean equals(ExecutableNode o){
		return this.compareTo(o)==0;
	}

	@Override
	public int compareTo(ExecutableNode o) {
		int ret = 0;
		if(o==null){
			throw new NullPointerException();
		}
		//TODO compare contracts
		return ret;	
	}

}
