package book.dao;

import book.domain.dataobject.BorrowDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hui zhang
 * @date 2018/3/21
 */
public interface BorrowDao {

    long addBorrowRecord(BorrowDO borrowDO);

    List<BorrowDO>listByUserId(long userId);

    long updateBorrow(BorrowDO borrowDO);

    BorrowDO queryByBorrowId(long borrowId);

    List<BorrowDO> listAllBorrows();

    long updateStatus(BorrowDO borrowDO);

    BorrowDO queryByUnionIdAndUserId(@Param("unionId")Long unionId, @Param("userId") Long userId);
}
