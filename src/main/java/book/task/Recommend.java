package book.task;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Classname Recommend
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/15 21:04
 * @Created by xlong99
 */
public class Recommend {
    public static void main(String[] args) throws IOException, TasteException {
        DataModel dataModel = new FileDataModel(new File("D:\\Desktop\\u.txt"));
        //皮尔逊系数
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
//        //余弦相似度
//        UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);
//        //欧几里德距离
//        UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
//        //Spearman秩相关系数
//        UserSimilarity similarity = new SpearmanCorrelationSimilarity(dataModel);
//        //Tanimoto 系数
//        UserSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
//        //对数似然相似度
//        UserSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
//        //曼哈顿距离
//        UserSimilarity similarity = new CityBlockSimilarity(dataModel);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, dataModel);
        GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
        //对id为196的用户进行推荐
        List<RecommendedItem> recommendedItems = recommender.recommend(196, 5);
        System.out.println(recommendedItems);

    }
}
