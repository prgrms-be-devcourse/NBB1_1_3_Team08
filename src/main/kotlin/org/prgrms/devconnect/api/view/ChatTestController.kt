package org.prgrms.devconnect.api.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatTestController {
    @GetMapping("/chat-test")
    fun testchat(): String = "SimpleChatTest"
}