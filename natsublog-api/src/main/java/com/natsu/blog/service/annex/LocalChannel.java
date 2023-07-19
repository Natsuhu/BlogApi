package com.natsu.blog.service.annex;

import com.natsu.blog.enums.StorageType;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.utils.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@Component
public class LocalChannel implements AnnexChannel {

    /**
     * 获取存储类型
     *
     * @return 存储方式代码
     */
    public Integer getType() {
        return StorageType.LOCAL.getType();
    }

    /**
     * 获取存储方式的描述
     *
     * @return 存储方式描述
     */
    public String getDescription() {
        return StorageType.LOCAL.getDescription();
    }

    /**
     * 存储文件。
     * 必填参数：parentPath(文件上级路径)，path(文件完整路径)
     *
     * @param multipartFile 分片文件
     * @param annexDTO 存储信息
     */
    @Override
    public void store(MultipartFile multipartFile, AnnexDTO annexDTO) {
        try {
            //建立本地文件存储目录
            String parentPath = annexDTO.getParentPath();
            File file = new File(parentPath);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            //转为文件
            multipartFile.transferTo(new File(annexDTO.getPath()));
        } catch (Exception e) {
            throw new RuntimeException("保存文件失败");
        }

    }

    /**
     * 存储文件
     * 必填项：reflectId(映射ID)，parentPath(存放目录)
     *
     * @param is 输入流
     * @param annexDTO 存储信息
     */
    @Override
    public void store(InputStream is, AnnexDTO annexDTO) {
        FileUtils.streamToFile(is, annexDTO.getReflectId(), annexDTO.getParentPath());
    }

    @Override
    public void store(File file, AnnexDTO annexDTO) {
        
    }

    @Override
    public InputStream fetch(AnnexDTO annexDTO) {
        return null;
    }

    @Override
    public void remove(AnnexDTO annexDTO) {

    }


}
