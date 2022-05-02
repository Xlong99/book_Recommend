package book.dao.impl;

import book.dao.CommonDao;
import book.dao.TastePrefDao;
import book.domain.dataobject.TastePrefDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname TastePrefDODaoImpl
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 5:11
 * @Created by xlong99
 */
@Repository(value = "tastePrefDao")
public class TastePrefDaoImpl extends CommonDao implements TastePrefDao {
    @Override
    public int deleteByPrimaryKey(Long userId, Long itemId) {
        return 0;
    }

    @Override
    public int insert(TastePrefDO record) {
        return 0;
    }

    @Override
    public int insertSelective(TastePrefDO record) {
        return 0;
    }

    @Override
    public TastePrefDO selectByPrimaryKey(Long userId, Long itemId) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(TastePrefDO record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(TastePrefDO record) {
        return 0;
    }

    @Override
    public int insertList(List<TastePrefDO> tastePrefDOList) {
        return getSqlSession().insert("book.dao.TastePrefDao.insertList", tastePrefDOList);
    }

    @Override
    public int deleteAllRows() {
        return getSqlSession().update("book.dao.TastePrefDao.deleteAllRows");
    }
}
