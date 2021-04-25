package com.kyblog.Dao;

import com.kyblog.entity.Kind;
import com.kyblog.entity.OrderMode;
import com.kyblog.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KindDao {

    List<Kind> queryKinds(@Param("status")Integer status, @Param("orderMode")OrderMode orderMode, @Param("page") Page page);

    int updateKind(Kind kind);

    int insertKind(Kind kind);

    int queryRows(@Param("status")Integer status);

    Kind queryKindById(Integer id);

    Kind queryKindByName(String name);
}
