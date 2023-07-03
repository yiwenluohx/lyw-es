package com.study.elastic.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 质采自有商品Dto
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/7/3 下午1:40
 * @menu 质采自有商品Dto
 */
public class ZcSelfGoodsModel implements Serializable {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品唯一编码
     */
    private String goodsUniqueCode;

    /**
     * 中台商品ID
     */
    private String middlegroundGoodsId;

    /**
     * 商品来源类型 1：正式 2：临时
     */
    private Integer sourceType;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 商品状态 1:正常 2:禁用
     */
    private Integer goodsState;

    /**
     * 商品删除状态 0：正常 1：删除
     */
    private Integer deleteFlag;

    /**
     * 类目ID
     */
    private Long categoryId;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 类目唯一编码
     */
    private String categoryUniqueCode;

    /**
     * 商品单位ID
     */
    private Long unitId;

    /**
     * 商品单位名称
     */
    private String unitName;

    /**
     * 单位符号
     */
    private String unitSymbol;

    /**
     * 单位精度
     */
    private String unitAccuracy;

    /**
     * 单位小数点位数
     */
    private Integer decimalDigit;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 商品属性信息
     */
    private String goodsAttrShow;

    /**
     * 商品属性项值组合集合
     */
    private List<String> goodsAttrValPair;

    /**
     * Gets the value of goodsId.
     *
     * @return the value of goodsId
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * Sets the goodsId. *
     * <p>You can use getGoodsId() to get the value of goodsId</p>
     * * @param goodsId goodsId
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * Gets the value of goodsName.
     *
     * @return the value of goodsName
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * Sets the goodsName. *
     * <p>You can use getGoodsName() to get the value of goodsName</p>
     * * @param goodsName goodsName
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * Gets the value of goodsCode.
     *
     * @return the value of goodsCode
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * Sets the goodsCode. *
     * <p>You can use getGoodsCode() to get the value of goodsCode</p>
     * * @param goodsCode goodsCode
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * Gets the value of goodsUniqueCode.
     *
     * @return the value of goodsUniqueCode
     */
    public String getGoodsUniqueCode() {
        return goodsUniqueCode;
    }

    /**
     * Sets the goodsUniqueCode. *
     * <p>You can use getGoodsUniqueCode() to get the value of goodsUniqueCode</p>
     * * @param goodsUniqueCode goodsUniqueCode
     */
    public void setGoodsUniqueCode(String goodsUniqueCode) {
        this.goodsUniqueCode = goodsUniqueCode;
    }

    /**
     * Gets the value of middlegroundGoodsId.
     *
     * @return the value of middlegroundGoodsId
     */
    public String getMiddlegroundGoodsId() {
        return middlegroundGoodsId;
    }

    /**
     * Sets the middlegroundGoodsId. *
     * <p>You can use getMiddlegroundGoodsId() to get the value of middlegroundGoodsId</p>
     * * @param middlegroundGoodsId middlegroundGoodsId
     */
    public void setMiddlegroundGoodsId(String middlegroundGoodsId) {
        this.middlegroundGoodsId = middlegroundGoodsId;
    }

    /**
     * Gets the value of sourceType.
     *
     * @return the value of sourceType
     */
    public Integer getSourceType() {
        return sourceType;
    }

    /**
     * Sets the sourceType. *
     * <p>You can use getSourceType() to get the value of sourceType</p>
     * * @param sourceType sourceType
     */
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
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
     * Gets the value of categoryId.
     *
     * @return the value of categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the categoryId. *
     * <p>You can use getCategoryId() to get the value of categoryId</p>
     * * @param categoryId categoryId
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
     * Gets the value of categoryUniqueCode.
     *
     * @return the value of categoryUniqueCode
     */
    public String getCategoryUniqueCode() {
        return categoryUniqueCode;
    }

    /**
     * Sets the categoryUniqueCode. *
     * <p>You can use getCategoryUniqueCode() to get the value of categoryUniqueCode</p>
     * * @param categoryUniqueCode categoryUniqueCode
     */
    public void setCategoryUniqueCode(String categoryUniqueCode) {
        this.categoryUniqueCode = categoryUniqueCode;
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
     * Gets the value of unitName.
     *
     * @return the value of unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Sets the unitName. *
     * <p>You can use getUnitName() to get the value of unitName</p>
     * * @param unitName unitName
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * Gets the value of unitSymbol.
     *
     * @return the value of unitSymbol
     */
    public String getUnitSymbol() {
        return unitSymbol;
    }

    /**
     * Sets the unitSymbol. *
     * <p>You can use getUnitSymbol() to get the value of unitSymbol</p>
     * * @param unitSymbol unitSymbol
     */
    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    /**
     * Gets the value of unitAccuracy.
     *
     * @return the value of unitAccuracy
     */
    public String getUnitAccuracy() {
        return unitAccuracy;
    }

    /**
     * Sets the unitAccuracy. *
     * <p>You can use getUnitAccuracy() to get the value of unitAccuracy</p>
     * * @param unitAccuracy unitAccuracy
     */
    public void setUnitAccuracy(String unitAccuracy) {
        this.unitAccuracy = unitAccuracy;
    }

    /**
     * Gets the value of decimalDigit.
     *
     * @return the value of decimalDigit
     */
    public Integer getDecimalDigit() {
        return decimalDigit;
    }

    /**
     * Sets the decimalDigit. *
     * <p>You can use getDecimalDigit() to get the value of decimalDigit</p>
     * * @param decimalDigit decimalDigit
     */
    public void setDecimalDigit(Integer decimalDigit) {
        this.decimalDigit = decimalDigit;
    }

    /**
     * Gets the value of createTime.
     *
     * @return the value of createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * Sets the createTime. *
     * <p>You can use getCreateTime() to get the value of createTime</p>
     * * @param createTime createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets the value of updateTime.
     *
     * @return the value of updateTime
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the updateTime. *
     * <p>You can use getUpdateTime() to get the value of updateTime</p>
     * * @param updateTime updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Gets the value of goodsAttrShow.
     *
     * @return the value of goodsAttrShow
     */
    public String getGoodsAttrShow() {
        return goodsAttrShow;
    }

    /**
     * Sets the goodsAttrShow. *
     * <p>You can use getGoodsAttrShow() to get the value of goodsAttrShow</p>
     * * @param goodsAttrShow goodsAttrShow
     */
    public void setGoodsAttrShow(String goodsAttrShow) {
        this.goodsAttrShow = goodsAttrShow;
    }

    /**
     * Gets the value of goodsAttrValPair.
     *
     * @return the value of goodsAttrValPair
     */
    public List<String> getGoodsAttrValPair() {
        return goodsAttrValPair;
    }

    /**
     * Sets the goodsAttrValPair. *
     * <p>You can use getGoodsAttrValPair() to get the value of goodsAttrValPair</p>
     * * @param goodsAttrValPair goodsAttrValPair
     */
    public void setGoodsAttrValPair(List<String> goodsAttrValPair) {
        this.goodsAttrValPair = goodsAttrValPair;
    }
}
