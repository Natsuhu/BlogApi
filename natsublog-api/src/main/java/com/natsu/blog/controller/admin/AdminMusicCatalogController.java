package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.MusicCatalogDTO;
import com.natsu.blog.model.dto.MusicCatalogQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.MusicCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/musicCatalog")
public class AdminMusicCatalogController {

    @Autowired
    private MusicCatalogService musicCatalogService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "歌单列表")
    @PostMapping("/getMusicCatalogList")
    public Result getMusicCatalogList(@RequestBody MusicCatalogQueryDTO queryCond) {
        try {
            List<MusicCatalogDTO> musicCatalogList = musicCatalogService.getMusicCatalogList(queryCond);
            return Result.success(musicCatalogList);
        } catch (Exception e) {
            log.error("获取歌单列表失败，{}", e.getMessage());
            return Result.fail("获取歌单列表G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.INSERT, description = "歌单")
    @PostMapping("/saveMusicCatalog")
    public Result saveMusicCatalog(@RequestBody MusicCatalogDTO musicCatalogDTO) {
        if (musicCatalogDTO.getId() != null) {
            return Result.fail("不要携带ID");
        }
        Result result = checkParam(musicCatalogDTO);
        if (result != null) {
            return result;
        }
        try {
           musicCatalogService.saveMusicCatalog(musicCatalogDTO);
           return Result.success("保存成功");
        } catch (Exception e) {
            log.error("保存歌单失败，{}", e.getMessage());
            return Result.fail("保存歌单G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "歌单")
    @PostMapping("/updateMusicCatalog")
    public Result updateMusicCatalog(@RequestBody MusicCatalogDTO musicCatalogDTO) {
        if (StrUtil.isBlank(musicCatalogDTO.getId())) {
            return Result.fail("ID必填");
        }
        try {
            musicCatalogService.updateMusicCatalog(musicCatalogDTO);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("保存歌单失败，{}", e.getMessage());
            return Result.fail("保存歌单G了：" + e.getMessage());
        }
    }

    private Result checkParam(MusicCatalogDTO musicCatalogDTO) {
        if (StrUtil.isBlank(musicCatalogDTO.getName())) {
            return Result.fail("歌单名称必填");
        }
        return null;
    }

}
