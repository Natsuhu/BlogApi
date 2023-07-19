package com.natsu.blog.service.annex;

import com.natsu.blog.enums.StorageType;
import com.natsu.blog.utils.SpringContextUtils;
import org.springframework.stereotype.Component;


/**
 * 附件存储工厂
 *
 * @author NatsuKaze
 * @date 2023/7/19
 */
@Component
public class AnnexChannelFactory {

    /**
     * 获取附件处理器
     *
     * @param storageType 处理器类型
     * @return 处理器实例
     */
    public AnnexChannel getChannel(StorageType storageType) {
        switch (storageType) {
            case LOCAL:
                return SpringContextUtils.getBean(LocalChannel.class);
            case MINIO:
                return null;
        }
        throw new RuntimeException("不支持的存储方式");
    }

}
