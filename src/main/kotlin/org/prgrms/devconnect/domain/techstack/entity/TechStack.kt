package org.prgrms.devconnect.domain.techstack.entity

import jakarta.persistence.*

@Entity
@Table(name = "tech_stack")
class TechStack(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id")
    var techStackId: Long? = null,

    @Column(name = "name", length = 50)
    val name: String,

    @Column(name = "job_code", length = 50)
    val code: String
) {


}