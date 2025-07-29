package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.MusicCatalogDTO;
import com.natsu.blog.model.dto.MusicDTO;
import com.natsu.blog.model.dto.MusicQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 音乐管理控制层
 */
@RestController
@Slf4j
@RequestMapping("/admin/music")
public class AdminMusicController {

    @Autowired
    private MusicService musicService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "歌曲列表")
    @PostMapping("/getMusicTable")
    public Result getMusicTable(@RequestBody MusicQueryDTO queryCond) {
        try {
            IPage<MusicDTO> musicTable = musicService.getMusicTable(queryCond);
            return Result.success(musicTable.getPages(), musicTable.getTotal(), musicTable.getRecords());
        } catch (Exception e) {
            log.error("获取歌曲列表失败，{}", e.getMessage());
            return Result.fail("获取歌曲列表G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.INSERT, description = "歌曲")
    @PostMapping("/saveMusic")
    public Result saveMusic(@RequestBody MusicDTO musicDTO) {
        if (StrUtil.isNotBlank(musicDTO.getId())) {
            return Result.fail("不要携带ID");
        }
        Result result = checkParam(musicDTO);
        if (result != null) {
            return result;
        }
        try {
            musicService.saveMusic(musicDTO);
            return Result.success("保存成功");
        } catch (Exception e) {
            log.error("新增歌曲失败，{}", e.getMessage());
            return Result.fail("新增歌曲G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "歌曲")
    @PostMapping("/updateMusic")
    public Result updateMusic(@RequestBody MusicDTO musicDTO) {
        if (StrUtil.isBlank(musicDTO.getId())) {
            return Result.fail("ID必填");
        }
        try {
            musicService.updateMusic(musicDTO);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新歌曲失败，{}", e.getMessage());
            return Result.fail("更新歌曲G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DELETE, description = "歌曲")
    @PostMapping("/deleteMusic")
    public Result deleteMusic(@RequestBody MusicDTO musicDTO) {
        if (StrUtil.isBlank(musicDTO.getId())) {
            return Result.fail("ID必填");
        }
        try {
            musicService.deleteMusic(musicDTO);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("更新歌曲失败，{}", e.getMessage());
            return Result.fail("更新歌曲G了：" + e.getMessage());
        }
    }

    private Result checkParam(MusicDTO musicDTO) {
        if (StrUtil.isBlank(musicDTO.getTitle())) {
            return Result.fail("曲名必填");
        }
        if (StrUtil.isBlank(musicDTO.getAuthor())) {
            return Result.fail("歌手必填");
        }
        if (StrUtil.isBlank(musicDTO.getMusicUrl())) {
            return Result.fail("歌曲链接必填");
        }
        if (StrUtil.isBlank(musicDTO.getCatalogId())) {
            return Result.fail("歌单必填");
        }
        return null;
    }



}
