package com.qihui.sun.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.domain.Article;
import com.qihui.sun.domain.PageReq;
import com.qihui.sun.mapper.ArticleMapper;
import com.qihui.sun.permission.StpRoleImpl;
import com.qihui.sun.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author bigsu
 * @description 针对表【article】的数据库操作Service实现
 * @createDate 2024-12-24 17:24:42
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {
    Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Override
    public boolean saveArticle(Article article) {
        if (article == null) {
            throw new RuntimeException("article is null");
        }
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
                logger.info("delete existing article");
                return update().set("delete_flag", 1).eq("id", id).update();
            } else {
                StpUtil.checkRole(StpRoleImpl.ADMINISTRATOR);
                return false;
            }
        } else {
            throw new RuntimeException("id not found");
        }
    }

    @Override
    public Page<Article> pageQuery(PageReq pageReq) {
        QueryChainWrapper<Article> queryChainWrapper = this.query()
                .eq("category_key", pageReq.getParamValue())
                .eq("delete_flag", 0);
        switch (pageReq.getSelectType()) {
            case ALL:
                break;
            case MINE:
                queryChainWrapper.eq("user_id", StpUtil.getLoginIdAsInt());
                break;
            case OTHERS:
                queryChainWrapper.ne("user_id", StpUtil.getLoginIdAsInt());
                break;
        }
        return queryChainWrapper.page(new Page<>(pageReq.getCurrent(), pageReq.getSize()));
    }
}




