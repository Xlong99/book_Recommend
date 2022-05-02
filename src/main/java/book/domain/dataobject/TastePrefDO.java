package book.domain.dataobject;

/**
 * @Classname TastePrefDO
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/5/2 4:48
 * @Created by xlong99
 */
public class TastePrefDO {
    private Long userId;

    private Long itemId;

    private Double prefValue;

    public TastePrefDO(Long userId, Long itemId, Double prefValue) {
        this.userId = userId;
        this.itemId = itemId;
        this.prefValue = prefValue;
    }

    public TastePrefDO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Double getPrefValue() {
        return prefValue;
    }

    public void setPrefValue(Double prefValue) {
        this.prefValue = prefValue;
    }
}