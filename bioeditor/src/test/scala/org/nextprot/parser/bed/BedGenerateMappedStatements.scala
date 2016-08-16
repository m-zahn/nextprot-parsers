package org.nextprot.parser.bed

import org.nextprot.commons.statements.StatementField
import org.nextprot.parsers.bed.converter.BedServiceStatementConverter
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class BedGenerateMappedStatements extends FlatSpec with Matchers {

  //cp /Volumes/common/Calipho/navmutpredict/xml/*.xml ~/Documents/bed/
  //cp /Volumes/common/Calipho/caviar/xml/*.xml ~/Documents/bed/
  BedServiceStatementConverter.addProxyDir("/Users/dteixeira/Documents/bed/");
  val statements = BedServiceStatementConverter.convert("brca1");

  it should "return more than 1000 statements for brca1" in {

    val statements = BedServiceStatementConverter.convert("brca1");
    assert(statements.length > 1000)

    val noImpactStatements = statements.filter { s => "no impact".equals(s.getValue(StatementField.ANNOT_CV_TERM_NAME)); }.toList
    assert(statements.length > 100)

    println(noImpactStatements(1).getValue(StatementField.ANNOT_DESCRIPTION));
    assert(noImpactStatements(1).getValue(StatementField.ANNOT_DESCRIPTION).startsWith("has no impact on"));

  }

  it should "generate correctly the description" in {

    assert(statements.length > 1000)

    val binaryInteraction = statements.filter { s => "increases binding to".equals(s.getValue(StatementField.ANNOT_DESCRIPTION)); }.toList
    assert(statements.length > 10)

    val noImpactStatements = statements.filter { s => "no impact".equals(s.getValue(StatementField.ANNOT_CV_TERM_NAME)); }.toList
    assert(statements.length > 10)
    assert(noImpactStatements(0).getValue(StatementField.ANNOT_DESCRIPTION).startsWith("has no impact on"));

  }

  it should "be gold for all variants and mutagenesis" in {

    val variantStatements = statements.filter(_.getValue(StatementField.ANNOTATION_CATEGORY).equals("variant")).toList
    assert(variantStatements.length > 10)

    val distinctQualities = variantStatements.map(_.getValue(StatementField.EVIDENCE_QUALITY)).toList.distinct;
    assert(distinctQualities.length.equals(1));
    assert(distinctQualities(0).equals("GOLD"));
    
  }

}