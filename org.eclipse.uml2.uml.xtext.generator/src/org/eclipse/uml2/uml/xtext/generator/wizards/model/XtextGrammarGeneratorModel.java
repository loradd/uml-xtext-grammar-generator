package org.eclipse.uml2.uml.xtext.generator.wizards.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

public class XtextGrammarGeneratorModel {

	/**
	 * GRAMMAR NAME
	 **/
	private String _grammarName;

	/**
	 * GRAMMAR URI
	 **/
	private String _grammarURI;

	/**
	 * GRAPH MODEL
	 **/
	private Graph _graphModel;

	/**
	 * UML MODEL
	 **/
	private Model _umlModel;

	/**
	 * CONSTRUCTOR (GrammarName, GrammarURI, UMLResource)
	 **/
	public XtextGrammarGeneratorModel(UMLResource _umlResource) {
		this._umlModel = _umlResource !=  null && _umlResource.getContents().size() > 0
				&& _umlResource.getContents().get(0) instanceof org.eclipse.uml2.uml.Model
						? (org.eclipse.uml2.uml.Model) _umlResource.getContents().get(0) : null;
		this._graphModel = _umlResource != null && _umlResource.getContents().size() > 0
				&& _umlResource.getContents().get(0) instanceof org.eclipse.uml2.uml.Model
						? createGraphFrom((UMLResource) _umlResource) : null;
		this._grammarName = this._umlModel != null ? this._umlModel.getName() : "";
		this._grammarURI = _umlResource != null ? _umlResource.getURI() + "Text" : ""; 
	}

	/**
	 * GRAMMAR NAME (Get)
	 **/
	public String grammarName() {
		return this._grammarName;
	}

	/**
	 * GRAMMAR NAME (Set)
	 **/
	public void grammarName(String _grammarName) {
		this._grammarName = _grammarName;
	}

	/**
	 * GRAMMAR URI (Get)
	 **/
	public String grammarURI() {
		return this._grammarURI;
	}

	/**
	 * GRAMMAR URI (Set)
	 **/
	public void grammarURI(String _grammarURI) {
		this._grammarURI = _grammarURI;
	}

	/**
	 * GRAPH MODEL (Get)
	 **/
	public Graph graphModel() {
		return this._graphModel;
	}

	/**
	 * UML MODEL (Get)
	 **/
	public org.eclipse.uml2.uml.Model umlModel() {
		return this._umlModel;
	}

	/**
	 * CREATE GRAPH (UMLResource)
	 **/
	private Graph createGraphFrom(UMLResource _resource) {
		/* Create Graph */
		Graph graph = new MultiGraph(_resource.getURI().toString(), false, true);
		/* Populate Graph From Model */
		populate(graph, (org.eclipse.uml2.uml.Model) _resource.getContents().get(0));
		/* Return Graph */
		return graph;
	}

