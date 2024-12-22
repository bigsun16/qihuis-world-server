package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.domain.Role;
import com.qihui.sun.domain.UserRole;
import com.qihui.sun.mapper.UserRoleMapper;
import com.qihui.sun.service.RoleService;
import com.qihui.sun.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @author bigsu
 * @description 针对表【user_role】的数据库操作Service实现
 * @createDate 2024-12-25 12:49:33
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {
    private final Logger log = Logger.getLogger(UserRoleServiceImpl.class.getName());
    private final RoleService roleService;

    public UserRoleServiceImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    public void addRoleToUser(Integer userId) {
        // 实现添加权限到用户的逻辑
        UserRole userRole = roleService.getObj(
                new QueryWrapper<Role>()
                        .eq("role_name", "normal")
                        .select("role_id"),
                roleId -> new UserRole(userId, Integer.parseInt(roleId.toString())));
        save(userRole);
    }
}




