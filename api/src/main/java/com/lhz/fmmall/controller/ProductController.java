package com.lhz.fmmall.controller;

import com.lhz.fmmall.service.ProductCommentsService;
import com.lhz.fmmall.service.ProductService;
import com.lhz.fmmall.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/product")
@Api(value = "提供商品信息相关的接口",tags = "商品管理")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCommentsService productCommentsService;

    @GetMapping("/detail-info/{pid}")
    @ApiOperation("商品基本信息查询接口")
    public ResultVO getProductBasicInfo(@PathVariable("pid") String pid) {
        return productService.getProductBasicInfo(pid);
    }

    @GetMapping("/detail-params/{pid}")
    @ApiOperation("商品参数信息查询接口")
    public ResultVO getProductParams(@PathVariable("pid") String pid) {
        return productService.getProductParamsById(pid);
    }

    @GetMapping("/detail-comments/{pid}")
    @ApiOperation("商品评论信息查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "当前页码", required = true),
            @ApiImplicitParam(dataType = "int", name = "limit", value = "每页显示条数", required = true)
    })
    public ResultVO getProductComments(@PathVariable("pid") String pid, int pageNum, int limit) {
        return productCommentsService.listCommentByProductId(pid,pageNum,limit);
    }

    @GetMapping("/detail-commentscount/{pid}")
    @ApiOperation("商品评价统计查询接口")
    public ResultVO getProductCommentsCount(@PathVariable("pid") String pid) {
        return productCommentsService.getCommentsCountByProductId(pid);
    }

    @GetMapping("/listbycid/{cid}")
    @ApiOperation("根据类别查询商品接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "当前页码", required = true),
            @ApiImplicitParam(dataType = "int", name = "limit", value = "每页显示条数", required = true)
    })
    public ResultVO getProductComments(@PathVariable("cid") int cid, int pageNum, int limit) {
        return productService.getProductsByCategoryId(cid, pageNum, limit);
    }

    @ApiOperation("根据类别查询商品品牌接口")
    @GetMapping("/listbrands/{cid}")
    public ResultVO getBrandsByCategoryId(@PathVariable("cid") int cid) {
        return productService.listBrands(cid);
    }

    @GetMapping("/listbykeyword")
    @ApiOperation("根据关键字查询商品接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "keyword", value = "搜索关键字", required = true),
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "当前页码", required = true),
            @ApiImplicitParam(dataType = "int", name = "limit", value = "每页显示条数", required = true)
    })
    public ResultVO searchProducts(String keyword, int pageNum, int limit) {
        return productService.searchProduct(keyword, pageNum, limit);
    }

    @ApiOperation("根据关键字查询商品品牌接口")
    @GetMapping("/listbrands-keyword")
    @ApiImplicitParam(dataType = "String", name = "keyword", value = "搜索关键字", required = true)
    public ResultVO getBrandsByKeyword(String keyword) {
        return productService.listBrands(keyword);
    }
}
