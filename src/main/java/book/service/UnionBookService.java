package book.service;

import book.domain.dataobject.UnionBookDO;
    /**
 * @Classname UnionBookDOService
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 2:39
 * @Created by xlong99
 */
public interface UnionBookService {


    int deleteByPrimaryKey(Integer unionBookId);

    int insert(UnionBookDO record);

    int insertSelective(UnionBookDO record);

    UnionBookDO selectByPrimaryKey(Long unionBookId);

    int updateByPrimaryKeySelective(UnionBookDO record);

    int updateByPrimaryKey(UnionBookDO record);

}
