package com.motomarket.controlller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/errors")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @GetMapping
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("/admin/errorPage");
        String errorMsg = "";
        String errorType = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorType = "400";
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorType = "401";
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorType = "404";
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 405: {
                errorType = "405";
                errorMsg = "Http Error Code: 405. Method Not Allowed";
                break;
            }
            case 500: {
                errorType = "500";
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("errorType", errorType);
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    @PostMapping
    public ModelAndView renderErrorPageMethod(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("/admin/errorPage");
        String errorMsg = "";
        String errorType = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorType = "400";
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorType = "401";
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorType = "404";
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 405: {
                errorType = "405";
                errorMsg = "Http Error Code: 405. Method Not Allowed";
                break;
            }
            case 409: {
                errorType = "409";
                errorMsg = "Http Error Code: 409. Data Conflict";
                break;
            }
            case 500: {
                errorType = "500";
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("errorType", errorType);
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    @RequestMapping(value = "500Error", method = RequestMethod.GET)
    public void throwRuntimeException() {
        throw new NullPointerException("Throwing a null pointer exception");
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }


}
