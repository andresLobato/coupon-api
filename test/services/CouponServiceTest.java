package services;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import models.CouponRequest;
import models.CouponResponse;
import models.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CouponServiceTest {

    private MLAService mockMlaService = mock(MLAService.class);
    private CouponService couponService = new CouponService(mockMlaService);
    private List<Item> itemList = Lists.newArrayList(
            new Item("MLA1", 100f),
            new Item("MLA2", 210f),
            new Item("MLA3", 260f),
            new Item("MLA4", 80f),
            new Item("MLA5", 90f));

    @Before
    public void setup() {
    }

    @Test
    public void testMaximizeCoupon() throws ExecutionException, InterruptedException {
        CouponRequest rqs = new CouponRequest(Lists.newArrayList("MLA1", "MLA2", "MLA3", "MLA4", "MLA5"), 500f);
        when(mockMlaService.getItems(any(List.class))).thenReturn(itemList);
        CompletionStage<CouponResponse> stage = couponService.maximizeCoupon(rqs);
        CouponResponse couponResponse = stage.toCompletableFuture().get();
        assertTrue(480f == couponResponse.getTotal());
    }

    @Test
    public void testCalculate() {
        Map<String, Float> items = Maps.newHashMap();
        items.put("MLA1", 100f);
        items.put("MLA2", 210f);
        items.put("MLA3", 260f);
        items.put("MLA4", 80f);
        items.put("MLA5", 90f);
        List<String> calculate = couponService.calculate(items, 480f);
        assertEquals(4, calculate.size());
        assertFalse(calculate.contains("MLA3"));
    }


}
