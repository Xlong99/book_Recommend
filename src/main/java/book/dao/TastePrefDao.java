package book.dao;

import book.domain.dataobject.TastePrefDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname TastePrefDOMapper
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 4:48
 * @Created by xlong99
 */
public interface TastePrefDao {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("itemId") Long itemId);

    int insert(TastePrefDO record);

    int insertSelective(TastePrefDO record);

    TastePrefDO selectByPrimaryKey(@Param("userId") Long userId, @Param("itemId") Long itemId);

    int updateByPrimaryKeySelective(TastePrefDO record);

    int updateByPrimaryKey(TastePrefDO record);
    int insertList(List<TastePrefDO> tastePrefDOList);
    int deleteAllRows();
}