package org.prgrms.devconnect.common.exception.jobpost

import org.prgrms.devconnect.common.exception.DevConnectException
import org.prgrms.devconnect.common.exception.ExceptionCode

class JobPostException(exceptionCode: ExceptionCode) : DevConnectException(exceptionCode)