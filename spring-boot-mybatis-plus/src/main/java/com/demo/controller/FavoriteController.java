package com.demo.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.dao.FavoriteMapper;
import com.demo.entity.DataBean;
import com.demo.entity.Favorite;
import com.demo.entity.Notice;
import com.demo.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("favorite")
public class FavoriteController {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @PostMapping("/list")
    public Object find(@RequestBody Favorite favorite) {
        List<Favorite> list = favoriteMapper.selectList(new QueryWrapper<Favorite>().like(!StringUtils.isEmpty(favorite.getTitle()), "title", favorite.getTitle()));
        R r = new R();
        r.setData(list);
        return r;
    }

    @PostMapping("/save")
    public Object save(@RequestBody Favorite favorite) {
        if (StringUtils.isEmpty(favorite.getTitle())){

        }
        favoriteMapper.insert(favorite);
        R r = new R();
        return r;
    }
}