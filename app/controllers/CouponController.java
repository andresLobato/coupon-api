package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.CouponRequest;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import services.CouponService;

import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
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
     *
     * @param request
     * @return
     */
    public CompletionStage<Result> coupon(Http.Request request) {
        JsonNode json = request.body().asJson();
        final CouponRequest resource = Json.fromJson(json, CouponRequest.class);
        return service.maximizeCoupon(resource)
                .thenApplyAsync(response -> {
                    if(response.hasItems())
                        return ok(Json.toJson(response));
                    else
                        return notFound();
                }, ec.current());
    }

}
