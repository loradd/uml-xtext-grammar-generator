package org.eclipse.uml2.uml.xtext.generator.wizards;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

public class GenerateXtextGrammarFromUmlWizard extends Wizard implements INewWizard {

	// current workbench
	private IWorkbench workbench;
	// current selection
	private IStructuredSelection selection;
	// grammar generator model
	private XtextGrammarGeneratorModel xtextGrammarGeneratorModel;
	// pages - grammar details
	private XtextGrammarDetailsPage _grammarDetailsPage = new XtextGrammarDetailsPage();
	// pages - grammar content
	private XtextGrammarContentPage _grammarContentPage = new XtextGrammarContentPage();

	// Default Constructor
	public GenerateXtextGrammarFromUmlWizard() {}

	// Constructor
	public GenerateXtextGrammarFromUmlWizard(IWorkbench workbench, IStructuredSelection selection) {
		this.init(workbench, selection);
	}
	
	// retrieve grammar details page
	public XtextGrammarDetailsPage grammarDetailsPage() {
		return this._grammarDetailsPage;
	}
	
	// retrieve grammar content page
	public XtextGrammarContentPage grammarContentPage() {
		return this._grammarContentPage;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// store current workbench
		this.workbench = workbench;
		// store current selection
		this.selection = selection;
		// store resource and initialize graph model
		if (selection instanceof ITreeSelection && selection.size() == 1 && selection.getFirstElement() instanceof IFile
				&& ((IFile) selection.getFirstElement()).getName().endsWith(".uml")) {
			// retrieve resource
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					UMLResource.Factory.INSTANCE);
			URI resourceURI = URI.createURI(((IFile) selection.getFirstElement()).getFullPath().toString());
			UMLResource resource = (UMLResource) resourceSet.getResource(resourceURI, true);
			// retrieve model
			if (resource.getContents().size() > 0
					&& resource.getContents().get(0) instanceof org.eclipse.uml2.uml.Model) {
				// retrieve model from resource
				org.eclipse.uml2.uml.Model model = (org.eclipse.uml2.uml.Model) resource.getContents().get(0);
				// create graph from model
				this.xtextGrammarGeneratorModel = new XtextGrammarGeneratorModel(model.getName(), resourceURI.toString() + "Text",
						populateGraphFrom(model, new MultiGraph(resourceURI.toString(), false, true), resource), resource);
			}
		}
	}

	@Override
	public void addPages() {
		this.addPage(this._grammarDetailsPage);
		this.addPage(this._grammarContentPage);
	}

	@Override
	public boolean performFinish() {
		return this._grammarDetailsPage.isPageComplete() && this._grammarContentPage.isPageComplete();
	}

	@Override
	public void dispose() {
		// avvia processo di generazione della grammatica
		super.dispose();
	}
	
	// retrieve graph model
	public XtextGrammarGeneratorModel getXtextGrammarGeneratorModel() {
		return this.xtextGrammarGeneratorModel;
	}

	// populate graph from model
	private Graph populateGraphFrom(org.eclipse.uml2.uml.Model _model, Graph _graph, UMLResource _resource) {
		// primitive types
		_model.getPackagedElements().stream()
				.filter(_packagedElement -> _packagedElement instanceof org.eclipse.uml2.uml.PrimitiveType)
				.map(org.eclipse.uml2.uml.PrimitiveType.class::cast)
				.forEach(_primitiveType -> populateGraphFrom(_primitiveType, _model, _graph, _resource));
		// enumerations
		_model.getPackagedElements().stream()
				.filter(_packagedElement -> _packagedElement instanceof org.eclipse.uml2.uml.Enumeration)
				.map(org.eclipse.uml2.uml.Enumeration.class::cast)
				.forEach(_enumeration -> populateGraphFrom(_enumeration, _model, _graph, _resource));
		// classes
		_model.getPackagedElements().stream()
				.filter(_packagedElement -> _packagedElement instanceof org.eclipse.uml2.uml.Class)
				.map(org.eclipse.uml2.uml.Class.class::cast)
				.forEach(_class -> populateGraphFrom(_class, _model, _graph, _resource));
		// return back populated graph
		return _graph;
	}

	// populate graph from primitive type
	private void populateGraphFrom(org.eclipse.uml2.uml.PrimitiveType _primitiveType, org.eclipse.uml2.uml.Model _model,
			Graph _graph, UMLResource _resource) {
		// primitive type
		String edgeId = _resource.getURIFragment(_model) + "::::" + _resource.getURIFragment(_primitiveType);
		String sourceId = _resource.getURIFragment(_model);
		String targetId = _resource.getURIFragment(_primitiveType);
		Map<String, Object> edgeAttributes = Stream
				.of(new SimpleEntry<String, Object>("composition", true),
						new SimpleEntry<String, Object>("selected", true))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue()));
		_graph.addEdge(edgeId, sourceId, targetId, true).addAttributes(edgeAttributes);
	}

	// populate graph from enumeration
	private void populateGraphFrom(org.eclipse.uml2.uml.Enumeration _enumeration, org.eclipse.uml2.uml.Model _model,
			Graph _graph, UMLResource _resource) {
		// enumeration
		String edgeId = _resource.getURIFragment(_model) + "::::" + _resource.getURIFragment(_enumeration);
		String sourceId = _resource.getURIFragment(_model);
		String targetId = _resource.getURIFragment(_enumeration);
		Map<String, Object> edgeAttributes = Stream
				.of(new SimpleEntry<String, Object>("composition", true), 
						new SimpleEntry<String, Object>("selected", true))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())); 
		_graph.addEdge(edgeId, sourceId, targetId, true).addAttributes(edgeAttributes);
		// enumeration literals
		_enumeration.getOwnedLiterals().forEach(
				_enumerationLiteral -> populateGraphFrom(_enumerationLiteral, _enumeration, _graph, _resource));
	}

	// populate graph from enumeration literal
	private void populateGraphFrom(org.eclipse.uml2.uml.EnumerationLiteral _enumerationLiteral,
			org.eclipse.uml2.uml.Enumeration _enumeration, Graph _graph, UMLResource _resource) {
		// enumeration literal
		String edgeId = _resource.getURIFragment(_enumeration) + "::::" + _resource.getURIFragment(_enumerationLiteral);
		String sourceId = _resource.getURIFragment(_enumeration);
		String targetId = _resource.getURIFragment(_enumerationLiteral);
		Map<String, Object> edgeAttributes = Stream
				.of(new SimpleEntry<String, Object>("composition", true), 
						new SimpleEntry<String, Object>("selected", true))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue()));
		_graph.addEdge(edgeId, sourceId, targetId, true).addAttributes(edgeAttributes);
	}

	// populate graph from class
	private void populateGraphFrom(org.eclipse.uml2.uml.Class _class, org.eclipse.uml2.uml.Model _model, Graph _graph,
			UMLResource _resource) {
		// class
		String edgeId = _resource.getURIFragment(_model) + "::::" + _resource.getURIFragment(_class);
		String sourceId = _resource.getURIFragment(_model);
		String targetId = _resource.getURIFragment(_class);
		Map<String, Object> edgeAttributes = Stream
				.of(new SimpleEntry<String, Object>("composition", true), 
						new SimpleEntry<String, Object>("selected", true))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())); 
		_graph.addEdge(edgeId, sourceId, targetId, true).addAttributes(edgeAttributes);
		// properties
		_class.getAttributes().forEach(_property -> {
			if (_property.getType() != null) {
				populateGraphFrom(_property, _class, _graph, _resource);
			}
		});
		// superclasses
		_class.getSuperClasses().forEach(_superClass -> populateGraphFrom(_class, _superClass, _graph, _resource));

	}

	// populate graph from property
	private void populateGraphFrom(org.eclipse.uml2.uml.Property _property, org.eclipse.uml2.uml.Class _class,
			Graph _graph, UMLResource _resource) {
		// attribute
		String edgeId = _resource.getURIFragment(_property);
		String sourceId = _resource.getURIFragment(_class);
		String targetId = _resource.getURIFragment(_property.getType());
		Map<String, Object> edgeAttributes = Stream
				.of(new SimpleEntry<String, Object>("composition", _property.isComposite()), 
						new SimpleEntry<String, Object>("selected", true))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())); 
		_graph.addEdge(edgeId, sourceId, targetId, true).addAttributes(edgeAttributes);
	}

	// populate graph from superclass
	private void populateGraphFrom(org.eclipse.uml2.uml.Class _class, org.eclipse.uml2.uml.Class _superClass,
			Graph _graph, UMLResource _resource) {
		String edgeId = _resource.getURIFragment(_superClass) + "::::" + _resource.getURIFragment(_class);
		String sourceId = _resource.getURIFragment(_superClass);
		String targetId = _resource.getURIFragment(_class);
		Map<String, Object> edgeAttributes = Stream
				.of(new SimpleEntry<String, Object>("composition", true), 
						new SimpleEntry<String, Object>("selected", true))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())); 
		_graph.addEdge(edgeId, sourceId, targetId, true).addAttributes(edgeAttributes);
	}

}
