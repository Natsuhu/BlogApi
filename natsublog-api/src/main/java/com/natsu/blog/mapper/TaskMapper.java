package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.natsu.blog.model.dto.TaskDTO;
import com.natsu.blog.model.dto.TaskQueryDTO;
import com.natsu.blog.model.entity.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMapper extends BaseMapper<Task> {

    IPage<TaskDTO> getTaskTable(Page<TaskDTO> page, @Param("queryCond") TaskQueryDTO queryCond);

}
