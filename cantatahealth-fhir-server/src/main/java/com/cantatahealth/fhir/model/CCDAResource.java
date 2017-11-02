package com.cantatahealth.fhir.model;

import java.util.ArrayList;
import java.util.List;
 
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
 
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
 
/**
 * This is an example of a custom resource that also uses a custom
 * datatype.
 *
 * Note that we are extensing DomainResource for an STU3
 * resource. For DSTU2 it would be BaseResource.
 */
@ResourceDef(name = "CCDA", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class CCDAResource extends DomainResource {
 
    private static final long serialVersionUID = 1L;
 
    /**
     * We give the resource a field with name "television". This field has no
     * specific type, so it's a choice[x] field for any type.
     */
    @Child(name="field1", min=1, max=Child.MAX_UNLIMITED, order=0)
    private List<Type> field1;
 
    /**
     * We'll give it one more field called "dogs"
     */
   @Child(name = "field2", min=0, max=1, order=1)
    private StringType field2;
     
    @Override
    public CCDAResource copy() {
    	CCDAResource retVal = new CCDAResource();
        super.copyValues(retVal);
        retVal.field1 = field1;
      retVal.field2 = field2;
        return retVal;
    }
 
   public List<Type> getField1() {
       if (field1 == null) {
    	   field1 = new ArrayList<Type>();
       }
        return field1;
    }
 
   public StringType getField2() {
      return field2;
   }
 
    @Override
    public ResourceType getResourceType() {
        return null;
    }
 
    @Override
    public FhirVersionEnum getStructureFhirVersionEnum() {
        return FhirVersionEnum.DSTU3;
    }
 
    @Override
    public boolean isEmpty() {
        return ElementUtil.isEmpty(field1, field2);
    }
 
    public void setField1(List<Type> theValue) {
        this.field1 = theValue;
    }
 
    public void setField2(StringType theValue) {
    	field2 = theValue;
   }
 
}
