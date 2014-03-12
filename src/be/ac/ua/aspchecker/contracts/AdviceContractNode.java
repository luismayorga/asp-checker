package be.ac.ua.aspchecker.contracts;

import org.aspectj.org.eclipse.jdt.internal.core.util.Util.Comparable;

public class AdviceContractNode implements Comparable{	
	private enum ContractKind {BEFORE, AFTER, AROUND};
	
	public AdviceContractNode(ContractKind kind, Contract contract) {
		super();
		Kind = kind;
		this.contract = contract;
	}
	
	private ContractKind Kind;
	private Contract contract;
	private AdviceContractNode next;
	
	public Contract getContract() {
		return contract;
	}
	
	public ContractKind getKind(){
		return Kind;
	}

	public AdviceContractNode getNext() {
		return next;
	}
	
	public void setNext(AdviceContractNode next) {
		this.next = next;
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
