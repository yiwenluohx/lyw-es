package com.study.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: luohx
 * @Description:
 * @Date: 2021/7/26 19:07
 */
@Repository
public interface ProductDao extends ElasticsearchRepository<Product,Long> {
}
