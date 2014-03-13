package be.ac.ua.aspchecker.init;

import org.eclipse.ajdt.core.model.AJProjectModelFacade;
import org.eclipse.ajdt.core.model.AJProjectModelFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import be.ac.ua.aspchecker.model.AdviceExecutionModel;

public class CheckingJob extends Job {

	private IProject project;

	public CheckingJob(String name, IProject project) {
		super(name);
		this.project = project;
		super.setPriority(Job.BUILD);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		if(monitor.isCanceled()){
			return Status.CANCEL_STATUS;
		}
		
		AJProjectModelFacade model = AJProjectModelFactory.getInstance().getModelForProject(project);
		if(model.hasModel()){
			AdviceExecutionModel aem = new AdviceExecutionModel(model);

			if(monitor.isCanceled()){
				return Status.CANCEL_STATUS;
			}

			aem.evaluate();
			return Status.OK_STATUS;
		}else{
			return new Status(Status.WARNING, getName(), "Model not present, rebuild to analyze");
		}
	}

}
