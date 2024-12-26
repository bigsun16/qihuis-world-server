package com.qihui.sun.controller.response;

import cn.dev33.satoken.util.SaResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class NotFoundController implements ErrorController {

    @RequestMapping("/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        return SaResult.get(404, "not found", null);
    }

}
