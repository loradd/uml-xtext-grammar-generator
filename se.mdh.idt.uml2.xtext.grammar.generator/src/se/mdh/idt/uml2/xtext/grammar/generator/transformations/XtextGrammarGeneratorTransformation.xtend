package se.mdh.idt.uml2.xtext.grammar.generator.transformations

import org.eclipse.swt.widgets.Tree

class XtextGrammarGeneratorTransformation {
	
	val String grammarName
	val String grammarUri
	val Tree grammarElements
	
	new (String grammarName, String grammarUri, Tree grammarElements) {
		this.grammarName = grammarName
		this.grammarUri = grammarUri
		this.grammarElements = grammarElements
	}
	
	def run () {
		println("Generating Xtext Grammar...")
	}
		
}
