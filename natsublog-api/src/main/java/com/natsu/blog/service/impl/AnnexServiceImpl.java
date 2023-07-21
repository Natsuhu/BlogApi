package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.config.properties.BlogProperties;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.AnnexMapper;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.entity.Annex;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.annex.AnnexStorageService;
import com.natsu.blog.utils.QQInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.UUID;

/**
 * 附件服务实现层
 *
 * @author NatsuKaze
 * @date 2023/7/21
 */
@Service
@Slf4j
public class AnnexServiceImpl extends ServiceImpl<AnnexMapper, Annex> implements AnnexService {

    @Autowired
    private AnnexMapper annexMapper;

    @Autowired
    private AnnexStorageService annexStorageService;

    @Autowired
    private BlogProperties blogProperties;

    @Override
    public String saveAnnex() {
        return null;
    }

    @Transactional
    @Override
    public String saveQQAvatar(String qq, Integer storageType) {
        try {
            String reflectId = UUID.randomUUID().toString().replace("-", "");
            //获取文件信息
            QQInfoUtils.ImageResource qqAvatar = QQInfoUtils.getQQAvatar(qq);
            //组装实体
            AnnexDTO annex = new AnnexDTO();
            annex.setReflectId(reflectId);
            annex.setName(Constants.FILE_NAME_QQ_AVATAR);
            annex.setStorageType(storageType);
            annex.setPath(blogProperties.getFile().get("path") + File.separator + reflectId);
            annex.setSuffix(qqAvatar.getImgType());
            annex.setSize(qqAvatar.getImgSize());
            annex.setParentPath(blogProperties.getFile().get("path"));
            //保存文件
            annexStorageService.store(qqAvatar.getInputStream(), annex);
            annexMapper.insert(annex);
            return annex.getId();
        } catch (Exception e) {
            log.error("保存QQ头像失败：{}", e.getMessage());
            throw new RuntimeException("保存QQ头像失败：" + e.getMessage());
        }
    }
}
