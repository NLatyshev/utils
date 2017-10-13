<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml example-original.xml?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:diffgr="urn:schemas-microsoft-com:xml-diffgram-v1"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions"
                exclude-result-prefixes="xs fn fo ">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <!-- match any other element and copy it with the attributes. Use local-name to get rid of the diffgr and ms-data namespaces -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- Remove the tag for the diffgram -->
    <xsl:template match="diffgr:diffgram">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- Remove schema -->
    <xsl:template match="xs:*">
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>
