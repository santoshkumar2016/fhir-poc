<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="xsl">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/ | @* | node()">
    <xsl:processing-instruction name="xml-stylesheet">
      <xsl:text>type="text/xsl" href="CDA.xsl"</xsl:text>
    </xsl:processing-instruction>
    <ClinicalDocument xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="urn:hl7-org:v3" xmlns:cda="urn:hl7-org:v3" xmlns:sdtc="urn:hl7-org:sdtc" >
      <!--
          ********************************************************
            CDA Header
          ********************************************************
            -->
      <realmCode code="US"/>
      <typeId root="2.16.840.1.113883.1.3" extension="POCD_HD000040"/>
      <!-- US General Header Template -->
      <xsl:choose>
        <xsl:when test="//Patient/DocumentType = 'RN'">
          <templateId root="2.16.840.1.113883.10.20.22.1.14" extension="2015-08-01"/>
        </xsl:when>
        <xsl:otherwise>
          <templateId root="2.16.840.1.113883.10.20.22.1.2" extension="2014-06-09"/>
        </xsl:otherwise>
      </xsl:choose>
      <!--put extension = doc id from database/self generated here. Should be unique & alphanumerid-->
      <id root="2.16.840.1.113883.19.5.99999.1">
        <xsl:attribute name="extension">
          <xsl:value-of select="normalize-space(//CDAQuery/@DocumentID)"/>
        </xsl:attribute>
      </id>
      <!--<xsl:variable name="now" select="current-dateTime()" />-->
      <xsl:choose>
        <xsl:when test="//Patient/DocumentType = 'RN'">
          <code code="57133-1" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="REFERRAL NOTE"/>
          <title>Referral Note - 2015 CCDA</title>
        </xsl:when>
        <xsl:otherwise>
          <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="34133-9" displayName="Summarization of episode note"/>
          <title>Summary of Patient Chart - 2015 CCDA</title>
        </xsl:otherwise>
      </xsl:choose>
      <effectiveTime>
        <xsl:attribute name="value">
          <xsl:value-of select="//Patient/DocumentDate/text()" />
        </xsl:attribute>
      </effectiveTime>
      <confidentialityCode code="N" displayName="normal" codeSystem="2.16.840.1.113883.5.25" codeSystemName="Confidentiality"/>
      <languageCode code="en-US"/>
      <!--<setId extension="sTT103" root="2.16.840.1.113883.19.5.99999.19"/>
      <versionNumber value="1"/>-->
      <recordTarget>
        <patientRole>
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
          <!-- Patient Address -->
          <xsl:choose>
            <xsl:when test="count(//Patient/address) &gt; 0">
              <xsl:for-each select="//Patient/address">
                <xsl:element name="addr">
                  <xsl:attribute name="use">
                    <xsl:choose>
                      <xsl:when test="//address/use/@value = 'home'">HP</xsl:when>
                      <xsl:otherwise>WP</xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
                  <xsl:variable name="address1" select="normalize-space(./line/@value)"/>
                  <xsl:variable name="city" select="normalize-space(./city/@value)"/>
                  <xsl:variable name="state" select="normalize-space(./state/@value)"/>
                  <xsl:variable name="postalCode" select="normalize-space(./postalCode/@value)"/>
                  <xsl:variable name="country" select="normalize-space(./country/@value)"/>
                  <xsl:if test="$address1 != ''">
                    <xsl:element name="streetAddressLine">
                      <xsl:value-of select="$address1"/>
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
          <!-- Patient Phone -->
          <xsl:for-each select="//Patient/telecom">
            <xsl:choose>
              <xsl:when test="normalize-space(./value/@value) !=''">
                <xsl:element name="telecom">
                  <xsl:attribute name="value">
                    <xsl:value-of select="concat('tel:+1',normalize-space(./value/@value))"/>
                  </xsl:attribute>
                  <xsl:if test="./use/@value ='home'">
                    <xsl:attribute name="use">
                      <xsl:value-of select="'HP'"/>
                    </xsl:attribute>
                  </xsl:if>
                </xsl:element>
              </xsl:when>
              <xsl:otherwise>
                <telecom nullFlavor="NI"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>
          <patient>
            <xsl:choose>
              <xsl:when test="count(//Patient/name) &gt; 0">
                <xsl:element name="name">
                  <xsl:if test="normalize-space(./NameUse/text()) != ''">
                    <xsl:attribute name ="use">
                      <xsl:value-of select="./NameUse/text()"/>
                    </xsl:attribute>
                  </xsl:if>
                  <xsl:if test="//Patient/name/prefix/@value !=''">
                    <xsl:element name="prefix">
                      <xsl:value-of select="normalize-space(//Patient/name/prefix/@value)"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/name/given/@value) != ''">
                    <xsl:element name="given">
                      <xsl:value-of select="normalize-space(//Patient/name/given/@value)"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="//Patient/name/family/@value != ''">
                    <xsl:element name="family">
                      <xsl:value-of select="normalize-space(//Patient/name/family/@value)"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/name/suffix/@value) != ''">
                    <xsl:element name="suffix">
                      <xsl:value-of select="normalize-space(//Patient/name/suffix/@value)"/>
                    </xsl:element>
                  </xsl:if>
                </xsl:element>
              </xsl:when>
            </xsl:choose>
            <administrativeGenderCode codeSystem="2.16.840.1.113883.5.1" codeSystemName="AdministrativeGender">
              <xsl:choose>
                <xsl:when test="//Patient/gender/@value !=''">
                  <xsl:attribute name="code">
                    <xsl:choose>
                      <xsl:when test="//Patient/gender/@value = 'female'">
                        <xsl:value-of select="'F'"/>
                      </xsl:when>
                      <xsl:when test="//Patient/gender/@value = 'male'">
                        <xsl:value-of select="'M'"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="'UN'"/>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
                  <xsl:attribute name="displayName">
                    <xsl:value-of select="normalize-space(//Patient/gender/@value)"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="UNK"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </administrativeGenderCode>
            <xsl:choose>
              <xsl:when test="//Patient/birthDate">
                <xsl:element name="birthTime">
                  <xsl:attribute name="value">
                    <xsl:value-of select="normalize-space(translate(//Patient/birthDate/@value,'-',''))"/>
                  </xsl:attribute>
                </xsl:element>
              </xsl:when>
              <xsl:otherwise>
                <birthTime nullFlavor="NI"/>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="//Patient/maritalStatus">
              <maritalStatusCode codeSystem="2.16.840.1.113883.5.2" codeSystemName="MaritalStatusCode">
                <xsl:attribute name="code">
                  <xsl:value-of select="normalize-space(//Patient/maritalStatus/coding/code/@value)"/>
                </xsl:attribute>
                <xsl:attribute name="displayName">
                  <xsl:value-of select="normalize-space(//Patient/maritalStatus/coding/display/@value)"/>
                </xsl:attribute>
              </maritalStatusCode>
            </xsl:if>
            <xsl:choose>
              <xsl:when test="count(//Patient/ParentRace) &gt; 0">
                <raceCode codeSystem="2.16.840.1.113883.6.238" codeSystemName="Race and Ethnicity - CDC">
                  <xsl:choose>
                    <xsl:when test="//Patient/ParentRace/Code/text() !='UNK'">
                      <xsl:attribute name="code">
                        <xsl:value-of select="//Patient/ParentRace/Code"/>
                      </xsl:attribute>
                      <xsl:attribute name="displayName">
                        <xsl:value-of select="//Patient/ParentRace/Name"/>
                      </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="nullFlavor">
                        <xsl:value-of select="'UNK'"/>
                      </xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </raceCode>
              </xsl:when>
              <xsl:otherwise>
                <raceCode  nullFlavor="UNK"/>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:for-each select="//Race">
              <sdtc:raceCode  codeSystem="2.16.840.1.113883.6.238" codeSystemName="Race &amp; Ethnicity - CDC">
                <xsl:choose>
                  <xsl:when test="normalize-space(./Code/text())!='UNK'">
                    <xsl:attribute name="code">
                      <xsl:value-of select="normalize-space(./Code/text())"/>
                    </xsl:attribute>
                    <xsl:attribute name="displayName">
                      <xsl:value-of select="normalize-space(./Name/text())"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'UNK'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </sdtc:raceCode>
            </xsl:for-each>
            <xsl:choose>
              <xsl:when test="count(//Patient/ParentEthnicity) &gt; 0">
                <ethnicGroupCode codeSystem="2.16.840.1.113883.6.238" codeSystemName="Race and Ethnicity - CDC">
                  <xsl:choose>
                    <xsl:when test="//Patient/ParentEthnicity/Code/text()!='UNK'">
                      <xsl:attribute name="code">
                        <xsl:value-of select="//Patient/ParentEthnicity/Code"/>
                      </xsl:attribute>
                      <xsl:attribute name="displayName">
                        <xsl:value-of select="//Patient/ParentEthnicity/Name"/>
                      </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="nullFlavor">
                        <xsl:value-of select="'UNK'"/>
                      </xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </ethnicGroupCode>
              </xsl:when>
              <xsl:otherwise>
                <ethnicGroupCode nullFlavor="UNK"/>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:if test ="count(//Patient/ParentEthnicity/Ethnicity) &gt; 0">
              <xsl:for-each select="//Patient/ParentEthnicity/Ethnicity">
                <sdtc:ethnicGroupCode  codeSystem="2.16.840.1.113883.6.238" codeSystemName="Race &amp; Ethnicity - CDC">
                  <xsl:choose>
                    <xsl:when test="normalize-space(./Code/text())!='UNK'">
                      <xsl:attribute name="code">
                        <xsl:value-of select="normalize-space(./Code/text())"/>
                      </xsl:attribute>
                      <xsl:attribute name="displayName">
                        <xsl:value-of select="normalize-space(./Name/text())"/>
                      </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="nullFlavor">
                        <xsl:value-of select="'UNK'"/>
                      </xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </sdtc:ethnicGroupCode>
              </xsl:for-each>
            </xsl:if>
            <languageCommunication>
              <xsl:choose>
                <xsl:when test="normalize-space(//Patient/communication/language/coding/display/@value) != ''">
                  <xsl:element name="languageCode">
                    <xsl:attribute name="code">
                      <xsl:value-of select="normalize-space(//Patient/communication/language/coding/display/@value)"/>
                    </xsl:attribute>
                  </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                  <languageCode nullFlavor="NI" />
                </xsl:otherwise>
              </xsl:choose>
            </languageCommunication>
          </patient>
          <xsl:if test="count(//Patient/Provider) &gt; 0">
            <providerOrganization>
              <id root="2.16.840.1.113883.4.6">
                <xsl:if test="normalize-space(//Provider/ProviderOrganizationNPI/text()) != ''">
                  <xsl:attribute name="extension">
                    <xsl:value-of select="//Provider/ProviderOrganizationNPI/text()"/>
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
                      <xsl:value-of select="'UNK'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </telecom>
              <xsl:for-each select="//Provider/Address">
                <xsl:choose>
                  <xsl:when test="//Provider/Address[.!='']">
                    <addr use="WP">
                      <xsl:if test="normalize-space(./Address1/text()) != ''">
                        <xsl:element name="streetAddressLine">
                          <xsl:value-of select="./Address1/text()"/>
                        </xsl:element>
                      </xsl:if>
                      <xsl:if test="normalize-space(./Address2/text()) != ''">
                        <xsl:element name="streetAddressLine">
                          <xsl:value-of select="normalize-space(./Address2/text())"/>
                        </xsl:element>
                      </xsl:if>
                      <xsl:if test="normalize-space(./City/text()) != ''">
                        <xsl:element name="city">
                          <xsl:value-of select="normalize-space(./City/text())"/>
                        </xsl:element>
                      </xsl:if>
                      <xsl:if test="normalize-space(./State/text()) != ''">
                        <xsl:element name="state">
                          <xsl:value-of select="normalize-space(./State/text())"/>
                        </xsl:element>
                      </xsl:if>
                      <xsl:if test="normalize-space(./Zip/text()) != ''">
                        <xsl:element name="postalCode">
                          <xsl:value-of select="normalize-space(./Zip/text())"/>
                        </xsl:element>
                      </xsl:if>
                      <xsl:if test="normalize-space(./Zip/text()) != ''">
                        <xsl:element name="country">
                          <xsl:value-of select="normalize-space(./Country/text())"/>
                        </xsl:element>
                      </xsl:if>
                    </addr>
                  </xsl:when>
                  <xsl:otherwise>
                    <addr nullFlavor="UNK"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:for-each>
            </providerOrganization>
          </xsl:if>
        </patientRole>
      </recordTarget>
      <!-- The author represents the person who provides the content in the document -->
      <xsl:if test="count(//Patient/Agency) &gt; 0">
        <author>
          <xsl:element name="time">
            <xsl:attribute name="value">
              <xsl:value-of select="normalize-space(//DocumentDate/text())" />
            </xsl:attribute>
          </xsl:element>
          <assignedAuthor>
            <xsl:choose>
              <xsl:when test="//Agency/AgencyName != ''">
                <xsl:element name="id">
                  <xsl:attribute name="extension">
                    <xsl:value-of select="normalize-space(//Agency/AgencyName/text())" />
                  </xsl:attribute>
                  <xsl:attribute name="root">
                    <xsl:value-of select="'2.16.840.1.113883.3.1141'" />
                  </xsl:attribute>
                </xsl:element>
              </xsl:when>
              <xsl:otherwise>
                <xsl:element name="id">
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="'UNK'"/>
                  </xsl:attribute>
                </xsl:element>
              </xsl:otherwise>
            </xsl:choose>
            <addr use="WP">
              <xsl:if test="normalize-space(//Agency/Address/Address1/text()) != ''">
                <xsl:element name="streetAddressLine">
                  <xsl:value-of select="normalize-space(//Agency/Address/Address1/text())"/>
                </xsl:element>
              </xsl:if>
              <xsl:if test="normalize-space(//Agency/Address/City/text()) != ''">
                <xsl:element name="city">
                  <xsl:value-of select="normalize-space(//Agency/Address/City/text())"/>
                </xsl:element>
              </xsl:if>
              <xsl:if test="normalize-space(//Agency/Address/State/text()) != ''">
                <xsl:element name="state">
                  <xsl:value-of select="normalize-space(//Agency/Address/State/text()) "/>
                </xsl:element>
              </xsl:if>
              <xsl:if test="normalize-space(//Agency/Address/Zip/text()) != ''">
                <xsl:element name="postalCode">
                  <xsl:value-of select="normalize-space(//Agency/Address/Zip/text())"/>
                </xsl:element>
              </xsl:if>
              <xsl:if test="normalize-space(//Agency/Address/Country/text()) != ''">
                <xsl:element name="country">
                  <xsl:value-of select="normalize-space(//Agency/Address/Country/text())"/>
                </xsl:element>
              </xsl:if>
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
                    <xsl:value-of select="'UNK'"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:element>
            <assignedAuthoringDevice>
              <manufacturerModelName>Netsmart Technologies</manufacturerModelName>
              <softwareName>Essentia</softwareName>
              <!--<xsl:if test="//Patient/AuthoringDeviceModelName/text() != ''">
                <xsl:element name="manufacturerModelName">
                  <xsl:value-of select="normalize-space(//Patient/AuthoringDeviceModelName/text())"/>
                </xsl:element>
              </xsl:if>
              <xsl:if test="//Patient/SoftwareName/text() != ''">
                <xsl:element name="softwareName">
                  <xsl:value-of select="normalize-space(//Patient/SoftwareName/text())"/>
                </xsl:element>
              </xsl:if>-->
            </assignedAuthoringDevice>
          </assignedAuthor>
        </author>
      </xsl:if>
      <!-- The dataEnterer transferred the content created by the author into the document -->
      <xsl:if test="count(//Patient/DataEnterer) &gt; 0">
        <dataEnterer>
          <assignedEntity>
            <xsl:element name="id">
              <xsl:attribute name="root">
                <xsl:value-of select="'2.16.840.1.113883.4.6'"/>
              </xsl:attribute>
              <xsl:attribute name="extension">
                <xsl:value-of select="normalize-space(//Patient/DataEnterer/Id/text())"/>
              </xsl:attribute>
            </xsl:element>
            <xsl:choose>
              <xsl:when test="/*/Patient/DataEnterer/Address/*[text()] !=''">
                <addr>
                  <xsl:if test="normalize-space(//Patient/DataEnterer/Address/Address1/text()) !=''">
                    <xsl:element name="streetAddressLine">
                      <xsl:value-of select="//Patient/DataEnterer/Address/Address1/text()"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/DataEnterer/Address/Address2/text()) !=''">
                    <xsl:element name="streetAddressLine">
                      <xsl:value-of select="normalize-space(//Patient/DataEnterer/Address/Address2/text())"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/DataEnterer/Address/City/text()) !=''">
                    <xsl:element name="city">
                      <xsl:value-of select="normalize-space(//Patient/DataEnterer/Address/City/text())"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/DataEnterer/Address/State/text()) !=''">
                    <xsl:element name="state">
                      <xsl:value-of select="normalize-space(//Patient/DataEnterer/Address//State/text())"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/DataEnterer/Address/Zip/text()) !=''">
                    <xsl:element name="postalCode">
                      <xsl:value-of select="normalize-space(//Patient/DataEnterer/Address/Zip/text())"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:if test="normalize-space(//Patient/DataEnterer/Address/Country/text()) !=''">
                    <xsl:element name="country">
                      <xsl:value-of select="normalize-space(//Patient/DataEnterer/Address/Country/text())"/>
                    </xsl:element>
                  </xsl:if>
                </addr>
              </xsl:when>
              <xsl:otherwise>
                <addr nullFlavor="UNK"/>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:element name="telecom">
              <xsl:choose>
                <xsl:when test="normalize-space(//Patient/DataEnterer/Telephone/text()) !=''">
                  <xsl:attribute name="value">
                    <xsl:value-of select="concat('tel:', //Patient/DataEnterer/Telephone/text())"/>
                  </xsl:attribute>
                  <xsl:attribute name="use">
                    <xsl:value-of select="'WP'"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="'UNK'"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:element>
            <assignedPerson>
              <name>
                <xsl:element name="given">
                  <xsl:value-of select ="normalize-space(//Patient/DataEnterer/FirstName/text())"/>
                </xsl:element>
                <xsl:element name="family">
                  <xsl:value-of select ="normalize-space(//Patient/DataEnterer/LastName/text())"/>
                </xsl:element>
              </name>
            </assignedPerson>
          </assignedEntity>
        </dataEnterer>
      </xsl:if>
      <!-- The informant represents any sources of information for document content -->
      <xsl:if test="count(//Patient/Informant) &gt; 0">
        <informant>
          <assignedEntity>
            <xsl:element name="id">
              <xsl:attribute name="root">
                <xsl:value-of select="'2.16.840.1.113883.19.5'"/>
              </xsl:attribute>
              <xsl:attribute name="extension">
                <xsl:value-of select="normalize-space(//Patient/Informant/Id/text())"/>
              </xsl:attribute>
            </xsl:element>
            <xsl:choose>
              <xsl:when test="/*/Patient/Informant/Address/*[text()] !=''">
                <addr>
                  <xsl:element name="streetAddressLine">
                    <xsl:choose>
                      <xsl:when test="normalize-space(//Patient/Informant/Address/Address1/text()) !=''">
                        <xsl:value-of select="//Patient/Informant/Address/Address1/text()"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'UNK'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:choose>
                    <xsl:when test="normalize-space(//Patient/Informant/Address/Address2/text()) !=''">
                      <xsl:element name="streetAddressLine">
                        <xsl:value-of select="normalize-space(//Patient/Informant/Address/Address2/text())"/>
                      </xsl:element>
                    </xsl:when>
                  </xsl:choose>
                  <xsl:element name="city">
                    <xsl:choose>
                      <xsl:when test="normalize-space(//Patient/Informant/Address/City/text()) !=''">
                        <xsl:value-of select="normalize-space(//Patient/Informant/Address/City/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'UNK'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:element name="state">
                    <xsl:choose>
                      <xsl:when test="normalize-space(//Patient/Informant/Address/State/text()) !=''">
                        <xsl:value-of select="normalize-space(//Patient/Informant/Address//State/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'UNK'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:element name="postalCode">
                    <xsl:choose>
                      <xsl:when test="normalize-space(//Patient/Informant/Address/Zip/text()) !=''">
                        <xsl:value-of select="normalize-space(//Patient/Informant/Address/Zip/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'UNK'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                  <xsl:element name="country">
                    <xsl:choose>
                      <xsl:when test="normalize-space(//Patient/Informant/Address/Country/text()) !=''">
                        <xsl:value-of select="normalize-space(//Patient/Informant/Address/Country/text())"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:attribute name="nullFlavor">
                          <xsl:value-of select="'UNK'"/>
                        </xsl:attribute>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                </addr>
              </xsl:when>
              <xsl:otherwise>
                <addr nullFlavor="UNK"/>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:element name="telecom">
              <xsl:choose>
                <xsl:when test="normalize-space(//Patient/Informant/Telephone/text()) !=''">
                  <xsl:attribute name="value">
                    <xsl:value-of select="concat('tel:', //Patient/Informant/Telephone/text())"/>
                  </xsl:attribute>
                  <xsl:attribute name="use">
                    <xsl:value-of select="'WP'"/>
                  </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:attribute name="nullFlavor">
                    <xsl:value-of select="'UNK'"/>
                  </xsl:attribute>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:element>
            <assignedPerson>
              <name>
                <xsl:element name="given">
                  <xsl:value-of select ="normalize-space(//Patient/Informant/Firstname/text())"/>
                </xsl:element>
                <xsl:element name="family">
                  <xsl:value-of select ="normalize-space(//Patient/Informant/Lastname/text())"/>
                </xsl:element>
              </name>
            </assignedPerson>
          </assignedEntity>
        </informant>
      </xsl:if>
      <!-- The custodian represents the organization charged with maintaining the original source document -->
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
            <name>
              <xsl:value-of select="//Agency/AgencyName/text()" />
            </name>
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
                      <xsl:value-of select="'UNK'"/>
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
                      <xsl:value-of select="'UNK'"/>
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
                      <xsl:value-of select="'UNK'"/>
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
                      <xsl:value-of select="'UNK'"/>
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
                      <xsl:value-of select="'UNK'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </addr>
          </representedCustodianOrganization>
        </assignedCustodian>
      </custodian>
      <!-- The informationRecipient represents the intended recipient of the document -->
      <xsl:if test="count(//Patient/InformationRecipient) &gt; 0">
        <informationRecipient>
          <intendedRecipient>
            <informationRecipient>
              <name>
                <xsl:element name="given">
                  <xsl:value-of select="//Patient/InformationRecipient/FirstName/text()"/>
                </xsl:element>
                <xsl:element name="family">
                  <xsl:value-of select="//Patient/InformationRecipient/LastName/text()"/>
                </xsl:element>
              </name>
            </informationRecipient>
          </intendedRecipient>
        </informationRecipient>
      </xsl:if>
      <xsl:if test="count(//Patient/RelatedIndividual) &gt; 0">
        <xsl:variable name="relatedIndividual" select="//Patient/RelatedIndividual"/>
        <xsl:for-each select="$relatedIndividual">
          <participant typeCode="IND">
            <!-- patient's grandfather -->
            <associatedEntity classCode="PRS">
              <code code="UNK" displayName="Unknown" codeSystem="2.16.840.1.113883.1.11.19563"
                codeSystemName="Personal Relationship Role Type Value Set"/>
              <xsl:if test="./ContactInfo/HomeAddressLine1/text() !='' or ./ContactInfo/HomeAddressLine1/text() !='' or  ./ContactInfo/HomeState/text() !='' or ./ContactInfo/HomePostal/text()!=''">
                <addr use="HP">
                  <!-- HP is "primary home" from codeSystem 2.16.840.1.113883.5.1119 -->
                  <xsl:element name="streetAddressLine">
                    <xsl:value-of select="concat(./ContactInfo/HomeAddressLine1/text(),',',./ContactInfo/HomeAddressLine2/text())"/>
                  </xsl:element>
                  <xsl:element name="city">
                    <xsl:value-of select="./ContactInfo/HomeCity/text()"/>
                  </xsl:element>
                  <xsl:element name="state">
                    <xsl:value-of select="./ContactInfo/HomeState/text()"/>
                  </xsl:element>
                  <xsl:if test="./ContactInfo/HomePostal/text() !=''">
                    <xsl:element name="postalCode">
                      <xsl:value-of select="./ContactInfo/HomePostal/text()"/>
                    </xsl:element>
                  </xsl:if>
                  <xsl:element name="country">
                    <xsl:value-of select="./ContactInfo/HomeCountry/text()"/>
                  </xsl:element>
                </addr>
              </xsl:if>
              <xsl:if test="./ContactInfo/WorkAddressLine1/text() !='' or ./ContactInfo/WorkAddressLine2/text() !='' or  ./ContactInfo/WorkState/text() !='' or ./ContactInfo/WorkPostal/text()!=''">
                <addr use="HP">
                  <!-- HP is "primary home" from codeSystem 2.16.840.1.113883.5.1119 -->
                  <xsl:element name="streetAddressLine">
                    <xsl:value-of select="concat(./ContactInfo/WorkAddressLine1/text(),',',./ContactInfo/WorkAddressLine2/text())"/>
                  </xsl:element>
                  <xsl:element name="city">
                    <xsl:value-of select="./ContactInfo/WorkCity/text()"/>
                  </xsl:element>
                  <xsl:element name="state">
                    <xsl:value-of select="./ContactInfo/WorkState/text()"/>
                  </xsl:element>
                  <xsl:element name="postalCode">
                    <xsl:value-of select="./ContactInfo/WorkPostal/text()"/>
                  </xsl:element>
                  <xsl:element name="country">
                    <xsl:value-of select="./ContactInfo/WorkCountry/text()"/>
                  </xsl:element>
                </addr>
              </xsl:if>
              <xsl:if test="./ContactInfo/HomePhn/text() !=''">
                <xsl:element name="telecom">
                  <xsl:attribute name="use">
                    <xsl:value-of select="'HP'"/>
                  </xsl:attribute>
                  <xsl:attribute name="value">
                    <xsl:value-of select="concat('tel:', ./ContactInfo/HomePhn/text())"/>
                  </xsl:attribute>
                </xsl:element>
              </xsl:if>
              <xsl:if test="./ContactInfo/HomeMob/text() !=''">
                <xsl:element name="telecom">
                  <xsl:attribute name="use">
                    <xsl:value-of select="'MC'"/>
                  </xsl:attribute>
                  <xsl:attribute name="value">
                    <xsl:value-of select="concat('tel:', ./ContactInfo/HomeMob/text())"/>
                  </xsl:attribute>
                </xsl:element>
              </xsl:if>
              <associatedPerson>
                <name>
                  <xsl:element name="given">
                    <xsl:value-of select="./FirstName/text()"/>
                  </xsl:element>
                  <xsl:element name="family">
                    <xsl:value-of select="./LastName/text()"/>
                  </xsl:element>
                </name>
              </associatedPerson>
            </associatedEntity>
          </participant>
        </xsl:for-each>
      </xsl:if>
      <xsl:variable name="provideraddr1" select="normalize-space(//AdmissionProvider/Address/Address1)"/>
      <xsl:variable name="provideraddr2" select="normalize-space(//AdmissionProvider/Address/Address2)"/>
      <xsl:variable name="providercity" select="normalize-space(//AdmissionProvider/Address/City)"/>
      <xsl:variable name="providerstate" select="normalize-space(//AdmissionProvider/Address/State)"/>
      <xsl:variable name="providerzip" select="normalize-space(//AdmissionProvider/Address/Zip)"/>
      <xsl:variable name="providercountry" select="normalize-space(//AdmissionProvider/Address/Country)"/>
      <xsl:variable name="providerphone" select="normalize-space(//AdmissionProvider/Address/Telephone)"/>
      <xsl:variable name="providerfname" select="normalize-space(//AdmissionProvider/FirstName)"/>
      <xsl:variable name="providerlname" select="normalize-space(//AdmissionProvider/LastName)"/>
      <documentationOf>
        <serviceEvent classCode="PCPR">
          <effectiveTime>
            <xsl:variable name="ReportStartDate" select="normalize-space(//CDAQuery/@fromDate)"/>
            <xsl:variable name="ReportEndDate" select="normalize-space(//CDAQuery/@toDate)"/>
            <!-- The low value represents when the summarized provision of care began. -->
            <xsl:element name="low">
              <xsl:attribute name="value">
                <xsl:value-of select="$ReportStartDate"/>
              </xsl:attribute>
            </xsl:element>
            <xsl:element name="high">
              <xsl:attribute name="value">
                <xsl:value-of select="$ReportEndDate"/>
              </xsl:attribute>
            </xsl:element>
          </effectiveTime>
          <xsl:if test="count(//Patient/AdmissionProvider) &gt; 0">
            <performer typeCode = "PPRF">
              <functionCode code="PCP" codeSystem="2.16.840.1.113883.5.88" codeSystemName="PARTICIPATIONFUNCTION" displayName="PRIMARY CARE PHYSICIAN">
                <originalText>Primary Care Provider</originalText>
              </functionCode>
              <assignedEntity>
                <xsl:element name="id">
                  <xsl:attribute name="root">
                    <xsl:value-of select="'2.16.840.1.113883.4.6'"/>
                  </xsl:attribute>
                  <xsl:attribute name="extension">
                    <xsl:choose>
                      <xsl:when test="//Patient/AdmissionProvider/ProviderNPI/text() !=''">
                        <xsl:value-of select="//Patient/AdmissionProvider/ProviderNPI/text()"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="'UNK'"/>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
                </xsl:element>
                <xsl:choose>
                  <xsl:when test="count(//AdmissionProvider/Address) &gt; 0">
                    <addr>
                      <xsl:if test="$provideraddr1 !='' or $provideraddr2 !=''">
                        <streetAddressLine>
                          <xsl:value-of select="concat($provideraddr1,$provideraddr2)"/>
                        </streetAddressLine>
                      </xsl:if>
                      <xsl:if test="$providercity !=''">
                        <city>
                          <xsl:value-of select="$providercity"/>
                        </city>
                      </xsl:if>
                      <xsl:if test="$providerstate !=''">
                        <state>
                          <xsl:value-of select="$providerstate"/>
                        </state>
                      </xsl:if>
                      <xsl:if test="$providerzip">
                        <postalCode>
                          <xsl:value-of select="$providerzip"/>
                        </postalCode>
                      </xsl:if>
                      <xsl:if test="$providercountry">
                        <country>
                          <xsl:value-of select="$providercountry"/>
                        </country>
                      </xsl:if>
                    </addr>
                  </xsl:when>
                  <xsl:otherwise>
                    <addr nullFlavor="NI"/>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                  <xsl:when test="$providerphone !=''">
                    <telecom use = "WP">
                      <xsl:attribute name="value">
                        <xsl:value-of select="$providerphone"/>
                      </xsl:attribute>
                    </telecom>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="telecom">
                      <xsl:attribute name="nullFlavor">
                        <xsl:value-of select="'UNK'"/>
                      </xsl:attribute>
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <assignedPerson>
                  <name>
                    <given>
                      <xsl:choose>
                        <xsl:when test="$providerfname !=''">
                          <xsl:value-of select='$providerfname'/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="'UNK'"/>
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
                            <xsl:value-of select="'UNK'"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </family>
                  </name>
                </assignedPerson>
              </assignedEntity>
            </performer>
          </xsl:if>
          <xsl:if test="count(//Patient/CareTeam) &gt; 0">
            <xsl:variable name="careTeam" select="//Patient/CareTeam/Provider"/>
            <xsl:for-each select="$careTeam">
              <performer typeCode="PRF">
                <time>
                  <low nullFlavor="UNK"/>
                </time>
                <assignedEntity>
                  <!-- this provider has an id, but it is not an NPI  -->
                  <id extension="91138" root="1.3.6.1.4.1.22812.4.99930.4"/>
                  <!-- the provider is a Registered Nurse - may not be so -->
                  <!-- note: we don't know what Tracy is from the test data
						 but since not specified, RN should not be an issue -db -->
                  <code codeSystem="2.16.840.1.113883.6.101" codeSystemName="NUCC Health Care Provider Taxonomy" code="163W00000X" displayName="Registered Nurse"/>
                  <assignedPerson>
                    <name>
                      <xsl:if test="./FirstName/text() !=''">
                        <given>
                          <xsl:value-of select="./FirstName/text()"/>
                        </given>
                      </xsl:if>
                      <xsl:if test="./LastName/text() !=''">
                        <family>
                          <xsl:value-of select="./LastName/text()"/>
                        </family>
                      </xsl:if>
                    </name>
                  </assignedPerson>
                </assignedEntity>
              </performer>
            </xsl:for-each>
          </xsl:if>
        </serviceEvent>
      </documentationOf>
      <!-- added componentOf to represent encounter and to represent length of stay -->
      <xsl:if test="count(//EncounterDiagnosis) &gt; 0">
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
                  <xsl:when test="//EncounterDiagnosis/StartDate !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="//EncounterDiagnosis/StartDate/text()"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'UNK'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </low>
              <high>
                <xsl:choose>
                  <xsl:when test="//EncounterDiagnosis/EndDate !=''">
                    <xsl:attribute name="value">
                      <xsl:value-of select="//EncounterDiagnosis/EndDate"/>
                    </xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="nullFlavor">
                      <xsl:value-of select="'UNK'"/>
                    </xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </high>
            </effectiveTime>
          </encompassingEncounter>
        </componentOf>
      </xsl:if>
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
          <xsl:variable name="allergies" select="//CCDAASFHIR/AllergyIntolerance"/>
          <xsl:if test="count($allergies) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count($allergies) &gt; 0">
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.6.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
                    <!-- Alerts section template -->
                    <code code="48765-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                    <title>ALLERGIES AND ADVERSE REACTIONS</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Allergy Type</th>
                            <th>Substance</th>
                            <th>Reaction</th>
                            <th>Onset Date</th>
                            <th>Status</th>
                          </tr>
                        </thead>
                        <tbody>
                          <xsl:for-each select="$allergies">
                            <xsl:element name="tr">
                              <xsl:element name="td">
                                <xsl:choose>
                                  <xsl:when test="./category/@value !=''">
                                    <xsl:value-of select="./category/@value"/>
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
                                <xsl:if test="./code/coding/display/@value !=''">
                                  <xsl:value-of select="./code/coding/display/@value"/>
                                </xsl:if>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('ALGREACT_',position())"/>
                                </xsl:attribute>
                                <xsl:if test="./reaction/manifestation/coding/display/@value != ''">
                                  <xsl:value-of select="./reaction/manifestation/coding/display/@value"/>
                                </xsl:if>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('ALGONSET_',position())"/>
                                </xsl:attribute>
                                <xsl:if test="./onsetPeriod/start/@value != ''">
                                  <xsl:value-of select="./onsetPeriod/start/@value "/>
                                </xsl:if>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:value-of select="./clinicalStatus/@value"/>
                              </xsl:element>
                            </xsl:element>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:for-each select="$allergies">
                      <xsl:variable name="effectDT" select="normalize-space(translate(./onsetPeriod/start/@value,':-T',''))"/>
                      <xsl:variable name="expDT" select="normalize-space(./*[local-name()='ExpirationDate'])"/>
                      <entry typeCode="DRIV">
                        <act classCode="ACT" moodCode="EVN">
                          <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
                          <!-- ** Allergy problem act ** -->
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
                          <!--<code code="48765-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Allergies, adverse reactions, alerts"/>-->
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
                                    <xsl:value-of select="'UNK'"/>
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
                                <high nullFlavor="UNK"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </effectiveTime>
                          <entryRelationship typeCode="SUBJ">
                            <observation classCode="OBS" moodCode="EVN">
                              <!-- allergy observation template -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
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
                                        <xsl:value-of select="'UNK'"/>
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
                                    <high nullFlavor="UNK"/>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </effectiveTime>
                              <value xsi:type = "CD" code = "419511003" displayName = "Propensity to adverse reactions to drug (disorder)" codeSystem = "2.16.840.1.113883.6.96" codeSystemName = "SNOMED CT"/>
                              <xsl:if test="count(./code/coding/code) &gt; 0">
                                <participant typeCode="CSM">
                                  <participantRole classCode="MANU">
                                    <playingEntity classCode="MMAT">
                                      <xsl:variable name="codeSystemName" select="normalize-space(./code/coding/system/@value)"/>
                                      <xsl:element name="code">
                                        <xsl:attribute name="code">
                                          <xsl:value-of select="normalize-space(./code/coding/code/@value)"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="displayName">
                                          <xsl:value-of select="normalize-space(./code/coding/display/@value)"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="codeSystemName">
                                          <xsl:value-of select="$codeSystemName"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="codeSystem">
                                          <xsl:choose>
                                            <xsl:when test="contains($codeSystemName,'rxnorm')">
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
                              <xsl:if test="count(./Reaction/Code) &gt; 0 and ./Reaction/Code !=''">
                                <entryRelationship typeCode="MFST" inversionInd="true">
                                  <observation classCode="OBS" moodCode="EVN">
                                    <!-- ** Reaction Observation (V2) ** -->
                                    <templateId root="2.16.840.1.113883.10.20.22.4.9" extension="2014-06-09"/>
                                    <templateId root="2.16.840.1.113883.10.20.22.4.9"/>
                                    <id>
                                      <xsl:attribute name="root">
                                        <xsl:value-of select="generate-id()"/>
                                      </xsl:attribute>
                                    </id>
                                    <code code = "ASSERTION" codeSystem = "2.16.840.1.113883.5.4"/>
                                    <text>
                                      <xsl:element name="reference">
                                        <xsl:attribute name="value">
                                          <xsl:value-of select="concat('#ALGREACT_',position())"/>
                                        </xsl:attribute>
                                      </xsl:element>
                                    </text>
                                    <statusCode code="completed"/>
                                    <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96">
                                      <xsl:attribute name="displayName">
                                        <xsl:value-of select="./reaction/manifestation/coding/display/@value"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="code">
                                        <xsl:value-of select="./reaction/manifestation/coding/code/@value"/>
                                      </xsl:attribute>
                                    </value>
                                    <xsl:if test="count(./Severity/Code) &gt; 0 and normalize-space(./Severity/Code/text()) !=''">
                                      <!-- Severity of the allergy -->
                                      <entryRelationship typeCode="SUBJ" inversionInd="true">
                                        <observation classCode="OBS" moodCode="EVN">
                                          <templateId root="2.16.840.1.113883.10.20.22.4.8" extension="2014-06-09"/>
                                          <templateId root="2.16.840.1.113883.10.20.22.4.8"/>
                                          <!-- ** Severity observation template ** -->
                                          <code code="SEV" displayName="Severity Observation" codeSystem="2.16.840.1.113883.5.4" codeSystemName="ActCode"/>
                                          <text>
                                            <xsl:element name="reference">
                                              <xsl:attribute name="value">
                                                <xsl:value-of select="concat('#ALGSEV_',position())"/>
                                              </xsl:attribute>
                                            </xsl:element>
                                          </text>
                                          <statusCode code="completed"/>
                                          <xsl:element name="value">
                                            <xsl:choose>
                                              <xsl:when test="./Severity/Code != ''">
                                                <xsl:attribute name="code">
                                                  <xsl:value-of select="./*[local-name()='Severity' and namespace-uri()='']/*[local-name()='Code' and namespace-uri()='']"/>
                                                </xsl:attribute>
                                                <xsl:attribute name="displayName">
                                                  <xsl:value-of select="./*[local-name()='Severity' and namespace-uri()='']/*[local-name()='Name' and namespace-uri()='']"/>
                                                </xsl:attribute>
                                                <xsl:attribute name="xsi:type">
                                                  <xsl:value-of select="'CD'"/>
                                                </xsl:attribute>
                                                <xsl:attribute name="codeSystem">
                                                  <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                                                </xsl:attribute>
                                                <xsl:attribute name="codeSystemName">
                                                  <xsl:value-of select="'SNOMED CT'"/>
                                                </xsl:attribute>
                                              </xsl:when>
                                              <xsl:otherwise>
                                                <xsl:attribute name="nullflavor">
                                                  <xsl:value-of select="'UNK'"/>
                                                </xsl:attribute>
                                              </xsl:otherwise>
                                            </xsl:choose>
                                          </xsl:element>
                                        </observation>
                                      </entryRelationship>
                                    </xsl:if>
                                  </observation>
                                </entryRelationship>
                              </xsl:if>
                            </observation>
                          </entryRelationship>
                        </act>
                      </entry>
                    </xsl:for-each>
                  </section>
                </xsl:when>
                <xsl:otherwise>
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.6.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.6.1"/>
                    <!-- Alerts section template -->
                    <code code="48765-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                    <title>ALLERGIES AND ADVERSE REACTIONS</title>
                    <text>No Known Allergies</text>
                    <entry typeCode="DRIV">
                      <act classCode="ACT" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.30" extension="2015-08-01"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.30"/>
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
                        <statusCode code="active"/>
                        <effectiveTime>
                          <xsl:element name="low">
                            <xsl:attribute name="value">
                              <xsl:value-of select="normalize-space(//DocumentDate/text())"/>
                            </xsl:attribute>
                          </xsl:element>
                        </effectiveTime>
                        <entryRelationship typeCode="SUBJ">
                          <!-- using negationInd="true" to signify that there are is NO food allergy (disorder) allergy -db -->
                          <observation classCode="OBS" moodCode="EVN" negationInd="true">
                            <!-- ** Allergy observation (V2) ** -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.7" extension="2014-06-09"/>
                            <templateId root="2.16.840.1.113883.10.20.22.4.7"/>
                            <id root="4adc1020-7b16-11db-9fe1-0800200c9a66"/>
                            <code code="ASSERTION" codeSystem="2.16.840.1.113883.5.4"/>
                            <statusCode code="completed"/>
                            <effectiveTime nullFlavor="NA"/>
                            <value xsi:type="CD" code="419199007"
                              displayName="Allergy to substance (disorder)"
                              codeSystem="2.16.840.1.113883.6.96"
                              codeSystemName="SNOMED-CT">
                            </value>
                            <participant typeCode="CSM">
                              <participantRole classCode="MANU">
                                <playingEntity classCode="MMAT">
                                  <code nullFlavor="NA"/>
                                </playingEntity>
                              </participantRole>
                            </participant>
                          </observation>
                        </entryRelationship>
                      </act>
                    </entry>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!--
            ********************************************************
            Medication
            ********************************************************
            -->
          <xsl:variable name="meds" select="//Patient/Medications"/>
          <xsl:if test="count($meds) &gt; 0">
            <component>

              <xsl:choose>
                <xsl:when test="count($meds) &gt; 0">
                  <section>
                    <!-- *** Medications Section (entries required) (V2) *** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.1.1" extension="2014-06-09"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
                    <code code="10160-0" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="HISTORY OF MEDICATION USE"/>
                    <title>MEDICATIONS</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Medication</th>
                            <th>Start Date</th>
                            <th>Stop Date</th>
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
                            <xsl:variable name="strengthUnit" select="normalize-space(./StrengthUnit/text())"/>
                            <xsl:element name="tr">
                              <xsl:element name="td">
                                <xsl:element name="content">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('Med',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat($medName, ',',$strength,$strengthUnit,'[RxNorm:',$rxNorm,']')"/>
                                </xsl:element>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:value-of select="normalize-space(./StartDate/text())"/>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:value-of select="normalize-space(./StopDate/text())"/>
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
                          <!-- ** Medication Activity (V2) ** -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.16" extension="2014-06-09"/>
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
                            <xsl:value-of select="normalize-space(concat($medName,' ' ,$strength,' ',$route,' ',$form,'[RxNorm: ',$rxNorm,']'))"/>
                          </text>
                          <statusCode code="active"/>
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
                          <!-- InstituionSpecified = "true" means that it can be given 4 times per day but need not be exactly timed to every 6 hours-->
                          <!-- InstituionSpecified = "false" would mean that timing needs to be administered exactly as structured-->
                          <!-- Operator "A" means that this timing information is in addition to previous effectiveTime information provided-->
                          <effectiveTime xsi:type="PIVL_TS" institutionSpecified="false" operator="A">
                            <period>
                              <xsl:choose>
                                <xsl:when test="./Frequency ='' or normalize-space(./FrequencyUnit/text()) =''">
                                  <xsl:attribute name="nullFlavor">
                                    <xsl:value-of select="'UNK'"/>
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
                                    <xsl:value-of select="'UNK'"/>
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
                                <!--<xsl:attribute name="unit">
                                <xsl:value-of select="normalize-space(./DoseUnit/text())"/>
                              </xsl:attribute>-->
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
                              <!-- ** Medication information ** -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09"/>
                              <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
                              <xsl:element name="id">
                                <xsl:attribute name="root">
                                  <xsl:value-of select="generate-id()"/>
                                </xsl:attribute>
                              </xsl:element>
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
                                        <xsl:value-of select="'UNK'"/>
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
                  <section>
                    <!-- *** Medications Section (entries required) (V2) *** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.1.1" extension="2014-06-09"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.1.1"/>
                    <code code="10160-0" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="HISTORY OF MEDICATION USE"/>
                    <title>MEDICATIONS</title>
                    <text>No known Medications</text>
                    <entry>
                      <!-- Act.actionNegationInd -->
                      <substanceAdministration moodCode="EVN" classCode="SBADM" negationInd="true">
                        <!-- ** Medication Activity (V2) ** -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.16" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.16"/>
                        <id root="cdbd33f0-6cde-12db-9fe1-0800200c9a66"/>
                        <statusCode code="active"/>
                        <effectiveTime nullFlavor="NA"/>
                        <doseQuantity nullFlavor="NA"/>
                        <consumable>
                          <manufacturedProduct classCode="MANU">
                            <!-- ** Medication information ** -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09"/>
                            <templateId root="2.16.840.1.113883.10.20.22.4.23"/>
                            <manufacturedMaterial>
                              <code nullFlavor="OTH" codeSystem="2.16.840.1.113883.6.88">
                                <translation code="410942007" displayName="drug or medication"
                                  codeSystem="2.16.840.1.113883.6.96"
                                  codeSystemName="SNOMED-CT"/>
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
          </xsl:if>
          <!--
            ********************************************************
            PROBLEM LIST
            ********************************************************
            -->
          <xsl:variable name="problems" select="//CCDAASFHIR/Condition"/>
          <xsl:if test="count($problems) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count($problems) &gt; 0">
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.5.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.5.1"/>
                    <code code="11450-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="PROBLEM LIST"/>
                    <title>PROBLEMS</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Problem Name</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Status</th>
                          </tr>
                        </thead>
                        <tbody>
                          <xsl:for-each select="$problems">
                            <xsl:variable name="problem" select="normalize-space(./code/coding/code/@value)"/>
                            <xsl:variable name="status" select="normalize-space(./clinicalStatus/@value)"/>
                            <tr>
                              <td>
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('problem',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="concat(normalize-space(./code/coding/display/@value),' [', normalize-space(./code/coding/code/@value),']')"/>
                              </td>
                              <td>
                                <xsl:value-of select="normalize-space(./onsetDateTime/@value)"/>
                              </td>
                              <td>
                                <xsl:value-of select="normalize-space(./abatementDateTime/@value)"/>
                              </td>
                              <td>
                                <xsl:value-of select="normalize-space(./clinicalStatus/@value)"/>
                              </td>
                            </tr>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:for-each select="$problems">
                      <xsl:variable name="effDT" select="translate(normalize-space(./onsetDateTime/@value),'-:T','')"/>
                      <xsl:variable name="expDT" select="translate(normalize-space(./abatementDateTime/@value),'-:T','')"/>
                      <entry typeCode="DRIV">
                        <act classCode="ACT" moodCode="EVN">
                          <!-- Problem act template -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.3" extension="2015-08-01" />
                          <templateId root="2.16.840.1.113883.10.20.22.4.3"/>
                          <xsl:element name="id">
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </xsl:element>
                          <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
                          <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
                          <code code="CONC" codeSystem="2.16.840.1.113883.5.6" displayName="Concern"/>
                          <xsl:choose>
                            <xsl:when test="normalize-space(./clinicalStatus/@value)!=''">
                              <xsl:element name="statusCode">
                                <xsl:attribute name="code">
                                  <xsl:value-of select="translate(normalize-space(./clinicalStatus/@value), $uppercase, $smallcase)" />
                                </xsl:attribute>
                              </xsl:element>
                            </xsl:when>
                            <xsl:otherwise>
                              <statusCode code="active"/>
                            </xsl:otherwise>
                          </xsl:choose>
                          <effectiveTime>
                            <xsl:element name="low">
                              <xsl:choose>
                                <xsl:when test="$effDT != ''">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="$effDT"/>
                                  </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                  <xsl:attribute name="nullFlavor">
                                    <xsl:value-of select="'UNK'"/>
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
                                    <xsl:value-of select="'UNK'"/>
                                  </xsl:attribute>
                                </xsl:otherwise>
                              </xsl:choose>
                            </xsl:element>
                          </effectiveTime>
                          <entryRelationship typeCode="SUBJ">
                            <observation classCode="OBS" moodCode="EVN">
                              <!-- ** Problem observation  (V3)** -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
                              <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
                              <xsl:element name="id">
                                <xsl:attribute name="root">
                                  <xsl:value-of select="generate-id()"/>
                                </xsl:attribute>
                              </xsl:element>
                              <xsl:choose>
                                <xsl:when test="./TypeCode='64572001'">
                                  <code code="64572001" displayName="Condition" codeSystemName="SNOMED-CT" codeSystem="2.16.840.1.113883.6.96">
                                    <translation code="75323-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Condition"/>
                                  </code>
                                </xsl:when>
                                <xsl:otherwise>
                                  <code code="55607006" displayName="Problem" codeSystemName="SNOMED-CT" codeSystem="2.16.840.1.113883.6.96">
                                    <translation code="75326-9" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Problem"/>
                                  </code>
                                </xsl:otherwise>
                              </xsl:choose>
                              <text>
                                <xsl:element name="reference">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="concat('#problem',position())"/>
                                  </xsl:attribute>
                                </xsl:element>
                              </text>
                              <statusCode code="completed"/>
                              <effectiveTime>
                                <xsl:element name="low">
                                  <xsl:choose>
                                    <xsl:when test="$effDT != ''">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="$effDT"/>
                                      </xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:attribute name="nullFlavor">
                                        <xsl:value-of select="'UNK'"/>
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
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:attribute>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </xsl:element>
                              </effectiveTime>
                              <value xsi:type="CD">
                                <xsl:choose>
                                  <xsl:when test="normalize-space(./code/coding/code/@value) !=''">
                                    <xsl:attribute name="code">
                                      <xsl:value-of select="normalize-space(./code/coding/code/@value)"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="displayName">
                                      <xsl:value-of select="./code/coding/display/@value"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="codeSystem">
                                      <xsl:choose>
                                        <xsl:when test="contains(normalize-space(./code/coding/system/@value),'ICD9')">
                                          <xsl:value-of select="'2.16.840.1.113883.6.42'"/>
                                        </xsl:when>
                                        <xsl:when test="contains(normalize-space(./code/coding/system/@value),'ICD10')">
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
                                      <xsl:value-of select="'UNK'"/>
                                    </xsl:attribute>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </value>
                            </observation>
                          </entryRelationship>
                        </act>
                      </entry>
                    </xsl:for-each>
                  </section>
                </xsl:when>
                <xsl:otherwise>
                  <section>
                    <!--  *** Problem Section (entries required) (V3) *** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.5.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.5.1"/>
                    <code code="11450-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="PROBLEM LIST"/>
                    <title>PROBLEMS</title>
                    <text ID="Concern_1">
                      <content ID="problems1">
                        <content ID="problemType1">No Known Problems</content>
                      </content>
                    </text>
                    <entry typeCode="DRIV">
                      <!-- Problem Concern Act -->
                      <act classCode="ACT" moodCode="EVN">
                        <!-- ** Problem Concern Act (V3) ** -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.3" extension="2015-08-01" />
                        <templateId root="2.16.840.1.113883.10.20.22.4.3" />
                        <id root="36e3e930-7b14-13db-9fe1-0800200c9a66"/>
                        <!-- SDWG supports 48765-2 or CONC in the code element -->
                        <code code="CONC" codeSystem="2.16.840.1.113883.5.6"/>
                        <text>
                          <reference value="#Concern_1"></reference>
                        </text>
                        <statusCode code="active"/>
                        <effectiveTime>
                          <xsl:element name="low">
                            <xsl:attribute name="value">
                              <xsl:value-of select="normalize-space(//DocumentDate/text())"/>
                            </xsl:attribute>
                          </xsl:element>
                        </effectiveTime>
                        <entryRelationship typeCode="SUBJ">
                          <observation classCode="OBS" moodCode="EVN" negationInd="true">
                            <!-- ** Problem observation  (V3)** -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
                            <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
                            <id root="4adc1021-7b15-14db-9fe1-0800200c9a67"/>
                            <!-- updated for R2.1 -db -->
                            <code code="55607006" displayName="Problem" codeSystemName="SNOMED-CT" codeSystem="2.16.840.1.113883.6.96">
                              <!-- This code SHALL contain at least one [1..*] translation, which SHOULD be selected from ValueSet Problem Type (LOINC) -->
                              <translation code="75326-9" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Problem"/>
                            </code>
                            <text>
                              <reference value="#problems1"></reference>
                            </text>
                            <statusCode code="completed"/>
                            <effectiveTime>
                              <xsl:element name="low">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="normalize-space(//DocumentDate/text())"/>
                                </xsl:attribute>
                              </xsl:element>
                            </effectiveTime>
                            <value xsi:type="CD" code="55607006"
                              displayName="Problem"
                              codeSystem="2.16.840.1.113883.6.96"
                              codeSystemName="SNOMED-CT">
                              <originalText>
                                <reference value="#problems1"></reference>
                              </originalText>
                            </value>
                          </observation>
                        </entryRelationship>
                      </act>
                    </entry>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!--
            ********************************************************
            SOCIAL HISTORY
            ********************************************************
            -->
          <xsl:if test="count(//Patient/SmokingStatus) &gt;0">
            <component>
              <!--   Social History ******** -->
              <section>
                <!--  ** Social History Section (V3) ** -->
                <templateId root="2.16.840.1.113883.10.20.22.2.17" extension="2015-08-01"/>
                <templateId root="2.16.840.1.113883.10.20.22.2.17"/>
                <!--  ********  Social history section template   ******** -->
                <code code="29762-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Social History"/>
                <title>SOCIAL HISTORY</title>
                <text>
                  <table border="1" width="100%">
                    <thead>
                      <tr>
                        <th>Social History Observation</th>
                        <th>Description</th>
                        <th>Dates Observed</th>
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
                                <xsl:variable name="startDate" select="normalize-space(./Date/text())"/>
                                <xsl:value-of select="$startDate"/>
                              </td>
                            </tr>
                          </xsl:for-each>
                          <tr>
                            <td ID="BirthSexInfo">
                              Birth Sex
                            </td>
                            <td>
                              <xsl:value-of select="//Patient/Gender/Name/text()"/>
                            </td>
                            <td>
                              <xsl:value-of select="//DocumentDate/text()"/>
                            </td>
                          </tr>
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
                <xsl:for-each select="//Patient/SmokingStatus">
                  <xsl:variable name="smokingcode" select="normalize-space(./Code/text())"/>
                  <xsl:variable name="smokingname" select="normalize-space(./Name/text())"/>
                  <entry typeCode="DRIV">
                    <observation classCode="OBS" moodCode="EVN">
                      <!-- ** Smoking Status - Meaningful Use (V2) ** -->
                      <templateId root="2.16.840.1.113883.10.20.22.4.78" extension="2014-06-09"/>
                      <templateId root="2.16.840.1.113883.10.20.22.4.78"/>
                      <id root="2.16.840.1.113883.19"/>
                      <code code="72166-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Tobacco smoking status NHIS"/>
                      <statusCode code="completed"/>
                      <xsl:variable name="startDate" select="normalize-space(./Date/text())"/>
                      <effectiveTime>
                        <xsl:choose>
                          <xsl:when test="$startDate != ''">
                            <xsl:attribute name="value">
                              <xsl:value-of select="$startDate"/>
                            </xsl:attribute>
                          </xsl:when>
                          <xsl:otherwise>
                            <xsl:attribute name="nullFlavor">
                              <xsl:value-of select="'UNK'"/>
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
                </xsl:for-each>
                <!-- Add Birth Sex entry -->
                <entry>
                  <observation classCode="OBS" moodCode="EVN">
                    <templateId root="2.16.840.1.113883.10.20.22.4.200" extension="2016-06-01"/>
                    <code code="76689-9" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Sex Assigned At Birth"/>
                    <text>
                      <reference value="#BirthSexInfo"/>
                    </text>
                    <statusCode code="completed"/>
                    <effectiveTime value="20150722"/>
                    <xsl:element name="value">
                      <xsl:attribute name="xsi:type">
                        <xsl:value-of select="'CD'"/>
                      </xsl:attribute>
                      <xsl:attribute name="codeSystem">
                        <xsl:value-of select="'2.16.840.1.113883.5.1'"/>
                      </xsl:attribute>
                      <xsl:choose>
                        <xsl:when test="//Patient/Gender/Code !='' and (//Patient/Gender/Code = 'M' or //Patient/Gender/Code = 'F' or //Patient/Gender/Code = 'UN')">
                          <xsl:attribute name="code">
                            <xsl:value-of select="//Patient/Gender/Code"/>
                          </xsl:attribute>
                          <xsl:attribute name="displayName">
                            <xsl:value-of select="//Patient/Gender/Name"/>
                          </xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:attribute name="nullFlavor">
                            <xsl:value-of select="UNK"/>
                          </xsl:attribute>
                        </xsl:otherwise>
                      </xsl:choose>
                    </xsl:element>
                  </observation>
                </entry>
              </section>
            </component>
          </xsl:if>
          <!--
            ********************************************************
            VITAL SIGNS
            ********************************************************
            -->
          <xsl:if test="count(//VitalSigns) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count(//VitalSigns) &gt; 0">
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.4.1" extension="2015-08-01"/>
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
                        <!-- Vital signs organizer template -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.26" extension="2015-08-01"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.26"/>
                        <xsl:element name="id">
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </xsl:element>
                        <code code="46680005" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" displayName="Vital signs">
                          <!-- A vitals organizer conformant to both C-CDA 1.1 and C-CDA 2.1 would contain the SNOMED code (46680005) from R1.1 in the root code and a LOINC code in the translation -->
                          <translation code="74728-7" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Vital signs"/>
                        </code>
                        <statusCode code="completed"/>
                        <xsl:element name="effectiveTime">
                          <xsl:attribute name="value">
                            <xsl:value-of select="//DocumentDate/text()"/>
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
                                <templateId root="2.16.840.1.113883.10.20.22.4.27" extension="2014-06-09"/>
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
                                <!--<code code="8302-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Height"/>
                              <text>
                                <reference>
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="concat('#vit-Type', position())"/>
                                  </xsl:attribute>
                                </reference>
                              </text>-->
                                <statusCode code="completed"/>
                                <xsl:element name="effectiveTime">
                                  <xsl:choose>
                                    <xsl:when test="$recordDT !=''">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="$recordDT"/>
                                      </xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:attribute name="nullFlavor">
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:attribute>
                                    </xsl:otherwise>
                                  </xsl:choose>
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
                                        <xsl:value-of select="'UNK'"/>
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
                    <templateId root="2.16.840.1.113883.10.20.22.2.4.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.4.1"/>
                    <code code="8716-3" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="VITAL SIGNS"/>
                    <title>VITAL SIGNS</title>
                    <text>No Vital Signs Information</text>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!--
           ********************************************************
           RESULTS
           ********************************************************
              -->
          <xsl:variable name="results" select="//LabResults"/>
          <xsl:variable name="radiology" select="//RadiologyReport"/>
          <xsl:if test="count($results) &gt; 0 or count($radiology) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count($results) &gt; 0 or count($radiology) &gt; 0">
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.3.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.3.1"/>
                    <code code="30954-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Relevant diagnostic tests and/or laboratory data"/>
                    <!-- 
                  <code code="30954-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="RESULTS"/> -->
                    <title>RESULTS</title>
                    <text>
                      <xsl:if test="count($results) &gt; 0">
                        <table border="1" width="100%">
                          <thead>
                            <tr>
                              <th colspan="4">LABORATORY INFORMATION</th>
                            </tr>
                            <tr>
                              <td colspan="4">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('labfacility',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select ="concat('Lab Facility -',//LabFacility/Name/text())"/>
                                <xsl:value-of select="concat(' Address - ',//LabFacility/Address/Address1/text())"/>
                                <xsl:value-of select="concat(', ',normalize-space(//LabFacility/Address/City/text()))"/>
                                <xsl:value-of select="concat(', ',normalize-space(//LabFacility/Address/State/text()))"/>
                                <xsl:value-of select="concat(', ',normalize-space(//LabFacility/Address/Country/text()))"/>
                                <xsl:value-of select="concat(', ',normalize-space(//LabFacility/Address/Zip/text()))"/>
                                <xsl:value-of select="concat(', ph:+1',normalize-space(//LabFacility/Address/Telephone/text()))"/>
                              </td>
                            </tr>
                          </thead>
                          <tbody>
                            <xsl:for-each select="$results">
                              <xsl:variable name="resultCount" select="position()"/>
                              <tr>
                                <th colspan="4">
                                  <xsl:value-of select="concat('Lab Test: ',./TestName/text(),'[',./TestCode/text(),']')"/>
                                </th>
                              </tr>
                              <xsl:for-each select="./Values">
                                <xsl:variable name="ResultDescription" select="normalize-space(./Name/text())"/>
                                <xsl:variable name="ReferenceRange" select="normalize-space(./ReferenceRange/text())"/>
                                <xsl:variable name="Unit" select="normalize-space(./Unit/text())"/>
                                <tr>
                                  <td>
                                    <xsl:element name="content">
                                      <xsl:attribute name="ID">
                                        <xsl:value-of select="concat('result',$resultCount,'-',position())"/>
                                      </xsl:attribute>
                                    </xsl:element>
                                    <xsl:value-of select="concat('Results: ', $ResultDescription)"/>
                                  </td>
                                  <td>
                                    <xsl:if test="$ReferenceRange != ''">
                                      <xsl:value-of select="concat('Reference Range: ', $ReferenceRange)"/>
                                    </xsl:if>
                                  </td>
                                  <td>
                                    <xsl:if test="./Value/text() != ''">
                                      <xsl:value-of select="concat('Value: ', ./Value/text(),$Unit)"/>
                                    </xsl:if>
                                  </td>
                                </tr>
                              </xsl:for-each>
                            </xsl:for-each>
                          </tbody>
                        </table>
                      </xsl:if>
                      <xsl:if test="count($radiology) &gt; 0">
                        <table border="1" width="100%">
                          <thead>
                            <tr>
                              <th colspan="4">DIAGNOSTIC IMAGING REPORT</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <th>
                                Radiology Exam
                              </th>
                              <th>Date</th>
                              <th>Interpretation</th>
                            </tr>
                            <xsl:for-each select="$radiology">
                              <tr>
                                <td>
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('rad',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat(./DisplayName/text(), ' [',./Code/text(),']')"/>
                                </td>
                                <td>
                                  <xsl:value-of select="./TestDate/text()"/>
                                </td>
                                <td>
                                  <xsl:value-of select="./Comments/text()"/>
                                </td>
                              </tr>
                            </xsl:for-each>
                          </tbody>
                        </table>
                      </xsl:if>
                    </text>
                    <xsl:for-each select="$results">
                      <xsl:variable name="recorddate" select="./RecordDate/text()"/>
                      <entry typeCode="DRIV">
                        <xsl:variable name="resultCount" select="position()"/>
                        <organizer classCode="BATTERY" moodCode="EVN">
                          <!-- Result organizer template -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.1" extension="2015-08-01"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.1"/>
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC">
                            <xsl:choose>
                              <xsl:when test="normalize-space(./TestCode/text()) !=''">
                                <xsl:attribute name="code">
                                  <xsl:value-of select="normalize-space(./TestCode/text())"/>
                                </xsl:attribute>
                                <xsl:attribute name="displayName">
                                  <xsl:value-of select="normalize-space(./TestName/text())"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'UNK'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </code>
                          <statusCode code="completed"/>
                          <effectiveTime>
                            <xsl:choose>
                              <xsl:when test="normalize-space(./TestDate/text()) != ''">
                                <xsl:element name="low">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="normalize-space(./TestDate/text())"/>
                                  </xsl:attribute>
                                </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="UNK"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                            <xsl:choose>
                              <xsl:when test="normalize-space(./TestDate/text()) != ''">
                                <xsl:element name="high">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="normalize-space(./TestDate/text())"/>
                                  </xsl:attribute>
                                </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="UNK"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </effectiveTime>
                          <xsl:for-each select="./Values">
                            <component>
                              <observation classCode="OBS" moodCode="EVN">
                                <!-- Result observation template -->
                                <templateId root="2.16.840.1.113883.10.20.22.4.2" extension="2015-08-01"/>
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
                                  <xsl:choose>
                                    <xsl:when test="./EffectiveDate/text() !=''">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="normalize-space(./EffectiveDate/text())"/>
                                      </xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                      <xsl:attribute name="nullFlavor">
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:attribute>
                                    </xsl:otherwise>
                                  </xsl:choose>
                                </effectiveTime>
                                <xsl:variable name="alpha" select="'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
                                <xsl:choose>
                                  <xsl:when test="contains(normalize-space(./Value/text()), 'a') or contains(normalize-space(./Value/text()), 'A') or contains(normalize-space(./Value/text()), 'e') or contains(normalize-space(./Value/text()), 'E') or contains(normalize-space(./Value/text()), 'i')">
                                    <xsl:element name="value">
                                      <xsl:attribute name="xsi:type">
                                        <xsl:value-of select="'ST'"/>
                                      </xsl:attribute>
                                      <xsl:value-of select="normalize-space(./Value/text())"/>
                                    </xsl:element>
                                  </xsl:when>
                                  <xsl:when test="normalize-space(./Value/text()) !='' and number(./Value) = ./Value">
                                    <xsl:element name="value">
                                      <xsl:attribute name="xsi:type">
                                        <xsl:value-of select="'PQ'"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="normalize-space(./Value/text())"/>
                                      </xsl:attribute>
                                      <xsl:if test="normalize-space(./Unit/text()) !=''">
                                        <xsl:attribute name="unit">
                                          <xsl:value-of select="normalize-space(./Unit/text())"/>
                                        </xsl:attribute>
                                      </xsl:if>
                                    </xsl:element>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:attribute name="nullFlavor">
                                      <xsl:value-of select="'NI'"/>
                                    </xsl:attribute>
                                  </xsl:otherwise>
                                </xsl:choose>
                                <interpretationCode codeSystem="2.16.840.1.113883.5.83" code="N"/>
                                <xsl:variable name="referenceRangeStart" select="normalize-space(./ReferenceRangeStart/text())"/>
                                <xsl:variable name="referenceRangeEnd" select="normalize-space(./ReferenceRangeEnd/text())"/>
                                <xsl:variable name="unit" select="normalize-space(./Unit/text())"/>
                                <xsl:choose>
                                  <xsl:when test="$referenceRangeStart != '' and $referenceRangeEnd !=''">
                                    <referenceRange>
                                      <observationRange>
                                        <value xsi:type="IVL_PQ">
                                          <xsl:element name="low">
                                            <xsl:attribute name="value">
                                              <xsl:value-of select="$referenceRangeStart"/>
                                            </xsl:attribute>
                                          </xsl:element>
                                          <xsl:element name="high">
                                            <xsl:attribute name="value">
                                              <xsl:value-of select="$referenceRangeEnd"/>
                                            </xsl:attribute>
                                          </xsl:element>
                                        </value>
                                      </observationRange>
                                    </referenceRange>
                                  </xsl:when>
                                </xsl:choose>
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
                    <templateId root="2.16.840.1.113883.10.20.22.2.3.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.3.1"/>
                    <code code="30954-2" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="RESULTS"/>
                    <title>RESULTS</title>
                    <text>No Lab Test required. No Lab results.</text>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!-- ************************************
            Encounter  Diagnoses
            ************************************ -->
          <xsl:if test="count(//EncounterDiagnosis) &gt; 0">
            <xsl:choose>
              <xsl:when test="count(//EncounterDiagnosis) &gt; 0">
                <component>
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.22.1" extension="2015-08-01"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.22.1"/>
                    <!-- Encounters Section - Entries optional -->
                    <code code="46240-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Encounters"/>
                    <title>ENCOUNTERS</title>
                    <text>
                      <table border = "1" width = "100%">
                        <thead>
                          <tr>
                            <th>Encounter Diagnosis</th>
                            <th>Encounter</th>
                            <th>Performer</th>
                            <th>Date</th>
                          </tr>
                        </thead>
                        <tbody>
                          <xsl:for-each select="//EncounterDiagnosis">
                            <tr>
                              <td>
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('encproblem', position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="concat(./Problem/Name/text(),'[',./Problem/Code/text(),']')"/>
                              </td>
                              <td>
                                <xsl:value-of select="concat(./Name/text(),'[',./Code/text(),']')"/>
                              </td>
                              <td>
                                <xsl:value-of select="concat(./Performer/FirstName/text(),' ', ./Performer/LastName/text())"/>
                              </td>
                              <td>
                                <xsl:value-of select="./StartDate/text()"/>
                              </td>
                            </tr>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:if test="count(//EncounterDiagnosis) &gt; 0">
                      <xsl:for-each select="//EncounterDiagnosis">
                        <entry typeCode="DRIV">
                          <encounter classCode="ENC" moodCode="EVN">
                            <templateId root="2.16.840.1.113883.10.20.22.4.49" extension="2015-08-01"/>
                            <templateId root="2.16.840.1.113883.10.20.22.4.49"/>
                            <id>
                              <xsl:attribute name="root">
                                <xsl:value-of select="generate-id()"/>
                              </xsl:attribute>
                            </id>
                            <xsl:element name="code">
                              <xsl:attribute name="code">
                                <xsl:value-of select="./Code/text()"/>
                              </xsl:attribute>
                              <xsl:attribute name="displayName">
                                <xsl:value-of select="./Name/text()"/>
                              </xsl:attribute>
                              <xsl:attribute name="codeSystemName">
                                <xsl:value-of select="./CodeSystemName/text()"/>
                              </xsl:attribute>
                              <xsl:choose>
                                <xsl:when test="normalize-space(./CodeSystemName/text()) ='CPT'">
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
                              <originalText>
                                <xsl:element name="reference">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="concat('#encproblem', position())"/>
                                  </xsl:attribute>
                                </xsl:element>
                              </originalText>
                              <translation code="AMB" codeSystem="2.16.840.1.113883.5.4" displayName="Ambulatory" codeSystemName="HL7 ActEncounterCode" />
                            </xsl:element>
                            <effectiveTime>
                              <xsl:variable name="effDT" select="normalize-space(./StartDate)"/>
                              <xsl:choose>
                                <xsl:when test="$effDT != ''">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="$effDT"/>
                                  </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                  <xsl:attribute name="nullFlavor">
                                    <xsl:value-of select="'UNK'"/>
                                  </xsl:attribute>
                                </xsl:otherwise>
                              </xsl:choose>
                            </effectiveTime>
                            <xsl:if test="normalize-space(./Problem/Code/text()) !=''">
                              <xsl:if test="count(./Performer) &gt; 0">
                                <performer>
                                  <assignedEntity>
                                    <xsl:element name="id">
                                      <xsl:attribute name="extension">
                                        <xsl:value-of select="normalize-space(./Performer/ProviderNPI/text())"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="root">
                                        <xsl:value-of select="'2.16.840.1.113883.4.6'"/>
                                      </xsl:attribute>
                                    </xsl:element>
                                    <xsl:if test="./Performer/Code !=''">
                                      <code  codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT">
                                        <xsl:attribute name="code">
                                          <xsl:value-of select="./Performer/Code"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="displayName">
                                          <xsl:value-of select="./Performer/Name"/>
                                        </xsl:attribute>
                                      </code>
                                    </xsl:if>
                                  </assignedEntity>
                                </performer>
                              </xsl:if>
                              <entryRelationship typeCode="SUBJ">
                                <!-- ** Encounter Diagnosis (V3)** -->
                                <act classCode="ACT" moodCode="EVN">
                                  <templateId root="2.16.840.1.113883.10.20.22.4.80" extension="2015-08-01"/>
                                  <templateId root="2.16.840.1.113883.10.20.22.4.19"/>
                                  <id>
                                    <xsl:attribute name="root">
                                      <xsl:value-of select="generate-id()"/>
                                    </xsl:attribute>
                                  </id>
                                  <code xsi:type="CE" code="29308-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="ENCOUNTER DIAGNOSIS"/>
                                  <effectiveTime>
                                    <xsl:element name="low">
                                      <xsl:attribute name="value">
                                        <xsl:value-of select="./StartDate/text()"/>
                                      </xsl:attribute>
                                    </xsl:element>
                                  </effectiveTime>
                                  <entryRelationship typeCode="SUBJ" inversionInd="false">
                                    <!-- Problem Observation (V3) -->
                                    <observation classCode="OBS" moodCode="EVN">
                                      <templateId root="2.16.840.1.113883.10.20.22.4.4" extension="2015-08-01"/>
                                      <templateId root="2.16.840.1.113883.10.20.22.4.4"/>
                                      <id>
                                        <xsl:attribute name="root">
                                          <xsl:value-of select="generate-id()"/>
                                        </xsl:attribute>
                                      </id>
                                      <code code="404684003" displayName="Finding" codeSystemName="SNOMED-CT" codeSystem="2.16.840.1.113883.6.96">
                                        <translation code="75321-0" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Clinical finding"/>
                                      </code>
                                      <statusCode code="completed"/>
                                      <effectiveTime>
                                        <xsl:element name="low">
                                          <xsl:attribute name="value">
                                            <xsl:value-of select="./StartDate/text()"/>
                                          </xsl:attribute>
                                        </xsl:element>
                                      </effectiveTime>
                                      <xsl:element name="value">
                                        <xsl:attribute name="xsi:type">
                                          <xsl:value-of select="'CD'"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="code">
                                          <xsl:choose>
                                            <xsl:when test="normalize-space(./Problem/Code/text()) !=''">
                                              <xsl:value-of select="normalize-space(./Problem/Code/text())"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                              <xsl:value-of select="'UNK'"/>
                                            </xsl:otherwise>
                                          </xsl:choose>
                                        </xsl:attribute>
                                        <xsl:attribute name="codeSystem">
                                          <xsl:choose>
                                            <xsl:when test="normalize-space(./Problem/CodeSystem/text()) ='ICD9'">
                                              <xsl:value-of select="'2.16.840.1.113883.6.42'"/>
                                            </xsl:when>
                                            <xsl:when test="normalize-space(./Problem/CodeSystem/text()) ='ICD10'">
                                              <xsl:value-of select="'2.16.840.1.113883.6.3'"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                              <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                                            </xsl:otherwise>
                                          </xsl:choose>
                                        </xsl:attribute>
                                        <xsl:attribute name="displayName">
                                          <xsl:value-of select="normalize-space(./Problem/Name/text())"/>
                                        </xsl:attribute>
                                      </xsl:element>
                                    </observation>
                                  </entryRelationship>
                                </act>
                              </entryRelationship>
                            </xsl:if>
                          </encounter>
                        </entry>
                      </xsl:for-each>
                    </xsl:if>
                  </section>
                </component>
              </xsl:when>
            </xsl:choose>
          </xsl:if>
          <xsl:variable name="referrals" select="normalize-space(//Referral/ReasonforReferral/text())"/>
          <xsl:variable name="referralAddress1" select="normalize-space(//Referral/Address/Address1/text())"/>
          <xsl:variable name="referralCity" select="normalize-space(//Referral/Address/City/text())"/>
          <xsl:variable name="referralState" select="normalize-space(//Referral/Address/State/text())"/>
          <xsl:variable name="referralZip" select="normalize-space(//Referral/Address/Zip/text())"/>
          <xsl:variable name="referralCountry" select="normalize-space(//Referral/Address/Country/text())"/>
          <xsl:variable name="referralTelePh" select="normalize-space(//Referral/Address/Telephone1/text())"/>
          <xsl:variable name="referralDate" select="normalize-space(//Referral/ScheduledDate/text())"/>
          <xsl:if test="count(//Referral) &gt; 0">
            <component>
              <section>
                <templateId root="1.3.6.1.4.1.19376.1.5.3.1.3.1" extension="2014-06-09"/>
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
                          <xsl:value-of select="normalize-space($referralTelePh)"/>
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
                <xsl:if test="count(//Referral) &gt; 0">
                  <entry>
                    <act classCode="PCPR" moodCode="INT">
                      <templateId root="2.16.840.1.113883.10.20.22.4.140"/>
                      <id>
                        <xsl:attribute name="root">
                          <xsl:value-of select="generate-id()"/>
                        </xsl:attribute>
                      </id>
                      <code code="44383000" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT" displayName="Patient referral for consultation"/>
                      <statusCode code="active"/>
                      <effectiveTime/>
                      <author>
                        <time/>
                        <assignedAuthor>
                          <id root="2.16.840.1.113883.4.6" extension="1"/>
                          <code/>
                          <addr nullFlavor="UNK"/>
                          <telecom nullFlavor="UNK"/>
                          <assignedPerson>
                            <name>
                              <xsl:element name="given">
                                <xsl:value-of select="normalize-space(//Referral/ReferredTo/text())"/>
                              </xsl:element>
                            </name>
                          </assignedPerson>
                        </assignedAuthor>
                      </author>
                    </act>
                  </entry>
                </xsl:if>
              </section>
            </component>
          </xsl:if>
          <!--
            ********************************************************
            Health Concerns
            ********************************************************
            -->
          <xsl:if test="count(//HealthConcerns) &gt; 0 or count(//HealthStatus) &gt; 0">
            <xsl:choose>
              <xsl:when test="count(//HealthConcerns) &gt; 0 or count(//HealthStatus) &gt; 0">
                <component>
                  <section>
                    <!-- Health Concerns Section (V2) (V1 was added as a NEW template in R2.0, V2 was updated in R2.1) -db -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.58" extension="2015-08-01"/>
                    <code code="75310-3" displayName="Health Concerns Document" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                    <title>HEALTH CONCERNS SECTION</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Health Status</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>
                              <xsl:value-of select="concat(//HealthStatus/Description/text(),'[',//HealthStatus/Code/text(),']')"/>
                            </td>
                          </tr>
                          <xsl:if test="count(//HealthConcerns/Problems) &gt; 0">
                            <xsl:variable name="healthconcerns" select="//HealthConcerns/Problems"/>
                            <xsl:for-each select="$healthconcerns">
                              <tr>
                                <th>Health Concern</th>
                              </tr>
                              <xsl:for-each select="$healthconcerns/Intervention">
                                <tr>
                                  <td>
                                    <xsl:value-of select="./Description/text()"/>
                                  </td>
                                </tr>
                              </xsl:for-each>
                            </xsl:for-each>
                          </xsl:if>
                        </tbody>
                      </table>
                    </text>
                    <entry>
                      <observation classCode="OBS" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.5" extension="2014-06-09"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.5"/>
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code code="11323-3" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Health Status"/>
                        <statusCode code="completed"/>
                        <xsl:element name="value">
                          <xsl:attribute name="xsi:type">
                            <xsl:value-of select="'CD'"/>
                          </xsl:attribute>
                          <xsl:attribute name="code">
                            <xsl:value-of select="normalize-space(//HealthStatus/Code/text())"/>
                          </xsl:attribute>
                          <xsl:attribute name="displayName">
                            <xsl:value-of select="normalize-space(//HealthStatus/Description/text())"/>
                          </xsl:attribute>
                          <xsl:attribute name="codeSystem">
                            <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                          </xsl:attribute>
                          <xsl:attribute name="codeSystemName">
                            <xsl:value-of select="'SNOMED-CT'"/>
                          </xsl:attribute>
                        </xsl:element>
                      </observation>
                    </entry>
                    <entry>
                      <!-- Health Concerns Act (V2) (V1 was added as a NEW template in R2.0, V2 was updated in R2.1) -db -->
                      <act classCode="ACT" moodCode="EVN">
                        <templateId root="2.16.840.1.113883.10.20.22.4.132" extension="2015-08-01"/>
                        <id>
                          <xsl:attribute name="root">
                            <xsl:value-of select="generate-id()"/>
                          </xsl:attribute>
                        </id>
                        <code code="75310-3" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Health Concern"/>
                        <statusCode code="completed"/>
                        <!-- Concerns -db -->
                        <entryRelationship typeCode="REFR">
                          <act classCode="ACT" moodCode="EVN">
                            <templateId root="2.16.840.1.113883.10.20.22.4.122"/>
                            <!-- This ID equals the problem, HyperTension in this case -db -->
                            <id root="ab1791b1-5c71-11db-b0de-0800200c9a66"/>
                            <code nullFlavor="NP"/>
                            <statusCode code="completed"/>
                          </act>
                        </entryRelationship>
                      </act>
                    </entry>
                  </section>
                </component>
              </xsl:when>
              <xsl:otherwise>
                <component>
                  <section nullFlavor="NI">
                    <!-- Health Concerns Section (V2) (V1 was added as a NEW template in R2.0, V2 was updated in R2.1) -db -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.58" extension="2015-08-01"/>
                    <code code="75310-3" displayName="Health Concerns Document"
                      codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                    <title>HEALTH CONCERNS</title>
                    <text>No Health Concerns Information</text>
                  </section>
                </component>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
          <!--
            ********************************************************
            Immunization
            ********************************************************
            -->
          <xsl:if test="count(//CCDAASFHIR/Immunization) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count(//CCDAASFHIR/Immunization) &gt; 0">
                  <section>
                    <!-- *** Immunizations Section (entries required) (V2) *** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.2.1" extension="2014-06-09"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.2.1"/>
                    <code code="11369-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="History of immunization"/>
                    <title>IMMUNIZATIONS</title>
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
                          <xsl:for-each select="//CCDAASFHIR/Immunization">
                            <tr>
                              <td>
                                <content ID="immun1">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select=" concat('immun', position())"/>
                                  </xsl:attribute>
                                </content>
                                <xsl:value-of select="concat(normalize-space(./vaccineCode/coding/display/@value),' [',normalize-space(./vaccineCode/coding/code/@value),']')"/>
                              </td>
                              <td>
                                <xsl:value-of select="translate(normalize-space(./date/@value),':-T','')"/>
                              </td>
                              <td>
                                <xsl:choose>
                                  <xsl:when test="normalize-space(./notGiven/@value) = 'true'">
                                    <xsl:value-of select="'Declined'"/>
                                  </xsl:when>
                                  <xsl:otherwise>Completed</xsl:otherwise>
                                </xsl:choose>
                              </td>
                            </tr>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:for-each select="//CCDAASFHIR/Immunization">
                      <entry typeCode="DRIV">
                        <substanceAdministration classCode="SBADM" moodCode="EVN">
                          <xsl:attribute name="negationInd">
                            <xsl:value-of select="./notGiven/@value"/>
                          </xsl:attribute>
                          <!-- ** Immunization Activity (V3) ** -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.52" extension="2015-08-01"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
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
                          <statusCode>
                            <xsl:attribute name="code">
                              <xsl:value-of select="./status/@value"/>
                            </xsl:attribute>
                          </statusCode>
                          <effectiveTime xsi:type="IVL_TS">
                            <xsl:choose>
                              <xsl:when test="./date/@value != ''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="translate(normalize-space(./date/@value),'-:T','')"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'UNK'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </effectiveTime>
                          <xsl:if test="count(./route) &gt; 0 and ./route/coding/code/@value !=''">
                            <routeCode codeSystem="2.16.840.1.113883.3.26.1.1" codeSystemName="National Cancer Institute (NCI) Thesaurus" displayName="Intramuscular injection">
                              <xsl:attribute name="code">
                                <xsl:value-of select="normalize-space(./route/coding/code/@value)"/>
                              </xsl:attribute>
                              <xsl:attribute name="displayName">
                                <xsl:value-of select="normalize-space(./route/coding/display/@value)"/>
                              </xsl:attribute>
                            </routeCode>
                          </xsl:if>
                          <consumable>
                            <manufacturedProduct classCode="MANU">
                              <!-- ** Immunization Medication Information (V2) ** -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.54" extension="2014-06-09"/>
                              <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
                              <xsl:variable name="immzcode">
                                <xsl:choose>
                                  <xsl:when test="string-length(normalize-space(./vaccineCode/coding/code/@value)) = 1">
                                    <xsl:value-of select="concat('0',normalize-space(./vaccineCode/coding/code/@value))"/>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:value-of select="normalize-space(./vaccineCode/coding/code/@value)"/>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </xsl:variable>
                              <manufacturedMaterial>
                                <code codeSystem="2.16.840.1.113883.12.292" codeSystemName="CVX">
                                  <xsl:attribute name="code">
                                    <xsl:choose>
                                      <xsl:when test="normalize-space(./vaccineCode/coding/code/@value) != ''">
                                        <xsl:value-of select="$immzcode"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:choose>
                                      <xsl:when test="normalize-space(./vaccineCode/coding/display/@value) != ''">
                                        <xsl:value-of select="normalize-space(./vaccineCode/coding/display/@value)"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                </code>
                                <xsl:if test="normalize-space(./LotNumber) !=''">
                                  <lotNumberText>
                                    <xsl:value-of select="normalize-space(./lotNumber/@value)"/>
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
                  <section>
                    <!-- *** Immunizations Section (entries required) (V2) *** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.2.1" extension="2014-06-09"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.2.1"/>
                    <code code="11369-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="History of immunizations"/>
                    <title>IMMUNIZATIONS</title>
                    <text>No known immunization history</text>
                    <entry typeCode="DRIV">
                      <!-- using negationInd="true" to signify that there are no known immunizations -->
                      <substanceAdministration classCode="SBADM" moodCode="EVN" negationInd="true">
                        <!-- ** Immunization Activity (V3) ** -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.52" extension="2015-08-01"/>
                        <templateId root="2.16.840.1.113883.10.20.22.4.52"/>
                        <id root="de10790f-1496-4729-8fe6-f1b87b6219f7"/>
                        <statusCode code="active"/>
                        <effectiveTime nullFlavor="NA"/>
                        <routeCode nullFlavor="NA"/>
                        <consumable>
                          <manufacturedProduct classCode="MANU">
                            <!-- ** Immunization Medication Information (V2) ** -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.54" extension="2014-06-09"/>
                            <templateId root="2.16.840.1.113883.10.20.22.4.54"/>
                            <manufacturedMaterial>
                              <code nullFlavor="OTH">
                                <originalText>Vaccination</originalText>
                                <translation code="71181003" displayName="vaccine"
                                codeSystem="2.16.840.1.113883.6.96"
                                codeSystemName="SNOMED-CT"/>
                              </code>
                              <lotNumberText nullFlavor="NA"/>
                            </manufacturedMaterial>
                          </manufacturedProduct>
                        </consumable>
                      </substanceAdministration>
                    </entry>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!--
            ********************************************************
            Functional Status
            ********************************************************
            -->
          <xsl:if test="count(//CognitiveStatus) &gt; 0">
            <xsl:if test="//Patient/DocumentType = 'RN' or //Patient/DocumentType = 'ToC'">
              <xsl:choose>
                <xsl:when test="count(//CognitiveStatus) &gt; 0">
                  <component>
                    <section>
                      <!--**** Functional status section template **** -->
                      <templateId root="2.16.840.1.113883.10.20.22.2.14" extension="2014-06-09"/>
                      <templateId root="2.16.840.1.113883.10.20.22.2.14"/>
                      <code code="47420-5" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Functional Status"/>
                      <title>FUNCTIONAL STATUS</title>
                      <text>
                        <table border="1" width="100%">
                          <thead>
                            <tr>
                              <th>Functional Condition</th>
                              <th>Code</th>
                              <th>Date</th>
                            </tr>
                          </thead>
                          <tbody>
                            <xsl:for-each select="//CognitiveStatus">
                              <tr>
                                <td>
                                  <content>
                                    <xsl:attribute name="ID">
                                      <xsl:value-of select="concat('function', position())"/>
                                    </xsl:attribute>
                                  </content>
                                  <xsl:value-of select="normalize-space(./Type/text())"/>
                                </td>
                                <td>
                                  <xsl:value-of select="normalize-space(./Code/text())"/>
                                </td>
                                <td>
                                  <xsl:value-of select="normalize-space(./Date/text())"/>
                                </td>
                              </tr>
                            </xsl:for-each>
                          </tbody>
                        </table>
                      </text>
                      <xsl:for-each select="//CognitiveStatus">
                        <entry>
                          <organizer classCode="CLUSTER" moodCode="EVN">
                            <!-- Functional Status Organizer (V2) -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.66" extension="2014-06-09"/>
                            <!-- Functional Status Result Organizer -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.66"/>
                            <id>
                              <xsl:attribute name="root">
                                <xsl:value-of select="generate-id()"/>
                              </xsl:attribute>
                            </id>
                            <code code="d5" displayName="Self-Care" codeSystem="2.16.840.1.113883.6.254" codeSystemName="ICF"/>
                            <statusCode code="completed"/>
                            <component>
                              <observation classCode="OBS" moodCode="EVN">
                                <!-- Functional Status Observation (V2)-->
                                <templateId root="2.16.840.1.113883.10.20.22.4.67" extension="2014-06-09"/>
                                <!-- Functional Status Result Observation -->
                                <templateId root="2.16.840.1.113883.10.20.22.4.67"/>
                                <id>
                                  <xsl:attribute name="root">
                                    <xsl:value-of select="generate-id()"/>
                                  </xsl:attribute>
                                </id>
                                <code code="54522-8" displayName="Functional status" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                                <text>
                                  <xsl:element name="reference">
                                    <xsl:attribute name="value">
                                      <xsl:value-of select="concat('function', position())"/>
                                    </xsl:attribute>
                                  </xsl:element>
                                </text>
                                <statusCode code="completed"/>
                                <xsl:element name= "effectiveTime">
                                  <xsl:attribute name="value">
                                    <xsl:value-of select="normalize-space(./Date/text())"/>
                                  </xsl:attribute>
                                </xsl:element>
                                <xsl:choose>
                                  <xsl:when test="normalize-space(./Code/text())!=''">
                                    <xsl:element name="value">
                                      <xsl:attribute name="code">
                                        <xsl:value-of select="normalize-space(./Code/text())"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="displayName">
                                        <xsl:value-of select="normalize-space(./Type/text())"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="xsi:type">
                                        <xsl:value-of select="'CD'"/>
                                      </xsl:attribute>
                                      <xsl:attribute name="codeSystem">
                                        <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                                      </xsl:attribute>
                                    </xsl:element>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <value nullflavor="UNK"/>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </observation>
                            </component>
                            <component>
                              <observation classCode="OBS" moodCode="EVN">
                                <!-- Self-Care Activities (ADL and IADL) -->
                                <templateId root="2.16.840.1.113883.10.20.22.4.128"/>
                                <id root="c6b5a04b-2bf4-49d1-8336-636a3813df0a"/>
                                <code nullFlavor="NA" />
                                <statusCode code="completed"/>
                                <effectiveTime value="20050501"/>
                                <value xsi:type="CD" code="371153006" displayName="Independent" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT"/>
                              </observation>
                            </component>
                          </organizer>
                        </entry>
                      </xsl:for-each>
                    </section>
                  </component>
                </xsl:when>
                <xsl:otherwise>
                  <component>
                    <section nullFlavor="NI">
                      <!-- Functional Status Section (V2)-->
                      <templateId root="2.16.840.1.113883.10.20.22.2.14" extension="2014-06-09"/>
                      <!-- Functional Status Section -->
                      <templateId root="2.16.840.1.113883.10.20.22.2.14"/>
                      <code code="47420-5" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Functional Status"/>
                      <title>FUNCTIONAL STATUS</title>
                      <text>No Functional Status information</text>
                    </section>
                  </component>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:if>
          </xsl:if>
          <!-- 
			********************************************************
			Mental Status Section
			********************************************************
			-->
          <xsl:if test="count(//MentalStatus) &gt; 0">
            <xsl:if test="//Patient/DocumentType = 'RN' or //Patient/DocumentType = 'ToC'">
              <xsl:choose>
                <xsl:when test="count(//MentalStatus) &gt; 0">
                  <component>
                    <section>
                      <!-- note: the IG lists the wrong templateId in its example of this section, lists ...2,14 instead of 2.56 -db -->
                      <!-- There is no R1.1 version of this template -db -->
                      <templateId root="2.16.840.1.113883.10.20.22.2.56" extension="2015-08-01" />
                      <!-- Mental Status Section -->
                      <code code="10190-7" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="MENTAL STATUS" />
                      <title>MENTAL STATUS</title>
                      <text>
                        <table border="1" width="100%">
                          <thead>
                            <tr>
                              <th>Mental Status</th>
                              <th>Code</th>
                              <th>Date</th>
                            </tr>
                          </thead>
                          <tbody>
                            <xsl:for-each select="//MentalStatus">
                              <tr>
                                <td>
                                  <content ID="cognitive_status_1">
                                    <xsl:attribute name="ID">
                                      <xsl:value-of select="concat('cognitive', position())"/>
                                    </xsl:attribute>
                                  </content>
                                  <xsl:value-of select="normalize-space(./Type/text())"/>
                                </td>
                                <td>
                                  <xsl:value-of select="normalize-space(./Code/text())"/>
                                </td>
                                <td>
                                  <xsl:value-of select="normalize-space(./Date/text())"/>
                                </td>
                              </tr>
                            </xsl:for-each>
                          </tbody>
                        </table>
                      </text>
                      <xsl:for-each select="//MentalStatus">
                        <entry>
                          <observation classCode="OBS" moodCode="EVN">
                            <!-- Mental Status Observation (V3) -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.74" extension="2015-08-01" />
                            <!-- Cognitive Status Result Observation -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.74"/>
                            <id>
                              <xsl:attribute name="root">
                                <xsl:value-of select="generate-id()"/>
                              </xsl:attribute>
                            </id>
                            <code code="373930000" displayName="Cognitive function"
                                    codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT">
                              <translation code="75275-8"
                                      displayName="Cognitive function"
                                      codeSystem="2.16.840.1.113883.6.1"
                                      codeSystemName="LOINC"></translation>
                            </code>
                            <statusCode code="completed"/>
                            <xsl:element name= "effectiveTime">
                              <xsl:attribute name="value">
                                <xsl:value-of select="normalize-space(./Date/text())"/>
                              </xsl:attribute>
                            </xsl:element>
                            <xsl:choose>
                              <xsl:when test="normalize-space(./Code/text())!=''">
                                <xsl:element name="value">
                                  <xsl:attribute name="code">
                                    <xsl:value-of select="normalize-space(./Code/text())"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:value-of select="normalize-space(./Type/text())"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="xsi:type">
                                    <xsl:value-of select="'CD'"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="codeSystem">
                                    <xsl:value-of select="'2.16.840.1.113883.6.96'"/>
                                  </xsl:attribute>
                                </xsl:element>
                              </xsl:when>
                              <xsl:otherwise>
                                <value nullflavor="UNK"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </observation>
                        </entry>
                      </xsl:for-each>
                    </section>
                  </component>
                </xsl:when>
                <xsl:otherwise>
                  <component>
                    <section nullFlavor="NI">
                      <!-- note: the IG lists the wrong templateId in its example of this section, lists ...2,14 instead of 2.56 -db -->
                      <!-- There is no R1.1 version of this template -db -->
                      <templateId root="2.16.840.1.113883.10.20.22.2.56" extension="2015-08-01" />
                      <!-- Mental Status Section -->
                      <code code="10190-7" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="MENTAL STATUS" />
                      <title>MENTAL STATUS</title>
                      <text>No Mental Status information</text>
                    </section>
                  </component>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:if>
          </xsl:if>
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
          <xsl:if test="count(//PlanOfCare) &gt;0">
            <xsl:choose>
              <xsl:when test="count(//PlanOfCare) &gt;0">
                <component>
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.60"/>
                    <code code="61146-7" displayName="Goals" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                    <title>Goals Section</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Goal</th>
                            <th>Code</th>
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
                                <xsl:value-of select="normalize-space(./Code/text())"/>
                              </td>
                              <td>
                                <xsl:value-of select="normalize-space(./EstablishedDateTime)"/>
                              </td>
                            </tr>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:for-each select="//PlanOfCare">
                      <xsl:variable name="description" select="normalize-space(./Goal/text())"/>
                      <xsl:variable name="goalCode" select="normalize-space(./Code/text())"/>
                      <xsl:variable name="goalDate" select="normalize-space(./EstablishedDateTime/text())"/>
                      <entry>
                        <!-- Goal Observation -->
                        <observation classCode="OBS" moodCode="GOL">
                          <!-- Goal Observation -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.121"/>
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <code code="58144-7" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Resident's overall goal established during assessment process"/>
                          <statusCode code="active"/>
                          <xsl:choose>
                            <xsl:when test="$goalDate != ''">
                              <xsl:element name="effectiveTime">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="$goalDate"/>
                                </xsl:attribute>
                              </xsl:element>

                            </xsl:when>
                            <xsl:otherwise>
                              <effectiveTime nullFlavor="'UNK'"/>
                            </xsl:otherwise>
                          </xsl:choose>
                          <value xsi:type="ST">
                            <xsl:value-of select="$description"/>
                          </value>
                        </observation>
                      </entry>
                    </xsl:for-each>
                  </section>
                </component>
              </xsl:when>
              <xsl:otherwise>
                <component>
                  <section nullFlavor="NI">
                    <!-- Goals Section -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.60"/>
                    <code code="61146-7" displayName="Goals" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"/>
                    <title>Goals Section</title>
                    <text>No Goals Information</text>
                  </section>
                </component>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
          <!--
            ********************************************************
            PROCEDURES
            ********************************************************
            -->
          <xsl:variable name="procedures" select="//Patient/Procedures"/>
          <xsl:if test="count($procedures) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count($procedures) &gt; 0">
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.7.1" extension="2014-06-09"/>
                    <templateId root = "2.16.840.1.113883.10.20.22.2.7.1"/>
                    <code code="47519-4" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="HISTORY OF PROCEDURES"/>
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
                                  <xsl:value-of select="concat(normalize-space(./Name/text()), '[', normalize-space(./Code/text()), ']')"/>
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
                      <entry typeCode="DRIV">
                        <procedure classCode="PROC" moodCode="EVN">
                          <!-- ** Procedure activity procedure (V2) ** -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.14" extension="2014-06-09"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.14"/>
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <code>
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
                                <xsl:value-of select="'UNK'"/>
                              </xsl:otherwise>
                            </xsl:choose>
                          </code>
                          <statusCode code = "completed"/>
                          <effectiveTime>
                            <xsl:attribute name="value">
                              <xsl:value-of select="normalize-space(./Date/text())" />
                            </xsl:attribute>
                          </effectiveTime>
                          <methodCode nullFlavor = "UNK"/>
                          <performer>
                            <assignedEntity>
                              <id nullFlavor="NI"/>
                              <addr nullFlavor="UNK"/>
                              <telecom nullFlavor="UNK"/>
                              <representedOrganization>
                                <id root="2.16.840.1.113883.19.5"/>
                                <name>
                                  <xsl:value-of select="normalize-space(./Facility/Name/text())"/>
                                </name>
                                <telecom nullFlavor="UNK"/>
                                <addr nullFlavor="UNK"/>
                              </representedOrganization>
                            </assignedEntity>
                          </performer>
                        </procedure>
                      </entry>
                    </xsl:for-each>
                  </section>
                </xsl:when>
                <xsl:otherwise>
                  <section nullFlavor="NI">
                    <!-- Procedures Section (entries required) (V2) -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.7.1" extension="2014-06-09"/>
                    <templateId root = "2.16.840.1.113883.10.20.22.2.7.1"/>
                    <code code = "47519-4" codeSystem = "2.16.840.1.113883.6.1" codeSystemName = "LOINC" displayName = "PROCEDURES"/>
                    <title>PROCEDURES</title>
                    <text>
                      No procedures reported
                    </text>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!-- ************************************
            ASSESSMENT
            ************************************ -->
          <xsl:variable name="assessments" select="//Patient/Assessment/Description/text()"/>
          <xsl:if test="$assessments !=''">
            <component>
              <section>
                <templateId root="2.16.840.1.113883.10.20.22.2.8"/>
                <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="51848-0" displayName="ASSESSMENTS"/>
                <title>ASSESSMENTS</title>
                <xsl:choose>
                  <xsl:when test="$assessments !=''">
                    <text>
                      <xsl:value-of select="$assessments"/>
                    </text>
                  </xsl:when>
                  <xsl:otherwise>
                    <text>
                      No Assessment information
                    </text>
                  </xsl:otherwise>
                </xsl:choose>
              </section>
            </component>
          </xsl:if>
          <!-- ************************************
            Medical Device Equipment
            ************************************ -->
          <xsl:variable name="devices" select="//Patient/DeviceEquipment"/>
          <xsl:if test="count($devices) &gt; 0">
            <component>
              <xsl:choose>
                <xsl:when test="count($devices) &gt; 0">
                  <section>
                    <templateId root="2.16.840.1.113883.10.20.22.2.23"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.23" extension="2014-06-09"/>
                    <code code="46264-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Medical Equipment"/>
                    <title>Implants</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Device Name</th>
                            <th>Implanted Date</th>
                            <th>UDI</th>
                            <th>Status</th>
                          </tr>
                        </thead>
                        <tbody>
                          <xsl:for-each select="$devices">
                            <xsl:element name="tr">
                              <xsl:element name="td">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('DEVNAME_',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="normalize-space(./DeviceName/text())"/>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('IMPLANTEDDT_',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="normalize-space(./DateImplanted/text())"/>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('UDI_',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="normalize-space(./UDI/text())"/>
                              </xsl:element>
                              <xsl:element name="td">
                                <xsl:attribute name="ID">
                                  <xsl:value-of select="concat('STATUS_',position())"/>
                                </xsl:attribute>
                                <xsl:value-of select="normalize-space(./Status/text())"/>
                              </xsl:element>
                            </xsl:element>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:for-each select="$devices">
                      <entry>
                        <procedure classCode="PROC" moodCode="EVN">
                          <templateId root="2.16.840.1.113883.10.20.22.4.14"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.14" extension="2014-06-09"/>
                          <id extension="2744" root="1.2.840.114350.1.13.5552.1.7.2.737780"/>
                          <code nullFlavor="UNK"/>
                          <!-- Procedure is completed, even though the implant is still active -->
                          <statusCode code="completed"/>
                          <effectiveTime>
                            <!-- Placed Date -->
                            <xsl:element name="low"  >
                              <xsl:attribute name="value">
                                <xsl:value-of select="normalize-space(./DateImplanted/text())"/>
                              </xsl:attribute>
                            </xsl:element>
                          </effectiveTime>
                          <participant typeCode="DEV">
                            <participantRole classCode="MANU">
                              <!-- ** Product instance ** -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.37"/>
                              <!-- UDI -db -->
                              <!-- <id root="00643169007222"/> -->
                              <!-- <id root="d68b7e32-7810-4f5b-9cc2-acd54b0fd85e" extension="00643169007222"/> -->
                              <!-- root is FDA OID, extension is the UDI id -->
                              <xsl:element name="id">
                                <xsl:attribute name ="root">
                                  <xsl:value-of select="'2.16.840.1.113883.3.3719'"/>
                                </xsl:attribute>
                                <xsl:attribute name="extension">
                                  <xsl:value-of select="normalize-space(./UDI/text())"/>
                                </xsl:attribute>
                              </xsl:element>
                              <playingDevice>
                                <xsl:element name="code">
                                  <xsl:attribute name="code">
                                    <xsl:value-of select="'UNK'"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:value-of select="./DeviceName/text()"/>
                                  </xsl:attribute>
                                </xsl:element>
                              </playingDevice>
                              <!-- FDA Scoping Entity OID for UDI-db -->
                              <scopingEntity>
                                <id root="2.16.840.1.113883.3.3719"/>
                              </scopingEntity>
                            </participantRole>
                          </participant>
                        </procedure>
                      </entry>
                    </xsl:for-each>
                  </section>
                </xsl:when>
                <xsl:otherwise>
                  <section nullFlavor="NI">
                    <templateId root="2.16.840.1.113883.10.20.22.2.23" extension="2014-06-09"/>
                    <code code="46264-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Medical Equipment"/>
                    <title>Implants</title>
                    <text>No Device Implanted</text>
                  </section>
                </xsl:otherwise>
              </xsl:choose>
            </component>
          </xsl:if>
          <!-- ************************************************
              TREATMENT PLAN
              ************************************************* -->
          <xsl:variable name="treatmentPlan" select="//Patient/TreatmentPlan"/>
          <xsl:if test="count($treatmentPlan) &gt; 0">
            <xsl:choose>
              <xsl:when test="count($treatmentPlan) &gt; 0">
                <component>
                  <section>
                    <!--  **** Plan of Treatment Section (V2) **** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.10" extension="2014-06-09"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.10"/>
                    <code code="18776-5" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Treatment plan"/>
                    <title>TREATMENT PLAN</title>
                    <text>
                      <table border="1" width="100%">
                        <thead>
                          <tr>
                            <th>Planned Care</th>
                            <th>Start Date</th>
                          </tr>
                        </thead>
                        <tbody>
                          <xsl:for-each select="$treatmentPlan/LabResult">
                            <xsl:if test="./TestName/text() !='' and ./TestCode/text()!='' ">
                              <xsl:element name="tr">
                                <xsl:element name="td">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('TXPLANLAB_',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat(./TestName/text(), ' [', ./TestCode/text() , ']')"/>
                                </xsl:element>
                                <xsl:element name="td">
                                  <xsl:value-of select="./TestDate/text()"/>
                                </xsl:element>
                              </xsl:element>
                            </xsl:if>
                          </xsl:for-each>
                          <xsl:for-each select="$treatmentPlan/DiagnosticImaging">
                            <xsl:if test="./TestName/text() !='' and ./TestCode/text()!='' ">
                              <xsl:element name="tr">
                                <xsl:element name="td">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('TXPLANDI_',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat(./TestName/text(), ' [', ./TestCode/text() , ']')"/>
                                </xsl:element>
                                <xsl:element name="td">
                                  <xsl:value-of select="./TestDate/text()"/>
                                </xsl:element>
                              </xsl:element>
                            </xsl:if>
                          </xsl:for-each>
                          <xsl:for-each select="$treatmentPlan/Procedures">
                            <xsl:if test="./Name/text() !='' and ./Code/text()!='' ">
                              <xsl:element name="tr">
                                <xsl:element name="td">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('TXPLANPRO_',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat(./Name/text(), ' [', ./Code/text() , ']')"/>
                                </xsl:element>
                                <xsl:element name="td">
                                  <xsl:value-of select="./Date/text()"/>
                                </xsl:element>
                              </xsl:element>
                            </xsl:if>
                          </xsl:for-each>
                          <xsl:for-each select="$treatmentPlan/Medication">
                            <xsl:if test="./Displayname/text() !='' and ./Code/text()!='' ">
                              <xsl:element name="tr">
                                <xsl:element name="td">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('TXPLANMED_',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat(./Displayname/text(), ' [', ./Code/text() , ']')"/>
                                </xsl:element>
                                <xsl:element name="td">
                                  <xsl:value-of select="./RecordedDate/text()"/>
                                </xsl:element>
                              </xsl:element>
                            </xsl:if>
                          </xsl:for-each>
                          <xsl:for-each select="$treatmentPlan/Encounters">
                            <xsl:if test="./name/text() !='' and ./Code/text()!='' ">
                              <xsl:element name="tr">
                                <xsl:element name="td">
                                  <xsl:attribute name="ID">
                                    <xsl:value-of select="concat('TXPLANENC_',position())"/>
                                  </xsl:attribute>
                                  <xsl:value-of select="concat(./name/text(), ' [', ./Code/text() , ']')"/>
                                </xsl:element>
                                <xsl:element name="td">
                                  <xsl:value-of select="./StartDate/text()"/>
                                </xsl:element>
                              </xsl:element>
                            </xsl:if>
                          </xsl:for-each>
                        </tbody>
                      </table>
                    </text>
                    <xsl:for-each select="$treatmentPlan/Encounters">
                      <xsl:variable name="encCode" select="normalize-space(./Code/text())"/>
                      <xsl:variable name="encName" select="normalize-space(./name/text())"/>
                      <xsl:variable name="encDate" select="normalize-space(./StartDate/text())"/>
                      <xsl:variable name="facAdd1" select="normalize-space(./Performer/Address/Address1/text())"/>
                      <xsl:variable name="facCity" select="normalize-space(./Performer/Address/City/text())"/>
                      <xsl:variable name="facState" select="normalize-space(./Performer/Address/State/text())"/>
                      <xsl:variable name="facZip" select="normalize-space(./Performer/Address/Zip/text())"/>
                      <xsl:variable name="facCountry" select="normalize-space(./Performer/Address/Country/text())"/>
                      <xsl:variable name="facTele" select="normalize-space(./Performer/Address/Telephone/text())"/>
                      <xsl:variable name="facName" select="normalize-space(./Performer/Address/Name/text())"/>
                      <entry>
                        <!-- encounter with moodCode indicating Intent -->
                        <encounter classCode="ENC" moodCode="INT">
                          <!-- Planned Encounter (V2) templateId -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.40" extension="2014-06-09" />
                          <templateId root="2.16.840.1.113883.10.20.22.4.40"/>
                          <id root="1.3.6.1.42424242.4.99930.4.3.4"/>
                          <!-- templateId and id are all that are required or specified in C-CDA R1.1; with C-CDA R2.0 the documentation changes quite a bit -->
                          <!-- code from the Planned Encounter Type value set; 11429006 is reflective of the example data, but local policy and use may result in selection of a different code -->
                          <code codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED-CT" code="11429006" displayName="consultation"/>
                          <!-- statusCode added as per C-CDA R2.1 -db -->
                          <statusCode code="active"/>
                          <xsl:element name="effectiveTime">
                            <xsl:element name ="low">
                              <xsl:attribute name="value">
                                <xsl:value-of select="$encDate"/>
                              </xsl:attribute>
                            </xsl:element>
                          </xsl:element>
                          <performer>
                            <assignedEntity>
                              <id root="2.16.840.1.113883.19.5.9999.456"/>
                              <xsl:choose>
                                <xsl:when test="./Performer/Address[.!='']">
                                  <addr use="WP">
                                    <xsl:if test="normalize-space($facAdd1) != ''">
                                      <xsl:element name="streetAddressLine">
                                        <xsl:value-of select="$facAdd1"/>
                                      </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="normalize-space($facCity) != ''">
                                      <xsl:element name="city">
                                        <xsl:value-of select="normalize-space($facCity)"/>
                                      </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="normalize-space($facState) != ''">
                                      <xsl:element name="state">
                                        <xsl:value-of select="normalize-space($facState)"/>
                                      </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="normalize-space($facZip) != ''">
                                      <xsl:element name="postalCode">
                                        <xsl:value-of select="normalize-space($facZip)"/>
                                      </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="normalize-space($facCountry) != ''">
                                      <xsl:element name="country">
                                        <xsl:value-of select="normalize-space($facCountry)"/>
                                      </xsl:element>
                                    </xsl:if>
                                  </addr>
                                </xsl:when>
                                <xsl:otherwise>
                                  <addr nullFlavor="UNK"/>
                                </xsl:otherwise>
                              </xsl:choose>
                              <representedOrganization>
                                <id nullFlavor="NI"/>
                                <name>
                                  <xsl:value-of select="$facName"/>
                                </name>
                                <telecom nullFlavor="NP"/>
                                <addr nullFlavor="NP"/>
                              </representedOrganization>
                            </assignedEntity>
                          </performer>
                        </encounter>
                      </entry>
                    </xsl:for-each>
                    <xsl:for-each select="$treatmentPlan/LabResult">
                      <entry>
                        <!-- For lab, this should be an RQO -->
                        <observation classCode="OBS" moodCode="RQO">
                          <!-- Planned Observation (V2) -> Plan Of Care Activity Observation -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.44" extension="2014-06-09"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.44"/>
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <xsl:element name="code">
                            <xsl:attribute name="code">
                              <xsl:value-of select="./TestCode/text()"/>
                            </xsl:attribute>
                            <xsl:attribute name="codeSystem">
                              <xsl:value-of select="'2.16.840.1.113883.6.1'"/>
                            </xsl:attribute>
                            <xsl:attribute name="codeSystemName">
                              <xsl:value-of select="'LOINC'"/>
                            </xsl:attribute>
                            <xsl:attribute name="displayName">
                              <xsl:value-of select="./TestName/text()"/>
                            </xsl:attribute>
                          </xsl:element>
                          <statusCode code="active" />
                          <xsl:element name="effectiveTime">
                            <xsl:choose>
                              <xsl:when test="normalize-space(./TestDate/text()) !=''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="normalize-space(./TestDate/text())"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'UNK'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                        </observation>
                      </entry>
                    </xsl:for-each>
                    <xsl:for-each select="$treatmentPlan/DiagnosticImaging">
                      <entry>
                        <!-- encounter with moodCode indicating Intent -->
                        <observation classCode="OBS" moodCode="INT">
                          <!-- Planned Observation (V2) -> Plan Of Care Activity Observation -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.44" extension="2014-06-09"/>
                          <templateId root="2.16.840.1.113883.10.20.22.4.44"/>
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <xsl:element name="code">
                            <xsl:attribute name="code">
                              <xsl:value-of select="'UNK'"/>
                            </xsl:attribute>
                            <xsl:attribute name="codeSystem">
                              <xsl:value-of select="'2.16.840.1.113883.6.1'"/>
                            </xsl:attribute>
                            <xsl:attribute name="codeSystemName">
                              <xsl:value-of select="'LOINC'"/>
                            </xsl:attribute>
                            <xsl:attribute name="displayName">
                              <xsl:value-of select="./TestName/text()"/>
                            </xsl:attribute>
                          </xsl:element>
                          <statusCode code="active" />
                          <xsl:element name="effectiveTime">
                            <xsl:choose>
                              <xsl:when test="normalize-space(./TestDate/text()) !=''">
                                <xsl:attribute name="value">
                                  <xsl:value-of select="normalize-space(./TestDate/text())"/>
                                </xsl:attribute>
                              </xsl:when>
                              <xsl:otherwise>
                                <xsl:attribute name="nullFlavor">
                                  <xsl:value-of select="'UNK'"/>
                                </xsl:attribute>
                              </xsl:otherwise>
                            </xsl:choose>
                          </xsl:element>
                        </observation>
                      </entry>
                    </xsl:for-each>
                    <xsl:for-each select="$treatmentPlan/Medication">
                      <entry>
                        <!-- Planned Medication Activity (V2)- -->
                        <substanceAdministration moodCode="INT" classCode="SBADM">
                          <templateId root="2.16.840.1.113883.10.20.22.4.42" extension="2014-06-09" />
                          <!-- added r1.1 templateId -db -->
                          <templateId root="2.16.840.1.113883.10.20.22.4.42" />
                          <id>
                            <xsl:attribute name="root">
                              <xsl:value-of select="generate-id()"/>
                            </xsl:attribute>
                          </id>
                          <text>
                            <xsl:value-of select="./Displayname/text()"/>
                          </text>
                          <statusCode code="active" />
                          <!-- The effectiveTime in a planned medication activity 
							represents the time that the medication activity should occur. -->
                          <xsl:element name="effectiveTime">
                            <xsl:attribute name="value">
                              <xsl:value-of select="./RecordedDate/text()"/>
                            </xsl:attribute>
                          </xsl:element>
                          <consumable>
                            <manufacturedProduct classCode="MANU">
                              <!-- Medication Information (V2) -->
                              <templateId root="2.16.840.1.113883.10.20.22.4.23" extension="2014-06-09" />
                              <templateId root="2.16.840.1.113883.10.20.22.4.23" />
                              <manufacturedMaterial>
                                <xsl:element name="code">
                                  <xsl:attribute name="code">
                                    <xsl:choose>
                                      <xsl:when test="./Code/text() !=''">
                                        <xsl:value-of select="./Code/text()"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                  <xsl:attribute name="codeSystem">
                                    <xsl:value-of select="'2.16.840.1.113883.6.88'"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="codeSystemName">
                                    <xsl:value-of select="'RxNorm'"/>
                                  </xsl:attribute>
                                  <xsl:attribute name="displayName">
                                    <xsl:choose>
                                      <xsl:when test="./Displayname/text() !=''">
                                        <xsl:value-of select="./Displayname/text()"/>
                                      </xsl:when>
                                      <xsl:otherwise>
                                        <xsl:value-of select="'UNK'"/>
                                      </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:attribute>
                                </xsl:element>
                              </manufacturedMaterial>
                            </manufacturedProduct>
                          </consumable>
                        </substanceAdministration>
                      </entry>
                    </xsl:for-each>
                    <xsl:for-each select="$treatmentPlan/Procedures">
                    </xsl:for-each>
                  </section>
                </component>
              </xsl:when>
              <xsl:otherwise>
                <component>
                  <section>
                    <!--  **** Plan of Treatment Section (V2) **** -->
                    <templateId root="2.16.840.1.113883.10.20.22.2.10" extension="2014-06-09"/>
                    <templateId root="2.16.840.1.113883.10.20.22.2.10"/>
                    <code code="18776-5" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" displayName="Treatment plan"/>
                    <title>TREATMENT PLAN</title>
                    <text>
                      No Treatment Plans
                    </text>
                  </section>
                </component>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:if>
          <!-- ************************************************
              DISCHARGE INSTRUCTION
              ************************************************* 
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
          </xsl:choose> -->
        </structuredBody>
      </component>
    </ClinicalDocument>
  </xsl:template>
</xsl:stylesheet>