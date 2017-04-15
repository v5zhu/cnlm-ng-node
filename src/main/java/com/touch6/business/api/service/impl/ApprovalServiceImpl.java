package com.touch6.business.api.service.impl;

import com.touch6.business.api.service.ApprovalService;
import com.touch6.business.dto.common.ApprovalDto;
import com.touch6.business.dto.common.OpposeDto;
import com.touch6.business.entity.common.Approval;
import com.touch6.business.entity.common.Oppose;
import com.touch6.business.mybatis.ToutiaoMybatisDao;
import com.touch6.business.mybatis.article.ArticleCommentMybatisDao;
import com.touch6.business.mybatis.article.ArticleCommentReplyMybatisDao;
import com.touch6.business.mybatis.article.ArticleMybatisDao;
import com.touch6.business.mybatis.common.ApprovalMybatisDao;
import com.touch6.business.mybatis.common.OpposeMybatisDao;
import com.touch6.core.exception.CoreException;
import com.touch6.core.exception.ECodeUtil;
import com.touch6.core.exception.error.constant.CommonErrorConstant;
import com.touch6.core.exception.error.constant.SystemErrorConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.BeanMapper;

import java.util.Date;

/**
 * Created by xuan.touch6@qq.com on 2017/4/14.
 */
@SuppressWarnings("ALL")
@Service
public class ApprovalServiceImpl implements ApprovalService {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);

    @Autowired
    private ApprovalMybatisDao approvalMybatisDao;
    @Autowired
    private OpposeMybatisDao opposeMybatisDao;
    @Autowired
    private ArticleMybatisDao articleMybatisDao;
    @Autowired
    private ArticleCommentMybatisDao articleCommentMybatisDao;
    @Autowired
    private ArticleCommentReplyMybatisDao articleCommentReplyMybatisDao;
    @Autowired
    private ToutiaoMybatisDao toutiaoMybatisDao;

    @Override
    @Transactional
    public ApprovalDto makeApproval(ApprovalDto approvalDto) {
        //点赞
        Approval approval = BeanMapper.map(approvalDto, Approval.class);
        //插入点赞
        Date time = new Date();
        approval.setCreateTime(time);
        approval.setUpdateTime(time);
        int inserted = approvalMybatisDao.addApproval(approval);
        if (inserted == 1) {
            //插入返回1对象点赞数增加
            switch (approval.getTargetObject()) {
                case ARTICLE:
                    articleMybatisDao.increaseApprovalAmount(approval.getObjectId());
                    break;
                case ARTICLE_COMMENT:
                    articleCommentMybatisDao.increaseApprovalAmount(approval.getObjectId());
                    break;
                case ARTICLE_COMMENT_REPLY:
                    articleCommentReplyMybatisDao.increaseApprovalAmount(approval.getObjectId());
                    break;
                case NEWS:
                    toutiaoMybatisDao.increaseApprovalAmount(approval.getObjectId());
                    break;
            }
            return BeanMapper.map(approval, ApprovalDto.class);
        } else {
            throw new CoreException(ECodeUtil.getCommError(CommonErrorConstant.COMMON_PARAMS_ERROR));
        }
    }
}