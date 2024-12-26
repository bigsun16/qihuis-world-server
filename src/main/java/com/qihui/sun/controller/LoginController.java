package com.qihui.sun.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qihui.sun.domain.Users;
import com.qihui.sun.permission.RateLimit;
import com.qihui.sun.service.UserRoleService;
import com.qihui.sun.service.UsersService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class LoginController {

    Logger logger = Logger.getLogger(LoginController.class.getName());
    private final UsersService usersService;
    private final UserRoleService userRoleService;

    public LoginController(UsersService usersService, UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
        this.usersService = usersService;
    }


    @RateLimit(permitsPerSecond = 10)
    @PostMapping("/login")
    public SaResult doLogin(@RequestBody Users user) {
        logger.info("用户登录：" + user);
        String password = SaSecureUtil.sha256(user.getPassword());
        Users one = usersService.getOne(getWrapper(user, password));
        if (one == null) {
            one = doRegister(user);
            userRoleService.addRoleToUser(one.getUserId());
        }

        StpUtil.login(one.getUserId());
        return SaResult.ok("登录成功")
                .setData(StpUtil.getTokenInfo())
                .set("username", one.getUsername())
                .set("roleList", StpUtil.getRoleList());
    }

    @GetMapping("/logout")
    public SaResult logout() {
        logger.info(LocalDate.now() + "退出登录");
        StpUtil.logout();
        return SaResult.ok("退出成功");
    }

    @GetMapping("/isLogin")
    public SaResult isLogin() {
         return SaResult.data(StpUtil.isLogin());
    }

    public Users doRegister(Users user) {
        logger.info("注册用户：" + user);
        String password = SaSecureUtil.sha256(user.getPassword());
        user.setPassword(password);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        usersService.save(user);

        return usersService.getOne(getWrapper(user, password));
    }

    public Wrapper<Users> getWrapper(Users user, String password) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        wrapper.eq("password", password);
        return wrapper;
    }

}
