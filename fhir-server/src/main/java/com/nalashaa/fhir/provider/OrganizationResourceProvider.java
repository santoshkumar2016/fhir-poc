package com.nalashaa.fhir.provider;

import java.util.LinkedList;
import java.util.List;

import com.nalashaa.fhir.model.MyOrganization;

import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * This is a simple resource provider which only implements "read/GET" methods, but
 * which uses a custom subclassed resource definition to add statically bound
 * extensions.
 * 
 * See the MyOrganization definition to see how the custom resource 
 * definition works.
 */
public class OrganizationResourceProvider implements IResourceProvider {

	/**
	 * The getResourceType method comes from IResourceProvider, and must be overridden to indicate what type of resource this provider supplies.
	 */
	@Override
	public Class<MyOrganization> getResourceType() {
		return MyOrganization.class;
	}

	/**
	 * The "@Read" annotation indicates that this method supports the read operation. It takes one argument, the Resource type being returned.
	 * 
	 * @param theId
	 *            The read operation takes one parameter, which must be of type IdDt and must be annotated with the "@Read.IdParam" annotation.
	 * @return Returns a resource matching this identifier, or null if none exists.
	 */
	@Read(version = true)
	public MyOrganization getResourceById(@IdParam IdDt theId) {
		
		/*
		 * We only support one organization, so the follwing
		 * exception causes an HTTP 404 response if the 
		 * ID of "1" isn't used.
		 */
		if (!"1".equals(theId.getValue())) {
			throw new ResourceNotFoundException(theId);
		}
		
		MyOrganization retVal = new MyOrganization();
		retVal.setId("1");
		retVal.addIdentifier().setSystem("urn:example:orgs").setValue("ResourceByIdFooOrganization");
		retVal.addAddress().addLine("123 Fake Street").setCity("Toronto");
		retVal.addTelecom().setUse(ContactPointUseEnum.WORK).setValue("1-888-123-4567");
		
		// Populate the first, primitive extension
		retVal.setBillingCode(new CodeDt("00102-1"));
		
		// The second extension is repeatable and takes a block type
		MyOrganization.EmergencyContact contact = new MyOrganization.EmergencyContact();
		contact.setActive(new BooleanDt(true));
		contact.setContact(new ContactPointDt());
		retVal.getEmergencyContact().add(contact);
		
		return retVal;
	}
	
	@Read
	public MyOrganization readOrganization(@IdParam IdDt theId) {
		MyOrganization retVal = new MyOrganization();
		retVal.setId("1");
		retVal.addIdentifier().setSystem("urn:example:orgs").setValue("ReadFooOrganization");
		retVal.addAddress().addLine("123 Fake Street").setCity("Toronto");
		retVal.addTelecom().setUse(ContactPointUseEnum.WORK).setValue("1-888-123-4567");
		
		// Populate the first, primitive extension
		retVal.setBillingCode(new CodeDt("00102-1"));
		
		// The second extension is repeatable and takes a block type
		MyOrganization.EmergencyContact contact = new MyOrganization.EmergencyContact();
		contact.setActive(new BooleanDt(true));
		contact.setContact(new ContactPointDt());
		retVal.getEmergencyContact().add(contact);
		
		return retVal;
	}

	@Search
	public List<MyOrganization> findOrganizationssUsingArbitraryCtriteria() {
		
		LinkedList<MyOrganization> retVal = new LinkedList<MyOrganization>();
		
		
		MyOrganization myOrg = new MyOrganization();
		myOrg.setId("1");
		myOrg.addIdentifier().setSystem("urn:example:orgs").setValue("SearchFooOrganization");
		myOrg.addAddress().addLine("123 Fake Street").setCity("Toronto");
		myOrg.addTelecom().setUse(ContactPointUseEnum.WORK).setValue("1-888-123-4567");
		
		// Populate the first, primitive extension
		myOrg.setBillingCode(new CodeDt("00102-1"));
		
		// The second extension is repeatable and takes a block type
		MyOrganization.EmergencyContact contact = new MyOrganization.EmergencyContact();
		contact.setActive(new BooleanDt(true));
		contact.setContact(new ContactPointDt());
		myOrg.getEmergencyContact().add(contact);
		
		retVal.add(myOrg);
		
		return retVal;
		
	}

}
