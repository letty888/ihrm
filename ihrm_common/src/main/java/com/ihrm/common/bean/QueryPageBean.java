package com.ihrm.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装查询条件
 *
 * @author zhang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryPageBean implements Serializable {

    private static final long serialVersionUID = -4726576663407624336L;
    /**
     * 页码
     */
    private Integer currentPage;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 查询条件
     */
    private String queryString;


}