package se.mdh.idt.uml2.xtext.grammar.generator.wizards.pages

import java.util.Iterator
import org.eclipse.jface.wizard.WizardPage
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Group
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Enumeration
import org.eclipse.uml2.uml.EnumerationLiteral
import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.NamedElement
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Profile
import org.eclipse.uml2.uml.Property
import se.mdh.idt.uml2.xtext.grammar.generator.wizards.XtextGrammarGeneratorWizard

class XtextGrammarGeneratorWizardPage extends WizardPage implements Listener {

	new() {
		super("generateXtextGrammarWizardPage")
		title = "Generate Xtext Grammar"
		description = "Setup Xtext grammar details and content"
	}

	override XtextGrammarGeneratorWizard getWizard() {
		super.getWizard() as XtextGrammarGeneratorWizard
	}

	override createControl(Composite parent) {
		/* Container Composite */
		val containerComposite = new Composite(parent, SWT.NONE)
		/* Container Composite - Layout - Fill Horizontally */
		val containerCompositeLayoutData = new GridData(GridData.FILL_HORIZONTAL)
		containerComposite.layoutData = containerCompositeLayoutData
		/* Container Composite - Inner Layout (one column) */
		val containerGridLayout = new GridLayout
		containerGridLayout.numColumns = 1
		containerComposite.layout = containerGridLayout
		
		/* Grammar Details Group */
		val grammarDetailsGroup = new Group(containerComposite, SWT.NONE)
		/* Grammar Details Group - Layout - Fill Horizontally */
		val grammarDetailsGridData = new GridData(GridData.FILL_HORIZONTAL)
		grammarDetailsGroup.layoutData = grammarDetailsGridData
		/* Grammar Details Group - Inner Layout (two columns) */
		val grammarDetailsGridLayout = new GridLayout
		grammarDetailsGridLayout.numColumns = 2
		grammarDetailsGroup.layout = grammarDetailsGridLayout
		/* Grammar Details Group - Header Text */
		grammarDetailsGroup.text = "Grammar Details"
		/* Grammar Details Group - Grammar Name */
		createGrammarNameText(grammarDetailsGroup)
		/* Grammar Details Group - Grammar URI */
		createGrammarUriText(grammarDetailsGroup)
		
		/* Grammar Content Group */
		val grammarContentGroup = new Group(containerComposite, SWT.NONE)
		/* Grammar Content Group - Composite Layout - Fill Horizontally */
		val grammarContentGridData = new GridData(GridData.FILL_BOTH)
		grammarContentGroup.layoutData = grammarContentGridData
		/* Grammar Content Group - Setup label text */
		grammarContentGroup.text = "Grammar Content"
		/* Grammar Content Group - Setup inner layout (one column) */
		val grammarContentGridLayout = new GridLayout
		grammarContentGridLayout.numColumns = 1
		grammarContentGroup.layout = grammarContentGridLayout
		/* Grammar Content Group - Grammar Content - Tree */
		grammarContentGroup.createGrammarContentTree
		
		/* Set composite as dialog page control */
		control = containerComposite
	}
	
	/* Create Grammar Name - Text */
	def void createGrammarNameText(Composite parent) {
		/* Grammar Details Group - Grammar Name - Label */
		val grammarNameLabel = new Label(parent, SWT.LEFT)
		/* Grammar Details Group - Grammar Name - Label Text */
		grammarNameLabel.text = "Name: "
		/* Grammar Details Group - Grammar Name - Text */
		wizard.xtextGrammarNameText = new Text(parent, SWT.SINGLE.bitwiseOr(SWT.BORDER))
		/* Grammar Details Group - Grammar Name - Text - Layout - Fill Horizontally */
		val grammarNameTextGridData = new GridData
		grammarNameTextGridData.horizontalAlignment = GridData.FILL
		grammarNameTextGridData.grabExcessHorizontalSpace = true
		wizard.xtextGrammarNameText.layoutData = grammarNameTextGridData
		/* Grammar Details Group - Grammar Name - Text - Content */
		wizard.xtextGrammarNameText.text = wizard.xtextGrammarName
	}
	
