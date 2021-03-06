//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.28 at 06:31:45 PM IST 
//


package org.hl7.sdtc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.hl7.v3.BL;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ED;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.TS;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.hl7.sdtc package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RaceCode_QNAME = new QName("urn:hl7-org:sdtc", "raceCode");
    private final static QName _DeceasedInd_QNAME = new QName("urn:hl7-org:sdtc", "deceasedInd");
    private final static QName _SignatureText_QNAME = new QName("urn:hl7-org:sdtc", "signatureText");
    private final static QName _StatusCode_QNAME = new QName("urn:hl7-org:sdtc", "statusCode");
    private final static QName _Desc_QNAME = new QName("urn:hl7-org:sdtc", "desc");
    private final static QName _Id_QNAME = new QName("urn:hl7-org:sdtc", "id");
    private final static QName _InFulfillmentOf1_QNAME = new QName("urn:hl7-org:sdtc", "inFulfillmentOf1");
    private final static QName _Patient_QNAME = new QName("urn:hl7-org:sdtc", "patient");
    private final static QName _DischargeDispositionCode_QNAME = new QName("urn:hl7-org:sdtc", "dischargeDispositionCode");
    private final static QName _AsPatientRelationship_QNAME = new QName("urn:hl7-org:sdtc", "asPatientRelationship");
    private final static QName _PriorityNumber_QNAME = new QName("urn:hl7-org:sdtc", "priorityNumber");
    private final static QName _BirthTime_QNAME = new QName("urn:hl7-org:sdtc", "birthTime");
    private final static QName _EthnicGroupCode_QNAME = new QName("urn:hl7-org:sdtc", "ethnicGroupCode");
    private final static QName _DeceasedTime_QNAME = new QName("urn:hl7-org:sdtc", "deceasedTime");
    private final static QName _MultipleBirthOrderNumber_QNAME = new QName("urn:hl7-org:sdtc", "multipleBirthOrderNumber");
    private final static QName _MultipleBirthInd_QNAME = new QName("urn:hl7-org:sdtc", "multipleBirthInd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.hl7.sdtc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link INTPOS }
     * 
     */
    public INTPOS createINTPOS() {
        return new INTPOS();
    }

    /**
     * Create an instance of {@link AsPatientRelationship }
     * 
     */
    public AsPatientRelationship createAsPatientRelationship() {
        return new AsPatientRelationship();
    }

    /**
     * Create an instance of {@link InFulfillmentOf1 }
     * 
     */
    public InFulfillmentOf1 createInFulfillmentOf1() {
        return new InFulfillmentOf1();
    }

    /**
     * Create an instance of {@link SdtcPatient }
     * 
     */
    public SdtcPatient createSdtcPatient() {
        return new SdtcPatient();
    }

    /**
     * Create an instance of {@link ActReference }
     * 
     */
    public ActReference createActReference() {
        return new ActReference();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "raceCode")
    public JAXBElement<CE> createRaceCode(CE value) {
        return new JAXBElement<CE>(_RaceCode_QNAME, CE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "deceasedInd")
    public JAXBElement<BL> createDeceasedInd(BL value) {
        return new JAXBElement<BL>(_DeceasedInd_QNAME, BL.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ED }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "signatureText")
    public JAXBElement<ED> createSignatureText(ED value) {
        return new JAXBElement<ED>(_SignatureText_QNAME, ED.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "statusCode")
    public JAXBElement<CS> createStatusCode(CS value) {
        return new JAXBElement<CS>(_StatusCode_QNAME, CS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ED }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "desc")
    public JAXBElement<ED> createDesc(ED value) {
        return new JAXBElement<ED>(_Desc_QNAME, ED.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link II }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "id")
    public JAXBElement<II> createId(II value) {
        return new JAXBElement<II>(_Id_QNAME, II.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InFulfillmentOf1 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "inFulfillmentOf1")
    public JAXBElement<InFulfillmentOf1> createInFulfillmentOf1(InFulfillmentOf1 value) {
        return new JAXBElement<InFulfillmentOf1>(_InFulfillmentOf1_QNAME, InFulfillmentOf1 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SdtcPatient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "patient")
    public JAXBElement<SdtcPatient> createPatient(SdtcPatient value) {
        return new JAXBElement<SdtcPatient>(_Patient_QNAME, SdtcPatient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "dischargeDispositionCode")
    public JAXBElement<CE> createDischargeDispositionCode(CE value) {
        return new JAXBElement<CE>(_DischargeDispositionCode_QNAME, CE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsPatientRelationship }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "asPatientRelationship")
    public JAXBElement<AsPatientRelationship> createAsPatientRelationship(AsPatientRelationship value) {
        return new JAXBElement<AsPatientRelationship>(_AsPatientRelationship_QNAME, AsPatientRelationship.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link INT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "priorityNumber")
    public JAXBElement<INT> createPriorityNumber(INT value) {
        return new JAXBElement<INT>(_PriorityNumber_QNAME, INT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "birthTime")
    public JAXBElement<TS> createBirthTime(TS value) {
        return new JAXBElement<TS>(_BirthTime_QNAME, TS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "ethnicGroupCode")
    public JAXBElement<CE> createEthnicGroupCode(CE value) {
        return new JAXBElement<CE>(_EthnicGroupCode_QNAME, CE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "deceasedTime")
    public JAXBElement<TS> createDeceasedTime(TS value) {
        return new JAXBElement<TS>(_DeceasedTime_QNAME, TS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link INTPOS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "multipleBirthOrderNumber")
    public JAXBElement<INTPOS> createMultipleBirthOrderNumber(INTPOS value) {
        return new JAXBElement<INTPOS>(_MultipleBirthOrderNumber_QNAME, INTPOS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:sdtc", name = "multipleBirthInd")
    public JAXBElement<BL> createMultipleBirthInd(BL value) {
        return new JAXBElement<BL>(_MultipleBirthInd_QNAME, BL.class, null, value);
    }

}
