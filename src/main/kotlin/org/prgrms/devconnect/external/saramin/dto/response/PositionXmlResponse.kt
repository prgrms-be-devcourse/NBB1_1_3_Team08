import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class PositionXmlResponse(
        @JacksonXmlProperty(localName = "title")
        val title: String? = null,

        @JacksonXmlProperty(localName = "location")
        val location: String? = null,

        @JacksonXmlProperty(localName = "job-type")
        val jobType: String? = null,

        @JacksonXmlProperty(localName = "job-code")
        val jobCode: String? = null,

        @JacksonXmlProperty(localName = "experience-level")
        val experienceLevel: String? = null,

        @JacksonXmlProperty(localName = "required-education-level")
        val requiredEducation: String? = null,

        @JacksonXmlProperty(localName = "industry")
        val industry: String? = null,

        @JacksonXmlProperty(localName = "job-mid-code")
        val jobMidCode: String? = null
)
