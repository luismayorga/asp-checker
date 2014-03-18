package be.ac.ua.aspchecker.init;

import java.util.List;
import java.util.Map;

import org.eclipse.ajdt.core.builder.IAJBuildListener;
import org.eclipse.ajdt.core.lazystart.IAdviceChangedListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.compiler.CategorizedProblem;

public class BuildListener implements IAJBuildListener {

	@Override
	public void addAdviceListener(IAdviceChangedListener arg0) {}

	@Override
	public void postAJBuild(int arg0, IProject arg1, boolean arg2,
			Map<IFile, List<CategorizedProblem>> arg3) {
		CheckingJob job = new CheckingJob("ASP", arg1);
		job.setUser(true);
		job.schedule();
	}

	@Override
	public void postAJClean(IProject arg0) {}

	@Override
	public void preAJBuild(int arg0, IProject arg1, IProject[] arg2) {}

	@Override
	public void removeAdviceListener(IAdviceChangedListener arg0) {}

}
