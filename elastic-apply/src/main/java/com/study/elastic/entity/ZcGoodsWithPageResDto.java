package com.study.elastic.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 质采商品es返回实体dto
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/7/3 下午1:53
 * @menu 质采商品es返回实体dto
 */
public class ZcGoodsWithPageResDto<T> implements Serializable {

    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 总结果数
     */
    private Long totalNum;

    /**
     * 返回结果集
     */
    private List<T> resList;

    /**
     * Gets the value of totalPage.
     *
     * @return the value of totalPage
     */
    public Long getTotalPage() {
        return totalPage;
    }

    /**
     * Sets the totalPage. *
     * <p>You can use getTotalPage() to get the value of totalPage</p>
     * * @param totalPage totalPage
     */
    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * Gets the value of totalNum.
     *
     * @return the value of totalNum
     */
    public Long getTotalNum() {
        return totalNum;
    }

    /**
     * Sets the totalNum. *
     * <p>You can use getTotalNum() to get the value of totalNum</p>
     * * @param totalNum totalNum
     */
    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * Gets the value of resList.
     *
     * @return the value of resList
     */
    public List<T> getResList() {
        return resList;
    }

    /**
     * Sets the resList. *
     * <p>You can use getResList() to get the value of resList</p>
     * * @param resList resList
     */
    public void setResList(List<T> resList) {
        this.resList = resList;
    }
}
