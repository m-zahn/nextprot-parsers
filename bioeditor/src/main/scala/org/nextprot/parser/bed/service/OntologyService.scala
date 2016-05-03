package org.nextprot.parser.bed.service

import org.apache.jena.query.QueryFactory
import org.apache.jena.sparql.engine.http.QueryEngineHTTP
import org.nextprot.parser.bed.utils.BEDUtils
import org.nextprot.parser.bed.utils.Memoize.memoize

object OntologyService {

  /*Try with this
  val sparqlQuery =
    """
    PREFIX ot: <http://purl.obolibrary.org/obo/> 
    PREFIX otw: <http://www.geneontology.org/formats/oboInOwl#>
    SELECT ?namespace
    FROM <http://purl.obolibrary.org/obo/merged/GO>
    WHERE { ot:GO_0044376 otw:hasOBONamespace ?namespace . } 
    LIMIT 2
    """;
   */
  val sparqlNxQuery =
    """
    PREFIX cv: <http://nextprot.org/rdf/terminology/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    SELECT *
	   WHERE {
	    cv:GO_ACCESSION rdf:type ?type ;
	  	 	          rdfs:label ?label .
		  }
    """;

  val endpoint = "http://kant:8890/sparql";

  def getGoSubCategoryFromAccession(accession: String): String = {
    return getSPARQLResultMemoized(accession);
  }

  val getSPARQLResultMemoized = memoize(getSPARQLResult _)

  def getSPARQLResult(accession: String): String = {

    val sparqlNewQuery = sparqlNxQuery.replaceAll("GO_ACCESSION", accession.replaceAll(":", "_"));
    val query = QueryFactory.create(sparqlNewQuery);

    val httpQuery = new QueryEngineHTTP(endpoint, query);
    val results = httpQuery.execSelect();
    while (results.hasNext()) {
      val solution = results.next();
      val term = solution.get("type").asResource().toString();
      val label = solution.get("label").asLiteral().toString();
      val v = term.replaceAll("http://nextprot.org/rdf#", "");
      val result = BEDUtils.camelToDashes(v).substring(1);
      println(accession + " " + result + " " + v + label);
      return result;
    }
    return null;
  }

}