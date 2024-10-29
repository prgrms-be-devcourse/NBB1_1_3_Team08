package org.prgrms.devconnect.common.exception.refresh

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode


class RefreshTokenException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)