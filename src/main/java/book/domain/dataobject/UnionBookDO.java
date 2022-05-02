package book.domain.dataobject;

/**
 * @Classname UnionBookDO
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 2:39
 * @Created by xlong99
 */
public class UnionBookDO {
    /**
    * 唯一图书索引，书名和作者相同的表示同一本书
    */
    private Long unionBookId;

    public UnionBookDO(String bookName, String author) {
        this.bookName = bookName;
        this.author = author;
    }

    public UnionBookDO() {
    }

    @Override
    public String toString() {
        return "UnionBookDO{" +
                "unionBookId=" + unionBookId +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    /**
    * 书名
    */
    private String bookName;

    /**
    * 作者
    */
    private String author;

    public Long getUnionBookId() {
        return unionBookId;
    }

    public void setUnionBookId(Long unionBookId) {
        this.unionBookId = unionBookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}