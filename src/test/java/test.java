import book.controller.AdminController;
import book.dao.*;
import book.domain.dataobject.*;
import book.domain.dto.BookDTO;
import book.domain.dto.BorrowDTO;
import book.domain.dto.UserDTO;
import book.service.BookInfoService;
import book.service.BorrowService;
import book.service.RegisterService;
import book.service.UserService;
import book.task.MahoutRecommendation;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MySQLJDBCIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

/**
 * @author hui zhang
 * @date 2018-4-9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext.xml","classpath:spring/spring-mvc.xml"})
public class test {
    @Resource(name = "UserService")
   private UserService userService;

    @Resource(name ="RegisterService")
    private RegisterService registerService;

    @Resource(name = "borrowService")
    BorrowService borrowService;
    @Resource(name = "unionBookDao")
    UnionBookDao unionBookDao;
    @Resource(name = "bookDao")
     BookDao bookDao;
    @Resource(name = "dataSource")
    DataSource dataSource;
    @Resource(name = "borrowDao")
    private BorrowDao borrowDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "tastePrefDao")
    private TastePrefDao tastePrefDao;
    @Resource(name = "mahoutRecommendation")
    MahoutRecommendation mahoutRecommendation;
    @Test
    public void pretreatmentData() throws TasteException {
        List<BorrowDO> borrowDOList = borrowDao.listAllBorrows();
        List<TastePrefDO> prefDOList = new ArrayList<>();

        //????????????????????????????????????????????????,key???user-item??????????????????
        Map<String,Integer> hasSeenTheBook= new HashMap<>();
        //?????????????????????????????????????????????????????????
        Map<Long,Integer> readCount= new HashMap<>();
        //???????????????????????????
        Queue<Map.Entry<Long,Integer>> priorityQueue=new PriorityQueue<>((a, b)->(b.getValue().compareTo(a.getValue())));
        for (BorrowDO borrowDO : borrowDOList) {
            BookDO bookDO = bookDao.queryBookByBookId(borrowDO.getBookId());
            UserDO userDO = userDao.queryByUserId(borrowDO.getUserId());
            //???????????????
            if (bookDO == null || userDO == null) {
                continue;
            }
            //TODO ??????????????????

            //??????????????????????????????id
            UnionBookDO unionBookDO = unionBookDao.selectOneByBookNameAndAuthor(bookDO.getBookName(), bookDO.getAuthor());
            String key = Joiner.on("-").skipNulls().join(borrowDO.getUserId(),unionBookDO.getUnionBookId());

            //????????????topN
            readCount.put(unionBookDO.getUnionBookId(), readCount.getOrDefault(unionBookDO.getUnionBookId(),0));
            priorityQueue.clear();
            priorityQueue.addAll(readCount.entrySet());

            Integer first = hasSeenTheBook.get(key);
            //???????????????????????????????????????
            if(first!=null){
                //??????????????????
                prefDOList.get(first).setPrefValue((double) borrowDO.getGoal());
            }
            else{
                //????????????
                hasSeenTheBook.put(key,prefDOList.size());
                prefDOList.add(new TastePrefDO(borrowDO.getUserId(),unionBookDO.getUnionBookId(),(double)borrowDO.getGoal()));
            }
        }
        tastePrefDao.deleteAllRows();
        tastePrefDao.insertList(prefDOList);
    }
    @Test
    public void testCF() throws TasteException {
        mahoutRecommendation.userCF();
    }



}
