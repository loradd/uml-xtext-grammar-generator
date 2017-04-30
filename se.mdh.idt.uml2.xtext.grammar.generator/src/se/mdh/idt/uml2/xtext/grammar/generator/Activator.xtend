package se.mdh.idt.uml2.xtext.grammar.generator

import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

class Activator extends AbstractUIPlugin {
	
	/* Plugin - ID */
	public static val PLUGIN_ID = "se.mdh.idt.uml2.xtext.grammar.generator" // $NON-NLS-1$
	
	/* Plugin - Shared Instance */
	static var Activator plugin
	
	/* Constructor */
	new () { /* Do Nothing */ }
	
	/* Plugin - Shared Instance - Get */
	def static getDefault() {
		plugin	
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	 override start (BundleContext context) throws Exception {
	 	super.start(context)
	 	plugin = this
	 }
	 
	 /*
	 * (non-Javadoc)
	 * @see AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	 override stop (BundleContext context) throws Exception {
	 	plugin = null
	 	super.stop(context)
	 }
	
}