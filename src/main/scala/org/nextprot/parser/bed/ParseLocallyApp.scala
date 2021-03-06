package org.nextprot.parser.bed

import java.lang.System.currentTimeMillis

import scala.collection.JavaConversions.seqAsJavaList

import org.nextprot.commons.statements.Statement
import org.nextprot.parser.bed.converter.BedStatementConverter
import java.util.HashSet
import org.nextprot.commons.statements.constants.NextProtSource
import org.nextprot.commons.statements.StatementField

/**
 * This app is used to test the parsing of the statements and errors.
 * Web statements should be use in production (on kant) instead
 */
object ParseLocallyApp extends App {

  val location = "/Users/dteixeira/Documents/nxflat-proxy/";
  val load = false;

  val beforeParsing = currentTimeMillis();

  BedStatementConverter.addProxyDir(location);
  
  val release = "2017-04-17";
  val database = "bioeditor";
  
  val proxyDir = BedStatementConverter.getProxyDir(database, release)
  val (statements, debugInfo) = BedStatementConverter.convert(proxyDir)
  
  //FOR DEBUG
  //statements.foreach { s => println(s.getValue(StatementField.ANNOT_CV_TERM_NAME) + " - " + s.getValue(StatementField.ANNOT_DESCRIPTION)) };
  
  //statementLoaderService.loadRawStatementsForSource(new HashSet(statements.toList), NextProtSource.BioEditor);

  println("Parsed in " + (currentTimeMillis() - beforeParsing) + " ms for " + statements.size + " statements");

}