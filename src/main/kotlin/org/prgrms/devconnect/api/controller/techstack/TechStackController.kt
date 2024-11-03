package org.prgrms.devconnect.api.controller.techstack

import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.api.service.techstack.TechStackQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/tech-stacks")
@RestController
class TechStackController(
  private val techStackQueryService: TechStackQueryService
) {

  @GetMapping
  fun getAllTechStacks(): ResponseEntity<List<TechStackResponseDto>> {
    val techStacks = techStackQueryService.getAllTechStacks()
    return ResponseEntity.ok(techStacks)
  }
}