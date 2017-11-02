<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:java="http://xml.apache.org/xslt/java" 
exclude-result-prefixes="java">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/ | @* | node()">
    <ClinicalDocument xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="urn:hl7-org:v3" xmlns:cda="urn:hl7-org:v3" xmlns:sdtc="urn:hl7-org:sdtc" >
      <!--
          ********************************************************
            CDA Header
          ********************************************************
            -->
      <realmCode code="US"/>
      <typeId root="2.16.840.1.113883.1.3" extension="POCD_HD000040"/>
      <!-- US General Header Template -->
      <templateId root="2.16.840.1.113883.10.20.22.1.1"/>
      <!-- *** Note:  The next templateId, code and title will differ depending on what type of document is being sent. *** -->
      <!-- conforms to the document specific requirements  -->
      <templateId root="2.16.840.1.113883.10.20.22.1.4"/>
      <!--put extension = doc id from database/self generated here. Should be unique & alphanumerid-->
      <id root="2.16.840.1.113883.19.5.99999.1">
      	<xsl:attribute name="extension">
           <xsl:value-of select="normalize-space(/CDAQuery/@DocumentID)"/>
         </xsl:attribute>
       </id>
      <!--<xsl:variable name="now" select="current-dateTime()" />-->
      <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="34133-9" displayName="Summarization of episode note"/>
      <title>2014 Consolidated CDA</title>
      <effectiveTime>
        <xsl:attribute name="value">
          <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('yyyyMMddHHmmss'), java:java.util.Date.new())" />
        </xsl:attribute>
      </effectiveTime>
      <confidentialityCode code="N" codeSystem="2.16.840.1.113883.5.25"/>
      <languageCode code="en-US"/>
      <setId extension="sTT988" root="2.16.840.1.113883.19.5.99999.19"/>
      <versionNumber value="1"/>
      <recordTarget>
        <patientRole>
          <id root="2.16.840.1.113883.19.5.99999.2">
            <xsl:attribute name="extension">
              <xsl:value-of select="normalize-space(//CASEId)"/>
            </xsl:attribute>
          </id>
          <!-- SSN -->
          <xsl:variable name="ssn" select="normalize-space(//SSN)"/>
          <xsl:if test="$ssn != ''">
            <xsl:element name="id">
              <xsl:attribute name="extension">
                <xsl:value-of select="$ssn"/>
              </xsl:attribute>
              <xsl:attribute name="root">
                <xsl:value-of select="'2.16.840.1.113883.4.1'"/>
              </xsl:attribute>
            </xsl:element>
          </xsl:if>
          <xsl:choose>
            <xsl:when test="count(//Patient/Address) &gt; 0">
              <xsl:for-each select="//Patient/Address">
                <xsl:element name="addr">
                  <xsl:attribute name="use">
                    <xsl:choose>
                      <xsl:when test="//Address/Type = 'H'">HP</xsl:when>
                      <xsl:otherwise>WP</xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
                  <xsl:variable name="address1" select="normalize-space(./Address1)"/>
                  <xsl:variable name="address2" select="normalize-space(./Address2)"/>
                  <xsl:variable name="city" select="normalize-space(./City)"/>
                  <xsl:variable name="state" select="normalize-space(./State)"/>
                  <xsl:variable name="postalCode" select="normalize-space(./Zip)"/>
                  <xsl:variable name="country" select="normalize-space(./Country)"/>
                  <xsl:if test="$address1 != ''">
                    <xsl:element name="streetAddressLine">
                      <xsl:value-of select="$address1"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="$address2 != ''">
                    <xsl:element name="streetAddressLine">
                      <xsl:value-of select="$address2"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="$city != ''">
                    <xsl:element name="city">
                      <xsl:value-of select="$city"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="$state != ''">
                    <xsl:element name="state">
                      <xsl:value-of select="$state"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="$postalCode != ''">
                    <xsl:element name="postalCode">
                      <xsl:value-of select="$postalCode"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="$country != ''">
                    <xsl:element name="country">
                      <xsl:value-of select="$country"/>
                    </xsl:element>
                  </xsl:if>
                </xsl:element>
              </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              <addr nullFlavor="NI"/>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:choose>
            <xsl:when test="count(//Patient/Phone) &gt; 0">
              <xsl:for-each select="//Patient/Phone">
                <xsl:choose>
                  <xsl:when test="normalize-space(./PhoneNumber/text()) !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="concat('tel:',./PhoneNumber/text())"/>
                    </xsl:attribute>
                    <xsl:attribute name="use">
                      <xsl:choose>
                        <xsl:when test="./Type = 'H'">HP</xsl:when>
                        <xsl:otherwise>WP</xsl:otherwise>
                      </xsl:choose>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              <telecom nullFlavor="NI"/>
            </xsl:otherwise>
          </xsl:choose>
          <patient>
            <xsl:element name="name">
              <xsl:element name="family">
                <xsl:value-of select="//Patient/LastName"/>
              </xsl:element>
              <xsl:element name="given">
                <xsl:value-of select="//Patient/FirstName"/>
              </xsl:element>
            </xsl:element>
            <administrativeGenderCode codeSystem="2.16.840.1.113883.5.1">
              <xsl:choose>
                <xsl:when test="//Patient/Gender/Code !='' and (//Patient/Gender/Code = 'M' or //Patient/Gender/Code = 'F' or //Patient/Gender/Code = 'UN')">
                  <xsl:attribute name="code">
                    <xsl:value-of select="//Patient/Gender/Code"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="code"/>
                </xsl:otherwise>
              </xsl:choose>
              <xsl:attribute name="displayName">
                <xsl:value-of select="//Patient/Gender/Name"/>
              </xsl:attribute>
            </administrativeGenderCode>
            <xsl:element name="birthTime">
              <xsl:attribute name="value">
                <xsl:value-of select="//Patient/BirthDate"/>
              </xsl:attribute>
            </xsl:element>
            <xsl:if test="normalize-space(//Patient/MaritalStatus/Code/text()) != ''">
              <maritalStatusCode codeSystem="2.16.840.1.113883.5.2" codeSystemName="MaritalStatusCode">
                <xsl:attribute name="code">
                  <xsl:value-of select="//Patient/MaritalStatus/Code/text()"/>
                </xsl:attribute>
                <xsl:attribute name="displayName">
                  <xsl:value-of select="//Patient/MaritalStatus/Name/text()"/>
                </xsl:attribute>
              </maritalStatusCode>
            </xsl:if>
            <xsl:if test="count(//Patient/Race) &gt; 0">
              <raceCode codeSystem="2.16.840.1.113883.6.238" codeSystemName="Race and Ethnicity - CDC">
                <xsl:attribute name="code">
                  <xsl:value-of select="//Patient/Race/Code"/>
                </xsl:attribute>
                <xsl:attribute name="displayName">
                  <xsl:value-of select="//Patient/Race/Name"/>
                </xsl:attribute>
              </raceCode>
            </xsl:if>
            <xsl:choose>
              <xsl:when test="count(//Patient/Ethnicity) &gt; 0">
                <ethnicGroupCode codeSystem="2.16.840.1.113883.6.238" codeSystemName="Race and Ethnicity - CDC">
                  <xsl:attribute name="code">
                    <xsl:value-of select="//Patient/Ethnicity/Code"/>
                  </xsl:attribute>
                  <xsl:attribute name="displayName">
                    <xsl:value-of select="//Patient/Ethnicity/Name"/>
                  </xsl:attribute>
                </ethnicGroupCode>
              </xsl:when>
              <xsl:otherwise>
                <ethnicGroupCode code = "2186-5" displayName = "Not Hispanic or Latino" codeSystem = "2.16.840.1.113883.6.238" codeSystemName = "Race &amp; Ethnicity - CDC"/>
              </xsl:otherwise>
            </xsl:choose>
            <languageCommunication>
              <languageCode code="eng" />
            </languageCommunication>
          </patient>
          <xsl:if test="count(//Patient/Provider) &gt; 0">
            <providerOrganization>
              <id root="2.16.840.1.113883.4.6">
                <xsl:if test="normalize-space(//Provider/FacilityId/text()) != ''">
                  <xsl:attribute name="extension">
                    <xsl:value-of select="//Provider/FacilityId/text()"/>
                  </xsl:attribute>
                </xsl:if>
              </id>
              <xsl:element name="name">
                <xsl:value-of select="//Provider/FacilityName/text()"/>
              </xsl:element>
              <telecom use="WP">
                <xsl:choose>
                  <xsl:when test="normalize-space(//Provider/Telephone/text()) !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="concat('tel:', //Provider/Telephone/text())"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </telecom>
              <xsl:for-each select="//Provider/Address">
                <addr use="WP">
                  <xsl:element name="streetAddressLine">
                    <xsl:choose>
                      <xsl:when test="normalize-space(./Address1/text()) !=''">
                        <xsl:value-of select="./Address1/text()"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:choose>
                    <xsl:when test="normalize-space(./Address2/text()) !=''">
                      <xsl:element name="streetAddressLine">
                        <xsl:value-of select="normalize-space(./Address2/text())"/>
                      </xsl:element>
                    </xsl:when>
                  </xsl:choose>
                  <xsl:element name="city">
                    <xsl:choose>
                      <xsl:when test="normalize-space(./City/text()) !=''">
                        <xsl:value-of select="normalize-space(./City/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:element name="state">
                    <xsl:choose>
                      <xsl:when test="normalize-space(./State/text()) !=''">
                        <xsl:value-of select="normalize-space(./State/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:element name="postalCode">
                    <xsl:choose>
                      <xsl:when test="normalize-space(./Zip/text()) !=''">
                        <xsl:value-of select="normalize-space(./Zip/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:element name="country">
                    <xsl:choose>
                      <xsl:when test="normalize-space(./Country/text()) !=''">
                        <xsl:value-of select="normalize-space(./Country/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                </addr>
              </xsl:for-each>
            </providerOrganization>
          </xsl:if>
        </patientRole>
      </recordTarget>
      <author>
        <xsl:element name="time">
          <xsl:attribute name="value">
            <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('yyyyMMddHHmmss'), java:java.util.Date.new())" />
          </xsl:attribute>
        </xsl:element>
        <assignedAuthor>
          <xsl:choose>
            <xsl:when test="//Agency/AgencyName != ''">
              <xsl:element name="id">
                <xsl:attribute name="extension">
                  <xsl:value-of select="//Agency/AgencyName/text()" />
                </xsl:attribute>
                <xsl:attribute name="root">
                  <xsl:value-of select="'2.16.840.1.113883.3.1141'" />
                </xsl:attribute>
              </xsl:element>
            </xsl:when>
            <xsl:otherwise>
              <xsl:element name="id">
                <xsl:attribute name="nullFlavor">
                  <xsl:value-of select="'NI'"/>
                </xsl:attribute>
              </xsl:element>
            </xsl:otherwise>
          </xsl:choose>
          <addr use="WP">
            <streetAddressLine><xsl:value-of select="//Agency/Address/Address1/text()" /></streetAddressLine>
            <city><xsl:value-of select="//Agency/Address/City/text()" /></city>
            <state><xsl:value-of select="//Agency/Address/State/text()" /></state>
            <postalCode><xsl:value-of select="//Agency/Address/Zip/text()" /></postalCode>
            <country><xsl:value-of select="//Agency/Address/Country/text()" /></country>
          </addr>
          <xsl:element name="telecom">
            <xsl:choose>
              <xsl:when test="normalize-space(//Agency/AgencyTelephone/text()) !=''">
                <xsl:attribute name="value">
                  <xsl:value-of select="concat('tel:', //Agency/AgencyTelephone/text())"/>
                </xsl:attribute>
                <xsl:attribute name="use">
                  <xsl:value-of select="'WP'"/>
                </xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="nullFlavor">
                  <xsl:value-of select="'NI'"/>
                </xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
          <assignedAuthoringDevice>
            <manufacturerModelName>Essentia</manufacturerModelName>
            <softwareName>Essentia</softwareName>
          </assignedAuthoringDevice>
        </assignedAuthor>
	  </author>
      <custodian>
        <assignedCustodian>
          <representedCustodianOrganization>
            <xsl:element name="id">
		<xsl:attribute name="extension">
			<xsl:value-of select="//Agency/AgencyName/text()" />
		</xsl:attribute>
		<xsl:attribute name="root">
			<xsl:value-of select="'2.16.840.1.113883.3.1141'" />
		</xsl:attribute>
		</xsl:element>
            <name><xsl:value-of select="//Agency/AgencyName/text()" /></name>
             <xsl:element name="telecom">
		  <xsl:attribute name="use">
			<xsl:value-of select="'WP'"/>
			</xsl:attribute>
			<xsl:attribute name="value">
			<xsl:value-of select="//Agency/AgencyTelephone/text()" />
			</xsl:attribute>
		  </xsl:element>
            <addr use="WP">
              <xsl:element name="streetAddressLine">
                <xsl:choose>
                  <xsl:when test="normalize-space(//Agency/Address/Address1/text()) !=''">
                    <xsl:value-of select="normalize-space(//Agency/Address/Address1/text())"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
              <xsl:element name="city">
                <xsl:choose>
                  <xsl:when test="normalize-space(//Agency/Address/City/text()) !=''">
                    <xsl:value-of select="normalize-space(//Agency/Address/City/text())"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
              <xsl:element name="state">
                <xsl:choose>
                  <xsl:when test="normalize-space(//Agency/Address/State/text()) !=''">
                    <xsl:value-of select="normalize-space(//Agency/Address/State/text())"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
              <xsl:element name="postalCode">
                <xsl:choose>
                  <xsl:when test="normalize-space(//Agency/Address/Zip/text()) !=''">
                    <xsl:value-of select="normalize-space(//Agency/Address/Zip/text())"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
              <xsl:element name="country">
                <xsl:choose>
                  <xsl:when test="normalize-space(//Agency/Address/Country/text()) !=''">
                    <xsl:value-of select="normalize-space(//Agency/Address/Country/text())"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </addr>
          </representedCustodianOrganization>
        </assignedCustodian>
      </custodian>
      <xsl:variable name="providerfacId" select="normalize-space(//Agency/AgencyId)"/>
      <xsl:variable name="providerfacname" select="normalize-space(//Agency/AgencyName)"/>
      <xsl:variable name="provideraddr1" select="normalize-space(//Agency/Address/Address1)"/>
      <xsl:variable name="provideraddr2" select="normalize-space(//Agency/Address/Address2)"/>
      <xsl:variable name="providercity" select="normalize-space(//Agency/Address/City)"/>
      <xsl:variable name="providerstate" select="normalize-space(//Agency/Address/State)"/>
      <xsl:variable name="providerzip" select="normalize-space(//Agency/Address/Zip)"/>
      <xsl:variable name="providercountry" select="normalize-space(//Agency/Address/Country)"/>
      <xsl:variable name="providerphone" select="concat('tel:',//Agency/AgencyTelephone)"/>
      <xsl:variable name="providerfname" select="normalize-space(//EncounterDiagnosis/Performer/FirstName)"/>
      <xsl:variable name="providerlname" select="normalize-space(//EncounterDiagnosis/Performer/LastName)"/>
      <xsl:variable name="visitDate" select="normalize-space(//EncounterDiagnosis/EffectiveDate)"/>
      <xsl:variable name="visitendDate" select="normalize-space(//EncounterDiagnosis/ExpirationDate)"/>
      <xsl:variable name="visitTypeCode" select="normalize-space(//EncounterDiagnosis/Code)"/>
      <xsl:variable name="visitTypeName" select="normalize-space(//EncounterDiagnosis/Name)"/>
      <xsl:variable name="progTypeCode" select="normalize-space(//EncounterDiagnosis/ProgramType/Code)"/>
      <xsl:variable name="progTypeName" select="normalize-space(//EncounterDiagnosis/ProgramType/Name)"/>
      <xsl:variable name="reasonforvisit" select="normalize-space(//EncounterDiagnosis/ReasonForVisit)"/>
      <documentationOf>
        <serviceEvent classCode = "ACT">
          <id root = "1.2.840.113619.2.62.994044785528.114289542805"/>
          <id extension = "123453" root = "1.2.840.113619.2.62.994044785528.26"/>
          <code codeSystem = "2.16.840.1.113883.6.12" codeSystemName = "CPT4">
            <xsl:attribute name="code">
              <xsl:choose>
                <xsl:when test="$visitTypeCode!=''">
                  <xsl:value-of select="$visitTypeCode"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="'99214'"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="displayName">
              <xsl:choose>
                <xsl:when test="$visitTypeName !=''">
                  <xsl:value-of select="$visitTypeName"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="'Office Visit'"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
          </code>
          <effectiveTime>
            <low>
              <xsl:choose>
                <xsl:when test="$visitDate !=''">
                  <xsl:attribute name="value">
                    <xsl:value-of select="$visitDate"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="'NI'"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </low>
          </effectiveTime>
          <performer typeCode = "PRF">
            <templateId root = "2.16.840.1.113883.10.20.6.2.1"/>
            <assignedEntity>
              <id extension = "99999999" root = "2.16.840.1.113883.4.6"/>
              <code codeSystem = "2.16.840.1.113883.6.101" codeSystemName = "NUCC">
                <xsl:choose>
                  <xsl:when test="$progTypeCode != ''">
                    <xsl:attribute name="code">
                      <xsl:value-of select="$progTypeCode"/>
                    </xsl:attribute>
                    <xsl:attribute name="displayName">
                      <xsl:value-of select="$progTypeName"/>
                    </xsl:attribute>
                  </xsl:when>
                </xsl:choose>
              </code>
              <addr>
                <xsl:choose>
                  <xsl:when test="count(//Provider/Address) &gt; 0">
                    <streetAddressLine>
                      <xsl:choose>
                        <xsl:when test="$provideraddr1 !=''">
                          <xsl:value-of select="concat($provideraddr1,$provideraddr2)"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </streetAddressLine>
                    <city>
                      <xsl:choose>
                        <xsl:when test="$providercity !=''">
                          <xsl:value-of select="$providercity"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </city>
                    <state>
                      <xsl:choose>
                        <xsl:when test="$providerstate !=''">
                          <xsl:value-of select="$providerstate"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </state>
                    <postalCode>
                      <xsl:choose>
                        <xsl:when test="$providerzip !=''">
                          <xsl:value-of select="$providerzip"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </postalCode>
                    <country>
                      <xsl:choose>
                        <xsl:when test="$providercountry !=''">
                          <xsl:value-of select="$providercountry"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </country>
                  </xsl:when>
                  <xsl:otherwise>
                    <streetAddressLine nullFlavor='NI'/>
                    <city nullFlavor='NI'/>
                    <state nullFlavor='NI'/>
                    <postalCode nullFlavor='NI'/>
                    <country nullFlavor='NI'/>
                  </xsl:otherwise>
                </xsl:choose>
              </addr>
              <telecom use = "WP">
                <xsl:choose>
                  <xsl:when test="substring-after($providerphone,'tel:') !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="$providerphone"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </telecom>
              <assignedPerson>
                <name>
                  <given>
                    <xsl:choose>
                      <xsl:when test="$providerfname !=''">
                        <xsl:value-of select='$providerfname'/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </given>
                  <family>
                    <xsl:choose>
                      <xsl:when test="$providerlname !=''">
                        <xsl:value-of select='$providerlname'/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </family>
                </name>
              </assignedPerson>
            </assignedEntity>
          </performer>
        </serviceEvent>
      </documentationOf>
      <componentOf>
        <encompassingEncounter>
          <id root = "2.16.840.1.113883.19">
          	<xsl:attribute name="extension">
              <xsl:value-of select="normalize-space(//EncounterDiagnosis/Code)"/>
            </xsl:attribute>
          </id>
          <effectiveTime>
            <low>
              <xsl:choose>
                <xsl:when test="$visitDate !=''">
                  <xsl:attribute name="value">
                    <xsl:value-of select="$visitDate"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="'NI'"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </low>
            <high>
              <xsl:choose>
                <xsl:when test="$visitendDate !=''">
                  <xsl:attribute name="value">
                    <xsl:value-of select="$visitendDate"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="'NI'"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </high>
          </effectiveTime>
          <responsibleParty>
            <assignedEntity>
              <id extension = "99999999" root = "2.16.840.1.113883.4.6"/>
              <code code = "207QA0505X" codeSystem = "2.16.840.1.113883.6.101" codeSystemName = "NUCC" displayName = "Adult Medicine"/>
              <addr>
                <xsl:choose>
                  <xsl:when test="count(//Provider/Address) &gt; 0">
                    <streetAddressLine>
                      <xsl:choose>
                        <xsl:when test="$provideraddr1 !=''">
                          <xsl:value-of select="concat($provideraddr1,$provideraddr2)"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </streetAddressLine>
                    <city>
                      <xsl:choose>
                        <xsl:when test="$providercity !=''">
                          <xsl:value-of select="$providercity"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </city>
                    <state>
                      <xsl:choose>
                        <xsl:when test="$providerstate !=''">
                          <xsl:value-of select="$providerstate"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </state>
                    <postalCode>
                      <xsl:choose>
                        <xsl:when test="$providerzip !=''">
                          <xsl:value-of select="$providerzip"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </postalCode>
                    <country>
                      <xsl:choose>
                        <xsl:when test="$providercountry !=''">
                          <xsl:value-of select="$providercountry"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </country>
                  </xsl:when>
                  <xsl:otherwise>
                    <streetAddressLine nullFlavor='NI'/>
                    <city nullFlavor='NI'/>
                    <state nullFlavor='NI'/>
                    <postalCode nullFlavor='NI'/>
                    <country nullFlavor='NI'/>
                  </xsl:otherwise>
                </xsl:choose>
              </addr>
              <telecom use = "WP">
                <xsl:choose>
                  <xsl:when test="substring-after($providerphone,'tel:') !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="$providerphone"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </telecom>
              <representedOrganization>
                <name>
                  <xsl:choose>
                    <xsl:when test="$providerfacname !=''">
                      <xsl:value-of select="$providerfacname"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="'NA'"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </name>
              </representedOrganization>
            </assignedEntity>
          </responsibleParty>
          <encounterParticipant typeCode = "ATND">
            <time>
              <low>
                <xsl:choose>
                  <xsl:when test="$visitDate !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="$visitDate"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </low>
              <high>
                <xsl:choose>
                  <xsl:when test="$visitendDate !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="$visitendDate"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </high>
            </time>
            <assignedEntity>
              <id extension = "657654389" root = "2.16.840.1.113883.4.6"/>
              <code code = "207QA0505X" codeSystem = "2.16.840.1.113883.6.101" codeSystemName = "NUCC" displayName = "Adult Medicine"/>
              <addr nullFlavor="NI"/>
              <telecom use = "WP">
                <xsl:choose>
                  <xsl:when test="substring-after($providerphone,'tel:') !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="$providerphone"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'NI'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </telecom>
              <assignedPerson>
                <name>
                  <given>
                    <xsl:choose>
                      <xsl:when test="$providerfname !=''">
                        <xsl:value-of select='$providerfname'/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </given>
                  <family>
                    <xsl:choose>
                      <xsl:when test="$providerlname !=''">
                        <xsl:value-of select='$providerlname'/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'NI'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </family>
                </name>
              </assignedPerson>
            </assignedEntity>
          </encounterParticipant>
          <location>
            <healthCareFacility>
              <id extension = "9985647" root = "2.16.840.1.113883.19"/>
              <code code = "261QU0200X" codeSystem = "2.16.840.1.113883.6.101" codeSystemName = "NUCC" displayName = "Urgent Care"/>
              <location>
                <name>
                  <xsl:choose>
                    <xsl:when test="$providerfacname !=''">
                      <xsl:value-of select="$providerfacname"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="'NA'"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </name>
                <addr>
                  <xsl:choose>
                    <xsl:when test="count(//Provider/Address) &gt; 0">
                      <streetAddressLine>
                        <xsl:choose>
                          <xsl:when test="$provideraddr1 !=''">
                            <xsl:value-of select="concat($provideraddr1,$provideraddr2)"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </streetAddressLine>
                      <city>
                        <xsl:choose>
                          <xsl:when test="$providercity !=''">
                            <xsl:value-of select="$providercity"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </city>
                      <state>
                        <xsl:choose>
                          <xsl:when test="$providerstate !=''">
                            <xsl:value-of select="$providerstate"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </state>
                      <postalCode>
                        <xsl:choose>
                          <xsl:when test="$providerzip !=''">
                            <xsl:value-of select="$providerzip"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </postalCode>
                      <country>
                        <xsl:choose>
                          <xsl:when test="$providercountry !=''">
                            <xsl:value-of select="$providercountry"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </country>
                    </xsl:when>
                    <xsl:otherwise>
                      <streetAddressLine nullFlavor='NI'/>
                      <city nullFlavor='NI'/>
                      <state nullFlavor='NI'/>
                      <postalCode nullFlavor='NI'/>
                      <country nullFlavor='NI'/>
                    </xsl:otherwise>
                  </xsl:choose>
                </addr>
              </location>
            </healthCareFacility>
          </location>
        </encompassingEncounter>
      </componentOf>
      <!-- ********************************************************
          CDA Body
          ******************************************************** -->
      <component>
        <structuredBody>
          <!--
            ********************************************************
            Allergies, Adverse Reactions, Alerts
            ********************************************************
            -->
          <xsl:variable name="allergies" select="//Patient/Allergies"/>
          <component>
            <xsl:choose>
              <xsl:when test="count($allergies) &gt; 0">
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
                  <!-- Alerts section template -->
                  <code code="48765-2" codeSystem="2.16.840.1.113883.6.1"/>
                  <title>ALLERGIES, ADVERSE REACTIONS, ALERTS</title>
                  <text>
                    <table border="1" width="100%">
                      <thead>
                        <tr>
                          <th>Type</th>
                          <th>Substance</th>
                          <th>Reaction</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        <xsl:for-each select="$allergies">
                          <xsl:variable name="actualDisplay" select="./*[local-name()='Reaction']/*[local-name()='Name']"/>
                          <xsl:element name="tr">
                            <xsl:element name="td">
                              <xsl:attribute name="ID">
                                <xsl:value-of select="concat('ALGTYPE_',position())"/>
                              </xsl:attribute>
                              <xsl:choose>
                                <xsl:when test="./Name !=''">
                                  <xsl:value-of select="./Name"/>
                                </xsl:when>
                                <xsl:otherwise>
                                  Drug Allergy
                                </xsl:otherwise>
                              </xsl:choose>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:attribute name="ID">
                                <xsl:value-of select="concat('ALGSUB_',position())"/>
                              </xsl:attribute>
                              <xsl:if test="./Substance/Name != ''">
                                <xsl:value-of select="./Substance/Name"/>
                              </xsl:if>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:attribute name="ID">
                                <xsl:value-of select="concat('ALGREACT_',position())"/>
                              </xsl:attribute>
                              <xsl:if test="$actualDisplay != ''">
                                <xsl:value-of select="$actualDisplay"/>
                              </xsl:if>
                            </xsl:element>
                            <!-- Status -->
                            <xsl:element name="td">
                              <xsl:value-of select="./*[local-name()='Status']/*[local-name()='Name']"/>
                            </xsl:element>
                          </xsl:element>
                        </xsl:for-each>
                      </tbody>
                    </table>
                  </text>
                  <xsl:for-each select="$allergies">
                    <xsl:variable name="effectDT" select="normalize-space(./*[local-name()='EffectiveDate'])"/>
                    <xsl:variable name="expDT" select="normalize-space(./*[local-name()='ExpirationDate'])"/>
                    <entry typeCode="DRIV">
                      <act classCode="ACT" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
                        <!-- ** Allergy problem act ** -->
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code code="48765-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Allergies, adverse reactions, alerts"/>
                        <statusCode code="active"/>
                        <effectiveTime>
                          <xsl:element name="low">
                            <xsl:choose>
                              <xsl:when test="$effectDT != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$effectDT"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                          <xsl:choose>
                            <xsl:when test="expDT != ''">
                              <xsl:element name="high">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$expDT"/>
                                </xsl:attribute>
                              </xsl:element>
                            </xsl:when>
                            <xsl:otherwise>
                              <high nullFlavor="NI"/>
                            </xsl:otherwise>
                          </xsl:choose>
                        </effectiveTime>
                        <entryRelationship typeCode="SUBJ">
                          <observation classCode="OBS" moodCode="EVN">
                            <!-- allergy observation template -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
                            <id>
                              <xsl:attribute name="root">
                                <xsl:value-of select="generate-id()"/>
                              </xsl:attribute>
                            </id>
                            <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                            <statusCode code="completed"/>
                            <effectiveTime>
                              <xsl:element name="low">
                                <xsl:choose>
                                  <xsl:when test="$effectDT != ''">
                                    <xsl:attribute name="value">
                                      <xsl:value-of select="$effectDT"/>
                                    </xsl:attribute>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:attribute name="nullFlavor">
                                      <xsl:value-of select="'NI'"/>
                                    </xsl:attribute>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </xsl:element>
                            </effectiveTime>
                            <xsl:choose>
                              <xsl:when test="normalize-space(./Code) != ''">
                                <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT">
                                  <xsl:attribute name="code">
                                    <xsl:value-of select="normalize-space(./Code)"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:value-of select="normalize-space(./Name)"/>
                                  </xsl:attribute>
                                  <originalText>
                                    <xsl:element name="reference">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="concat('#ALGTYPE_',position())"/>
                                      </xsl:attribute>
                                    </xsl:element>
                                  </originalText>
                                </value>
                              </xsl:when>
                              <xsl:otherwise>
                                <value xsi:type = "CD" code = "419511003" displayName = "Propensity to adverse reactions to drug (disorder)" codeSystem = "2.16.840.1.113883.6.96" codeSystemName = "SNOMED CT">
                                  <originalText>
                                    <reference value = "#ALGTYPE_1"/>
                                  </originalText>
                                </value>
                              </xsl:otherwise>
                            </xsl:choose>
                            <xsl:if test="count(./Substance/Code) &gt; 0">
                              <participant typeCode="CSM">
                                <participantRole classCode="MANU">
                                  <playingEntity classCode="MMAT">
                                    <xsl:variable name="codeSystemName" select="normalize-space(./Substance/CodeSystem)"/>
                                    <xsl:element name="code">
                                      <xsl:attribute name="code">
                                        <xsl:value-of select="normalize-space(./Substance/Code)"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="displayName">
                                        <xsl:value-of select="normalize-space(./Substance/Name)"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="codeSystemName">
                                        <xsl:value-of select="$codeSystemName"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="codeSystem">
                                        <xsl:choose>
                                          <xsl:when test="$codeSystemName = 'RxNorm'">
                                            <xsl:value-of select="'2.16.840.1.113883.6.88'"/>
                                          </xsl:when>
                                          <xsl:when test="$codeSystemName = 'NDC'">
                                            <xsl:value-of select="'2.16.840.1.113883.6.69'"/>
                                          </xsl:when>
                                          <xsl:when test="$codeSystemName = 'ICD9'">
                                            <xsl:value-of select="'2.16.840.1.113883.6.103'"/>
                                          </xsl:when>
                                          <xsl:when test="$codeSystemName = 'UNII'">
                                            <xsl:value-of select="'2.16.840.1.113883.4.9'"/>
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <xsl:value-of select="'2.16.840.1.113883.6.88'"/>
                                          </xsl:otherwise>
                                        </xsl:choose>
                                      </xsl:attribute>
                                      <originalText>
                                        <xsl:element name="reference">
                                          <xsl:attribute name="value">
                                            <xsl:value-of select="concat('#ALGSUB_',position())"/>
                                          </xsl:attribute>
                                        </xsl:element>
                                      </originalText>
                                    </xsl:element>
                                  </playingEntity>
                                </participantRole>
                              </participant>
                            </xsl:if>
                            <xsl:if test="count(./Severity/Code) &gt; 2 and normalize-space(./Severity/Code/text()) !=''">
                              <!-- Severity of the allergy -->
                              <entryRelationship typeCode="SUBJ" inversionInd="true">
                                <observation classCode="OBS" moodCode="EVN">
                                  <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                                  <!-- ** Severity observation template ** -->
                                  <code code="SEV" displayName="Severity Observation" codeSystem="2.16.840.1.113883.5.4" codeSystemName="ActCode"/>
                                  <text>
                                    <xsl:element name="reference">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="concat('#ALGSEVE_',position())"/>
                                      </xsl:attribute>
                                    </xsl:element>
                                  </text>
                                  <statusCode code="completed"/>
                                  <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT">
                                    <xsl:element name="value">
                                      <xsl:choose>
                                        <xsl:when test="./Severity/Code != ''">
                                          <xsl:attribute name="code">
                                            <xsl:value-of select="./*[local-name()='Severity' and namespace-uri()='']/*[local-name()='Code' and namespace-uri()='']"/>
                                          </xsl:attribute>
                                          <xsl:attribute name="displayName">
                                            <xsl:value-of select="./*[local-name()='Severity' and namespace-uri()='']/*[local-name()='Name' and namespace-uri()='']"/>
                                          </xsl:attribute>
                                        </xsl:when>
                                        <xsl:otherwise>
                                          <xsl:attribute name="nullflavor">
                                            <xsl:value-of select="'NI'"/>
                                          </xsl:attribute>
                                        </xsl:otherwise>
                                      </xsl:choose>
                                    </xsl:element>
                                  </value>
                                </observation>
                              </entryRelationship>
                            </xsl:if>
                            <entryRelationship typeCode="SUBJ" inversionInd="true">
                              <observation classCode="OBS" moodCode="EVN">
                                <templateId root="2.16.840.1.113883.10.20.22.4.28"/>
                                <!-- Allergy status observation template -->
                                <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Status"/>
                                <statusCode code="completed"/>
                                <value xsi:type="CE" codeSystem="2.16.840.1.113883.6.96">
                                  <xsl:attribute name="displayName">
                                    <xsl:value-of select="normalize-space(./Status/Name)"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="code">
                                    <xsl:choose>
                                      <xsl:when test="./Status/Code != ''">
                                           <xsl:value-of select="normalize-space(./Status/Code)"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="55561003"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                </value>
                              </observation>
                            </entryRelationship>
                           <!-- <xsl:if test="count(./Reaction/Code) &gt; 0 and ./Reaction/Code !=''">-->
                              <entryRelationship typeCode="MFST" inversionInd="true">
                                <observation classCode="OBS" moodCode="EVN">
                                  <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                                  <!-- Reaction observation template -->
                                  <id root="4adc1020-7b14-11db-9fe1-0800200c9a64"/>
                                  <code code = "ASSERTION" codeSystem = "2.16.840.1.113883.5.4"/>
                                  <statusCode code="completed"/>
                                  <effectiveTime>
                                    <xsl:element name="low">
                                      <xsl:choose>
                                        <xsl:when test="$effectDT != ''">
                                          <xsl:attribute name="value">
                                            <xsl:value-of select="$effectDT"/>
                                          </xsl:attribute>
                                        </xsl:when>
                                        <xsl:otherwise>
                                          <xsl:attribute name="nullFlavor">
                                            <xsl:value-of select="'NI'"/>
                                          </xsl:attribute>
                                        </xsl:otherwise>
                                      </xsl:choose>
                                    </xsl:element>
                                  </effectiveTime>
                                  <value xsi:type="CD" code="UNK" codeSystem="2.16.840.1.113883.6.96">                                    
                                    <xsl:attribute name="displayName">
                                      <xsl:value-of select="./*[local-name()='Reaction' and namespace-uri()='']/*[local-name()='Name' and namespace-uri()='']"/>
                                    </xsl:attribute>
                                    <originalText>
                                      <xsl:element name="reference">
                                        <xsl:attribute name="value">
                                          <xsl:value-of select="concat('#ALGREACT_',position())"/>
                                        </xsl:attribute>
                                      </xsl:element>
                                    </originalText>
                                  </value>
                                </observation>
                              </entryRelationship>
                            <!--</xsl:if>-->
                          </observation>
                        </entryRelationship>
                      </act>
                    </entry>
                  </xsl:for-each>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor="NI">
                  <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
                  <!-- Alerts section template -->
                  <code code="48765-2" codeSystem="2.16.840.1.113883.6.1"/>
                  <title>ALLERGIES, ADVERSE REACTIONS, ALERTS</title>
                  <text ID="noallergies">No allergies or drug sensitivities.</text>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <!--
            ********************************************************
            Medication
            ********************************************************
            -->
          <component>
            <xsl:variable name="meds" select="//Medications"/>
            <xsl:choose>
              <xsl:when test="count($meds) &gt; 0">
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
                  <code code="10160-0" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="HISTORY OF MEDICATION USE"/>
                  <title>MEDICATIONS</title>
                  <text>
                    <table border="1" width="100%">
                      <thead>
                        <tr>
                          <th>Medication</th>
                          <th>Start Date</th>
                          <th>Route</th>
                          <th>Dose</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        <xsl:for-each select="$meds">
                          <xsl:variable name="medName" select="normalize-space(./Name/text())"/>
                          <xsl:variable name="strength" select="normalize-space(./Strength/text())"/>
                          <xsl:variable name="rxNorm" select="normalize-space(./Code/text())"/>
                          <xsl:variable name="route" select="normalize-space(./Route/Name/text())"/>
                          <xsl:variable name="form" select="normalize-space(./Form/text())"/>
                          <xsl:element name="tr">
                            <xsl:element name="td">
                              <xsl:element name="content">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('Med',position())"/>
                                </xsl:attribute>
                                <!-- TODO C# Helper to clean up this string. -->
                                <xsl:value-of select="concat($medName, ',',$strength,' ',./Strength/StrengthUnit,',',$route,',','[RxNorm:',$rxNorm,']')"/>
                              </xsl:element>
                            </xsl:element>
                            <xsl:element name="td">
                              <!-- TODO Change date format to user readable -->
                              <xsl:value-of select="./StartDate"/>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:value-of select="normalize-space(./Route/Name/text())"/>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:value-of select="normalize-space(concat(./Dose/text(),./DoseUnit/text()))"/>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:value-of select="normalize-space(./Status/text())"/>
                            </xsl:element>
                          </xsl:element>
                        </xsl:for-each>
                      </tbody>
                    </table>
                  </text>
                  <xsl:for-each select="$meds">
                    <xsl:variable name="medName" select="normalize-space(./Name/text())"/>
                    <xsl:variable name="strength" select="normalize-space(./Strength/text())"/>
                    <xsl:variable name="rxNorm" select="normalize-space(./Code/text())"/>
                    <xsl:variable name="route" select="normalize-space(./Route/Name/text())"/>
                    <xsl:variable name="form" select="./Form/text()"/>
                    <xsl:variable name="startDate" select="normalize-space(./StartDate)"/>
                    <xsl:variable name="endDate" select="normalize-space(./StopDate)"/>
                    <entry typeCode="DRIV">
                      <substanceAdministration classCode="SBADM" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.16"/>
                        <xsl:element name="id">
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </xsl:element>
                        <text>
                          <xsl:element name="reference">
                            <xsl:attribute name="value">
                              <xsl:value-of select="concat('#Med',position())"/>
                            </xsl:attribute>
                          </xsl:element>
                          <!-- TODO C# Helper to clean up this string. -->
                          <xsl:value-of select="normalize-space(concat($medName,' ' ,$strength,' ',$route,' ',$form,'[RxNorm: ',$rxNorm,']'))"/>
                        </text>
                        <statusCode code="completed"/>
                        <effectiveTime xsi:type="IVL_TS">
                          <xsl:element name="low">
                            <xsl:choose>
                              <xsl:when test="$startDate != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$startDate"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                          <xsl:element name="high">
                            <xsl:choose>
                              <xsl:when test="$endDate != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$endDate"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                        </effectiveTime>
                        <effectiveTime xsi:type="PIVL_TS" institutionSpecified="false" operator="A">
                          <period>
                            <xsl:choose>
                              <xsl:when test="./Frequency ='' or normalize-space(./FrequencyUnit/text()) =''">
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:when test="normalize-space(./Frequency/text()) !=''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="normalize-space(./Frequency/text())"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:when test="./FrequencyUnit !=''">
                                <xsl:attribute name="unit">
                                  <xsl:value-of select="normalize-space(./FrequencyUnit/text())"/>
                                </xsl:attribute>
                              </xsl:when>
                            </xsl:choose>
                          </period>
                        </effectiveTime>
                        <xsl:if test="count(./Route) &gt; 0 and  normalize-space(./Route/Code/text()) !=''">
                          <routeCode codeSystem="2.16.840.1.113883.3.26.1.1" codeSystemName="NCI Thesaurus">
                            <xsl:choose>
                              <xsl:when test="normalize-space(./Route/Code/text()) !=''">
                                <xsl:attribute name="code">
                                  <xsl:value-of select="normalize-space(./Route/Code/text())"/>
                                </xsl:attribute>
                                <xsl:attribute name="displayName">
                                  <xsl:value-of select="normalize-space(./Route/Name/text())"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </routeCode>
                        </xsl:if>
                        <doseQuantity>
                          <xsl:choose>
                            <xsl:when test="normalize-space(./Dose/text()) !='' and  normalize-space(./DoseUnit/text()) !=''">
                              <xsl:attribute name="value">
                                <xsl:value-of select="normalize-space(./Dose/text())"/>
                              </xsl:attribute>
                              <xsl:attribute name="unit">
                                <xsl:value-of select="normalize-space(./DoseUnit/text())"/>
                              </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:attribute name="nullFlavor">
                                <xsl:value-of select="'NI'"/>
                              </xsl:attribute>
                            </xsl:otherwise>
                          </xsl:choose>
                        </doseQuantity>
                        <consumable>
                          <manufacturedProduct classCode="MANU">
                            <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
                            <id root="ae514bd0-f976-40c5-95c2-6a5444456725"/>
                            <manufacturedMaterial>
                              <code codeSystem="2.16.840.1.113883.6.88" codeSystemName="RxNorm">
                                <xsl:choose>
                                  <xsl:when test="normalize-space(./Code/text()) != ''">
                                    <xsl:attribute name="code">
                                      <xsl:value-of select="normalize-space(./Code/text())"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="displayName">
                                      <xsl:value-of select="normalize-space($medName)"/>
                                    </xsl:attribute>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:attribute name="nullFlavor">
                                      <xsl:value-of select="'NI'"/>
                                    </xsl:attribute>
                                  </xsl:otherwise>
                                </xsl:choose>
                                <originalText>
                                  <xsl:element name="reference">
                                    <xsl:attribute name="value">
                                      <xsl:value-of select="concat('#Med',position())"/>
                                    </xsl:attribute>
                                  </xsl:element>
                                </originalText>
                              </code>
                              <name>
                                <xsl:value-of select="$medName"/>
                              </name>
                            </manufacturedMaterial>
                          </manufacturedProduct>
                        </consumable>
                      </substanceAdministration>
                    </entry>
                  </xsl:for-each>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor="NI">
                  <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
                  <code code="10160-0" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="HISTORY OF MEDICATION USE"/>
                  <title>MEDICATIONS</title>
                  <text>No Known Medication</text>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <!--
            ********************************************************
            PROBLEM LIST
            ********************************************************
            -->
          <xsl:variable name="problems" select="//Problems"/>
          <component>
            <xsl:choose>
              <xsl:when test="count($problems) &gt; 0">
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.5.1"/>
                  <code code="11450-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="PROBLEM LIST"/>
                  <title>PROBLEMS</title>
                  <text>
                    <content ID="problems"/>
                    <list listType="ordered">
                      <xsl:for-each select="$problems">
                        <xsl:variable name="problem" select="normalize-space(./Name/text())"/>
                        <xsl:variable name="status" select="normalize-space(./Status/text())"/>
                        <item>
                          <xsl:element name="content">
                            <xsl:attribute name="ID">
                              <xsl:value-of select="concat('problem',position())"/>
                            </xsl:attribute>
                            <xsl:value-of select="concat($problem, '[Status-',$status,']')"/>
                          </xsl:element>
                        </item>
                      </xsl:for-each>
                    </list>
                  </text>
                  <xsl:for-each select="$problems">
                    <entry typeCode="DRIV">
                      <act classCode="ACT" moodCode="EVN">
                        <!-- Problem act template -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.3"/>
                        <xsl:element name="id">
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </xsl:element>
                        <code code="CONC" codeSystem="2.16.840.1.113883.5.6" displayName="Concern"/>
                        <statusCode code="completed"/>
                        <effectiveTime>
                          <xsl:variable name="effDT" select="normalize-space(./EffectiveDate)"/>
                          <xsl:variable name="expDT" select="normalize-space(./ExpirationDate)"/>
                          <xsl:element name="low">
                            <xsl:choose>
                              <xsl:when test="$effDT != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$effDT"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                          <xsl:element name="high">
                            <xsl:choose>
                              <xsl:when test="$expDT != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$expDT"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                        </effectiveTime>
                        <entryRelationship typeCode="SUBJ">
                          <observation classCode="OBS" moodCode="EVN">
                            <!-- Problem observation template -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
                            <xsl:element name="id">
                              <xsl:attribute name="root">
                                <xsl:value-of select="generate-id()"/>
                              </xsl:attribute>
                            </xsl:element>
                            <code code="55607006" codeSystem="2.16.840.1.113883.6.96" displayName="Problem"/>
                            <text>
                              <xsl:element name="reference">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="concat('#problem',position())"/>
                                </xsl:attribute>
                              </xsl:element>
                            </text>
                            <statusCode code="completed"/>
                            <effectiveTime>
                              <xsl:variable name="effDT" select="normalize-space(./EffectiveDate)"/>
                              <xsl:element name="low">
                                <xsl:choose>
                                  <xsl:when test="$effDT != ''">
                                    <xsl:attribute name="value">
                                      <xsl:value-of select="$effDT"/>
                                    </xsl:attribute>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:attribute name="nullFlavor">
                                      <xsl:value-of select="'NI'"/>
                                    </xsl:attribute>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </xsl:element>
                            </effectiveTime>
                            <value xsi:type="CD">
                              <xsl:choose>
                                <xsl:when test="normalize-space(./Code) !=''">
                                  <xsl:attribute name="code">
                                    <xsl:value-of select="normalize-space(./Code)"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:value-of select="./Name"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="codeSystem">
                                    <xsl:choose>
                                      <xsl:when test="normalize-space(./CodeSystem) ='ICD9'">
                                        <xsl:value-of select="'2.16.840.1.113883.6.42'"/>
                                      </xsl:when>
                                      <xsl:when test="normalize-space(./CodeSystem) ='ICD10'">
                                        <xsl:value-of select="'2.16.840.1.113883.6.3'"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                  <xsl:attribute name="nullFlavor">
                                    <xsl:value-of select="'NI'"/>
                                  </xsl:attribute>
                                </xsl:otherwise>
                              </xsl:choose>
                            </value>
                            <entryRelationship typeCode="REFR">
                              <observation classCode="OBS" moodCode="EVN">
                                <!-- ** Problem status observation ** -->
                                <templateId root="2.16.840.1.113883.10.20.22.4.6"/>
                                <id root="ab1791b0-5c71-11db-b0de-0800200c9a66"/>
                                <code code="33999-4" codeSystem="2.16.840.1.113883.6.1" displayName="Status"/>
                                <statusCode code="completed"/>
                                <effectiveTime>
                                  <xsl:variable name="effDT" select="normalize-space(./EffectiveDate)"/>
                                  <xsl:element name="low">
                                    <xsl:choose>
                                      <xsl:when test="$effDT != ''">
                                        <xsl:attribute name="value">
                                          <xsl:value-of select="$effDT"/>
                                        </xsl:attribute>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:attribute name="nullFlavor">
                                          <xsl:value-of select="'NI'"/>
                                        </xsl:attribute>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:element>
                                </effectiveTime>
                                <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96">
                                  <xsl:variable name="status" select="normalize-space(./Status)"/>
                                  <xsl:attribute name="code">
                                    <xsl:choose>
                                      <xsl:when test="$status = 'Active'">
                                        <xsl:value-of select="'55561003'"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="'413322009'"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:value-of select="$status"/>
                                  </xsl:attribute>
                                </value>
                              </observation>
                            </entryRelationship>
                          </observation>
                        </entryRelationship>
                      </act>
                    </entry>
                  </xsl:for-each>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor='NI'>
                  <templateId root="2.16.840.1.113883.10.20.22.2.5.1"/>
                  <code code="11450-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="PROBLEM LIST"/>
                  <title>PROBLEMS</title>
                  <text>
                    <list listType="ordered">
                      <item>No Known Problem</item>
                    </list>
                  </text>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <!--
            ********************************************************
            SOCIAL HISTORY
            ********************************************************
            -->
          <component>
            <!--   Social History ******** -->
            <section>
              <templateId root="2.16.840.1.113883.10.20.22.2.17"/>
              <!--  ********  Social history section template   ******** -->
              <code code="29762-2" codeSystem="2.16.840.1.113883.6.1" displayName="Social History"/>
              <title>SOCIAL HISTORY</title>
              <text>
                <table border="1" width="100%">
                  <thead>
                    <tr>
                      <th>Social History Element</th>
                      <th>Description</th>
                      <th>Effective Dates</th>
                    </tr>
                  </thead>
                  <tbody>
                    <xsl:choose>
                      <xsl:when test="count(//Patient/SmokingStatus) &gt;0">
                        <xsl:for-each select="//Patient/SmokingStatus">
                          <tr>
                            <td>
                              <xsl:element name="content">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('soc',position())"/>
                                </xsl:attribute>
                              </xsl:element>
                              <xsl:value-of select="'Smoking Status'"/>
                            </td>
                            <td>
                              <xsl:variable name="description" select="normalize-space(./Name)"/>
                              <xsl:choose>
                                <xsl:when test="$description != ''">
                                  <xsl:value-of select="$description"/>
                                </xsl:when>
                                <xsl:otherwise>
                                  <xsl:value-of select="'none'"/>
                                </xsl:otherwise>
                              </xsl:choose>
                            </td>
                            <td>
                              <xsl:variable name="startDate" select="normalize-space(./StartDate/text())"/>
                              <xsl:variable name="endDate" select="normalize-space(./EndDate/text())"/>
                              <xsl:value-of select="concat($startDate, ' - ' ,$endDate)"/>
                            </td>
                          </tr>
                        </xsl:for-each>
                      </xsl:when>
                      <xsl:otherwise>
                        <tr>
                          <td colspan="3">
                            <xsl:value-of select="'Unknown if ever smoked'"/>
                          </td>
                        </tr>
                      </xsl:otherwise>
                    </xsl:choose>
                  </tbody>
                </table>
              </text>
              <xsl:variable name="smokingcode" select="normalize-space(//SmokingStatus/Code)"/>
              <xsl:variable name="smokingname" select="normalize-space(//SmokingStatus/Name)"/>
              <entry typeCode="DRIV">
                <observation classCode="OBS" moodCode="EVN">
                  <!-- Smoking status observation template -->
                  <templateId root="2.16.840.1.113883.10.20.22.4.78"/>
                  <id root="2.16.840.1.113883.19"/>
                  <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                  <statusCode code="completed"/>
                  <xsl:variable name="startDate" select="normalize-space(//SmokingStatus/StartDate)"/>
                  <xsl:variable name="endDate" select="normalize-space(//SmokingStatus/EndDate)"/>
				  <effectiveTime>                   
                      <xsl:choose>
                        <xsl:when test="$startDate != ''">
                          <xsl:attribute name="value">
                            <xsl:value-of select="$startDate"/>
                          </xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                  </effectiveTime>
                  <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96">
                    <xsl:choose>
                      <xsl:when test="$smokingcode != ''">
                        <xsl:attribute name="code">
                          <xsl:value-of select="$smokingcode"/>
                        </xsl:attribute>
                        <xsl:attribute name="displayName">
                          <xsl:value-of select="$smokingname"/>
                        </xsl:attribute>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="code">
                          <xsl:value-of select="'266927001'"/>
                        </xsl:attribute>
                        <xsl:attribute name="displayName">
                          <xsl:value-of select="'Unknown if ever smoked'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </value>
                </observation>
              </entry>
            </section>
          </component>
          <!--
            ********************************************************
            VITAL SIGNS
            ********************************************************
            -->
          <component>
            <xsl:choose>
              <xsl:when test="count(//VitalSigns) &gt; 0">
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.4.1"/>
                  <code code="8716-3" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="VITAL SIGNS"/>
                  <title>VITAL SIGNS</title>
                  <text>
                    <table border="1" width="100%">
                      <thead>
                        <tr>
                          <th>Date/Time: </th>
                          <th>Test</th>
                          <th>Result</th>
                        </tr>
                      </thead>
                      <tbody>
                        <xsl:for-each select="//VitalSigns">
                          <xsl:element name="tr">
                            <xsl:attribute name="ID">
                              <xsl:value-of select="concat('vit-Type', position())"/>
                            </xsl:attribute>
                            <xsl:element name="td">
                              <xsl:element name="content">
                                <xsl:value-of select="./RecordDate"/>
                              </xsl:element>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:element name="content">
                                <xsl:value-of select="concat(./Type,' ')"/>
                              </xsl:element>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:element name="content">
                                <xsl:value-of select="concat(./Value,' ',./Unit)"/>
                              </xsl:element>
                            </xsl:element>
                          </xsl:element>
                        </xsl:for-each>
                      </tbody>
                    </table>
                  </text>
                  <entry typeCode="DRIV">
                    <organizer classCode="CLUSTER" moodCode="EVN">
                      <templateId root="2.16.840.1.113883.10.20.22.4.26"/>
                      <!-- Vital signs organizer template -->
                      <id root="c6f88320-67ad-11db-bd13-0800200c9a66"/>
                      <code code="46680005" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Vital signs"/>
                      <statusCode code="completed"/>
                      <xsl:element name="effectiveTime">
                        <xsl:attribute name="value">
                          <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('yyyyMMddHHmmss'), java:java.util.Date.new())"/>
                        </xsl:attribute>
                      </xsl:element>
                      <xsl:for-each select="//VitalSigns">
                        <xsl:variable name="id" select="generate-id()"/>
                        <xsl:variable name="recordDT" select="./RecordDate/text()"/>
                        <xsl:variable name="type" select="./Type/text()"/>
                        <xsl:variable name="value" select="./Value/text()"/>
                        <xsl:variable name="unit" select="./Unit/text()"/>
                        <xsl:variable name="codeValue" select="./CodeValue/text()"/>
                        <xsl:variable name="codeType" select="./CodeType/text()"/>
                        <xsl:if test ="$codeValue !=''">
                          <component>
                            <observation classCode="OBS" moodCode="EVN">
                              <templateId root="2.16.840.1.113883.10.20.22.4.27"/>
                              <!-- Vital Sign Observation template -->
                              <xsl:element name="id">
                                <xsl:attribute name="root">
                                  <xsl:value-of select="$id"/>
                                </xsl:attribute>
                              </xsl:element>
                              <xsl:element name="code">
                                <xsl:attribute name="code">
                                  <xsl:value-of select="$codeValue"/>
                                </xsl:attribute>
                                <xsl:attribute name="displayName">
                                  <xsl:value-of select="$type"/>
                                </xsl:attribute>
                                <xsl:attribute name="codeSystemName">
                                  <xsl:value-of select="$codeType"/>
                                </xsl:attribute>
                                <xsl:attribute name="codeSystem">
                                  <xsl:value-of select="'2.16.840.1.113883.6.1'"/>
                                </xsl:attribute>
                              </xsl:element>
                              <!--<code code="8302-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Height"/>-->
                              <text>
                                <reference>
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="concat('#vit-Type', position())"/>
                                  </xsl:attribute>
                                </reference>
                              </text>
                              <statusCode code="completed"/>
                              <xsl:element name="effectiveTime">
                                <low>
                                  <xsl:choose>
                                    <xsl:when test="$recordDT !=''">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="$recordDT"/>
                                      </xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:attribute name="nullFlavor">
                                        <xsl:value-of select="'NI'"/>
                                      </xsl:attribute>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </low>
                              </xsl:element>
                              <xsl:choose>
								  <xsl:when test="$value !=''">
								    <value xsi:type="PQ">
								      <xsl:if test="$unit !=''">
								        <xsl:attribute name="unit">
								          <xsl:value-of select="$unit"/>
								        </xsl:attribute>
								      </xsl:if>
								      <xsl:attribute name="value">
								        <xsl:value-of select="$value"/>
								      </xsl:attribute>
								    </value>
								  </xsl:when>
								  <xsl:otherwise>
								    <value xsi:type="PQ">
								      <xsl:attribute name="nullFlavor">
								        <xsl:value-of select="'NI'"/>
								      </xsl:attribute>
								    </value>
								  </xsl:otherwise>
								</xsl:choose>
                              
                              <interpretationCode code="N" codeSystem="2.16.840.1.113883.5.83"/>
                            </observation>
                          </component>
                        </xsl:if>
                      </xsl:for-each>
                    </organizer>
                  </entry>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor='NI'>
                  <templateId root="2.16.840.1.113883.10.20.22.2.4.1"/>
                  <code code="8716-3" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="VITAL SIGNS"/>
                  <title>VITAL SIGNS</title>
                  <text>
                    <table border="1" width="100%">
                      <thead>
                        <tr>
                          <th>Date/Time: </th>
                          <th>Height</th>
                          <th>Weight</th>
                          <th>Blood Pressure</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td colspan="4">Not Specified</td>
                        </tr>
                      </tbody>
                    </table>
                  </text>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <!--
              ********************************************************
              RESULTS
              ********************************************************
              -->
          <xsl:variable name="results" select="//LabResults"/>
          <component>
            <xsl:choose>
              <xsl:when test="count($results) &gt; 0 and normalize-space(//LabResults/Code) !=''">
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.3.1"/>
                  <code code="30954-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="RESULTS"/>
                  <title>RESULTS</title>
                  <text>
                    <table>
                      <tbody>
                        <tr>
                          <td colspan="2">LABORATORY INFORMATION</td>
                        </tr>
                        <tr>
                          <td colspan="2">Chemistries and drug levels</td>
                        </tr>
                        <xsl:for-each select="$results">
                          <xsl:variable name="resultCount" select="position()"/>
                          <tr>
                            <td colspan="2">
                              <xsl:value-of select="./Name/text()"/>
                            </td>
                          </tr>
                          <xsl:for-each select="./Values">
                            <tr>
                              <td>
                                <content>
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('result',$resultCount,'-',position())"/>
                                  </xsl:attribute>
                                </content>
                                <xsl:variable name="ResultDescription" select="normalize-space(./Name/text())"/>
                                <xsl:variable name="ReferenceRange" select="normalize-space(./ReferenceRange/text())"/>
                                <xsl:variable name="Unit" select="normalize-space(./Unit/text())"/>
                                <xsl:value-of select="concat($ResultDescription, '(', $ReferenceRange, ' ', $Unit, ')') "/>
                              </td>
                              <td>
                                <xsl:value-of select="./Value"/>
                              </td>
                            </tr>
                          </xsl:for-each>
                        </xsl:for-each>
                      </tbody>
                    </table>
                  </text>
                  <xsl:for-each select="$results">
                    <xsl:variable name="recorddate" select="./RecordDate/text()"/>
                    <entry typeCode="DRIV">
                      <xsl:variable name="resultCount" select="position()"/>
                      <organizer classCode="BATTERY" moodCode="EVN">
                        <!-- Result organizer template -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.1"/>
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC">
                          <xsl:choose>
                            <xsl:when test="normalize-space(./Code/text()) !=''">
                              <xsl:attribute name="code">
                                <xsl:value-of select="normalize-space(./Code/text())"/>
                              </xsl:attribute>
                              <xsl:attribute name="displayName">
                                <xsl:value-of select="normalize-space(./Name/text())"/>
                              </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:attribute name="nullFlavor">
                                <xsl:value-of select="'NI'"/>
                              </xsl:attribute>
                            </xsl:otherwise>
                          </xsl:choose>
                        </code>
                        <statusCode code="completed"/>
                        <xsl:for-each select="./Values">
                          <component>
                            <observation classCode="OBS" moodCode="EVN">
                              <!-- Result observation template -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.2"/>
                              <id>
                                <xsl:attribute name="root">
                                  <xsl:value-of select="generate-id()"/>
                                </xsl:attribute>
                              </id>
                              <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC">
                                <xsl:attribute name="displayName">
                                  <xsl:value-of select="normalize-space(./Name/text())"/>
                                </xsl:attribute>
                                <xsl:attribute name="code">
                                  <xsl:value-of select="normalize-space(./Code/text())"/>
                                </xsl:attribute>
                              </code>
                              <text>
                                <reference>
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="concat('#result',$resultCount,'-',position())"/>
                                  </xsl:attribute>
                                </reference>
                              </text>
                              <statusCode code="completed"/>
                              <effectiveTime>
                                <low>
                                  <xsl:choose>
                                    <xsl:when test="./EffectiveDate/text() !=''">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="normalize-space(./EffectiveDate/text())"/>
                                      </xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:attribute name="nullFlavor">
                                        <xsl:value-of select="'NI'"/>
                                      </xsl:attribute>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </low>
                              </effectiveTime>
                              <value xsi:type="PQ">
                                <xsl:attribute name="value">
                                  <xsl:choose>
                                    <xsl:when test="./Value/text() !='' and number(./Value) = ./Value">
                                      <xsl:value-of select="./Value"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:value-of select="'0'"/>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:attribute>
                                <xsl:attribute name="unit">
                                  <xsl:choose>
                                    <xsl:when test="./Unit/text()">
                                      <xsl:value-of select="./Unit/text()"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:value-of select="'UNK'"/>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:attribute>
                              </value>
                              <xsl:variable name="abnormalFlag" select="normalize-space(./AbnormalFlag/text())"/>
                              <xsl:if test="$abnormalFlag != ''">
                                <interpretationCode codeSystem="2.16.840.1.113883.5.83">
                                  <xsl:attribute name="code">
                                    <xsl:value-of select="$abnormalFlag"/>
                                  </xsl:attribute>
                                </interpretationCode>
                              </xsl:if>
                              <referenceRange>
                                <observationRange>
                                  <xsl:element name="text">
                                    <xsl:variable name="ReferenceRange" select="normalize-space(./ReferenceRange/text())"/>
                                    <xsl:variable name="Unit" select="normalize-space(./Unit/text())"/>
                                    <xsl:value-of select="concat($ReferenceRange, ' ', $Unit)"/>
                                  </xsl:element>
                                </observationRange>
                              </referenceRange>
                            </observation>
                          </component>
                        </xsl:for-each>
                      </organizer>
                    </entry>
                  </xsl:for-each>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor="NI">
                  <templateId root="2.16.840.1.113883.10.20.22.2.3.1"/>
                  <code code="30954-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="RESULTS"/>
                  <title>RESULTS</title>
                  <text>No Known Lab Results</text>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <!-- ************************************
            Encounter  Diagnoses
            ************************************ -->
          <xsl:choose>
            <xsl:when test="count(//EncounterDiagnoses) &gt; 0">
              <component>
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.22.1"/>
                  <!-- Encounters Section - Entries optional -->
                  <code code="46240-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="History of encounters"/>
                  <title>Encounters</title>
                  <text>
                    <table border = "1" width = "100%">
                      <thead>
                        <tr>
                          <th>Encounter Diagnosis</th>
                          <th>Performer</th>
                          <th>Date</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>
                            <xsl:attribute name="ID">
                              <xsl:value-of select="concat('encproblem', position())"/>
                            </xsl:attribute>
                            <xsl:value-of select="concat(//EncounterDiagnoses/Name/text(),'[',//EncounterDiagnoses/CodeSystem/text(),' ',//EncounterDiagnoses/Code/text(),']')"/>
                          </td>
                          <td>
                            <xsl:value-of select="//EncounterDiagnosis/Performer"/>
                          </td>
                          <td>
                            <xsl:value-of select="//EncounterDiagnosis/EffectiveDate"/>
                          </td>
                          <td>
                            <xsl:value-of select="//EncounterDiagnosis/Status"/>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </text>
                  <xsl:if test="count(//EncounterDiagnoses) &gt; 0">
                    <entry typeCode="DRIV">
                      <encounter classCode="ENC" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.49"/>
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code code = "99241" displayName = "Office consultation - 15 minutes" codeSystemName = "CPT" codeSystem = "2.16.840.1.113883.6.12" codeSystemVersion = "4">
                          <originalText>
                            <xsl:element name="reference">
                              <xsl:attribute name="value">
                                <xsl:value-of select="concat('#encproblem', position())"/>
                              </xsl:attribute>
                            </xsl:element>
                          </originalText>
                        </code>
                        <effectiveTime>
                          <xsl:variable name="effDT" select="normalize-space(./EffectiveDate)"/>
                          <low>
                            <xsl:choose>
                              <xsl:when test="$effDT != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$effDT"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'NI'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </low>
                        </effectiveTime>
                        <xsl:if test="count(./Performer) &gt; 0">
                          <performer>
                            <assignedEntity>
                              <id root="2a620155-9d11-439e-92a3-5d9815ff4de8" />
                              <code  codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT">
                                <xsl:attribute name="code">
                                  <xsl:value-of select="./Performer/Code"/>
                                </xsl:attribute>
                                <xsl:attribute name="displayName">
                                  <xsl:value-of select="./Performer/Name"/>
                                </xsl:attribute>
                              </code>
                            </assignedEntity>
                          </performer>
                        </xsl:if>
                        <entryRelationship typeCode = "RSON">
                          <observation classCode = "OBS" moodCode = "EVN">
                            <templateId root = "2.16.840.1.113883.10.20.22.4.19"/>
                            <id root = "db734647-fc99-424c-a864-7e3cda82e703" extension = "45665"/>
                            <code code = "404684003" displayName = "Finding" codeSystem = "2.16.840.1.113883.6.96" codeSystemName = "SNOMED CT"/>
                            <statusCode code = "completed"/>
                            <effectiveTime>
                              <low>
                                <xsl:choose>
                                  <xsl:when test="./Problem/EffectiveDate !=''">
                                    <xsl:attribute name="value">
                                      <xsl:value-of select="./Problem/EffectiveDate"/>
                                    </xsl:attribute>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:attribute name="nullFlavor">
                                      <xsl:value-of select="'NI'"/>
                                    </xsl:attribute>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </low>
                            </effectiveTime>
                            <value xsi:type = "CD"  codeSystem = "2.16.840.1.113883.6.96">
                              <xsl:if test="count(./Problem/Code) &gt; 0">
                                <xsl:attribute name="code">
                                  <xsl:value-of select="./Problem/Code"/>
                                </xsl:attribute>
                                <xsl:attribute name="displayName">
                                  <xsl:value-of select="./Problem/Name"/>
                                </xsl:attribute>
                              </xsl:if>
                            </value>
                          </observation>
                        </entryRelationship>
                      </encounter>
                    </entry>
                  </xsl:if>
                </section>
              </component>
            </xsl:when>
          </xsl:choose>
          <xsl:variable name="referrals" select="normalize-space(//Referral/ReasonforReferral/text())"/>
          <xsl:variable name="referralAddress1" select="normalize-space(//Referral/Address/Address1/text())"/>
          <xsl:variable name="referralCity" select="normalize-space(//Referral/Address/City/text())"/>
          <xsl:variable name="referralState" select="normalize-space(//Referral/Address/State/text())"/>
          <xsl:variable name="referralZip" select="normalize-space(//Referral/Address/Zip/text())"/>
          <xsl:variable name="referralCountry" select="normalize-space(//Referral/Address/Country/text())"/>
          <xsl:variable name="referralTelePh" select="normalize-space(//Referral/Address/Telephone1/text())"/>
          <xsl:variable name="referralDate" select="normalize-space(//Referral/ScheduledDate/text())"/>
          <component>
            <section>
              <templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.1"/>
              <!-- ** Reason for Referral Section Template ** -->
              <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="42349-1" displayName="REASON FOR REFERRAL"/>
              <title>REASON FOR REFERRAL</title>
              <text>
                <xsl:choose>
                  <xsl:when test="count(//Referral) &gt; 0">
                    <xsl:for-each select="//Referral">
                      <xsl:element name="paragraph">
                        <xsl:value-of select="normalize-space($referrals)"/>
                      </xsl:element>
                      <xsl:element name="paragraph">
                        <xsl:value-of select="normalize-space(concat('Referred To - ',./ReferredTo/text(), ' on ', $referralDate))"/>
                      </xsl:element>
                      <xsl:element name="paragraph">
                        <xsl:value-of select="normalize-space(concat($referralAddress1,',',$referralCity,',', $referralState, ',',$referralCountry,',',$referralZip))"/>
                      </xsl:element>
                      <xsl:element name="paragraph">
                        <xsl:value-of select="normalize-space(concat('Ph:',$referralTelePh))"/>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="paragraph">
                      <xsl:value-of select="'No Referrals'"/>
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
              </text>
            </section>
          </component>
          <!--
            ********************************************************
            Immunization
            ********************************************************
            -->
          <component>
            <xsl:choose>
              <xsl:when test="count(//Immunizations) &gt; 0">
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.2"/>
                  <templateId root = "2.16.840.1.113883.10.20.22.2.2.1"/>
                  <!--  ********  Immunizations section template   ******** -->
                  <code code="11369-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="History of immunizations"/>
                  <title>Immunizations</title>
                  <text>
                    <table border="1" width="100%">
                      <thead>
                        <tr>
                          <th>Vaccine</th>
                          <th>Date</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        <xsl:for-each select="//Immunizations">
                          <tr>
                            <td>
                              <content ID="immun1">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select=" concat('immun', position())"/>
                                </xsl:attribute>
                              </content>
                              <xsl:value-of select="./Name/text()"/>
                            </td>
                            <td>
                              <!-- TODO Get string date like Dec 1, 2013-->
                              <xsl:value-of select="./Date/text()"/>
                            </td>
                            <td>
                              <xsl:choose>
                                <xsl:when test="normalize-space(./DeclineReason/text()) != ''">
                                  <xsl:value-of select="normalize-space(./DeclineReason/text())"/>
                                </xsl:when>
                                <xsl:otherwise>Completed</xsl:otherwise>
                              </xsl:choose>
                            </td>
                          </tr>
                        </xsl:for-each>
                      </tbody>
                    </table>
                  </text>
                  <xsl:for-each select="//Immunizations">
                    <entry typeCode="DRIV">
                      <substanceAdministration classCode="SBADM" moodCode="EVN">
                        <xsl:attribute name="negationInd">
                          <xsl:choose>
                            <xsl:when test="normalize-space(./DeclineReason/text()) != ''">true</xsl:when>
                            <xsl:otherwise>false</xsl:otherwise>
                          </xsl:choose>
                        </xsl:attribute>
                        <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
                        <!--  ****  Immunization activity template  **** -->
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <text>
                          <reference>
                            <xsl:attribute name="value">
                              <xsl:value-of select="concat('#immun', position())"/>
                            </xsl:attribute>
                          </reference>
                        </text>
                        <statusCode code="completed"/>
                        <effectiveTime xsi:type="IVL_TS">
                          <xsl:choose>
                            <xsl:when test="./Date/text() != ''">
                              <xsl:attribute name="value">
                                <xsl:value-of select="./Date/text()"/>
                              </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:attribute name="nullFlavor">
                                <xsl:value-of select="'NI'"/>
                              </xsl:attribute>
                            </xsl:otherwise>
                          </xsl:choose>
                        </effectiveTime>
                        <xsl:if test="count(./Route) &gt; 0 and ./Route/Code !=''">
                          <routeCode codeSystem="2.16.840.1.113883.3.26.1.1" codeSystemName="National Cancer Institute (NCI) Thesaurus" displayName="Intramuscular injection">
                            <xsl:attribute name="code">
                              <xsl:value-of select="./Route/Code"/>
                            </xsl:attribute>
                            <xsl:attribute name="displayName">
                              <xsl:value-of select="./Route/Name"/>
                            </xsl:attribute>
                          </routeCode>
                        </xsl:if>
                        <doseQuantity unit="ml">
                          <xsl:choose>
                            <xsl:when test="normalize-space(./Dose/text()) !=''">
                              <xsl:attribute name="value">
                                <xsl:value-of select="normalize-space(./Dose/text())"/>
                              </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:attribute name="nullFlavor">
                                <xsl:value-of select="'UNK'"/>
                              </xsl:attribute>
                            </xsl:otherwise>
                          </xsl:choose>
                        </doseQuantity>
                        <consumable>
                          <manufacturedProduct classCode="MANU">
                            <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
                            <!-- ******** Immunization  Information ******** -->
                            <xsl:variable name="immzcode">
                              <xsl:choose>
                                <xsl:when test="string-length(./Code/text()) = 1">
                                  <xsl:value-of select="concat('0',./Code/text())"/>
                                </xsl:when>
                                <xsl:otherwise>
                                  <xsl:value-of select="./Code/text()"/>
                                </xsl:otherwise>
                              </xsl:choose>
                            </xsl:variable>
                            <manufacturedMaterial>
                              <code codeSystem="2.16.840.1.113883.12.292" codeSystemName="CVX">
                                <xsl:attribute name="code">
                                  <xsl:choose>
                                    <xsl:when test="normalize-space(./Code/text()) != ''">
                                      <xsl:value-of select="$immzcode"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:value-of select="'Nullflavor'"/>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:attribute>
                                <xsl:attribute name="displayName">
                                  <xsl:choose>
                                    <xsl:when test="normalize-space(./Name/text()) != ''">
                                      <xsl:value-of select="normalize-space(./Name/text())"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:value-of select="'UNK'"/>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:attribute>
                              </code>
                              <xsl:if test="normalize-space(./LotNumber) !=''">
                                <lotNumberText>
                                  <xsl:value-of select="normalize-space(./LotNumber/text())"/>
                                </lotNumberText>
                              </xsl:if>
                            </manufacturedMaterial>
                          </manufacturedProduct>
                        </consumable>
                      </substanceAdministration>
                    </entry>
                  </xsl:for-each>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor='NI'>
                  <templateId root="2.16.840.1.113883.10.20.22.2.2"/>
                  <templateId root = "2.16.840.1.113883.10.20.22.2.2.1"/>
                  <!--  ********  Immunizations section template   ******** -->
                  <code code="11369-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="History of immunizations"/>
                  <title>Immunizations</title>
                  <text>
                    <table border="1" width="100%">
                      <thead>
                        <tr>
                          <th>Vaccine</th>
                          <th>Date</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td colspan="3">
                            <content ID="immun1">
                              <xsl:attribute name="ID">
                                <xsl:value-of select="'immun1'"/>
                              </xsl:attribute>
                            </content>
                            <xsl:value-of select="'No Vaccines Reported'"/>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </text>
                  <entry typeCode="DRIV">
                    <substanceAdministration classCode = "SBADM" moodCode = "EVN" negationInd = "false">
                      <templateId root = "2.16.840.1.113883.10.20.22.4.52"/>
                      <id >
                        <xsl:attribute name="root">
                          <xsl:value-of select="generate-id()"/>
                        </xsl:attribute>
                      </id>
                      <text>
                        <reference value = "#immun1"/>
                      </text>
                      <statusCode code = "completed"/>
                      <effectiveTime nullFlavor="NI"/>
                      <doseQuantity nullFlavor = "NI"/>
                      <consumable>
                        <manufacturedProduct classCode = "MANU">
                          <templateId root = "2.16.840.1.113883.10.20.22.4.54"/>
                          <manufacturedMaterial>
                            <code nullFlavor="NI">
                              <originalText>
                                <reference value = "#immun1"/>
                                No Vaccines reported
                              </originalText>
                            </code>
                          </manufacturedMaterial>
                        </manufacturedProduct>
                      </consumable>
                    </substanceAdministration>
                  </entry>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <component>
            <section>
              <templateId root = "2.16.840.1.113883.10.20.22.2.13"/>
              <code code = "46239-0" codeSystem = "2.16.840.1.113883.6.1" codeSystemName = "LOINC" displayName = "CHIEF COMPLAINT AND REASON FOR VISIT"/>
              <title>CHIEF COMPLAINT</title>
              <text>
                <table border = "1" width = "100%">
                  <thead>
                    <tr>
                      <th>Reason for Visit/Chief Complaint</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>
                        <paragraph>
                          <xsl:choose>
                            <xsl:when test="$reasonforvisit !=''">
                              <xsl:value-of select="$reasonforvisit"/>
                            </xsl:when>
                            <xsl:when test="normalize-space(//ReasonForVisit) !=''">
                              <xsl:value-of select="normalize-space(//ReasonForVisit)"/>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:value-of select="'No reason for visit/chief complaints specified'"/>
                            </xsl:otherwise>
                          </xsl:choose>
                        </paragraph>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </text>
            </section>
          </component>
          <!--
            ********************************************************
            Functional Status
            ********************************************************
            -->
          <component>
            <section>
              <xsl:if test="count(//FunctionalStatus) &gt; 0 and count(//CognitiveStatus) &gt; 0">
                <xsl:attribute name="nullFlavor">
                  <xsl:value-of select="'NI'"/>
                </xsl:attribute>
              </xsl:if>
              <templateId root="2.16.840.1.113883.10.20.22.2.14"/>
              <!--**** Functional status section template **** -->
              <code code="47420-5" codeSystem="2.16.840.1.113883.6.1"/>
              <title>FUNCTIONAL STATUS</title>
              <text>
                <table border="1" width="100%">
                  <thead>
                    <tr>
                      <th>Type</th>
                      <th>Date</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <xsl:if test="count(//FunctionalStatus) &gt; 0">
                      <xsl:for-each select="//FunctionalStatus">
                        <tr>
                          <td>
                            <content>
                              <xsl:attribute name="ID">
                                <xsl:value-of select="concat('function', position())"/>
                              </xsl:attribute>
                            </content>
                            <xsl:value-of select="concat(./Type, ./Code)"/>
                          </td>
                          <td>
                            <xsl:value-of select="./Date/text()"/>
                          </td>
                          <td>
                            <xsl:value-of select="./Status/text()"/>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </xsl:if>
                    <xsl:if test="count(//CognitiveStatus) &gt; 0">
                      <xsl:for-each select="//CognitiveStatus">
                        <tr>
                          <td>
                            <content ID="cognitive_status_1">
                              <xsl:attribute name="ID">
                                <xsl:value-of select="concat('cognitive', position())"/>
                              </xsl:attribute>
                            </content>
                            <xsl:value-of select="concat(./Type, ./Code)"/>
                          </td>
                          <td>
                            <xsl:value-of select="./Date/text()"/>
                          </td>
                          <td>
                            <xsl:value-of select="./Status/text()"/>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </xsl:if>
                    <xsl:if test="count(//FunctionalStatus) &lt; 1 and count(//CognitiveStatus) &lt; 1">
                      <tr>
                        <td>
                          <content ID="cognitive1"/>No known cognitive/functional status
                        </td>
                      </tr>
                    </xsl:if>
                  </tbody>
                </table>
              </text>
              <xsl:for-each select="//CognitiveStatus">
                <entry typeCode="DRIV">
                  <observation classCode="OBS" moodCode="EVN">
                    <templateId root="2.16.840.1.113883.10.20.22.4.73"/>
                    <!--**** Cognitive Status Problem Observation template **** -->
                    <id root="24dda93c-dcdf-4b42-a30e-84481af75c2f"/>
                    <xsl:element name="id">
                      <xsl:attribute name="root">
                        <xsl:value-of select="generate-id()"/>
                      </xsl:attribute>
                    </xsl:element>
                    <code code="373930000" codeSystem="2.16.840.1.113883.6.96" displayName="Cognitive function finding"/>
                    <text>
                      <reference>
                        <xsl:attribute name="value">
                          <xsl:choose>
                            <xsl:when test="count(//FunctionalStatus) &gt; 0">
                              <xsl:value-of select="'#function1'"/>
                            </xsl:when>
                            <xsl:when test="count(//CognitiveStatus) &gt; 0">
                              <xsl:value-of select="'#cognitive1'"/>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:value-of select="'#cognitive1'"/>
                            </xsl:otherwise>
                          </xsl:choose>
                        </xsl:attribute>
                      </reference>
                    </text>
                    <statusCode code="completed"/>
                    <effectiveTime>
                      <low>
                        <xsl:choose>
                          <xsl:when test="normalize-space(./Date/text()) !=''">
                            <xsl:attribute name="value">
                              <xsl:value-of select="normalize-space(./Date/text())"/>
                            </xsl:attribute>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </low>
                    </effectiveTime>
                    <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96">
                      <xsl:choose>
                        <xsl:when test="normalize-space(./Code/text()) !=''">
                          <xsl:attribute name="code">
                            <xsl:value-of select="normalize-space(./Code/text())"/>
                          </xsl:attribute>
                          <xsl:attribute name="displayName">
                            <xsl:value-of select="normalize-space(./Type/text())"/>
                          </xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'NI'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </value>
                  </observation>
                </entry>
              </xsl:for-each>
            </section>
          </component>
          <xsl:variable name="clinicalinstructions" select="normalize-space(//ClinicalInstruction/text())"/>
          <xsl:variable name="futureappointments" select="normalize-space(//FutureAppointments)"/>
          <xsl:variable name="decisionaids" select="normalize-space(//RecommendedPatientDecisionAids/text())"/>
          <xsl:variable name="diagnostictestpending" select="normalize-space(//DiagnosticTestPending/Name/text())"/>
          <xsl:variable name="futurescheduledtests" select="normalize-space(//FutureScheduledTests/Name/text())"/>
          <xsl:variable name="referralreason" select="normalize-space(//Referral/ReasonforReferral/text())"/>
          <xsl:variable name="refby" select="normalize-space(//Referral/ReferringProviderName/text())"/>
          <!--********************************************************
						PLAN OF CARE
						********************************************************-->
          <component>
            <section>
              <templateId root="2.16.840.1.113883.10.20.22.2.10"/>
              <!--  **** Plan of Care section template  **** -->
              <code code="18776-5" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Treatment plan"/>
              <title>PLAN OF CARE</title>
              <text>
                <table border="1" width="100%">
                  <thead>
                    <tr>
                      <th>Goal</th>
                      <th>Instructions</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    <xsl:for-each select="//PlanOfCare">
                      <tr>
                        <td>
                          <xsl:attribute name="ID">
                            <xsl:value-of select="concat('planofcare',position())"/>
                          </xsl:attribute>
                          <xsl:value-of select="normalize-space(./Goal/text())"/>
                        </td>
                        <td>
                          <xsl:value-of select="normalize-space(./Description/text())"/>
                        </td>
                        <td>
                          <!-- TODO Get string dt -->
                          <xsl:value-of select="normalize-space(./EstablishedDateTime)"/>
                        </td>
                      </tr>
                    </xsl:for-each>
                    <!--<xsl:if test="$clinicalinstructions != ''">										
												<tr>
													<td>
														<xsl:attribute name="ID">
															<xsl:value-of select="concat('instruction',position())"/>
														</xsl:attribute>														
													</td>
                          <td>
                            <xsl:value-of select="$clinicalinstructions"/>
                          </td>
                          <td>
                            <xsl:value-of select="normalize-space($visitDate)"/>
                          </td>
												</tr>
												</xsl:if>
												<xsl:if test="$decisionaids != ''">		
												<tr>																									
													<td>
													<xsl:value-of select="$decisionaids"/>													
													</td>
													<td>											
														<xsl:value-of select="$visitDate"/>
													</td>
													<td>
														<xsl:attribute name="ID">
															<xsl:value-of select="concat('decisionaid',position())"/>
														</xsl:attribute>
													</td>
												</tr>												
												</xsl:if>-->
                    <xsl:if test="$futureappointments != ''">
                      <tr>
                        <td>
                          <xsl:value-of select="'Future Appointment'"/>
                        </td>
                        <td>
                          <xsl:value-of select="$futureappointments"/>
                        </td>
                        <td>
                          <xsl:text></xsl:text>
                        </td>
                      </tr>
                    </xsl:if>
                    <xsl:for-each select="//DiagnosticTestPending">
                      <xsl:if test="normalize-space(./Name/text()) != ''">
                        <tr>
                          <td>
                            <xsl:value-of select="'Diagnostic Test Pending'"/>
                          </td>
                          <td>
                            <xsl:value-of select="normalize-space(./Name/text())"/>
                          </td>
                          <td>
                            <xsl:value-of select="normalize-space(./StartDate/text())"/>
                          </td>
                        </tr>
                      </xsl:if>
                    </xsl:for-each>
                    <xsl:for-each select="//FutureScheduledTests">
                      <xsl:if test="normalize-space(./Name/text()) != ''">
                        <tr>
                          <td>
                            <xsl:value-of select="'Future Scheduled Tests'"/>
                          </td>
                          <td>
                            <xsl:value-of select="normalize-space(./Name/text())"/>
                          </td>
                          <td>
                            <xsl:value-of select="normalize-space(./StartDate/text())"/>
                          </td>
                        </tr>
                      </xsl:if>
                    </xsl:for-each>
					<tr><td></td><td></td><td></td></tr>
                  </tbody>
                </table>
              </text>
              <xsl:for-each select="//PlanOfCare">
                <xsl:variable name="description" select="normalize-space(./Description/text())"/>
                <entry typeCode="DRIV">
                  <observation classCode="OBS" moodCode="RQO">
                    <templateId root="2.16.840.1.113883.10.20.22.4.44"/>
                    <!--   Plan of Care Activity Observation template   -->
                    <id>
                      <xsl:attribute name="root">
                        <xsl:value-of select="generate-id()"/>
                      </xsl:attribute>
                    </id>
                    <code codeSystem = "2.16.840.1.113883.6.96">
                      <xsl:attribute name="code">
                        <xsl:choose>
                          <xsl:when test="normalize-space(./Code/text()) !=''">
                            <xsl:value-of select="normalize-space(./Code/text())"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:value-of select="'UNK'"/>
                          </xsl:otherwise>
                        </xsl:choose>
                      </xsl:attribute>
                      <xsl:attribute name="displayName">
                        <xsl:choose>
                          <xsl:when test="normalize-space(./Goal/text()) !=''">
                            <xsl:value-of select="normalize-space(./Goal/text())"/>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:value-of select="'Unknown'"/>
                          </xsl:otherwise>
                        </xsl:choose>
                      </xsl:attribute>
                    </code>
                    <statusCode code = "new"/>
                    <effectiveTime>
                      <center>
                        <xsl:choose>
                          <xsl:when test="normalize-space(./EstablishedDateTime/text())!=''">
                            <xsl:attribute name="value">
                              <xsl:value-of select="normalize-space(./EstablishedDateTime/text())"/>
                            </xsl:attribute>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'NI'"/>
                            </xsl:attribute>
                          </xsl:otherwise>
                        </xsl:choose>
                      </center>
                    </effectiveTime>
                  </observation>
                </entry>
                <xsl:if test="$description != ''">
                  <entry typeCode="DRIV">
                    <act classCode="ACT" moodCode="INT">
                      <templateId root="2.16.840.1.113883.10.20.22.4.20"/>
                      <!-- ** Instructions Template ** -->
                      <code code="311401005" codeSystem="2.16.840.1.113883.6.96" displayName="patient education"/>
                      <text>
                        <reference>
                          <xsl:attribute name="value">
                            <xsl:value-of select="concat('#planofcare', position())"/>
                          </xsl:attribute>
                        </reference>
                        <xsl:value-of select="$description"/>
                      </text>
                      <statusCode code="completed"/>
                    </act>
                  </entry>
                </xsl:if>
              </xsl:for-each>
              <entry>
                <encounter moodCode = "INT" classCode = "ENC">
                  <templateId root = "2.16.840.1.113883.10.20.22.4.40"/>
                  <id root = "63e41adf-415a-459d-a803-90484722a896"/>
                </encounter>
              </entry>
            </section>
          </component>
          <!--
            ********************************************************
            INTERVENTIONS
            ********************************************************
            -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.21.2.3"/>
							<code code="62387-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="INTERVENTIONS PROVIDED"/>
							<title>INTERVENTIONS PROVIDED</title>
							<text>
								<list listType="ordered">
									<xsl:for-each select="//Interventions/Description">
										<xsl:element name="item">
											<xsl:value-of select="./text()"/>
										</xsl:element>
									</xsl:for-each>
								</list>
							</text>
						</section>
					</component>-->
          <!--
            ********************************************************
            PROCEDURES
            ********************************************************
            -->
          <xsl:variable name="procedures" select="//Procedures"/>
          <component>
            <xsl:choose>
              <xsl:when test="count($procedures) &gt; 0">
                <section>
                  <templateId root = "2.16.840.1.113883.10.20.22.2.7"/>
                  <templateId root = "2.16.840.1.113883.10.20.22.2.7.1"/>
                  <code code = "47519-4" codeSystem = "2.16.840.1.113883.6.1" codeSystemName = "LOINC" displayName = "PROCEDURES"/>
                  <title>PROCEDURES</title>
                  <text>
                    <table border = "1" width = "100%">
                      <thead>
                        <tr>
                          <th>Name</th>
                          <th>Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        <xsl:for-each select="$procedures">
                          <xsl:element name="tr">
                            <xsl:element name="td">
                              <xsl:element name="content">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('procedure',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="normalize-space(./Name/text())"/>
                              </xsl:element>
                            </xsl:element>
                            <xsl:element name="td">
                              <xsl:value-of select="normalize-space(./Date/text())"/>
                            </xsl:element>
                          </xsl:element>
                        </xsl:for-each>
                      </tbody>
                    </table>
                  </text>
                  <xsl:for-each select="$procedures">
                    <entry>
                      <observation classCode = "OBS" moodCode = "EVN">
                        <templateId root = "2.16.840.1.113883.10.20.22.4.13"/>
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code>
                          <xsl:attribute name="xsi:type">
                            <xsl:value-of select="'CE'"/>
                          </xsl:attribute>
                          <xsl:choose>
                            <xsl:when test="normalize-space(./CodeSystem/text()) ='CPT'">
                              <xsl:attribute name="codeSystem">
                                <xsl:value-of select="'2.16.840.1.113883.6.12'"/>
                              </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:attribute name="codeSystem">
                                <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                              </xsl:attribute>
                            </xsl:otherwise>
                          </xsl:choose>
                          <xsl:choose>
                            <xsl:when test="normalize-space(./Code/text()) !=''">
                              <xsl:attribute name="code">
                                <xsl:value-of select="normalize-space(./Code/text())"/>
                              </xsl:attribute>
                              <xsl:attribute name="displayName">
                                <xsl:value-of select="normalize-space(./Name/text())"/>
                              </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                              <xsl:attribute name="nullFlavor">
                                <xsl:value-of select="'NI'"/>
                              </xsl:attribute>
                            </xsl:otherwise>
                          </xsl:choose>
                          <originalText>
                            <reference value = "#procedure1"/>
                            <xsl:value-of select="normalize-space(./Name/text())"/>
                          </originalText>
                        </code>
                        <statusCode code = "completed"/>
                        <effectiveTime>
                          <xsl:attribute name="value">
                            <xsl:value-of select="normalize-space(./Date/text())" />
                          </xsl:attribute>
                        </effectiveTime>
                        <value xsi:type = "CD"/>
                        <methodCode nullFlavor = "UNK"/>
                        <performer>
                          <assignedEntity>
                            <id nullFlavor = "NI"/>
                            <addr nullFlavor = "UNK"/>
                            <telecom nullFlavor = "UNK"/>
                            <representedOrganization>
                              <id root="2.16.840.1.113883.19.5"/>
                              <name>
                                <xsl:value-of select="normalize-space(./Facility/Name/text())"/>
                              </name>
                              <telecom nullFlavor = "UNK"/>
                              <addr nullFlavor = "UNK"/>
                            </representedOrganization>
                          </assignedEntity>
                        </performer>
                      </observation>
                    </entry>
                  </xsl:for-each>
                </section>
              </xsl:when>
              <xsl:otherwise>
                <section nullFlavor="NI">
                  <templateId root = "2.16.840.1.113883.10.20.22.2.7"/>
                  <templateId root = "2.16.840.1.113883.10.20.22.2.7.1"/>
                  <code code = "47519-4" codeSystem = "2.16.840.1.113883.6.1" codeSystemName = "LOINC" displayName = "PROCEDURES"/>
                  <title>PROCEDURES</title>
                  <text>
                    <table border = "1" width = "100%">
                      <thead>
                        <tr>
                          <th>Name</th>
                          <th>Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td colspan="2">
                            <content ID="procedure1">No procedures reported</content>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </text>
                </section>
              </xsl:otherwise>
            </xsl:choose>
          </component>
          <component>
            <section>
              <templateId root = "2.16.840.1.113883.10.20.22.2.45"/>
              <code code = "69730-0" codeSystem = "2.16.840.1.113883.6.1" codeSystemName = "LOINC" displayName = "INSTRUCTIONS"/>
              <title>INSTRUCTIONS</title>
              <text>
                <table width = "100%" border = "1">
                  <thead>
                    <tr>
                      <th colspan = "3">Clinical Instructions/Patient Decision Aids</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>Clinical Instructions</td>
                      <td ID = "sect1">
                        <xsl:value-of select="$clinicalinstructions"/>
                      </td>
                      <td>
                        <xsl:value-of select="$visitDate"/>
                      </td>
                    </tr>
                    <tr>
                      <td>Patient Decision Aids</td>
                      <td ID = "sect2">
                        <xsl:value-of select="$decisionaids"/>
                      </td>
                      <td>
                        <xsl:value-of select="$visitDate"/>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </text>
              <entry typeCode = "DRIV">
                <act classCode = "ACT" moodCode = "INT">
                  <templateId root = "2.16.840.1.113883.10.20.22.4.20"/>
                  <code xsi:type = "CE" code = "311401005" codeSystem = "2.16.840.1.113883.6.96" displayName = "Patient Education"/>
                  <text>
                    <reference value = "#sect1"/>
                    <xsl:value-of select=" $clinicalinstructions"/>
                  </text>
                  <statusCode code = "completed"/>
                </act>
              </entry>
              <entry typeCode = "DRIV">
                <act classCode = "ACT" moodCode = "INT">
                  <templateId root = "2.16.840.1.113883.10.20.22.4.20"/>
                  <code xsi:type = "CE" code = "311401005" codeSystem = "2.16.840.1.113883.6.96" displayName = "Patient Education"/>
                  <text>
                    <reference value = "#sect2"/>
                    <xsl:value-of select=" $decisionaids"/>
                  </text>
                  <statusCode code = "completed"/>
                </act>
              </entry>
            </section>
          </component>
          <!-- ************************************
            ASSESSMENT
            ************************************ -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.22.2.8"/>
							<code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="51848-0" displayName="ASSESSMENT"/>
							<title>ASSESSMENT</title>
							<text>
								<list listType="ordered">
									<item>
                    Recurrent GI bleed of unknown etiology; hypotension perhaps
                    secondary to this but as likely secondary to polypharmacy.
                  </item>
									<item>Acute on chronic anemia secondary to #1.</item>
									<item>
                    Azotemia, acute renal failure with volume loss secondary to
                    #1.
                  </item>
									<item>Hyperkalemia secondary to #3 and on ACE and K+ supplement.</item>
									<item>Other chronic diagnoses as noted above, currently stable.</item>
								</list>
							</text>
						</section>
					</component>-->
          <!-- ************************************
            CHIEF COMPLAINT / REASON FOR VISIT
            ************************************ -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.22.2.13"/>
							<code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="46239-0" displayName="REASON FOR VISIT + CHIEF COMPLAINT"/>
							<title>REASON FOR VISIT/CHIEF COMPLAINT</title>
							<text>
								<paragraph>Dark stools.</paragraph>
							</text>
						</section>
					</component>-->
          <!--
            ********************************************************
            FAMILY HISTORY
            ********************************************************
            -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.22.2.15"/>	
							<code code="10157-6" codeSystem="2.16.840.1.113883.6.1"/>
							<title>FAMILY HISTORY</title>
							<text>
								<paragraph>Father (deceased)</paragraph>
								<table border="1" width="100%">
									<thead>
										<tr>
											<th>Diagnosis</th>
											<th>Age At Onset</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Myocardial Infarction (cause of death)</td>
											<td>57</td>
										</tr>
										<tr>
											<td>Diabetes</td>
											<td>40</td>
										</tr>
									</tbody>
								</table>
							</text>
							<entry typeCode="DRIV">
								<organizer moodCode="EVN" classCode="CLUSTER">
									<templateId root="2.16.840.1.113883.10.20.22.4.45"/>
									<statusCode code="completed"/>
									<subject>
										<relatedSubject classCode="PRS">
											<code code="FTH" displayName="Father" codeSystemName="HL7 FamilyMember" codeSystem="2.16.840.1.113883.5.111">
												<translation code="9947008" displayName="Biological father" codeSystemName="SNOMED" codeSystem="2.16.840.1.113883.6.96"/>
											</code>
											<subject>
												<administrativeGenderCode code="M" codeSystem="2.16.840.1.113883.5.1" displayName="Male"/>
												<birthTime value="1910"/>
											</subject>
										</relatedSubject>
									</subject>
									<component>
										<observation classCode="OBS" moodCode="EVN">
											<templateId root="2.16.840.1.113883.10.20.22.4.46"/>
											<id root="d42ebf70-5c89-11db-b0de-0800200c9a66"/>
											<code code="64572001" displayName="Condition" codeSystemName="SNOMED CT" codeSystem="2.16.840.1.113883.6.96"/>
											<statusCode code="completed"/>
											<effectiveTime value="1967"/>
											<value xsi:type="CD" code="22298006" codeSystem="2.16.840.1.113883.6.96" displayName="Myocardial infarction"/>
											<entryRelationship typeCode="CAUS">
												<observation classCode="OBS" moodCode="EVN">
													<templateId root="2.16.840.1.113883.10.20.22.4.47"/>
													<id root="6898fae0-5c8a-11db-b0de-0800200c9a66"/>
													<code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
													<statusCode code="completed"/>
													<value xsi:type="CD" code="419099009" codeSystem="2.16.840.1.113883.6.96" displayName="Dead"/>
												</observation>
											</entryRelationship>
											<entryRelationship typeCode="SUBJ" inversionInd="true">
												<observation classCode="OBS" moodCode="EVN">
													<templateId root="2.16.840.1.113883.10.20.22.4.31"/>
													<code code="445518008" codeSystem="2.16.840.1.113883.6.96" displayName="Age At Onset"/>
													<statusCode code="completed"/>
													<value xsi:type="PQ" value="57" unit="a"/>
												</observation>
											</entryRelationship>
										</observation>
									</component>
									<component>
										<observation classCode="OBS" moodCode="EVN">
											<templateId root="2.16.840.1.113883.10.20.22.4.46"/>
											<id root="5bfe3ec0-5c8b-11db-b0de-0800200c9a66"/>
											<code code="64572001" displayName="Condition" codeSystemName="SNOMED CT" codeSystem="2.16.840.1.113883.6.96"/>
											<statusCode code="completed"/>
											<effectiveTime value="1950"/>
											<value xsi:type="CD" code="46635009" codeSystem="2.16.840.1.113883.6.96" displayName="Diabetes mellitus type 1"/>
											<entryRelationship typeCode="SUBJ" inversionInd="true">
												<observation classCode="OBS" moodCode="EVN">
													<templateId root="2.16.840.1.113883.10.20.22.4.31"/>													
													<code code="445518008" codeSystem="2.16.840.1.113883.6.96" displayName="Age At Onset"/>
													<statusCode code="completed"/>
													<value xsi:type="PQ" value="40" unit="a"/>
												</observation>
											</entryRelationship>
										</observation>
									</component>
								</organizer>
							</entry>
						</section>
					</component>-->
          <!-- ************************************
            GENERAL STATUS
            ************************************ -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.2.5"/>
							<code code="10210-3" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="GENERAL STATUS"/>
							<title>GENERAL STATUS</title>
							<text>
								<paragraph>Alert and in good spirits, no acute distress. </paragraph>
							</text>
						</section>
					</component>-->
          <!-- ************************************
            HISTORY OF PAST ILLNESS
            ************************************ -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.22.2.20"/>
							<code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="11348-0" displayName="HISTORY OF PAST ILLNESS"/>
							<title>PAST MEDICAL HISTORY</title>
							<text>
								<paragraph>See History of Present Illness.</paragraph>
							</text>
						</section>
					</component>-->
          <!--
            ********************************************************
            HISTORY OF PRESENT ILLNESS
            ********************************************************
            -->
          <!--					<component>
						<section>
							<templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.4"/>
							<code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="10164-2" displayName="HISTORY OF PRESENT ILLNESS"/>
							<title>HISTORY OF PRESENT ILLNESS</title>
							<text>
								<paragraph>
                  This patient was only recently discharged for a recurrent GI
                  bleed as described below.
                </paragraph>
								<paragraph>
                  He presented to the ER today c/o a dark stool yesterday but a
                  normal brown stool today. On exam he was hypotensive in the 80?s
                  resolved after .... .... ....
                </paragraph>
								<paragraph>
                  Lab at discharge: Glucose 112, BUN 16, creatinine 1.1,
                  electrolytes normal. H. pylori antibody pending. Admission hematocrit
                  16%, discharge hematocrit 29%. WBC 7300, platelet count 256,000.
                  Urinalysis normal. Urine culture: No growth. INR 1.1, PTT
                  40.
                </paragraph>
								<paragraph>
                  He was transfused with 6 units of packed red blood cells with
                  .... .... ....
                </paragraph>
								<paragraph>
                  GI evaluation 12 September: Colonoscopy showed single red clot in
                  .... .... ....
                </paragraph>
							</text>
						</section>
					</component>-->
          <!-- ************************************
            PHYSICAL EXAMINATION
            ************************************ -->
          <!--					<component>
						<section>
							<templateId root="2.16.840.1.113883.10.20.2.10"/>
							<code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="29545-1" displayName="PHYSICAL FINDINGS"/>
							<title>PHYSICAL EXAMINATION</title>
							<text>
								<list listType="ordered">
									<item>HEENT: All normal to examination.</item>
									<item>Heart: RRR, no murmur.</item>
									<item>THORAX &amp; LUNGS: Clear without rhonchi or wheeze.</item>
									<item>
                    ABDOMEN: No distension, tenderness, or guarding, obese, pos bowel
                    sounds.
                  </item>
									<item>
                    BACK: Normal to inspection and palpation, without tenderness; no
                    presacral edema.
                  </item>
									<item>
                    EXTREMITIES: Doughy edema bilaterally, chronic stasis changes, no
                    asymmetrical swelling.
                  </item>
								</list>
							</text>
						</section>
					</component>-->
          <!-- ************************************
            REVIEW OF SYSTEMS
            ************************************ -->
          <!-- <component>
						<section>
							<templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.18"/>
							<code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="10187-3" displayName="REVIEW OF SYSTEMS"/>
							<title>REVIEW OF SYSTEMS</title>
							<text>
								<paragraph>
                  Patient denies recent history of fever or malaise. Positive for
                  weakness and shortness of breath. One episode of melena. No recent
                  headaches. Positive for osteoarthritis in hips, knees and hands.
                </paragraph>
							</text>
						</section>
					</component>-->
          <!-- ************************************************
              DISCHARGE INSTRUCTION
              ************************************************* -->
          <xsl:variable name="DischargeInstructions" select="//DischargeInstructions"/>
          <xsl:choose>
            <xsl:when test="count($DischargeInstructions) &gt; 0">
              <component>
                <section>
                  <templateId root="2.16.840.1.113883.10.20.22.2.41"/>
                  <code code="8653-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="HOSPITAL DISCHARGE INSTRUCTIONS"/>
                  <title>HOSPITAL DISCHARGE INSTRUCTIONS</title>
                  <text>
                    <xsl:value-of select="normalize-space(//DischargeInstructions/text())"/>
                  </text>
                </section>
              </component>
            </xsl:when>
          </xsl:choose>
        </structuredBody>
      </component>
    </ClinicalDocument>
  </xsl:template>
</xsl:stylesheet>