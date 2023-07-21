package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.Annex;

public interface AnnexService extends IService<Annex> {

    String saveAnnex();

    /**
     * 保存qq头像
     *
     * @param qq　qq号
     * @param storageType　存储类型
     * @return 文件ID
     */
    String saveQQAvatar(String qq, Integer storageType);

}
