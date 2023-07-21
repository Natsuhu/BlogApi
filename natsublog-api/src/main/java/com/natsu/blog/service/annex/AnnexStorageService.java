package com.natsu.blog.service.annex;

import com.natsu.blog.model.dto.AnnexDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 附件存储服务
 *
 * @author NatsuKaze
 * @date 2023/7/21
 */
@Service
@Slf4j
public class AnnexStorageService {

    /**
     * 附件存储工厂
     */
    @Autowired
    private AnnexChannelFactory annexChannelFactory;

    /**
     * 存储
     *
     * @param multipartFile 分片文件
     * @param annexDTO      存储信息
     */
    public void store(MultipartFile multipartFile, AnnexDTO annexDTO) {
        annexChannelFactory.getChannel(annexDTO.getStorageType()).store(multipartFile, annexDTO);
    }

    /**
     * 存储
     *
     * @param is       输入流
     * @param annexDTO 存储信息
     */
    public void store(InputStream is, AnnexDTO annexDTO) {
        annexChannelFactory.getChannel(annexDTO.getStorageType()).store(is, annexDTO);
    }

    /**
     * 存储
     *
     * @param file     文件对象
     * @param annexDTO 存储信息
     */
    public void store(File file, AnnexDTO annexDTO) {
        annexChannelFactory.getChannel(annexDTO.getStorageType()).store(file, annexDTO);
    }

    /**
     * 获取
     *
     * @param annexDTO 存储信息
     * @return InputStream
     */
    public InputStream fetch(AnnexDTO annexDTO) {
        return annexChannelFactory.getChannel(annexDTO.getStorageType()).fetch(annexDTO);
    }

    /**
     * 删除文件
     *
     * @param annexDTO 存储信息
     * @return 删除成功或失败
     */
    public Boolean remove(AnnexDTO annexDTO) {
        return annexChannelFactory.getChannel(annexDTO.getStorageType()).remove(annexDTO);
    }

}
