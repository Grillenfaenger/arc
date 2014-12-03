package de.uni_koeln.spinfo.arc.tagger;

import java.util.Map;
import java.util.TreeSet;

public class SursilvanTagger extends POSMatcher {

	
	
	public SursilvanTagger(Map<String, TreeSet<String>> fullForms, String collectionName) {
		super(fullForms, collectionName);
	}

	public SursilvanTagger() {
		super();
		//super(fullForms, collectionName);
	}

	@Override
	public boolean isIndImperfect(String token) {
		if (token.endsWith("avel") || token.endsWith("avas")
				|| token.endsWith("ava") || token.endsWith("avan")
				|| token.endsWith("evel") || token.endsWith("evas")
				|| token.endsWith("eva") || token.endsWith("evan")){
			return true;
		}
		return false;
	}



	@Override
	public boolean isAdverb(String token) {
		if(token.endsWith("mein")){
			return true;
		}
		return false;
	}

}
