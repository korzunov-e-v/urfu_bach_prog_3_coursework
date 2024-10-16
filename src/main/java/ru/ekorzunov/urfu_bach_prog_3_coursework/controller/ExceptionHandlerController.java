package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import java.util.NoSuchElementException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex.getMessage());

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error-page");

        if (ex instanceof NoSuchElementException) {
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("code", HttpStatus.NOT_FOUND);
        } else if (ex instanceof AccessDeniedException) {
            mav.setStatus(HttpStatus.FORBIDDEN);
            mav.addObject("code", HttpStatus.FORBIDDEN);
        } else {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            mav.addObject("code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return mav;
    }
}
