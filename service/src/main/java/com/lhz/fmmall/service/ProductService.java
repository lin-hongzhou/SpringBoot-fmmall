package com.lhz.fmmall.service;

import com.lhz.fmmall.vo.ResultVO;

public interface ProductService {

    public ResultVO listRecommendProducts();

    public ResultVO getProductBasicInfo(String productId);

    public ResultVO getProductParamsById(String productId);

    public ResultVO getProductsByCategoryId(int categoryId, int pageNum, int limit);

    public ResultVO listBrands(int categoryId);

    //根据关键字查询商品
    public ResultVO searchProduct(String kw, int pageNum, int limit);

    public ResultVO listBrands(String kw);
}
