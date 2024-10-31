package org.prgrms.devconnect.external.saramin.dto.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class CompanyXmlResponse(
        @JacksonXmlProperty(isAttribute = true, localName = "name href")
        val href: String? = null,

        @JacksonXmlProperty(isAttribute = true, localName = "name")
        val name: String? = null
)
