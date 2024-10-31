package org.prgrms.devconnect.external.saramin.dto.response

import PositionXmlResponse
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import org.prgrms.devconnect.domain.Timestamp

data class JobXmlResponse(
        @JacksonXmlProperty(localName = "id")
        val id: Long = 0,

        @JacksonXmlProperty(localName = "url")
        val url: String? = null,

        @JacksonXmlProperty(localName = "posting-timestamp")
        val postingTimestamp: Long = 0,

        @JacksonXmlProperty(localName = "opening-timestamp")
        val openingTimestamp: Long = 0,

        @JacksonXmlProperty(localName = "expiration-timestamp")
        val expirationTimestamp: Long = 0,

        @JacksonXmlProperty(localName = "company")
        val company: CompanyXmlResponse? = null,

        @JacksonXmlProperty(localName = "position")
        val position: PositionXmlResponse? = null,

        @JacksonXmlProperty(localName = "salary")
        val salary: String? = null,

        @JacksonXmlProperty(localName = "active")
        val active: String? = null,

        @JacksonXmlProperty(localName = "modification-timestamp")
        val modificationTimestamp: Long? = null,

        @JacksonXmlProperty(localName = "close-type")
        val closeType: String? = null,

        @JacksonXmlProperty(localName = "keyword")
        val keyword: String? = null

)
