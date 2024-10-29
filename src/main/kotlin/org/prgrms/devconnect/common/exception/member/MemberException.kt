package org.prgrms.devconnect.common.exception.member

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode


class MemberException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)