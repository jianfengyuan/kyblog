package com.kyblog.Dao;

import com.kyblog.entity.Kind;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KindDao {

    List<Kind> queryKinds(Integer status, Integer orderMode);

    int updateKind(Kind kind);

    int insertKind(Kind kind);

    Kind queryKindById(Integer id);

    Kind queryKindByName(String name);
}
