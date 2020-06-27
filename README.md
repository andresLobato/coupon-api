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
    
para una mejor detalle leer: EjercicioCupon.pdf

# Instrucciones de Uso

Utilizando cualquier herramienta que permita el envío de peticiones HTTP REST (ej: Postman), 
realizar una peticion POST a 
        https://20200623t205757-dot-rosy-element-281216.uc.r.appspot.com/coupon
con el siguiente body del tipo (application/json) :

        {
        "item_ids": ["MLA705767382","MLA620294494","MLA829679201","MLA652592912"],
        "amount": 10000
        }

en el cual se detallan los ids de los items que un usuario posee en sus favoritos y el monto del cupon q se desea maximizar su uso.

# Especificacion de la Api

- La api fue desarrollada con Play framework 2.8 y java 1.8
- la cobertura de test se realiza mediante Jacoco, el cual se puede verificar corriendo el comando : sbt clean jacoco


