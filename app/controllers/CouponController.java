package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.CouponRequest;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CouponService;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This controller contains an action to handle HTTP requests
 * to the maximize coupon.
 */
public class CouponController extends Controller {

    private HttpExecutionContext ec;
    private CouponService service;

    @Inject
    public CouponController(HttpExecutionContext ec, CouponService service) {
        this.ec = ec;
        this.service = service;
    }

    /**
     * @param request
     * @return Ok if the process is correct with items.
     *          NotFound if the process is correct without items.
     *          badRequest otherwise.
     */
    public CompletionStage<Result> coupon(Http.Request request) {
        try {
            JsonNode json = request.body().asJson();
            final CouponRequest resource = Json.fromJson(json, CouponRequest.class);
            if (validateRequest(resource)) {
                return service.maximizeCoupon(resource)
                        .thenApplyAsync(response -> {
                            if (response.hasItems())
                                return ok(Json.toJson(response));
                            else
                                return notFound();
                        }, ec.current());
            } else {
                return supplyAsync(() -> badRequest("Expecting Json valid"));
            }
        } catch (Exception e) {
            Logger.error("Error couponController: ", e);
        }
        return supplyAsync(() -> internalServerError());
    }

    /**
     * Valid that the amount is greater than zero and the list of itemsIds is not empty.
     * @param rqt
     * @return True if this request is enabled for maximize coupon,
     *         false otherwise.
     */
    private boolean validateRequest(CouponRequest rqt) {
        return rqt.getAmount() > 0f && rqt.getItem_ids() != null && !rqt.getItem_ids().isEmpty();
    }

}