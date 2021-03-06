package org.nextprot.parser.bed.converter;

import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.StatementField;

public class DescriptionGenerator {

	public static String getDescriptionForPhenotypeAnnotation(String impact, Statement statement){
		
		String impactString = impact + "s";
		String category = statement.getValue(StatementField.ANNOTATION_CATEGORY).toLowerCase();

		if(impactString.equals("no impacts")){
			impactString = "has no impact on";
		}
		
		if(impactString.equals("no impact on temperature-dependence ofs")){
			impactString = "has no impact on temperature-dependence of";
		}else if(impactString.equals("impact on temperature-dependence ofs")){
			impactString = "impacts on temperature-dependence of";
		}
		
		
        if(category.equals("go-cellular-component")) {
        	return impactString + " localisation in " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        }else  if(category.equals("go-biological-process")) {
        	return impactString + " " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        }else  if(category.equals("protein-property")) {
        	return impactString + " " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        }else  if(category.equals("go-molecular-function")) {
        	return impactString + " " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        }else  if(category.equals("electrophysiological-parameter")) {
        	return impactString + " " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        }else  if(category.equals("mammalian-phenotype")) {
        	//According to https://issues.isb-sib.ch/browse/NEXTPROT-1195
        	if(impactString.equals("impacts")) {
            	return "causes phenotype " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        	}else if(impactString.equals("has no impact on")) {
            	return "does not cause phenotype " + statement.getValue(StatementField.ANNOT_CV_TERM_NAME);
        	} else throw new RuntimeException("Not expecting any other relation at this stage" + impactString);
        	
        } else if(category.equals("binary-interaction")) {
        	return impactString + " binding to " + statement.getValue(StatementField.BIOLOGICAL_OBJECT_NAME);
        }else  if(category.equals("small-molecule-interaction")) {
        	return impactString + " binding to " + statement.getValue(StatementField.BIOLOGICAL_OBJECT_NAME);
        }else throw new RuntimeException("Category " + category + " not defined");
		
	}
}
