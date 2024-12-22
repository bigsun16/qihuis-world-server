package com.qihui.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qihui.sun.domain.UserRole;

/**
 * @author bigsu
 * @description 针对表【user_role】的数据库操作Service
 * @createDate 2024-12-25 12:49:33
 */
public interface UserRoleService extends IService<UserRole> {
    void addRoleToUser(Integer userId);
}
