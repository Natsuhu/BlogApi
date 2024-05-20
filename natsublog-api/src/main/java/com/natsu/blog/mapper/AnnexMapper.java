package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.dto.AnnexQueryDTO;
import com.natsu.blog.model.entity.Annex;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnexMapper extends BaseMapper<Annex> {

    /**
     * 获取文件管理表格
     *
     * @param page 分页
     * @param queryCond 查询条件
     * @return 分页结果
     */
    IPage<AnnexDTO> getAnnexTable(IPage<AnnexDTO> page, @Param("queryCond") AnnexQueryDTO queryCond);

    /**
     * 获取文件管理后缀名筛选项
     *
     * @return string
     */
    List<String> getSuffixSelector();

}
