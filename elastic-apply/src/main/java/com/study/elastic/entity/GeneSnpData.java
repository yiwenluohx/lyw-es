package com.study.elastic.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * ES数据库文件类型
 * ES库名：gene_snp_data
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/29 下午5:51
 * @menu ES数据库文件类型
 */
@Document(indexName = "gene_snp_data")
public class GeneSnpData {
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 文件ID
     */
    @Field(type = FieldType.Keyword)
    private String file_id;
    /**
     * 位置
     */
    @Field(type = FieldType.Long)
    private Long position;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date,
            format = DateFormat.date_hour_minute_second)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone="UTC-8")
    private Date create_time;

    /**
     * 开始时间(用于时间范围条件查询)
     */
    private Date startTime;
    /**
     * 结束时间(用于时间范围条件查询)
     */
    private Date endTime;
    /**
     * 起始位置(用于范围条件查询)
     */
    private Long beginPosition;
    /**
     * 终止位置(用于范围条件查询)
     */
    private Long endPosition;

    /**
     * Gets the value of id.
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id. *
     * <p>You can use getId() to get the value of id</p>
     * * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the value of file_id.
     *
     * @return the value of file_id
     */
    public String getFile_id() {
        return file_id;
    }

    /**
     * Sets the file_id. *
     * <p>You can use getFile_id() to get the value of file_id</p>
     * * @param file_id file_id
     */
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    /**
     * Gets the value of position.
     *
     * @return the value of position
     */
    public Long getPosition() {
        return position;
    }

    /**
     * Sets the position. *
     * <p>You can use getPosition() to get the value of position</p>
     * * @param position position
     */
    public void setPosition(Long position) {
        this.position = position;
    }

    /**
     * Gets the value of create_time.
     *
     * @return the value of create_time
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * Sets the create_time. *
     * <p>You can use getCreate_time() to get the value of create_time</p>
     * * @param create_time create_time
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    /**
     * Gets the value of startTime.
     *
     * @return the value of startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the startTime. *
     * <p>You can use getStartTime() to get the value of startTime</p>
     * * @param startTime startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the value of endTime.
     *
     * @return the value of endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the endTime. *
     * <p>You can use getEndTime() to get the value of endTime</p>
     * * @param endTime endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the value of beginPosition.
     *
     * @return the value of beginPosition
     */
    public Long getBeginPosition() {
        return beginPosition;
    }

    /**
     * Sets the beginPosition. *
     * <p>You can use getBeginPosition() to get the value of beginPosition</p>
     * * @param beginPosition beginPosition
     */
    public void setBeginPosition(Long beginPosition) {
        this.beginPosition = beginPosition;
    }

    /**
     * Gets the value of endPosition.
     *
     * @return the value of endPosition
     */
    public Long getEndPosition() {
        return endPosition;
    }

    /**
     * Sets the endPosition. *
     * <p>You can use getEndPosition() to get the value of endPosition</p>
     * * @param endPosition endPosition
     */
    public void setEndPosition(Long endPosition) {
        this.endPosition = endPosition;
    }
}
