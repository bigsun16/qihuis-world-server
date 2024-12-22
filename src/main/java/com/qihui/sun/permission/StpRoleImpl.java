package com.qihui.sun.permission;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qihui.sun.domain.Role;
import com.qihui.sun.domain.UserRole;
import com.qihui.sun.service.RoleService;
import com.qihui.sun.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StpRoleImpl implements StpInterface {
    public static final String NORMAL = "NORMAL";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    public StpRoleImpl(RoleService roleService, UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<Integer> roleIds = userRoleService
                .listObjs(
                        new QueryWrapper<UserRole>()
                                .eq("user_id", Integer.parseInt(loginId.toString()))
                                .select("role_id")
                );
        log.info("roleIds:{}", roleIds);
        return roleService.listObjs(
                new QueryWrapper<Role>()
                        .in("role_id", roleIds)
                        .select("role_name")
        );
    }
}
