package book.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import book.dao.UnionBookDao;
import book.domain.dataobject.UnionBookDO;
import book.service.UnionBookService;
/**
 * @Classname UnionBookDOServiceImpl
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 2:39
 * @Created by xlong99
 */
@Service
public class UnionBookServiceImpl implements UnionBookService {

    @Resource(name = "unionBookDao")
    private UnionBookDao unionBookDao;

    @Override
    public int deleteByPrimaryKey(Integer unionBookId) {
        return unionBookDao.deleteByPrimaryKey(unionBookId);
    }

    @Override
    public int insert(UnionBookDO record) {
        return unionBookDao.insert(record);
    }

    @Override
    public int insertSelective(UnionBookDO record) {
        return unionBookDao.insertSelective(record);
    }

    @Override
    public UnionBookDO selectByPrimaryKey(Long unionBookId) {
        return unionBookDao.selectByPrimaryKey(unionBookId);
    }

    @Override
    public int updateByPrimaryKeySelective(UnionBookDO record) {
        return unionBookDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UnionBookDO record) {
        return unionBookDao.updateByPrimaryKey(record);
    }

}
