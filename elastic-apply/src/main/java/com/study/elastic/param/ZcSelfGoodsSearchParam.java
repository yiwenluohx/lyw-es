package com.study.elastic.param;

import java.io.Serializable;
import java.util.List;

/**
 * 质采自有商品查询入参param
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/7/3 下午2:07
 * @menu 质采自有商品查询入参param
 */
public class ZcSelfGoodsSearchParam implements Serializable {

    /**
     * 检索关键词
     */
    private String keywords;

    /**
     * 商品id集合
     */
    private List<Long> goodsIdList;

    /**
     * 商品编码集合
     */
    private List<String> goodsCodeList;

    /**
     * 商品唯一编码集合
     */
    private List<String> goodsUniqueCodeList;

    /**
     * 类目ID集合
     */
    private List<Long> categoryIdList;

    /**
     * 类目唯一编码
     */
    private List<String> categoryUniqueCodeList;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 属性项值集合
     */
    private List<String> attrsList;

    /**
     * 属性项值逻辑关系 1:and 0:or
     */
    private Integer operateType;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 商品状态
     */
    private Integer goodsState;

    /**
     * 商品删除状态 0：正常 1：删除
     */
    private Integer deleteFlag;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * Gets the value of keywords.
     *
     * @return the value of keywords
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets the keywords. *
     * <p>You can use getKeywords() to get the value of keywords</p>
     * * @param keywords keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * Gets the value of goodsIdList.
     *
     * @return the value of goodsIdList
     */
    public List<Long> getGoodsIdList() {
        return goodsIdList;
    }

    /**
     * Sets the goodsIdList. *
     * <p>You can use getGoodsIdList() to get the value of goodsIdList</p>
     * * @param goodsIdList goodsIdList
     */
    public void setGoodsIdList(List<Long> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }

    /**
     * Gets the value of goodsCodeList.
     *
     * @return the value of goodsCodeList
     */
    public List<String> getGoodsCodeList() {
        return goodsCodeList;
    }

    /**
     * Sets the goodsCodeList. *
     * <p>You can use getGoodsCodeList() to get the value of goodsCodeList</p>
     * * @param goodsCodeList goodsCodeList
     */
    public void setGoodsCodeList(List<String> goodsCodeList) {
        this.goodsCodeList = goodsCodeList;
    }

    /**
     * Gets the value of goodsUniqueCodeList.
     *
     * @return the value of goodsUniqueCodeList
     */
    public List<String> getGoodsUniqueCodeList() {
        return goodsUniqueCodeList;
    }

    /**
     * Sets the goodsUniqueCodeList. *
     * <p>You can use getGoodsUniqueCodeList() to get the value of goodsUniqueCodeList</p>
     * * @param goodsUniqueCodeList goodsUniqueCodeList
     */
    public void setGoodsUniqueCodeList(List<String> goodsUniqueCodeList) {
        this.goodsUniqueCodeList = goodsUniqueCodeList;
    }

    /**
     * Gets the value of categoryIdList.
     *
     * @return the value of categoryIdList
     */
    public List<Long> getCategoryIdList() {
        return categoryIdList;
    }

    /**
     * Sets the categoryIdList. *
     * <p>You can use getCategoryIdList() to get the value of categoryIdList</p>
     * * @param categoryIdList categoryIdList
     */
    public void setCategoryIdList(List<Long> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

    /**
     * Gets the value of categoryUniqueCodeList.
     *
     * @return the value of categoryUniqueCodeList
     */
    public List<String> getCategoryUniqueCodeList() {
        return categoryUniqueCodeList;
    }

    /**
     * Sets the categoryUniqueCodeList. *
     * <p>You can use getCategoryUniqueCodeList() to get the value of categoryUniqueCodeList</p>
     * * @param categoryUniqueCodeList categoryUniqueCodeList
     */
    public void setCategoryUniqueCodeList(List<String> categoryUniqueCodeList) {
        this.categoryUniqueCodeList = categoryUniqueCodeList;
    }

    /**
     * Gets the value of categoryName.
     *
     * @return the value of categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the categoryName. *
     * <p>You can use getCategoryName() to get the value of categoryName</p>
     * * @param categoryName categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets the value of attrsList.
     *
     * @return the value of attrsList
     */
    public List<String> getAttrsList() {
        return attrsList;
    }

    /**
     * Sets the attrsList. *
     * <p>You can use getAttrsList() to get the value of attrsList</p>
     * * @param attrsList attrsList
     */
    public void setAttrsList(List<String> attrsList) {
        this.attrsList = attrsList;
    }

    /**
     * Gets the value of operateType.
     *
     * @return the value of operateType
     */
    public Integer getOperateType() {
        return operateType;
    }

    /**
     * Sets the operateType. *
     * <p>You can use getOperateType() to get the value of operateType</p>
     * * @param operateType operateType
     */
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    /**
     * Gets the value of goodsType.
     *
     * @return the value of goodsType
     */
    public Integer getGoodsType() {
        return goodsType;
    }

    /**
     * Sets the goodsType. *
     * <p>You can use getGoodsType() to get the value of goodsType</p>
     * * @param goodsType goodsType
     */
    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * Gets the value of goodsState.
     *
     * @return the value of goodsState
     */
    public Integer getGoodsState() {
        return goodsState;
    }

    /**
     * Sets the goodsState. *
     * <p>You can use getGoodsState() to get the value of goodsState</p>
     * * @param goodsState goodsState
     */
    public void setGoodsState(Integer goodsState) {
        this.goodsState = goodsState;
    }

    /**
     * Gets the value of deleteFlag.
     *
     * @return the value of deleteFlag
     */
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the deleteFlag. *
     * <p>You can use getDeleteFlag() to get the value of deleteFlag</p>
     * * @param deleteFlag deleteFlag
     */
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * Gets the value of unitId.
     *
     * @return the value of unitId
     */
    public Long getUnitId() {
        return unitId;
    }

    /**
     * Sets the unitId. *
     * <p>You can use getUnitId() to get the value of unitId</p>
     * * @param unitId unitId
     */
    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    /**
     * Gets the value of pageIndex.
     *
     * @return the value of pageIndex
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    /**
     * Sets the pageIndex. *
     * <p>You can use getPageIndex() to get the value of pageIndex</p>
     * * @param pageIndex pageIndex
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * Gets the value of pageSize.
     *
     * @return the value of pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets the pageSize. *
     * <p>You can use getPageSize() to get the value of pageSize</p>
     * * @param pageSize pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