	/* Create Grammar Uri - Label and Text */
	def void createGrammarUriText(Composite parent) {
		/* Grammar Details Group - Grammar URI - Label */
		val grammarUriLabel = new Label(parent, SWT.LEFT)
		/* Grammar Details Group - Grammar URI - Label Text */
		grammarUriLabel.text = "    URI: "
		/* Grammar Details Group - Grammar URI - Text */
		wizard.xtextGrammarUriText = new Text(parent, SWT.SINGLE.bitwiseOr(SWT.BORDER))
		/* Grammar Details Group - Grammar URI - Text - Layout - Fill Horizontally */
		val grammarUriTextGridData = new GridData
		grammarUriTextGridData.horizontalAlignment = GridData.FILL
		grammarUriTextGridData.grabExcessHorizontalSpace = true
		wizard.xtextGrammarUriText.layoutData = grammarUriTextGridData
		/* Grammar Details Group - Grammar URI - Text - Content */
		wizard.xtextGrammarUriText.text = wizard.xtextGrammarUri
	}
	
	/* Create Grammar Content - Tree */
	def createGrammarContentTree(Composite parent) {
		/* Grammar Content - Tree */
		wizard.xtextGrammarContentTree = new Tree(parent,
			SWT.CHECK.bitwiseOr(SWT.BORDER).bitwiseOr(SWT.V_SCROLL).bitwiseOr(SWT.H_SCROLL))
		/* Grammar Content - Tree - Layout - Fill Both */
		wizard.xtextGrammarContentTree.layoutData = new GridData(GridData.FILL_BOTH)
		/* Grammar Content - Tree - Listener - Selection/Check */
		wizard.xtextGrammarContentTree.addListener(SWT.Selection, this)
		/* Grammar Content - Tree - Items */
		wizard.umlRootElement.asTreeItem
	}

	/* Create Tree Item - Profile */
	def dispatch private asTreeItem(Profile _profile) { /* TODO */ }

	/* Create Tree Item - Model */
	def dispatch private asTreeItem(Model _model) {
		System.err.println("asTreeItem : " + _model.name)
		/* Tree Item */
		val treeItem = new TreeItem(wizard.xtextGrammarContentTree, SWT.NONE)
		/* Tree Item - Text */
		treeItem.text = "[Model] " + _model.name
		/* Tree Item - Data */
		treeItem.data = _model
		/* Tree Item - Checked */
		treeItem.checked = true
		/* Tree Item - Items - Packaged Elements */
		_model.packagedElements.filter [ _packagedElement |
			_packagedElement instanceof Class || _packagedElement instanceof Enumeration ||
				_packagedElement instanceof PrimitiveType
		].forEach[_packagedElement|_packagedElement.asTreeItem(treeItem)]
	}

	/* Create Tree Item - Class */
	def dispatch private void asTreeItem(Class _class, TreeItem _parent) {
		System.err.println("asTreeItem : " + _class.name)
		/* Tree Item */
		val treeItem = new TreeItem(_parent, SWT.NONE)
		/* Tree Item - Text */
		switch (_class.superClasses.size) {
			case 0:
				treeItem.text = "[Class] " + _class.name
			default:
				treeItem.text = "[Class] " + _class.name + " -> " + _class.superClasses.join(", ", [ _superClass |
					_superClass.name
				])
		}
		/* Tree Item - Data */
		treeItem.data = _class
		/* Tree Item - Checked */
		treeItem.checked = true
		/* Tree Item - Data - Attributes */
		_class.attributes.forEach[_property|_property.asTreeItem(treeItem)]
	}

	/* Create Tree Item - Property */
	def dispatch private void asTreeItem(Property _property, TreeItem _parent) {
		System.err.println("asTreeItem : " + _property.name)
		/* Tree Item */
		val treeItem = new TreeItem(_parent, SWT.NONE)
		/* Tree Item - Text */
		treeItem.text = "[Property] " + _property.name + " : " + _property.type.name
		/* Tree Item - Data */
		treeItem.data = _property
		/* Tree Item - Checked */
		treeItem.checked = true
	}

	/* Create Tree Item - Enumeration */
	def dispatch private void asTreeItem(Enumeration _enumeration, TreeItem _parent) {
		System.err.println("asTreeItem : " + _enumeration.name)
		/* Tree Item */
		val treeItem = new TreeItem(_parent, SWT.NONE)
		/* Tree Item - Text */
		treeItem.text = "[Enumeration] " + _enumeration.name
		/* Tree Item - Data */
		treeItem.data = _enumeration
		/* Tree Item - Checked */
		treeItem.checked = true
		/* Tree Item - Items - Owned Literals */
		_enumeration.ownedLiterals.forEach[_enumerationLiteral|_enumerationLiteral.asTreeItem(treeItem)]
	}