	/**
	 * POPULATE GRAPH (Model)
	 **/
	private void populate(Graph _graph, org.eclipse.uml2.uml.Model _model) {
		/* Visited Check */
		if (_graph.getNode(_model.getQualifiedName()) != null)
			return;
		/* Create Node + Set As Selected */
		_graph.addNode(_model.getQualifiedName()).addAttribute("selected");
		/* Packaged Elements - Classes */
		_model.getPackagedElements().stream()
				.filter(_packagedElement -> _packagedElement instanceof org.eclipse.uml2.uml.Class)
				.map(org.eclipse.uml2.uml.Class.class::cast).forEach(_class -> {
					/* Populate Graph From Class */
					populate(_graph, _class);
					/* Connect Model To Class */
					_graph.addEdge(_model.getQualifiedName() + "::[classes]::" + _class.getQualifiedName(),
							_model.getQualifiedName(), _class.getQualifiedName(), true).addAttribute("composition");
					/* Connect Class To Model */
					_graph.addEdge(_class.getQualifiedName() + "::[model]::" + _model.getQualifiedName(),
							_class.getQualifiedName(), _model.getQualifiedName(), true);
				});
		/* Packaged Elements - Enumerations */
		_model.getPackagedElements().stream()
				.filter(_packagedElement -> _packagedElement instanceof org.eclipse.uml2.uml.Enumeration)
				.map(org.eclipse.uml2.uml.Enumeration.class::cast).forEach(_enumeration -> {
					/* Populate Graph From Enumeration */
					populate(_graph, _enumeration);
					/* Connect Model To Enumeration */
					_graph.addEdge(_model.getQualifiedName() + "::[enumerations]::" + _enumeration.getQualifiedName(),
							_model.getQualifiedName(), _enumeration.getQualifiedName(), true)
							.addAttribute("composition");
					/* Connect Enumeration To Model */
					_graph.addEdge(_enumeration.getQualifiedName() + "::[model]::" + _model.getQualifiedName(),
							_enumeration.getQualifiedName(), _model.getQualifiedName(), true);
				});
		/* Packaged Elements - Primitive Types */
		_model.getPackagedElements().stream()
				.filter(_packagedElement -> _packagedElement instanceof org.eclipse.uml2.uml.PrimitiveType)
				.map(org.eclipse.uml2.uml.PrimitiveType.class::cast).forEach(_primitiveType -> {
					/* Populate Graph From Primitive Type */
					populate(_graph, _primitiveType);
					/* Connect Model To Primitive Type */
					_graph.addEdge(
							_model.getQualifiedName() + "::[primitiveTypes]::" + _primitiveType.getQualifiedName(),
							_model.getQualifiedName(), _primitiveType.getQualifiedName(), true)
							.addAttribute("composition");
					/* Connect Primitive Type To Model */
					_graph.addEdge(_primitiveType.getQualifiedName() + "::[model]::" + _model.getQualifiedName(),
							_primitiveType.getQualifiedName(), _model.getQualifiedName(), true);
				});
	}

