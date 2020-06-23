package models;

import java.util.List;

public class CouponResponse {

    private List<String> item_ids;
    private float total;

    public CouponResponse(float total, List<String> itemsIds) {
        this.total = total;
        this.item_ids = itemsIds;
    }

    public List<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(List<String> item_ids) {
        this.item_ids = item_ids;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean hasItems(){
        return (item_ids != null && !item_ids.isEmpty() && total > 0f);
    }

}