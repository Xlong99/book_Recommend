package book.dao.impl;

import book.dao.CommonDao;
import book.dao.UnionBookDao;
import book.domain.dataobject.UnionBookDO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname UnionBookDao
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 2:53
 * @Created by xlong99
 */
@Repository(value = "unionBookDao")
public class UnionBookDaoImpl extends CommonDao implements UnionBookDao {
    @Override
    public int deleteByPrimaryKey(Integer unionBookId) {
        return 0;
    }

    @Override
    public int insert(UnionBookDO record) {
        return getSqlSession().insert("book.dao.UnionBookDao.insert", record);
    }

    @Override
    public int insertSelective(UnionBookDO record) {
        return 0;
    }

    @Override
    public UnionBookDO selectByPrimaryKey(Long unionBookId) {
        return getSqlSession().selectOne("book.dao.UnionBookDao.selectByPrimaryKey", unionBookId);
    }

    @Override
    public int updateByPrimaryKeySelective(UnionBookDO record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(UnionBookDO record) {
        return 0;
    }

    @Override
    public UnionBookDO selectOneByBookNameAndAuthor(String bookName, String author) {
        Map<String,Object> para = new HashMap<>();
        para.put("bookName",bookName);
        para.put("author",author);
        return getSqlSession().selectOne("book.dao.UnionBookDao.selectOneByBookNameAndAuthor", para);
    }
}