	/**
	 * POPULATE GRAPH (Class)
	 **/
	private void populate(Graph _graph, org.eclipse.uml2.uml.Class _class) {
		/* Visited Check */
		if (_graph.getNode(_class.getQualifiedName()) != null)
			return;
		/* Create Node + Set As Selected */
		_graph.addNode(_class.getQualifiedName()).addAttribute("class", true);
		/* Attributes - Primitive Types */
		_class.getAttributes().stream()
				.filter(_attribute -> _attribute.getType() instanceof org.eclipse.uml2.uml.PrimitiveType)
				.forEach(_attribute -> {
					/* Populate Graph From PrimitiveType */
					populate(_graph, (org.eclipse.uml2.uml.PrimitiveType) _attribute.getType());
					/* Connect Class To PrimitiveType */
					_graph.addEdge(
							_class.getQualifiedName() + "::[" + _attribute.getName() + "]::"
									+ _attribute.getType().getQualifiedName(),
							_class.getQualifiedName(), _attribute.getType().getQualifiedName(), true)
							.addAttributes(Stream
									.of(new SimpleEntry<String, Object>("composition", _attribute.isComposite()),
											new SimpleEntry<String, Object>("selected", true),
											new SimpleEntry<String, Object>("property", true), 
											new SimpleEntry<String, Object>("lowerBound", _attribute.lowerBound()), 
											new SimpleEntry<String, Object>("upperBound", _attribute.upperBound()))
									.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
				});
		/* Attributes - Enumerations */
		_class.getAttributes().stream()
				.filter(_attribute -> _attribute.getType() instanceof org.eclipse.uml2.uml.Enumeration)
				.forEach(_attribute -> {
					/* Populate Graph From Enumeration */
					populate(_graph, (org.eclipse.uml2.uml.Enumeration) _attribute.getType());
					/* Connect Class To Enumeration */
					_graph.addEdge(
							_class.getQualifiedName() + "::[" + _attribute.getName() + "]::"
									+ _attribute.getType().getQualifiedName(),
							_class.getQualifiedName(), _attribute.getType().getQualifiedName(), true)
							.addAttributes(Stream
									.of(new SimpleEntry<String, Object>("composition", _attribute.isComposite()),
											new SimpleEntry<String, Object>("selected", true), 
											new SimpleEntry<String, Object>("property", true), 
											new SimpleEntry<String, Object>("lowerBound", _attribute.lowerBound()), 
											new SimpleEntry<String, Object>("upperBound", _attribute.upperBound()))
									.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
				});
		/* Attributes - Classes */
		_class.getAttributes().stream().filter(_attribute -> _attribute.getType() instanceof org.eclipse.uml2.uml.Class)
				.forEach(_attribute -> {
					/* Populate Graph From Class */
					populate(_graph, (org.eclipse.uml2.uml.Class) _attribute.getType());
					/* Connect Class To Class */
					_graph.addEdge(
							_class.getQualifiedName() + "::[" + _attribute.getName() + "]::"
									+ _attribute.getType().getQualifiedName(),
							_class.getQualifiedName(), _attribute.getType().getQualifiedName(), true)
							.addAttributes(Stream
									.of(new SimpleEntry<String, Object>("composition", _attribute.isComposite()),
											new SimpleEntry<String, Object>("selected", true), 
											new SimpleEntry<String, Object>("property", true), 
											new SimpleEntry<String, Object>("lowerBound", _attribute.lowerBound()), 
											new SimpleEntry<String, Object>("upperBound", _attribute.upperBound()))
									.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
				});
		
		// super
	}

	/**
	 * POPULATE GRAPH (Enumeration)
	 **/
	private void populate(Graph _graph, org.eclipse.uml2.uml.Enumeration _enumeration) {
		/* Visited Check */
		if (_graph.getNode(_enumeration.getQualifiedName()) != null)
			return;
		/* Create Node + Set As Selected */
		_graph.addNode(_enumeration.getQualifiedName()).addAttribute("selected");
		/* Enumeration Literals */
		_enumeration.getOwnedLiterals().forEach(_enumerationLiteral -> {
			/* Populate Graph From Enumeration Literal */
			populate(_graph, _enumerationLiteral);
			/* Connect Enumeration To Enumeration Literal */
			_graph.addEdge(
					_enumeration.getQualifiedName() + "::[enumerationLiterals]::"
							+ _enumerationLiteral.getQualifiedName(),
					_enumeration.getQualifiedName(), _enumerationLiteral.getQualifiedName(), true)
					.addAttribute("composition");
			/* Connect Enumeration Literal To Enumeration */
			_graph.addEdge(
					_enumerationLiteral.getQualifiedName() + "::[enumeration]::" + _enumeration.getQualifiedName(),
					_enumerationLiteral.getQualifiedName(), _enumeration.getQualifiedName(), true);
		});
	}

	/**
	 * POPULATE GRAPH (Enumeration Literal)
	 **/
	private void populate(Graph _graph, org.eclipse.uml2.uml.EnumerationLiteral _enumerationLiteral) {
		/* Existence Check */
		if (_graph.getNode(_enumerationLiteral.getQualifiedName()) != null)
			return;
		/* Create Node + Set As Selected */
		_graph.addNode(_enumerationLiteral.getQualifiedName()).addAttribute("selected");
	}

	/**
	 * POPULATE GRAPH (Primitive Type)
	 **/
	private void populate(Graph _graph, org.eclipse.uml2.uml.PrimitiveType _primitiveType) {
		/* Existence Check */
		if (_graph.getNode(_primitiveType.getQualifiedName()) != null)
			return;
		/* Create Node + Set As Selected */
		_graph.addNode(_primitiveType.getQualifiedName()).addAttribute("selected");

	}

}
