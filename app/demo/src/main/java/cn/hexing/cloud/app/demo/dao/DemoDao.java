package com.cheetah.cloud.app.demo.dao;

import com.cheetah.common.web.entity.system.Pair;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * mybatis mapper interface demo
 *
 * @author cheetah.zsy
 */
public interface DemoDao {
    /**
     * 查询对应code下拉框的所有选项
     *
     * @param code         下拉框code
     * @param localeString 语言区域
     * @return 符合条件的选项列表
     */
    List<Pair> queryOptions(@Param("code") String code, @Param("localeString") String localeString);
}
