package com.qihui.sun.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.domain.Article;
import com.qihui.sun.mapper.ArticleMapper;
import com.qihui.sun.permission.StpRoleImpl;
import com.qihui.sun.service.ArticleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author bigsu
 * @description 针对表【article】的数据库操作Service实现
 * @createDate 2024-12-24 17:24:42
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {
    Logger logger = Logger.getLogger(ArticleServiceImpl.class.getName());

    @Override
    public boolean saveArticle(Article article) {
        Integer id = article.getId();
        LocalDateTime now = LocalDateTime.now();
        Article byId = getById(id);
        if (id != null && byId != null) {
            //如果是管理员或者本人则可以更新文章
            if (Objects.equals(byId.getUserId(), article.getUserId()) || StpUtil.hasRole(StpRoleImpl.ADMINISTRATOR)) {
                logger.info("updating existing article");
                article.setUpdateTime(now);
                return updateById(article);
            } else {
                StpUtil.checkRole(StpRoleImpl.ADMINISTRATOR);
                return false;
            }
        } else {
            logger.info("creating new article");
            article.setUpdateTime(now);
            article.setCreateTime(now);
            article.setDeleteFlag(0);
            return save(article);
        }
    }

    public boolean deleteById(Integer id) {
        if (id == null) {
            throw new RuntimeException("id not found");
        }
        Article byId = getById(id);
        if (byId != null) {
            //如果是管理员或者本人则可以更新文章
            if (String.valueOf(byId.getUserId()).equals(String.valueOf(StpUtil.getLoginId())) || StpUtil.hasRole(StpRoleImpl.ADMINISTRATOR)) {
                logger.info("updating existing article");
                return update().set("delete_flag", 1).eq("id", id).update();
            } else {
                StpUtil.checkRole(StpRoleImpl.ADMINISTRATOR);
                return false;
            }
        } else {
            throw new RuntimeException("id not found");
        }
    }
}




