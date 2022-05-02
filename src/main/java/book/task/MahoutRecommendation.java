package book.task;

import book.dao.*;
import book.domain.dataobject.*;
import com.google.common.base.Joiner;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

/**
 * @Classname MahoutRecommendation
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 4:09
 * @Created by xlong99
 */
@Component
public class MahoutRecommendation {

    @Resource(name = "unionBookDao")
    private UnionBookDao unionBookDao;
    @Resource(name = "bookDao")
    private BookDao bookDao;
    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "borrowDao")
    private BorrowDao borrowDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "tastePrefDao")
    private TastePrefDao tastePrefDao;
    @Resource(name = "recommendDao")
    private RecommendDao recommendDao;

    private final int RECOMMEND_COUNT = 3;

    public MahoutRecommendation() {
        System.out.println("MahoutRecommendation初始化");
    }

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void userCF() throws TasteException {
        //数据预处理
        PriorityQueue<Map.Entry<Long, Integer>> topBook = pretreatmentData();
        ArrayList<Long> topBookList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            topBookList.add(Objects.requireNonNull(topBook.poll()).getKey());
        }

        //构建推荐引擎
        DataModel dataModel = new MySQLJDBCDataModel(dataSource, "taste_pref", "user_id", "item_id", "pref_value", null);
        UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, dataModel);
        GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        recommendDao.deleteBefore(1);
        //为每个用户进行推荐
        List<UserDO> userDOList = userDao.listAllUsers();
        for (UserDO userDO : userDOList) {
            List<RecommendedItem> recommendedItems = recommender.recommend(userDO.getUserId(), RECOMMEND_COUNT);
            Set<Long> recommendList = new TreeSet<>();
            for (RecommendedItem recommendedItem : recommendedItems) {
                recommendList.add(recommendedItem.getItemID());
                UnionBookDO unionBookDO = unionBookDao.selectByPrimaryKey(recommendedItem.getItemID());
                pushToDataBase(unionBookDO.getBookName(), unionBookDO.getAuthor(), userDO.getUserId());
            }
            Iterator<Long> iterator = topBookList.iterator();
            while (recommendList.size() < 3 && iterator.hasNext()) {
                Long next = iterator.next();
                //如果借阅最大堆中取出的书读者已经看过，则跳过
                if (borrowDao.queryByUnionIdAndUserId(next, userDO.getUserId()) != null) {
                    continue;
                }
                //如果这本书已经在推荐列表中，则跳过
                if (recommendList.contains(next)) {
                    continue;
                }
                recommendList.add(next);
                UnionBookDO unionBookDO = unionBookDao.selectByPrimaryKey(next);
                pushToDataBase(unionBookDO.getBookName(), unionBookDO.getAuthor(), userDO.getUserId());
            }
        }
    }

    /**
     * 数据预处理,并返回热销书籍的最大堆
     *
     * @throws TasteException
     */
    public PriorityQueue<Map.Entry<Long, Integer>> pretreatmentData() throws TasteException {
        List<BorrowDO> borrowDOList = borrowDao.listAllBorrows();
        List<TastePrefDO> prefDOList = new ArrayList<>();

        //记录借阅记录第一次出现的索引位置,key为user-item拼接的字符串
        Map<String, Integer> hasSeenTheBook = new HashMap<>();
        //维护每本书被借阅的次数，用来维护最大堆
        Map<Long, Integer> readCount = new HashMap<>();
        //一个借阅量的最大堆
        PriorityQueue<Map.Entry<Long, Integer>> priorityQueue = new PriorityQueue<>((a, b) -> (b.getValue().compareTo(a.getValue())));
        for (BorrowDO borrowDO : borrowDOList) {
            BookDO bookDO = bookDao.queryBookByBookId(borrowDO.getBookId());
            UserDO userDO = userDao.queryByUserId(borrowDO.getUserId());
            //不通过校验
            if (bookDO == null || userDO == null) {
                continue;
            }
            //TODO 没打分的过滤

            //查询这本书对应的唯一id
            UnionBookDO unionBookDO = unionBookDao.selectOneByBookNameAndAuthor(bookDO.getBookName(), bookDO.getAuthor());
            String key = Joiner.on("-").skipNulls().join(borrowDO.getUserId(), unionBookDO.getUnionBookId());

            //借阅量和topN
            readCount.put(unionBookDO.getUnionBookId(), readCount.getOrDefault(unionBookDO.getUnionBookId(), 0)+1);
            priorityQueue.clear();
            priorityQueue.addAll(readCount.entrySet());

            Integer first = hasSeenTheBook.get(key);
            //这个人看这本书不是第一次看
            if (first != null) {
                //更新评分数据
                prefDOList.get(first).setPrefValue((double) borrowDO.getGoal());
            } else {
                //第一次看
                hasSeenTheBook.put(key, prefDOList.size());
                prefDOList.add(new TastePrefDO(borrowDO.getUserId(), unionBookDO.getUnionBookId(), (double) borrowDO.getGoal()));
            }
        }
        tastePrefDao.deleteAllRows();
        tastePrefDao.insertList(prefDOList);
        return priorityQueue;
    }

    private void pushToDataBase(String bookName, String author, long userId) {
        RecommendationDO recommendationDO = new RecommendationDO();
        recommendationDO.setUserId(userId);
        recommendationDO.setBookName(bookName);
        recommendationDO.setAuthor(author);
        recommendationDO.setRecommendType(1);
        recommendDao.addRecommendation(recommendationDO);
    }
}
