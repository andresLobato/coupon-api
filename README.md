# coupon-api


Mercado Libre está implementando un nuevo beneficio para los usuarios que más usan la
plataforma con un cupón de cierto monto gratis que les permitirá comprar tantos items
marcados como favoritos que no excedan el monto total. Para esto se está analizando
construir una API que dado una lista de item_id y el monto total pueda darle la lista de items
que maximice el total gastado sin excederlo.
Aclaraciones:

- Sólo se puede comprar una unidad por item_id.
- No hay preferencia en la cantidad total de items siempre y cuando gasten el máximo
    posible.

## Ejemplo

Asumiendo la siguiente lista de precios y un máximo de $
Item_id Precio
MLA1 $
MLA2 $
MLA3 $
MLA4 $
MLA5 $
La respuesta sería: [“MLA1”, “MLA2”, “MLA4”, “MLA5”]

## Desafío

**Nivel 1:**
Programar (en cualquier lenguaje de programación) la funcionalidad mencionada
respetando la siguiente firma:
List<String> calculate(Map<String, Float> items, Float amount)
Lenguajes sugeridos: Java, Golang, Javascript.


**Nivel 2:**
Crear una API REST, con el servicio “/coupon/” en donde se pueda enviar la lista de
item_ids y el monto del cupón y devuelva los items que tendría que comprar el usuario.
POST → /coupon/

### Body:

#### {

"item_ids": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
"amount": 500
}

### Response:

#### {

"item_ids": ["MLA1", "MLA2", "MLA4", "MLA5"],
"total": 480
}
Con status HTTP 200-OK y en caso de que el monto no sea suficiente como para comprar
mínimamente un item devolver 404-NOT_FOUND
**Nivel 3:**
Hostear esa API en un cloud computing libre (Google App Engine, Amazon AWS, etc).
**Consideraciones:**
● Hay usuarios que tienen miles de items en favoritos.
● Esta api tendría que escalar para soportar tráfico de hasta 100K rpm.
● Generalmente los usuarios suelen marcar como favoritos a los mismos items.
Test-Automáticos, Code coverage > 80%.

## Entregar

● Código Fuente (Para Nivel 2 y 3: En repositorio github).
● Instrucciones de cómo ejecutar el programa o la API. (Para Nivel 2 y 3: En README de
github).
● URL de la API (Nivel 2 y 3).


## Recursos

Para obtener el precio de un item consultar la API de Items y luego acceder al valor
"price"​.
curl -X GET ​https://api.mercadolibre.com/items/$ITEM_ID
Ej:
curl -X GET https://api.mercadolibre.com/items/MLA
Response:
{
"id": "MLA599260060",
"title": "Item De Test - Por Favor No Ofertar",
**"price": 130,**
"site_id": "MLA",
...
}


