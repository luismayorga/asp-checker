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
		IStatus ret;
		if(monitor.isCanceled()){
			ret = Status.CANCEL_STATUS;
		}else{
			AJProjectModelFacade model = AJProjectModelFactory.getInstance().getModelForProject(project);
			if(model.hasModel()){
				AdviceExecutionModel aem = new AdviceExecutionModel(model);
				aem.evaluate();
				//TODO
				ret = Status.OK_STATUS;
			}else{
				ret = new Status(Status.WARNING, getName(), "Model not present, rebuild to analyze");
			}
		}
		return ret;
	}

}
