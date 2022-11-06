package com.lhz.fmmall.service.impl;

import com.lhz.fmmall.dao.IndexImgMapper;
import com.lhz.fmmall.entity.IndexImg;
import com.lhz.fmmall.service.IndexImgService;
import com.lhz.fmmall.vo.ResStatus;
import com.lhz.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service    //业务层注入spring容器
public class IndexImgServiceImpl implements IndexImgService {

    @Autowired
    private IndexImgMapper indexImgMapper;

    @Override
    public ResultVO listIndexImgs() {
        //使用自定义sql排序查询语句，查询的数据存到 List集合中
        List<IndexImg> indexImgs = indexImgMapper.listIndexImgs();
        if (indexImgs.size() == 0) {
            return new ResultVO(ResStatus.NO,"fail",null);
        } else {
            return new ResultVO(ResStatus.OK,"success",indexImgs);
        }
    }
}
