package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.dto.AnnexQueryDTO;
import com.natsu.blog.model.entity.Annex;
import com.natsu.blog.model.vo.AnnexDownloadVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnnexService extends IService<Annex> {

    /**
     * 保存文件
     *
     * @param multipartFile 分片文件
     * @return ID
     */
    String upload(MultipartFile multipartFile, Boolean isPublished);

    /**
     * 保存qq头像，目前仅评论功能使用
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
    AnnexDownloadVO download(String annexId);

    /**
     * 获取文件访问地址
     *
     * @param annexId 文件ID
     * @return 访问地址
     */
    String getAnnexAccessAddress(String annexId);

    /**
     * Admin获取文件访问地址
     *
     * @param annexId 文件ID
     * @return 地址
     */
    String getAdminAnnexAccessAddress(String annexId);

    /**
     * 获取文件管理表格
     *
     * @param queryCond 查询条件
     * @return 分页结果
     */
    IPage<AnnexDTO> getAnnexTable(AnnexQueryDTO queryCond);

    /**
     * 获取文件管理后缀名筛选项
     *
     * @return string
     */
    List<String> getSuffixSelector();

    /**
     * 更新文件
     * @param annexDTO annexDTO
     */
    void updateAnnex(AnnexDTO annexDTO);

    /**
     * 删除文件
     */
    void deleteAnnex(AnnexDTO annexDTO);
}