	/* Create Tree Item - Enumeration Literal */
	def dispatch private void asTreeItem(EnumerationLiteral _enumerationLiteral, TreeItem _parent) {
		System.err.println("asTreeItem : " + _enumerationLiteral.name)
		/* Tree Item */
		val treeItem = new TreeItem(_parent, SWT.NONE)
		/* Tree Item - Text */
		treeItem.text = "[EnumerationLiteral] " + _enumerationLiteral.name
		/* Tree Item - Data */
		treeItem.data = _enumerationLiteral
		/* Tree Item - Checked */
		treeItem.checked = true
	}

	/* Create Tree Item - Primitive Type */
	def dispatch private void asTreeItem(PrimitiveType _primitiveType, TreeItem _parent) {
		System.err.println("asTreeItem : " + _primitiveType.name)
		/* Tree Item */
		val treeItem = new TreeItem(_parent, SWT.NONE)
		/* Tree Item - Text */
		treeItem.text = "[PrimitiveType] " + _primitiveType.name
		/* Tree Item - Data */
		treeItem.data = _primitiveType
		/* Tree Item - Checked */
		treeItem.checked = true
	}

	/* Dispose Wizard Page */
	override dispose() {
		println("Disposed")
		super.dispose()
	}

	/* Handle TreeItem Selections */
	override handleEvent(Event event) {
		if (event.detail == SWT.CHECK) {
			/* Propagate Down Event */
			event.propagateDown(event.item as TreeItem)
		}
		/* Update Page Completion */
		pageComplete = isPageComplete
	}

	/* Propagate Event Down Tree */
	def private void propagateDown(Event event, TreeItem treeItem) {
		/* Tree Item - Items */
		val items = treeItem.items
		if (items.size > 0) {
			treeItem.expanded = treeItem.checked
			items.forEach [ _item |
				_item.checked = treeItem.checked
				event.propagateDown(_item)
			]
		}
	}

	/* Check Tree Item Selection */
	def private boolean hasLegalSelection(TreeItem treeItem) {
		if (treeItem.data instanceof Model) {
			/* The model has been selected and all packaged elements must comply with their correct selection conditions */
			return treeItem.checked && treeItem.items.forall[packagedElement|packagedElement.hasLegalSelection]
		} else if (treeItem.data instanceof Class) {
			/* If at least one property has been selected, then all these have to comply with their correct selection conditions and the class has to be selected as well */
			val selectedProperties = treeItem.items.filter[property|property.checked]
			return selectedProperties.empty || (treeItem.checked && selectedProperties.forall [ property |
				property.hasLegalSelection
			])
		} else if (treeItem.data instanceof Property) {
			/* If the property has been selected, then its type has to be selected as well */
			return !treeItem.checked || treeItem.parent.containsChecked((treeItem.data as Property).type)
		} else if (treeItem.data instanceof Enumeration) {
			/* At least one enumeration literal has to be selected if and only if the enumeration has been selected */
			return !treeItem.checked == !treeItem.items.filter[enumerationLiteral|enumerationLiteral.checked].empty
		}
		true
	}

	/* Check if tree contains element and the corresponding tree item is checked 
	 * TODO :   the current implementation returns true if the element can not be found
	 * 			this should be revised whenever elements from other packaged will be supported.
	 */
	def private boolean containsChecked(Tree tree, NamedElement element) {
		try {
			return tree.find(element).checked
		} catch (NullPointerException e) {
			return true
		}
	}

	// search element in tree item
	def private TreeItem find(TreeItem treeItem, NamedElement element) {
		// search element in children
		for (child : treeItem.items)
			if (child.data instanceof NamedElement &&
				(child.data as NamedElement).qualifiedName.equals(element.qualifiedName))
				// element found in children
				return child
		// search element in nephews
		for (child : treeItem.items) {
			val nephew = child.find(element)
			if (nephew != null)
				// element found in nephews
				return nephew
		}
		// not found
		return null
	}
	
	// search element in tree
	def private TreeItem find(Tree tree, NamedElement element) {
		// search element in children
		for (child : tree.items) 
			if (child.data instanceof NamedElement &&
					(child.data as NamedElement).qualifiedName.equals(element.qualifiedName))
					// element found in children
					return child
				// search element in nephews
		for (child : tree.items) {
			val nephew = child.find(element)
			if (nephew != null)
				// element found in nephews
				return nephew
		}
		// not found
		return null
	}

	override isPageComplete() {
		wizard.xtextGrammarContentTree.items.forall[item|item.hasLegalSelection]
	}

}
