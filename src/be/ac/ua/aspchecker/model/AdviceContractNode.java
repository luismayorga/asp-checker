package be.ac.ua.aspchecker.model;

import org.aspectj.org.eclipse.jdt.internal.core.util.Util.Comparable;
import org.eclipse.jdt.core.IJavaElement;

public class AdviceContractNode implements Comparable {	

	private enum ContractKind {BEFORE, AFTER, AROUND_PRE, AROUND_POST};

	private AdviceContractNode() {
	}

	private AdviceContractNode lower;

	public AdviceContractNode(IJavaElement ije){
		//TODO replace stub
		System.out.println(ije.getElementName());
	}

	public AdviceContractNode getLower() {
		return lower;
	}

	public int compareTo(Comparable arg0) {
		int ret = 0;
		if(arg0==null){
			throw new NullPointerException();
		}else if(arg0 instanceof AdviceContractNode){
			throw new ClassCastException();
		}
		//TODO compare contracts
		return ret;
	}

	public boolean equals(AdviceContractNode o){
		return this.compareTo(o)==0;
	}

}
