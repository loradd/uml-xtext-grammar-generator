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

	/**
	 * GRAMMAR NAME
	 **/
	private Text _grammarName;

	/**
	 * GRAMMAR URI
	 **/
	private Text _grammarURI;

	/**
	 * CONSTRUCTOR
	 **/
	public XtextGrammarDetailsPage() {
		/* Superclass Constructor */
		super("Page 1");
		/* Setup Title */
		this.setTitle("Xtext Grammar Details");
		/* Setup Description */
		this.setDescription("Insert grammar name and uri");
	}

	@Override
	public void createControl(Composite parent) {
		/* Widgets Composite */
		Composite composite = new Composite(parent, SWT.NONE);
		/* Page Layout */
		GridLayout pageLayout = new GridLayout(4, true);
		composite.setLayout(pageLayout);
		/* Text Fields Layout */
		GridData textFieldLayout = new GridData(GridData.FILL_HORIZONTAL);
		textFieldLayout.horizontalSpan = 3;
		/* Setup Grammar Name Text Field */
		new Label(composite, SWT.NONE).setText("Grammar Name: ");
		this._grammarName = new Text(composite, SWT.BORDER);
		this._grammarName.setLayoutData(textFieldLayout);
		this._grammarName.setText(
				((GenerateXtextGrammarFromUmlWizard) this.getWizard()).xtextGrammarGeneratorModel().grammarName());
		/* Setup Grammar URI Text Field */
		new Label(composite, SWT.NONE).setText("Grammar URI: ");
		this._grammarURI = new Text(composite, SWT.BORDER);
		this._grammarURI.setLayoutData(textFieldLayout);
		this._grammarURI.setText(
				((GenerateXtextGrammarFromUmlWizard) this.getWizard()).xtextGrammarGeneratorModel().grammarURI());
		/* Setup Page Control */
		this.setControl(composite);
		/* Setup Widget Listeners */
		this.attachListeners();
	}

	@Override
	public void handleEvent(Event event) {
		/* Update Buttons (Page Completion) */
		this.getWizard().getContainer().updateButtons();
	}

	@Override
	public boolean isPageComplete() {
		/* Page Complete - Non-Empty Fields */
		return this.isComplete(_grammarName) && this.isComplete(_grammarURI);
	}

	@Override
	public IWizardPage getNextPage() {
		/* Detach Listeners */
		this.detachListeners();
		/* Update Grammar Name and Grammar URI */
		this.updateXtextGrammarGeneratorModel();
		/* Return Xtext Grammar Content Page */
		return ((GenerateXtextGrammarFromUmlWizard) this.getWizard()).grammarContentPage();
	}

	@Override
	public boolean canFlipToNextPage() {
		/* Page Complete - Non-Empty Fields */
		return this.isComplete(this._grammarName) && this.isComplete(this._grammarURI);
	}

	/**
	 * XTEXT GRAMMAR GENERATOR MODEL (Update)
	 **/
	private void updateXtextGrammarGeneratorModel() {
		if (this.getWizard() instanceof GenerateXtextGrammarFromUmlWizard) {
			/* Update Grammar Name */
			((GenerateXtextGrammarFromUmlWizard) this.getWizard()).xtextGrammarGeneratorModel()
					.grammarName(this._grammarName.getText());
			/* Update Grammar URI */
			((GenerateXtextGrammarFromUmlWizard) this.getWizard()).xtextGrammarGeneratorModel()
					.grammarURI(this._grammarURI.getText());
		}
	}

	/**
	 * LISTENERS - ATTACH
	 **/
	private void attachListeners() {
		/* Grammar Name - Attach KeyUp */
		this._grammarName.addListener(SWT.KeyUp, this);
		/* Grammar URI - Attach KeyUp */
		this._grammarURI.addListener(SWT.KeyUp, this);
	}

	/**
	 * LISTENERS - DETACH
	 **/
	private void detachListeners() {
		/* Grammar Name - Detach KeyUp */
		this._grammarName.removeListener(SWT.KeyUp, this);
		/* Grammar URI - Detach KeyUp */
		this._grammarURI.removeListener(SWT.KeyUp, this);
	}

	/**
	 * COMPLETION CHECK - TEXT FIELD
	 **/
	private boolean isComplete(Text _text) {
		/* Non-empty Text Field */
		return _text.getText() != null && _text.getText().trim().length() > 0;
	}

}
