package models;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CouponResponseTest {


    @Test
    public void testCreateSetterAndGetters() {
        List<String> originalList = Lists.newArrayList("MLA1", "MLA2");
        List<String> changedList = Lists.newArrayList("MLA1", "MLA3");
        CouponResponse couponResponse = new CouponResponse(90f, originalList);
        assertTrue(90f == couponResponse.getTotal());
        assertArrayEquals(originalList.toArray(), couponResponse.getItem_ids().toArray());
        assertTrue(couponResponse.hasItems());
        couponResponse.setTotal(100f);
        assertTrue(100f == couponResponse.getTotal());
        couponResponse.setItem_ids(changedList);
        assertArrayEquals(changedList.toArray(), couponResponse.getItem_ids().toArray());
    }

    @Test
    public void testConstructor() {
        CouponResponse couponResponse = new CouponResponse();
        assertFalse(couponResponse.hasItems());
    }

    @Test
    public void testTotalZero() {
        List<String> originalList = Lists.newArrayList("MLA1", "MLA2");
        CouponResponse couponResponse = new CouponResponse(0f, originalList);
        assertFalse(couponResponse.hasItems());
    }

    @Test
    public void testItemsNull() {
        CouponResponse couponResponse = new CouponResponse(90f, null);
        assertFalse(couponResponse.hasItems());
    }

}