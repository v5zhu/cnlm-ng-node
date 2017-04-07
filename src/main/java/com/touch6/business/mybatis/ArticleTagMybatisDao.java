package com.touch6.business.mybatis;


import com.touch6.business.entity.User;
import com.touch6.business.entity.article.ArticleTag;
import com.touch6.business.mybatis.common.MyBatisRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuxl on 2015/5/20.
 */

@MyBatisRepository
public interface ArticleTagMybatisDao {
    int addArticleTag(List<ArticleTag> articleTags);
}
