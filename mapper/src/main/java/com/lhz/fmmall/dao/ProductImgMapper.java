package com.lhz.fmmall.dao;

import com.lhz.fmmall.entity.ProductImg;
import com.lhz.fmmall.general.GeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImgMapper extends GeneralDAO<ProductImg> {

    public List<ProductImg> selectProductImgByProductId(int productId);

}