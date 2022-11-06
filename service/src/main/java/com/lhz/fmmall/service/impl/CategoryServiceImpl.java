package com.lhz.fmmall.service.impl;

import com.lhz.fmmall.dao.CategoryMapper;
import com.lhz.fmmall.entity.CategoryVO;
import com.lhz.fmmall.service.CategoryService;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询分类列表(包含三个级别的分类)
     * @return
     */
    @Override
    public ResultVO listCategories() {
        List<CategoryVO> categoryVOS = categoryMapper.selectAllCategories();
        ResultVO resultVO = new ResultVO(ResStatus.OK,"success",categoryVOS);
        return resultVO;
    }

    /**
     * 查询所有一级分类，同时查询当前一级分类下销量最高的6个商品
     * @return
     */
    @Override
    public ResultVO listFirstLecelCategories() {
        List<CategoryVO> categoryVOS = categoryMapper.selectFirstLevelCategories();
        ResultVO resultVO = new ResultVO(ResStatus.OK,"success",categoryVOS);
        return resultVO;
    }
}
