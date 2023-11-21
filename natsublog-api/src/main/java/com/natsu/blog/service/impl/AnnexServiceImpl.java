package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.config.properties.BlogProperties;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.StorageType;
import com.natsu.blog.mapper.AnnexMapper;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.dto.AnnexQueryDTO;
import com.natsu.blog.model.entity.Annex;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.annex.AnnexStorageService;
import com.natsu.blog.utils.FileUtils;
import com.natsu.blog.utils.QQInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 文件服务实现层
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

    @Transactional
    @Override
    public String upload(MultipartFile multipartFile, Boolean isPublished) {
        //获取文件信息
        String reflectId = UUID.randomUUID().toString().replace("-", "");
        String fileName = multipartFile.getOriginalFilename();
        //组装参数
        AnnexDTO annexDTO = new AnnexDTO();
        annexDTO.setReflectId(reflectId);
        annexDTO.setName(fileName);
        annexDTO.setSuffix(FileUtils.getFileSuffix(fileName));
        annexDTO.setSize(multipartFile.getSize());
        annexDTO.setIsPublished(isPublished);
        annexDTO.setPath(blogProperties.getAnnex().get(Constants.PATH) + File.separator + reflectId);
        annexDTO.setParentPath(blogProperties.getAnnex().get(Constants.PATH));
        //TODO 额外信息--后续需针对媒体文件进行解析。存储方式--后续准备接入minio，目前默认磁盘存储
        annexDTO.setExtra("{\"null\":\"null\"}");
        annexDTO.setStorageType(StorageType.LOCAL.getType());
        //开始保存
        annexStorageService.store(multipartFile, annexDTO);
        annexMapper.insert(annexDTO);
        return annexDTO.getId();
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
            annex.setName(Constants.FILE_NAME_QQ_AVATAR + "." + qqAvatar.getImgType());
            annex.setSuffix(qqAvatar.getImgType());
            annex.setSize(qqAvatar.getImgSize());
            annex.setIsPublished(Constants.PUBLISHED);
            annex.setStorageType(storageType);
            annex.setPath(blogProperties.getAnnex().get(Constants.PATH) + File.separator + reflectId);
            annex.setExtra(qq);
            annex.setParentPath(blogProperties.getAnnex().get(Constants.PATH));
            //保存文件
            annexStorageService.store(qqAvatar.getInputStream(), annex);
            annexMapper.insert(annex);
            return annex.getId();
        } catch (Exception e) {
            log.error("保存QQ头像失败：{}", e.getMessage());
            throw new RuntimeException("保存QQ头像失败：" + e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param annexId 文件Id
     * @return HashMap
     */
    @Override
    public HashMap<String, Object> download(String annexId) {
        try {
            //从文件表获取记录
            Annex annex = annexMapper.selectById(annexId);
            //调用文件框架获取流
            AnnexDTO annexDTO = new AnnexDTO();
            BeanUtils.copyProperties(annex, annexDTO);
            InputStream is = annexStorageService.fetch(annexDTO);
            //封装结果
            HashMap<String, Object> result = new HashMap<>();
            result.put("inputStream", is);
            result.put("size", annex.getSize());
            result.put("fileName", annex.getName());
            result.put("isPublished", annex.getIsPublished());
            return result;
        } catch (Exception e) {
            log.error("下载文件失败：{}", e.getMessage());
            throw new RuntimeException("下载文件失败：" + e.getMessage());
        }
    }

    /**
     * 获取文件访问地址
     *
     * @param annexId 文件ID
     * @return 访问地址
     */
    @Override
    public String getAnnexAccessAddress(String annexId) {
        return blogProperties.getApi() + "/annex/resource/" + annexId;
    }

    /**
     * Admin使用--获取文件访问地址
     *
     * @param annexId 文件ID
     * @return 访问地址
     */
    @Override
    public String getAdminAnnexAccessAddress(String annexId) {
        return blogProperties.getApi() + "/admin/annex/download/" + annexId;
    }

    /**
     * 获取文件管理表格
     *
     * @param queryCond 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<AnnexDTO> getAnnexTable(AnnexQueryDTO queryCond) {
        IPage<AnnexDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        IPage<AnnexDTO> annexTable = annexMapper.getAnnexTable(page, queryCond);
        List<AnnexDTO> records = annexTable.getRecords();
        for (AnnexDTO annexDTO : records) {
            annexDTO.setFormatSize(FileUtils.formatFileSize(annexDTO.getSize()));
        }
        IPage<AnnexDTO> pageResult = new Page<>(annexTable.getCurrent(), annexTable.getSize(), annexTable.getTotal());
        pageResult.setRecords(records);
        return pageResult;
    }

    /**
     * 获取文件管理后缀名筛选项
     *
     * @return string
     */
    @Override
    public List<String> getSuffixSelector() {
        return annexMapper.getSuffixSelector();
    }

    /**
     * 更新文件
     *
     * @param annexDTO annexDTO
     */
    @Override
    public void updateAnnex(AnnexDTO annexDTO) {
        if (!StringUtils.isBlank(annexDTO.getName())) {
            String suffix = FileUtils.getFileSuffix(annexDTO.getName());
            if (suffix == null) {
                throw new RuntimeException("未知文件类型");
            }
            annexDTO.setSuffix(suffix);
        }
        updateById(annexDTO);
    }

    /**
     * 删除文件
     *
     * @param annexDTO annexDTO
     */
    @Transactional
    @Override
    public void deleteAnnex(AnnexDTO annexDTO) {
        //获取记录
        Annex annex = annexMapper.selectById(annexDTO);
        BeanUtils.copyProperties(annex, annexDTO);
        //删除文件
        annexStorageService.remove(annexDTO);
        annexMapper.deleteById(annex);
    }
}
