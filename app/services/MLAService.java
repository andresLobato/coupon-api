package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import models.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import play.libs.Json;
import play.libs.ws.WSClient;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@Service
public class MLAService {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    final private Config config;
    final private WSClient ws;
    final private String urlApi;

    @Inject
    public MLAService(Config config, WSClient ws) {
        this.ws = ws;
        this.config = config;
        this.urlApi = this.config.getString("mercadolibre.api.url");
    }

  /*  /**
     * @param itemId
     * @return an Item with id and price.
     *
    public Item getItem(String itemId) {
        Item item = null;
        try {
            log.info("Getting item (" + itemId + ") from " + urlApi);
            CompletionStage<Item> response = ws.url(urlApi + "/" + itemId)
                    .get()
                    .thenApply(r -> Json.fromJson(r.asJson(), Item.class));

            item = response.toCompletableFuture().get(20, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("error getting item from mercardolibre", e);
        }

        return item;
    }*/

    /**
     * @param itemsIds
     * @return a list of Items with id and price.
     */
    public List<Item> getItems(List<String> itemsIds) {
        List<Item> result = Lists.newArrayList();
        try {
            log.info("Getting items (" + itemsIds + ") from " + urlApi);
            CompletionStage<List<Item>> response = ws.url(urlApi)
                    .addQueryParameter("ids", String.join(",", itemsIds))
                    .get()
                    .thenApply(r -> {
                        List<Item> items = Lists.newArrayList();
                        JsonNode jsonNode = r.asJson();
                        if (jsonNode.isArray()) {
                            jsonNode.forEach(j -> {
                                if (j.get("code").intValue() == 200) {
                                    items.add(Json.fromJson(j.get("body"), Item.class));
                                }
                            });
                        }
                        return items;
                    });

            result = response.toCompletableFuture().get(20, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("error getting items from mercardolibre", e);
        }

        return result;
    }

}