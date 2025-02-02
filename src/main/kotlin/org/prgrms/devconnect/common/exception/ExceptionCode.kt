package org.prgrms.devconnect.common.exception

enum class ExceptionCode(
    val code: Int,
    val message: String
) {
  //Alarm Error
  NOT_FOUND_ALARM(404, "존재하지 않는 알람입니다."),
  EMPTY_ALARMS(400, "알림이 비어있습니다."),
  METHOD_NOT_PROVIDE(403, "알림을 제공하지 않는 메소드입니다."),

  //Email Error
  EMAIL_SERVER_ERROR(500, "이메일을 전송하던 중에 오류가 발생했습니다."),

  //Board Error
  NOT_FOUND_BOARD(404, "존재하지 않는 게시물입니다."),
  ALREADY_CLOSED_BOARD(403, "마감된 게시물입니다."),
  ALREADY_DELETED_BOARD(400, "이미 삭제된 게시물입니다."),

  //BoardTechStack Error
  NOT_FOUND_BOARD_TECH_STACK(404, "존재하지 않는 테크스택입니다."),

  //Comment Error
  NOT_FOUND_COMMENT(404, "존재하지 않는 댓글입니다."),
  INVALID_PARENT_COMMENT(400, "대댓글은 최상위 댓글에만 작성할 수 있습니다."),

  //BugReport Error
  NOT_FOUND_BUG_REPORT(404, "존재하지 않는 버그리포트입니다."),

  //JobPost Error
  NOT_FOUND_JOB_POST(404, "존재하지 않는 채용공고입니다."),

  //MEMBER ERROR
  NOT_FOUND_MEMBER(404, "존재하지 않는 멤버입니다."),
  DUPLICATED_MEMBER_EMAIL(400, "이미 존재하는 이메일입니다."),
  INVALID_PASSWORD(400, "일치하지 않는 패스워드입니다."),

  //BUGREPORT ERROR
  NOT_FOUND_BUGREPORT(404, "존재하지 않는 버그리포트입니다."),

  //CHATTINGROOM ERROR
  NOT_FOUND_CHATROOM(404, "존재하지 않는 채팅방 입니다"),
  CHATROOM_ALREADY_INACTIVE(400, "채팅방은 이미 INACTIVE 상태입니다"),

  //CHATPART ERROR
  NOT_FOUND_CHATPART(404, "채팅에 참여하지 않았습니다"),
  ALREADY_JOINED_CHATROOM(400, "이미 참여한 채팅입니다"),

  //TECH_STACK ERROR
  NOT_FOUND_TECH_STACK(404, "존재하지 않는 기술 스택입니다."),

  // INTEREST ERROR
  DUPLICATED_INTEREST_BOARD(400, "이미 등록된 관심 게시글입니다."),
  NOT_FOUND_INTEREST_BOARD(404, "존재하지 않는 관심 게시글입니다."),
  DUPLICATED_INTEREST_JOB_POST(400, "이미 등록된 관심 채용 공고입니다."),
  NOT_FOUND_INTEREST_JOB_POST(404, "존재하지 않는 관심 채용 공고입니다."),

  // JWT ERROR
  JWT_TOKEN_EXPIRED(401, "JWT 토큰이 만료되었습니다."),
  JWT_TOKEN_MALFORMED(401, "형식이 잘못된 JWT 토큰입니다."),
  JWT_SIGNATURE_INVALID(401, "JWT 서명이 유효하지 않습니다."),

  // REFRESH TOKEN ERROR
  NOT_FOUND_REFRESH_TOKEN(404, "존재하지 않는 리프레시 토큰입니다.");
}
