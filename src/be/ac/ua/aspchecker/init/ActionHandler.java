package be.ac.ua.aspchecker.init;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import be.ac.ua.aspchecker.processing.CheckingJob;

public class ActionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//Extract project
		IStructuredSelection selection = (IStructuredSelection)HandlerUtil
				.getCurrentSelection(event);
		IJavaProject jp = (IJavaProject) selection.getFirstElement();

		//Run checking
		CheckingJob job = new CheckingJob("ASP", jp.getProject());
		job.setUser(true);
		job.schedule();
		return null;
	}


}
