package se.mdh.idt.uml2.xtext.grammar.generator.handlers

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.wizard.WizardDialog
import org.eclipse.ui.handlers.HandlerUtil
import se.mdh.idt.uml2.xtext.grammar.generator.wizards.XtextGrammarGeneratorWizard

class XtextGrammarGeneratorHandler extends AbstractHandler {
	
	/* Launch Xtext Grammar Generator Wizard */
	override execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActiveMenuSelectionChecked(event) instanceof IStructuredSelection) {
			/* Active Workbench Window */
			val workbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event)
			/* Active Workbench */
			val workbench = workbenchWindow.workbench
			/* Active Workbench Shell */
			val shell = workbenchWindow.shell
			/* Current Selection */
			val selection = HandlerUtil.getActiveMenuSelectionChecked(event) as IStructuredSelection
			/* Create Xtext Grammar Generator Wizard */
			val wizard = new XtextGrammarGeneratorWizard(workbench, selection)
			/* Launch Xtext Grammar Generator Wizard */
			new WizardDialog(shell, wizard).open
		}
		null
		
	}
	
}