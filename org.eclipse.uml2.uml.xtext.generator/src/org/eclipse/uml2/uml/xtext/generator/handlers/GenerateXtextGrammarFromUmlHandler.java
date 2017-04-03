package org.eclipse.uml2.uml.xtext.generator.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.uml2.uml.xtext.generator.wizards.GenerateXtextGrammarFromUmlWizard;

public class GenerateXtextGrammarFromUmlHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActiveMenuSelectionChecked(event) instanceof IStructuredSelection) {
			// retrieve active workbench window
			IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			// retrieve corresponding workbench
			IWorkbench workbench = workbenchWindow.getWorkbench(); 
			// retrieve corresponding shell
			Shell shell = workbenchWindow.getShell();
			// retrieve current selection
			IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelectionChecked(event);
			// create grammar generation wizard
			Wizard wizard = new GenerateXtextGrammarFromUmlWizard(workbench, selection);
			// launch grammar generation wizard
			new WizardDialog(shell, wizard).open();
		}
		return null;
	}

}
