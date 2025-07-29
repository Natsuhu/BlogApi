package com.natsu.blog.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.natsu.blog.config.properties.BlogProperties;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.dto.MusicApiQueryDTO;
import com.natsu.blog.model.entity.Music;
import com.natsu.blog.model.entity.MusicCatalog;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.MusicCatalogService;
import com.natsu.blog.service.MusicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 音乐API
 *
 * @author NatsuKaze
 */
@Slf4j
@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private AnnexService annexService;

    @Autowired
    private MusicService musicService;

    @Autowired
    private MusicCatalogService musicCatalogService;

    @Autowired
    private BlogProperties blogProperties;

    @GetMapping("/api")
    public JSONArray getMusicDetail(MusicApiQueryDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        //简单的跨域认证。避免其他网站在后台复制了HTML代码就能直接使用
        String origin = request.getHeader("Origin");
        if (StrUtil.isBlank(origin) ||
                (!origin.equals(blogProperties.getFront()) && !origin.equals(blogProperties.getMs()))) {
            log.warn("跨站请求已拦截：[{}]", origin);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        //查询音乐信息
        JSONArray result = new JSONArray();
        String type = queryDTO.getType();
        String id = queryDTO.getId();
        if ("song".equals(type)) {
            Music music = musicService.getById(id);
            if (music != null && music.getIsPublished()) {
                JSONObject musicInfo = new JSONObject();
                musicInfo.put("title", music.getTitle());
                musicInfo.put("author", music.getAuthor());
                musicInfo.put("url", annexService.getAnnexAccessAddress(music.getMusicUrl()));
                musicInfo.put("pic", annexService.getAnnexAccessAddress(music.getPicUrl()));
                musicInfo.put("lrc", annexService.getAnnexAccessAddress(music.getLrcUrl()));
                result.add(musicInfo);
            }
        } else if ("playlist".equals(type)) {
            MusicCatalog musicCatalog = musicCatalogService.getById(id);
            if (musicCatalog != null && musicCatalog.getIsPublished()) {
                List<Music> musicList = musicService.getCatalogMusicList(id);
                for (Music music : musicList) {
                    JSONObject musicInfo = new JSONObject();
                    musicInfo.put("title", music.getTitle());
                    musicInfo.put("author", music.getAuthor());
                    musicInfo.put("url", annexService.getAnnexAccessAddress(music.getMusicUrl()));
                    musicInfo.put("pic", annexService.getAnnexAccessAddress(music.getPicUrl()));
                    musicInfo.put("lrc", annexService.getAnnexAccessAddress(music.getLrcUrl()));
                    result.add(musicInfo);
                }
            }
        }
        return result;
    }

}
