package book.dao;

import book.domain.dataobject.UnionBookDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Classname UnionBookDOMapper
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 2:39
 * @Created by xlong99
 */

public interface UnionBookDao {
    int deleteByPrimaryKey(Integer unionBookId);

    int insert(UnionBookDO record);

    int insertSelective(UnionBookDO record);

    UnionBookDO selectByPrimaryKey(Long unionBookId);

    int updateByPrimaryKeySelective(UnionBookDO record);

    int updateByPrimaryKey(UnionBookDO record);
    UnionBookDO selectOneByBookNameAndAuthor(@Param("bookName") String bookName,@Param("author") String author);
}