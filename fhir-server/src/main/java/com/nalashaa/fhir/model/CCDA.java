package com.nalashaa.fhir.model;

import java.util.List;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;

@ResourceDef(name="CCDA", id="ccda")
public class CCDA extends ca.uhn.fhir.model.dstu2.resource.BaseResource
implements  IResource{
	
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="id",
		formalDefinition="An identifier for this CCDA"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	
	@Child(name="patient", order=15, min=0, max=1, summary=true, modifier=false, type={
			ca.uhn.fhir.model.dstu2.resource.Patient.class	})
		@Description(
			shortDefinition="",
			formalDefinition="Patient that in the CCDA"
		)
		private ResourceReferenceDt patient;
	
	
	
	/**
	 * Sets the value(s) for <b>identifier</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for this patient
     * </p> 
	 */
	public CCDA setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	public ResourceReferenceDt getPatient() {
		if (patient == null) {
			patient = new ResourceReferenceDt();
		}
		return patient;
	}

	public CCDA setPatient(ResourceReferenceDt patient) {
		this.patient = patient;
		
		return this;
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for this CCDA
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	
	
	
	
	
	

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getResourceName() {
		
		return "CCDA";
	}

	@Override
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU2;
    }

}
