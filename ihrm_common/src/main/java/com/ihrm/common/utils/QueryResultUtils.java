package com.ihrm.common.utils;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/29 11:17
 */
public class QueryResultUtils {

    /**
     * 判断查询出来的单个对象是否为空
     *
     * @param t   查询出来的单个对象
     * @param <T> 泛型
     * @return Result
     */
    public static <T> Result checkQueryResult(T t) {
        if (t == null) {
            return new Result(ResultCode.NO_DATA);
        }
        return new Result(ResultCode.SUCCESS, t);
    }


    /**
     * 判断查询出来的集合是否为空
     *
     * @param list 查询出来的集合
     * @param <T>  泛型
     * @return Result
     */
    public static <T> Result checkQueryListResult(List<T> list) {
        if (list != null && list.size() > 0) {
            return new Result(ResultCode.SUCCESS, list);
        }
        return new Result(ResultCode.NO_DATA);
    }

    /**
     * 判断查询出来的page对象是否为空
     *
     * @param page 查询出来的page对象
     * @param <T>  泛型
     * @return Result
     */
    public static <T> Result checkQueryPageResult(Page<T> page) {
        if (page != null && page.getTotalElements() > 0) {
            PageResult<T> pageResult =
                    new PageResult<>(page.getTotalElements(), page.getContent());
            return new Result(ResultCode.SUCCESS, pageResult);
        }
        return new Result(ResultCode.NO_DATA);
    }

    /**
     * 判断查询出来的map对象是否为空
     *
     * @param map 查询出来的map对象
     * @return Result
     */
    public static Result checkQueryMapResult(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            return new Result(ResultCode.SUCCESS,map);
        }
        return new Result(ResultCode.NO_DATA);
    }
}
