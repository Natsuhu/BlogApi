package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.Annex;

import java.util.HashMap;

public interface AnnexService extends IService<Annex> {

    /**
     * 保存文件
     *
     * @return ID
     */
    String saveAnnex();

    /**
     * 保存qq头像
     *
     * @param qq　qq号
     * @param storageType　存储类型
     * @return 文件ID
     */
    String saveQQAvatar(String qq, Integer storageType);

    /**
     * 下载文件
     *
     * @param annexId 文件Id
     * @return HashMap
     */
    HashMap<String, Object> download(String annexId);

    /**
     * 获取文件访问地址
     *
     * @param annexId 文件ID
     * @return 访问地址
     */
    String getAnnexAccessAddress(String annexId);
}
