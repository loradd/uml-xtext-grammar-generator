package org.eclipse.uml2.uml.xtext.generator.wizards.model;

import org.eclipse.uml2.uml.resource.UMLResource;
import org.graphstream.graph.Graph;

public class XtextGrammarGeneratorModel {

	private String _grammarName; // grammar name
	private String _grammarURI; // grammar uri
	private final Graph _graph; // graph model
	private final UMLResource _resource; // uml resource

	// constructor 
	public XtextGrammarGeneratorModel(String grammarName, String grammarURI, Graph graph, UMLResource resource) {
		this._grammarName = grammarName;
		this._grammarURI = grammarURI;
		this._graph = graph;
		this._resource = resource;
	}

	// retrieve grammar name
	public String grammarName() {
		return this._grammarName;
	}

	// set grammar name
	public void grammarName(String grammarName) {
		this._grammarName = grammarName; 
	} 
	
	// get grammar uri
	public String grammarURI() {
		return this._grammarURI;
	}
	
	// set grammar uri	
	public void grammarURI(String grammarURI) {
		this._grammarURI = grammarURI; 
	}
	
	// retrieve graph
	public Graph graph() {
		return this._graph;
	}

	// retrieve resource
	public UMLResource resource() {
		return this._resource;
	}

}
