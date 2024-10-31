package org.prgrms.devconnect.common.exception.interest

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode

class InterestException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)