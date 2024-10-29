package org.prgrms.devconnect.common.exception.techstack

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode

class TechStackException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)
