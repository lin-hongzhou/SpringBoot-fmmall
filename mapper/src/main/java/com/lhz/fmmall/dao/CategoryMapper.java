package com.lhz.fmmall.dao;

import com.lhz.fmmall.entity.Category;
import com.lhz.fmmall.entity.CategoryVO;
import com.lhz.fmmall.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends GeneralDAO<Category> {

    //1.使用连接查询实现分类查询
    public List<CategoryVO> selectAllCategories();

    //2.子查询，根据parentId查询子分类
    public List<CategoryVO> selectAllCategories2(int parentId);

    //查询一级类别
    public List<CategoryVO> selectFirstLevelCategories();
}