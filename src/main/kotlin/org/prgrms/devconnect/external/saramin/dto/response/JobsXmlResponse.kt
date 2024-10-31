package org.prgrms.devconnect.external.saramin.dto.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class JobsXmlResponse(
        @JacksonXmlProperty(localName = "total")
        val total: Int? = null,

        @JacksonXmlProperty(localName = "count")
        val count: Int? = null,

        @JacksonXmlProperty(localName = "start")
        val start: Int? = null,

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "job")
        val jobList: List<JobXmlResponse> = emptyList()
)
