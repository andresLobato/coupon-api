package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import models.Item;
import org.springframework.stereotype.Service;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


@Service
public class MLAService {

    final private Config config;
    final private WSClient ws;
    final private String urlApi;

    @Inject
    public MLAService(Config config, WSClient ws) {
        this.ws = ws;
        this.config = config;
        this.urlApi = this.config.getString("mercadolibre.api.url");
    }

    /**
     * @param itemsIds
     * @return a list of Items with id and price.
     */
    public List<Item> getItems(List<String> itemsIds) {
        List<Item> result = Lists.newArrayList();
        try {
            Logger.info("Getting items (" + itemsIds + ") from " + urlApi);
            CompletionStage<WSResponse> response = ws.url(urlApi)
                    .addQueryParameter("ids", String.join(",", itemsIds))
                    .get();
            WSResponse wsResponse = response.toCompletableFuture().get(20, TimeUnit.SECONDS);
            result = parseWsResponse(wsResponse);
        } catch (Exception e) {
            Logger.error("error getting items from mercardolibre", e);
        }

        return result;
    }

    /**
     * Method in charge of parsing the response from mercadolibre and filters the items that do not exist.
     *
     * @param response
     * @return a List of Items.
     */
    public List<Item> parseWsResponse(WSResponse response){
        List<Item> items = Lists.newArrayList();
        try{
            JsonNode jsonNode = response.asJson();
            if (jsonNode.isArray()) {
                jsonNode.forEach(j -> {
                    if (j.get("code").intValue() == 200) {
                        items.add(Json.fromJson(j.get("body"), Item.class));
                    }
                });
            }
        } catch (Exception e){
            Logger.error("error parsing response from ML-Api", e);
        }

        return items;
    }

}