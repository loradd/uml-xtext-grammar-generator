package org.eclipse.uml2.uml.xtext.generator.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class XtextGrammarContentPage extends WizardPage {
	
	public XtextGrammarContentPage() {
		super("Page 2"); 
		this.setTitle("Xtext Grammar Content");
		this.setDescription("Select grammar elements");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE); 
		this.setControl(composite); 
		this.setPageComplete(true);
	}
	
}
