package org.prgrms.devconnect.api.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ViewController {
  @GetMapping
  fun home(): String = "index"

  @GetMapping("/login")
  fun login(): String = "login"

  @GetMapping("/boards/{boardId}")
  fun boardDetail(@PathVariable boardId: String): String = "board-detail"

  @GetMapping("/job-posts/{jobPostId}")
  fun jobPostDetail(@PathVariable jobPostId: String): String = "job-detail"

  @GetMapping("/sign-up")
  fun signUp(): String = "sign-up"

  @GetMapping("/member-detail")
  fun memberDetail(): String = "member-detail"

}