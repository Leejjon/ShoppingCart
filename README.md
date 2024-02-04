# InterGamma assessment V2


## Design keuzes
* Een project gegenereerd via https://start.spring.io/
* Omdat API documentatie een optionele extra functionaliteit was, heb ik https://springdoc.org/ toegevoegd. Met Spring boot 3 en later kreeg ik deze docs alleen werkend met maven als build systeem.
* Voor het voorraadbeheer heb ik voor een in memory HSQL database gekozen. Simpelweg omdat deze met een dependency is toe te voegen.
* Het eerste wat ik deed voor deze opdracht was het datamodel voor de database maken. De opdracht spreekt over CRUD van productvoorraad. Om een productvoorraad goed te kunnen simuleren heb ik drie tabellen gemaakt:
  * Een STORES tabel met daarin de vier Gamma en Karwei vestigingen bij mij in de buurt.
  * Een PRODUCT tabel met de product informatie die het zelfde is ongeacht van de winkel.
  * Een STOCK kopppeltabel met het aantal exemplaren van het product, store id en product id.
* Voor de eis om een artikel voor vijf minuten te kunnen reserveren, heb ik nog twee tabellen gemaakt:
  * Een RESERVATIONS koppeltabel. Als iemand 2 stuks van product X reserveerd, zal ik in een database transactie het aantal van het product in de STOCK tabel verminderen EN een nieuwe record in de RESERVATIONS tabel aanmaken. Dit nieuwe record heeft een timestamp die over vijf minuten verloopt.
  * Een ORDERS koppeltabel. Als iemand de reservatie uit de RESERVATIONS tabel binnen vijf minuten afrekent, dan wordt het een order en krijgt de consumer een order id.
* Ik maak een /cleanupReservations endpoint aan die eventueel door een cloud scheduler iedere minuut aangeroepen kan worden om in een transactie de stock van verlopen reserveringen weer vrij te geven.
* De in memory database wordt bij het opstarten automatisch gevuld door de schema.sql en data.sql scripts in src/main/resources.

## Design keuzes door tijdgebrek
* Omdat de applicatie heel makkelijk te testen is met de Swagger docs pagina heb ik besloten om geen frontend erbij te bouwen: http://localhost:8080/swagger-ui/index.html
* Omdat ik de meeste uitdagingen die dit projectje had in SQL queries zijn opgelost heb ik besloten geen unit testen te schrijven. 
* De validaties en error handling zijn minimaal geimplementeerd

## Gebruik van de CRUD API
* Je kan de applicatie draaien met het volgende commando: ./mvnw spring-boot:run
* Je kan de gegenereerde API documentatie bekijken en testen met deze handige: http://localhost:8080/swagger-ui/index.html
* Een GET call naar http://localhost:8080/api/stores geeft data van alle fysieke winkels terug
* Een GET call naar http://localhost:8080/api/products laat alle producten zien en de totale voorraad per product
* Een POST call met als body `{"initialQuantity": 15}` naar http://localhost:8080/api/stores/0/product/2/stock voegt voorraad toe voor product met id 2 aan de eerste winkel.
* Een PUT call met als body `{"delta": 10}` naar http://localhost:8080/api/stores/0/product/2/stock laat de voorraad voor product X in winkel Y updaten. Als je een negatieve delta invoert wordt de voorraad minder. Het request zal falen als de negatieve delta de voorraad minder dan 0 laat zijn, of als er geen voorraad is.
* Een DELETE call naar http://localhost:8080/api/stores/0/product/2/stock haalt de voorraad voor dit product weg.
* De acties van de bovenste drie calls kan je controleren met de products call (regel 22)

## Het gebruik van de reserveer API
* Een POST call met als body `{"productId": 0, "quantity": 2}` naar http://localhost:8080/api/reservations doet, kijkt de applicatie welke van de vier winkels op zijn minst twee van het product met productId 0 op voorraad hebben. Als antwoord krijg je een nummer (reservering id) terug.
* Als je binnen vijf minuten een POST naar http://localhost:8080/api/reservations/order/0 (0 is in dit geval het reservering id van de vorige call) doet haalt hij de reservering weg en maakt hij een order aan in de order tabel. Je krijgt dan terug wat je besteld hebt:
```json
{
  "orderId": 0,
  "ipAddress": "127.0.0.1",
  "productId": 0,
  "quantity": 2,
  "brand": "Gamma",
  "location": "Omval 57, 1812 NA Alkmaar"
}
```

## Het verlopen van de reserveringen
* Bij het aanmaken van reserveringen sla ik reserveringen op met een expiration timestamp over vijf minuten van nu.
* 
