package com.study.membershipwithtdd.common.response;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.study.membershipwithtdd.common.exception.MembershipException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = MembershipException.class)
    public CommonResponse onMembershipException(MembershipException ex) {
        log.error("MembershipException occur: ", ex);
        return CommonResponse.fail(ex.getMessage(), ex.getMembershipErrorResult().name());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResponse onException(Exception ex) {
        log.error("Exception occur: ", ex.getMessage());
        return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MissingRequestHeaderException.class})
    public CommonResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occur: ", ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fe = bindingResult.getFieldError();
        if (fe != null) {
            String message = "Request Error" + " " + fe.getField() + "=" + fe.getRejectedValue() + " (" + fe.getDefaultMessage() + ")";
            return CommonResponse.fail(message, ErrorCode.COMMON_INVALID_PARAMETER.name());
        } else {
            return CommonResponse.fail(ErrorCode.COMMON_INVALID_PARAMETER.getErrorMsg(), ErrorCode.COMMON_INVALID_PARAMETER.name());
        }
    }


}
