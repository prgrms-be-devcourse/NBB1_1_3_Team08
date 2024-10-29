package org.prgrms.devconnect.common.exception.jwt

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode


class JwtException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)