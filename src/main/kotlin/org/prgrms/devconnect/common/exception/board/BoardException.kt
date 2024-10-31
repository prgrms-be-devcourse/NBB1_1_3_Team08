package org.prgrms.devconnect.common.exception.board

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode

class BoardException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)