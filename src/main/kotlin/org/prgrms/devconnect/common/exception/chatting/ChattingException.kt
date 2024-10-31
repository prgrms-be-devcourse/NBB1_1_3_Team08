package org.prgrms.devconnect.common.exception.chatting

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode

class ChattingException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)