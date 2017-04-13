package org.eclipse.uml2.uml.xtext.generator.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.xtext.generator.wizards.model.XtextGrammarGeneratorModel;
import org.eclipse.uml2.uml.xtext.generator.wizards.pages.XtextGrammarContentPage;
import org.eclipse.uml2.uml.xtext.generator.wizards.pages.XtextGrammarDetailsPage;

public class GenerateXtextGrammarFromUmlWizard extends Wizard implements INewWizard {

	/**
	 * WORKBENCH
	 **/
	private IWorkbench _workbench;
	
	/**
	 * SELECTION 
	 **/
	private IStructuredSelection _selection;
	
	/**
	 * XTEXT GRAMMAR GENERATOR MODEL
	 **/
	private XtextGrammarGeneratorModel _xtextGrammarGeneratorModel;
	
	/**
	 * PAGE 1 - XTEXT GRAMMAR DETAILS 
	 **/
	private XtextGrammarDetailsPage _grammarDetailsPage = new XtextGrammarDetailsPage();
	
	/**
	 * PAGE 2 - XTEXT CONTENT PAGE 
	 **/
	private XtextGrammarContentPage _grammarContentPage = new XtextGrammarContentPage();

	/**
	 * CONSTRUCTOR (IWorkbench, IStructuredSelection) 
	 **/
	public GenerateXtextGrammarFromUmlWizard(IWorkbench workbench, IStructuredSelection selection) {
		/* Superclass Constructor */
		super();
		/* Initialize Wizard */
		this.init(workbench, selection);
	}
	
	/**
	 * PAGE 1 - XTEXT GRAMMAR DETAILS (Get) 
	 **/
	public XtextGrammarDetailsPage grammarDetailsPage() {
		/* Return Grammar Details Page */
		return this._grammarDetailsPage;
	}
	
	/**
	 * PAGE 2 - XTEXT GRAMMAR CONTENT (Get) 
	 **/
	public XtextGrammarContentPage grammarContentPage() {
		/* Return Grammar Content Page */
		return this._grammarContentPage;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		/* Setup Workbench */
		this._workbench = workbench;
		/* Setup Selection */
		this._selection = selection;
		/* Initialize Xtext Grammar Generator Model */
		if (selection instanceof ITreeSelection && selection.size() == 1 && selection.getFirstElement() instanceof IFile
				&& ((IFile) selection.getFirstElement()).getFileExtension().equals("uml")) {
			/* ResourceSet */
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					UMLResource.Factory.INSTANCE);
			/* Retrieve Resource URI */
			URI resourceURI = URI.createURI(((IFile) selection.getFirstElement()).getFullPath().toString());
			/* Retrieve UMLResource */
			UMLResource resource = (UMLResource) resourceSet.getResource(resourceURI, true);
			/* Build Xtext Grammar Generator Model */
			this._xtextGrammarGeneratorModel = new XtextGrammarGeneratorModel(resource);
		}
	}
	
	@Override
	public void addPages() {
		/* Add Grammar Details Page */
		this.addPage(this._grammarDetailsPage);
		/* Add Grammar Content Page */
		this.addPage(this._grammarContentPage);
	}
	
	@Override
	public boolean performFinish() {
		// se l'ultima pagina è completa, avviamo il processo di generazione della grammatica xtext
		return this._grammarDetailsPage.isPageComplete() && this._grammarContentPage.isPageComplete();
	}
	
	/**
	 * XTEXT GRAMMAR GENERATOR MODEL (Get) 
	 **/
	public XtextGrammarGeneratorModel xtextGrammarGeneratorModel() {
		/* Return Xtext Grammar Generator Model */
		return this._xtextGrammarGeneratorModel;
	}

}
