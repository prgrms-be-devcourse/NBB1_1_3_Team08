package org.prgrms.devconnect.external.saramin.dto.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class JobSearchResponse(
        @JacksonXmlProperty(localName = "jobs")
        val jobs: JobsXmlResponse? = null
)
