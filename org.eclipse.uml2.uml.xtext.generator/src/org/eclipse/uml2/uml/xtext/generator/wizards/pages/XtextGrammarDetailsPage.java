package org.eclipse.uml2.uml.xtext.generator.wizards.pages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.xtext.generator.wizards.GenerateXtextGrammarFromUmlWizard;

public class XtextGrammarDetailsPage extends WizardPage implements Listener {

	private Text grammarName;
	private Text grammarURI;

	public XtextGrammarDetailsPage() {
		super("Page 1");
		this.setTitle("Xtext Grammar Details");
		this.setDescription("Insert grammar name and uri");
	}

	@Override
	public void createControl(Composite parent) {
		// composite containing widgets
		Composite composite = new Composite(parent, SWT.NONE);
		// page layout
		GridLayout pageLayout = new GridLayout(4, true);
		composite.setLayout(pageLayout);
		// text fields layout
		GridData textFieldLayout = new GridData(GridData.FILL_HORIZONTAL);
		textFieldLayout.horizontalSpan = 3;
		// setup grammar name text field
		new Label(composite, SWT.NONE).setText("Grammar Name: ");
		this.grammarName = new Text(composite, SWT.BORDER);
		this.grammarName.setLayoutData(textFieldLayout);
		this.grammarName.setText(
				((GenerateXtextGrammarFromUmlWizard) this.getWizard()).getXtextGrammarGeneratorModel().grammarName());
		// setup grammar uri text field
		new Label(composite, SWT.NONE).setText("Grammar URI: ");
		this.grammarURI = new Text(composite, SWT.BORDER);
		this.grammarURI.setLayoutData(textFieldLayout);
		this.grammarURI.setText(
				((GenerateXtextGrammarFromUmlWizard) this.getWizard()).getXtextGrammarGeneratorModel().grammarURI());
		// setup page control
		this.setControl(composite);
		// setup widget listeners
		this.attachListeners();
	}

	@Override
	public void handleEvent(Event event) {
		// update buttons to reflect possible page completion
		this.getWizard().getContainer().updateButtons();
	}

	// update xtext grammar generator model
	private void updateXtextGrammarGeneratorModel() {
		if (this.getWizard() instanceof GenerateXtextGrammarFromUmlWizard) {
			// update grammar name
			((GenerateXtextGrammarFromUmlWizard) this.getWizard()).getXtextGrammarGeneratorModel()
					.grammarName(this.grammarName.getText());
			// update grammar uri
			((GenerateXtextGrammarFromUmlWizard) this.getWizard()).getXtextGrammarGeneratorModel()
					.grammarURI(this.grammarURI.getText());
		}
	}

	// attach listeners
	private void attachListeners() {
		// attach listener to grammar name text field
		this.grammarName.addListener(SWT.KeyUp, this);
		// attach listener to grammar uri text field
		this.grammarURI.addListener(SWT.KeyUp, this);
	}

	// detach listeners
	private void detachListeners() {
		// remove listener from grammar name text field
		this.grammarName.removeListener(SWT.KeyUp, this);
		// remove listener from grammar URI text field
		this.grammarURI.removeListener(SWT.KeyUp, this);
	}

	@Override
	public boolean isPageComplete() {
		// grammar name and uri are not empty
		return this.isComplete(grammarName) && this.isComplete(grammarURI);
	}

	// check if the current page is complete
	private boolean isComplete(Text text) {
		// text field has been filled with a non-empty string
		return text.getText() != null && text.getText().trim().length() > 0;
	}

	@Override
	public void dispose() {
		super.dispose();
		// detach listeners
		this.detachListeners();
		// update grammar name and uri
		this.updateXtextGrammarGeneratorModel();
		// [DEBUG]
		System.err.println("XtextGrammarDetails - Page disposed");
		System.err.println("Grammar Name : "
				+ ((GenerateXtextGrammarFromUmlWizard) this.getWizard()).getXtextGrammarGeneratorModel().grammarName());
		System.err.println("Grammar URI : "
				+ ((GenerateXtextGrammarFromUmlWizard) this.getWizard()).getXtextGrammarGeneratorModel().grammarURI());
	}

	@Override
	public IWizardPage getNextPage() {
		return ((GenerateXtextGrammarFromUmlWizard) this.getWizard()).grammarContentPage();
	}

}
