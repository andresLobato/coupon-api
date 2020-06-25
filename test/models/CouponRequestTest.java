package models;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class CouponRequestTest {

    @Test
    public void testCreateSetterAndGetters() {
        List<String> originalList = Lists.newArrayList("MLA1", "MLA2");
        List<String> changedList = Lists.newArrayList("MLA1", "MLA3");
        CouponRequest couponRequest = new CouponRequest(originalList, 90f);
        assertTrue(90f == couponRequest.getAmount());
        assertArrayEquals(originalList.toArray(), couponRequest.getItem_ids().toArray());
        couponRequest.setAmount(100f);
        assertTrue(100f == couponRequest.getAmount());
        couponRequest.setItem_ids(changedList);
        assertArrayEquals(changedList.toArray(), couponRequest.getItem_ids().toArray());
    }

}
