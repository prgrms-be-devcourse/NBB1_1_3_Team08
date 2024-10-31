package org.prgrms.devconnect.common.exception.alarm

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode

class EmailException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)
