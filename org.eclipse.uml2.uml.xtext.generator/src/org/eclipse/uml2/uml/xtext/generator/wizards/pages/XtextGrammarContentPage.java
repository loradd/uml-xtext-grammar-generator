package org.eclipse.uml2.uml.xtext.generator.wizards.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.uml2.uml.xtext.generator.wizards.GenerateXtextGrammarFromUmlWizard;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class XtextGrammarContentPage extends WizardPage implements Listener {

	/**
	 * SELECTION TREE
	 **/
	private Tree _selectionTree;

	/**
	 * CONSTRUCTOR
	 **/
	public XtextGrammarContentPage() {
		/* Superclass Constructor */
		super("Page 2");
		/* Setup Title */
		this.setTitle("Xtext Grammar Content");
		/* Setup Description */
		this.setDescription("Select grammar elements");
	}

	@Override
	public void createControl(Composite parent) {
		/* Widgets Composite */
		Composite composite = new Composite(parent, SWT.NONE);
		/* Page Layout */
		GridLayout pageLayout = new GridLayout(1, true);
		composite.setLayout(pageLayout);
		/* Setup Selection Tree */
		this._selectionTree = createSelectionTree(composite);
		/* Setup Page Control */
		this.setControl(composite);
		/* Setup Widget Listeners */
		this.attachListeners();
	}

	private Tree createSelectionTree(Composite composite) {
		/* Setup Selection Tree */
		Tree selectionTree = new Tree(composite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		selectionTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		selectionTree.setHeaderVisible(true);
		/* Retrieve Xtext Grammar Generator Model (Graph) */
		Graph graph = ((GenerateXtextGrammarFromUmlWizard) this.getWizard()).xtextGrammarGeneratorModel().graphModel();
		/* Retrieve root element (no incoming positive composition edges) */
		Node root = graph.getNodeSet().stream()
				.filter(_node -> _node.getEnteringEdgeSet().stream().noneMatch(_enteringEdge -> {
					return !_enteringEdge.hasAttribute("property") && _enteringEdge.hasAttribute("composition")
							&& _enteringEdge.getAttribute("composition", boolean.class);
				})).findFirst().orElse(null);
		/* Populate Selection Tree */
		this.populate(selectionTree, root);
		/* Return Selection Tree */
		return selectionTree;
	}

	/**
	 * POPULATE SELECTION TREE - START
	 **/
	private void populate(Tree _tree, Node _node) {
		TreeItem treeItem = null;
		populate(treeItem, _node);
	}

	/**
	 * POPULATE SELECTION TREE - SUBTREE
	 **/
	private void populate(TreeItem _treeItem, Node _node) {
		TreeItem treeItem = null;
		populate(treeItem, _node);
	}

	@Override
	public void handleEvent(Event event) {
		/* Event - SWT.CHECK */
		if (event.detail == SWT.CHECK) {
			/* Retrieve checked TreeItem */
			TreeItem checkedItem = (TreeItem) event.item;
			/* Retrieve Node/Edge associated to the checked TreeItem */
			Element checkedItemElement = (Element) checkedItem.getData();
			/* Update Graph Model */
			checkedItemElement.addAttribute("selected", checkedItem.getChecked());
			/* Update Buttons (Page Completion) */
			this.getWizard().getContainer().updateButtons();
		}
	}

	@Override
	public boolean isPageComplete() {
		/* Page Complete - Legal Selection */
		return this.isLegalSelection();
	}

	@Override
	public IWizardPage getNextPage() {
		/* Detach Listeners */
		this.detachListeners();
		/*
		 * @TODO Return Xtext Grammar Check Page return
		 * ((GenerateXtextGrammarFromUmlWizard)
		 * this.getWizard()).grammarCheckPage();
		 */
		return super.getNextPage();
	}

	@Override
	public boolean canFlipToNextPage() {
		/* Page Complete - Legal Selection */
		return this.isLegalSelection();
	}

	/**
	 * LISTENERS - ATTACH
	 **/
	private void attachListeners() {
		/* Selection Tree - Attach Check */
		this._selectionTree.addListener(SWT.CHECK, this);
	}

	/**
	 * LISTENERS - DETACH
	 **/
	private void detachListeners() {
		/* Selection Tree - Detach Check */
		this._selectionTree.removeListener(SWT.CHECK, this);
	}

	/**
	 * SELECTION CHECK
	 **/
	private boolean isLegalSelection() {
		/* Retrieve Xtext Grammar Generator Model (Graph) */
		Graph graph = ((GenerateXtextGrammarFromUmlWizard) this.getWizard()).xtextGrammarGeneratorModel().graphModel();
		/* Connectivity and Class Check */
		return isConnected(graph) && isWellFormed(graph);
	}

	/**
	 * CONNECTIVITY CHECK All selected elements can be reached from a common
	 * root element
	 **/
	private boolean isConnected(Graph _graph) {
		/* Retrieve root element (no incoming positive composition edges) */
		Node root = _graph.getNodeSet().stream()
				.filter(_node -> _node.hasAttribute("selected") && _node.getAttribute("selected", boolean.class)
						&& _node.getEnteringEdgeSet().stream().noneMatch(_enteringEdge -> {
							return !_enteringEdge.hasAttribute("property") && _enteringEdge.hasAttribute("composition")
									&& _enteringEdge.getAttribute("composition", boolean.class);
						}))
				.findFirst().orElse(null);
		/* Retrieve graph leaves (no outgoing positive composition edges) */
		List<Node> leaves = _graph.getNodeSet().stream()
				.filter(_node -> _node.hasAttribute("selected") && _node.getAttribute("selected", boolean.class)
						&& _node.getLeavingEdgeSet().stream().noneMatch(_leavingEdge -> {
							return !_leavingEdge.hasAttribute("property") && _leavingEdge.hasAttribute("composition")
									&& _leavingEdge.getAttribute("composition", boolean.class);
						}))
				.collect(Collectors.toList());
		/* Every leaf node is connected to the root node */
		return leaves.stream().allMatch(_leafNode -> areConnected(root, _leafNode));
	}

	/**
	 * CONNECTIVITY CHECK EndNode can be reached from StartNode through
	 * composition-only edges
	 **/
	private boolean areConnected(Node _startNode, Node _endNode) {
		/* Retrieve StartNode outgoing edges */
		return _startNode.getLeavingEdgeSet().stream()
				/*
				 * Retain outgoing edges with positive "composition" attribute
				 */
				.filter(_leavingEdge -> !_leavingEdge.hasAttribute("property")
						&& _leavingEdge.hasAttribute("composition")
						&& _leavingEdge.getAttribute("composition", boolean.class))
				/* Check if any target node leads or corresponds to EndNode */
				.anyMatch(_leavingEdge -> _leavingEdge.getTargetNode().equals(_endNode)
						|| (_leavingEdge.getTargetNode().hasAttribute("selected")
								&& _leavingEdge.getTargetNode().getAttribute("selected", boolean.class)
								&& areConnected(_leavingEdge.getTargetNode(), _endNode)));
	}

	/**
	 * WELL-FORMED CHECK All nodes have the least required selected elements
	 **/
	private boolean isWellFormed(Graph _graph) {
		return _graph.getNodeSet().stream().filter(_node -> _node.hasAttribute("class"))
				.allMatch(_node -> isWellFormed(_node));
	}

	/**
	 * CLASS CHECK All mandatory properties have been selected
	 **/
	private boolean isWellFormed(Node _node) {
		return _node.getLeavingEdgeSet().stream()
				.allMatch(_leavingEdge -> !_leavingEdge.hasAttribute("property")
						|| _leavingEdge.getAttribute("lowerBound", int.class) < 1
						|| (_leavingEdge.getAttribute("selected", boolean.class)));
	}

}
