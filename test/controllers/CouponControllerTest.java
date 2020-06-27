package controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import models.Item;
import org.junit.Test;
import org.mockito.Mockito;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import services.CouponService;
import services.MLAService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.*;

public class CouponControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    final Http.Request requestMock = Mockito.mock(Http.Request.class);
    HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

    private MLAService mockMlaService = mock(MLAService.class);
    private CouponService couponService = new CouponService(mockMlaService);
    CouponController controller = new CouponController(ec, couponService);

    private List<Item> itemList = Lists.newArrayList(
            new Item("MLA1", 100f),
            new Item("MLA2", 210f),
            new Item("MLA3", 260f),
            new Item("MLA4", 80f),
            new Item("MLA5", 90f));


    @Test
    public void testIndex() throws ExecutionException, InterruptedException {
        String jsonStr = "{\"item_ids\": [\"MLA1\", \"MLA2\", \"MLA3\", \"MLA4\", \"MLA5\"],\"amount\": 500}";
        when(requestMock.body()).thenReturn(new Http.RequestBody(getJsonNode(jsonStr)));
        when(mockMlaService.getItems(any(List.class))).thenReturn(itemList);
        CompletionStage<Result> coupon = controller.coupon(requestMock);
        Result result = coupon.toCompletableFuture().get();
        assertEquals(OK, result.status());
    }

    @Test
    public void testValidateRequestAmount() throws ExecutionException, InterruptedException {
        String jsonStr = "{\"item_ids\": [\"MLA1\", \"MLA2\", \"MLA3\", \"MLA4\", \"MLA5\"],\"amount\": 0}";
        when(requestMock.body()).thenReturn(new Http.RequestBody(getJsonNode(jsonStr)));
        CompletionStage<Result> coupon = controller.coupon(requestMock);
        Result result = coupon.toCompletableFuture().get();
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testInternalError() throws ExecutionException, InterruptedException {
        String jsonStr = "{\"item_ids\": [\"MLA1\", \"MLA2\", \"MLA3\", \"MLA4\", \"MLA5\"],\"amount\": ed30}";
        when(requestMock.body()).thenReturn(new Http.RequestBody(getJsonNode(jsonStr)));
        CompletionStage<Result> coupon = controller.coupon(requestMock);
        Result result = coupon.toCompletableFuture().get();
        assertEquals(INTERNAL_SERVER_ERROR, result.status());
    }

    @Test
    public void testValidateEmptyList() throws ExecutionException, InterruptedException {
        String jsonStr = "{\"item_ids\": [],\"amount\": 30}";
        when(requestMock.body()).thenReturn(new Http.RequestBody(getJsonNode(jsonStr)));
        CompletionStage<Result> coupon = controller.coupon(requestMock);
        Result result = coupon.toCompletableFuture().get();
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testValidateNullList() throws ExecutionException, InterruptedException {
        String jsonStr = "{\"amount\": 30}";
        when(requestMock.body()).thenReturn(new Http.RequestBody(getJsonNode(jsonStr)));
        CompletionStage<Result> coupon = controller.coupon(requestMock);
        Result result = coupon.toCompletableFuture().get();
        assertEquals(BAD_REQUEST, result.status());
    }

    private JsonNode getJsonNode(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

}
