package org.prgrms.devconnect.common.exception

abstract class DevConnectException(
    val exceptionCode: ExceptionCode
) : RuntimeException(exceptionCode.message) {

}