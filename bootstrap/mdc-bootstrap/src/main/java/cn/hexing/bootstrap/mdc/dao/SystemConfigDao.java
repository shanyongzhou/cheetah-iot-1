package com.cheetah.bootstrap.mdc.dao;

import com.cheetah.bootstrap.mdc.entity.ConfigurationProperty;

import java.util.List;

/**
 * p_sys_configuration_items 系统配置表的相关数据库操作
 */
public interface SystemConfigDao {
    /**
     * 查询所有的系统配置
     * @return
     */
    List<ConfigurationProperty> getPropertiesList();

    /**
     * 根据配置项名称查询对应的配置值
     * @param name 配置项名称，数据库表中的 item_name 字段
     * @return 配置值，由于查询条件是主键，拥有唯一约束，可以查到唯一值
     */
    String getPropertyValueByName(String name);
}
