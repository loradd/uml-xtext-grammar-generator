package se.mdh.idt.uml2.xtext.grammar.generator.wizards

import org.eclipse.core.resources.IFile
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.wizard.Wizard
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Tree
import org.eclipse.ui.INewWizard
import org.eclipse.ui.IWorkbench
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.resource.UMLResource
import se.mdh.idt.uml2.xtext.grammar.generator.transformations.XtextGrammarGeneratorTransformation
import se.mdh.idt.uml2.xtext.grammar.generator.wizards.pages.XtextGrammarGeneratorWizardPage

class XtextGrammarGeneratorWizard extends Wizard implements INewWizard {

	/* Xtext - Grammar - Name */
	var String xtextGrammarName

	/* Xtext - Grammar - Name - Text */
	var Text xtextGrammarNameText

	/* Xtext - Grammar - URI */
	var String xtextGrammarUri

	/* Xtext - Grammar - URI - Text */
	var Text xtextGrammarUriText

	/* Xtext - Grammar - Content (Tree) */
	var Tree xtextGrammarContentTree

	/* UML - Resource */
	var UMLResource umlResource

	/* UML - Resource - Root Element */
	var Package umlRootElement

	/* Xtext Grammar Generator - Wizard Page */
	val xtextGrammarGeneratorWizardPage = new XtextGrammarGeneratorWizardPage
	
	new(IWorkbench workbench, IStructuredSelection selection) {
		init(workbench, selection)
	}

	/* Xtext - Grammar - Name - Get */
	def String getXtextGrammarName() {
		xtextGrammarName
	}

	/* Xtext - Grammar - Name - Text - Get */
	def Text getXtextGrammarNameText() {
		xtextGrammarNameText
	}

	/* Xtext - Grammar Name - Text - Set */
	def void setXtextGrammarNameText(Text xtextGrammarNameText) {
		this.xtextGrammarNameText = xtextGrammarNameText
	}

	/* Xtext - Grammar - URI - Get */
	def String getXtextGrammarUri() {
		xtextGrammarUri
	}

	/* Xtext - Grammar - URI - Text - Get */
	def Text getXtextGrammarUriText() {
		xtextGrammarUriText
	}

	/* Xtext - Grammar - URI - Text - Set */
	def void setXtextGrammarUriText(Text xtextGrammarUriText) {
		this.xtextGrammarUriText = xtextGrammarUriText
	}

	/* Xtext - Grammar - Content (Tree) - Get */
	def Tree getXtextGrammarContentTree() {
		xtextGrammarContentTree
	}

	/* Xtext - Grammar - Content (Tree) - Set */
	def void setXtextGrammarContentTree(Tree xtextGrammarContentTree) {
		this.xtextGrammarContentTree = xtextGrammarContentTree
	}

	/* UML - Resource - Get */
	def UMLResource getUmlResource() {
		umlResource
	}

	/* UML - Resource - Root Element */
	def Package getUmlRootElement() {
		umlRootElement
	}

	/* Final Operations - Xtext Grammar Generation */
	override performFinish() {
		if (canFinish) {
			new XtextGrammarGeneratorTransformation
				(xtextGrammarNameText.text, xtextGrammarUriText.text, 
					xtextGrammarContentTree).run
		}
		canFinish
	}

	/* Final Operations - Wizard Page Content Check */
	override canFinish() {
		xtextGrammarGeneratorWizardPage.pageComplete
	}

	/* Initialize Wizard  */
	override init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection instanceof ITreeSelection && selection.size == 1 && selection.firstElement instanceof IFile &&
			(selection.firstElement as IFile).fileExtension.equals("uml")) {
			val umlResourceSet = new ResourceSetImpl
			umlResourceSet.resourceFactoryRegistry.extensionToFactoryMap.put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE)
			val umlResourceUri = URI.createPlatformResourceURI((selection.firstElement as IFile).fullPath.toString,
				true)
			umlResource = umlResourceSet.getResource(umlResourceUri, true) as UMLResource
			umlRootElement = (umlResource.contents.get(0)) as Package
			xtextGrammarName = umlRootElement.name
			xtextGrammarUri = umlResourceUri.toString.replace("." + umlResourceUri.fileExtension, "Text") + "." +
				umlResourceUri.fileExtension
		}
	}

	/* Initialize Wizard - Wizard Pages */
	override addPages() {
		addPage(xtextGrammarGeneratorWizardPage)
	}

}
