package org.nextprot.parser.hpa.subcell
import org.scalatest._
import org.nextprot.parser.core.exception.NXException
import org.nextprot.parser.core.constants.NXQuality._
import org.nextprot.parser.hpa.subcell.cases._
import org.nextprot.parser.hpa.commons.rules.APEQualityRule
import org.nextprot.parser.hpa.commons.constants.HPAValidationValue._
import org.nextprot.parser.hpa.commons.rules.APEQualityRule
import org.nextprot.parser.core.NXParser
import org.nextprot.parser.hpa.commons.rules.APEQualityRule
import org.nextprot.parser.core.datamodel.annotation.AnnotationListWrapper
import org.nextprot.parser.hpa.HPAQuality
import org.nextprot.parser.hpa.HPAUtils
import org.nextprot.parser.hpa.datamodel.HPAAnnotationsWrapper

class ErrorCasesSpec extends HPASubcellTestBase {

  "The HPANXParser parser" should " be an instance of NXParser " in {
    val parser = new HPASubcellNXParser();
    assert(parser.isInstanceOf[NXParser])
  }

  // HPA_PARS_D3
  it should "throw a NXException with NXExceptionType == CASE_RNA_NOT_DETECTED if the RNA Sequence is not found in the cell lines" in {
    val parser = new HPASubcellNXParser();
    val thrown = intercept[NXException] {
      parser.parse("src/test/resources/ENSG_WITHOUT_RNA.xml");
    }
    assert(thrown.getNXExceptionType == CASE_RNA_NOT_DETECTED)
  }

  //HPA_PARS_SPEC_G1-2
  /*
   * "a valid HPA entry should produce at least 1 annotation of type "subcellular-location"
   */
  it should "produce at least 1 annotation of type subcellular-location" in {
    val parser = new HPASubcellNXParser();
    val rowAnnots = parser.parse("src/test/resources/hpa/subcell/subcell-file-input.xml").asInstanceOf[HPAAnnotationsWrapper]._rowAnnotations
    val SLCnt = rowAnnots.filter(annot => annot._type == "subcellular location").size
    assert(SLCnt >= 1)
  }

  //HPA_PARS_SPEC_G1-3
  it should "produce 1 annotation of type subcellular-location labelled 'main' with CV term SL-0091 and 1 labelled 'additional' with CV term SL-0188" in {
    val parser = new HPASubcellNXParser();
    val wrapper = parser.parse("src/test/resources/hpa/subcell/subcell-file-input.xml");
    val rowAnnots = ((wrapper.asInstanceOf[HPAAnnotationsWrapper]))._rowAnnotations
    
    assert(rowAnnots(0)._cvTermAcc == "SL-0188")
    assert(rowAnnots(0)._description.contains("Additional location"))
    assert(rowAnnots(1)._cvTermAcc == "SL-0091")
    assert(rowAnnots(1)._description.contains("Main location"))

  }

  // HPA_PARS_D1
  it should "throw a NXException with NXExceptionType == CASE_NO_UNIPROT_MAPPING when there is no UniProt / Swissprot mapping for the entry" in {
    val parser = new HPASubcellNXParser();
    val thrown = intercept[NXException] {
      parser.parse("src/test/resources/ENSG_WITHOUT_UNIPROT_MAPPING.xml");
    }
    assert(thrown.getNXExceptionType == CASE_NO_UNIPROT_MAPPING)
  }

  // HPA_PARS_D2	
  it should "throw a NXException with NXExceptionType == CASE_NO_SUBCELLULAR_LOCATION_DATA when there is no subcellular location data in the entry" in {
    val parser = new HPASubcellNXParser();
    val thrown = intercept[NXException] {
      parser.parse("src/test/resources/ENSG_WITHOUT_SUBCELLULAR_LOCATION_DATA.xml");
    }
    assert(thrown.getNXExceptionType == CASE_NO_SUBCELLULAR_LOCATION_DATA)
  }

  // HPA_PARS_D4
  it should "throw a NXException with NXExceptionType == CASE_SUBCELULLAR_MAPPING_NOT_APPLICABLE when the subcellular mapping for the given location is not applicable in the domain of NextProt" in {
    val parser = new HPASubcellNXParser();
    val thrown = intercept[NXException] {
      parser.parse("src/test/resources/ENSG_WITH_SUBCELULLAR_MAPPING_NOT_APPLICABLE.xml");
    }
    assert(thrown.getNXExceptionType == CASE_SUBCELULLAR_MAPPING_NOT_APPLICABLE)
  }

  // HPA_PARS_SPEC_G2
  "The HPA analysis quality parser" should "throw a NXException if type is not APE (integrated), SINGLE or SELECTED" in {
    a[NXException] should be thrownBy {
      HPAQuality.getQuality(<cellExpression technology="IF" type="unknown"></cellExpression>, slSection);
    }
  }

  it should "throw a NXException with NXExceptionType == CASE_IFTYPE_UNKNOWN if the RNA Sequence is not found in the cell lines" in {
    val thrown = intercept[NXException] {
      HPAQuality.getQuality(<cellExpression technology="IF" type="unknown"></cellExpression>, slSection);
    }
    assert(thrown.getNXExceptionType == CASE_IFTYPE_UNKNOWN)
  }



  it should "throw a NXException with NXExceptionType == CASE_MULTIPLE_UNIPROT_MAPPING for subcellular location parser if the entry contains multiple antibodies" in {

    val fname = "src/test/resources/ENSG-test-multiple-uniprot-ids.xml"
    val parser = new HPASubcellNXParser();

    val thrown = intercept[NXException] {
      val template = parser.parse(fname);
    }
    assert(thrown.getNXExceptionType == CASE_MULTIPLE_UNIPROT_MAPPING)
  }

}

