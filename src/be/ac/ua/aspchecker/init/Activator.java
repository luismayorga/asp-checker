package be.ac.ua.aspchecker.init;

import org.eclipse.ajdt.core.builder.AJBuilder;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import ccw.util.osgi.ClojureOSGi;

public class Activator extends  AbstractUIPlugin implements IStartup {

	public static final String PLUGIN_ID = "asp-checker";
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		startClojureCode(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	@Override
	public void earlyStartup() {
		AJBuilder.addAJBuildListener(new BuildListener());
	}
	
	private void startClojureCode(BundleContext bundleContext) throws Exception {
		Bundle b = bundleContext.getBundle();
		String[] namespaces= { "damp.ekeko", "be.ac.ua.aspchecker.core" };	
		for(String namespace : namespaces) {
			try {
				ClojureOSGi.require(b, namespace);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
