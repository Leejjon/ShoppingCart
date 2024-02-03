/* Create the physical stores around my area */
INSERT INTO STORE (brand, location)
VALUES('Gamma', 'Omval 57, 1812 NA Alkmaar');

INSERT INTO STORE (brand, location)
VALUES('Gamma', 'J.J.P. Oudweg 16, 1703 DE Heerhugowaard');

INSERT INTO STORE (brand, location)
VALUES('Karwei', 'Koedijkerstraat 1, 1823 CR Alkmaar');

INSERT INTO STORE (brand, location)
VALUES('Karwei', 'Middelmoot 10, 1721 CZ Broek op Langedijk');

/* Make up two products */
INSERT INTO PRODUCT (name, color, DESCRIPTION)
VALUES('Houten Tafel', 'geel', 'Deze tafel is perfect geschikt voor vier personen.');

INSERT INTO PRODUCT (name, color, DESCRIPTION)
VALUES('Stoel', 'geel', 'Luxe stoel met armleuningen.');

INSERT INTO PRODUCT (name, color, DESCRIPTION)
VALUES('Stoel', 'blauw', 'Extra test');


/* Insert some stock */
INSERT INTO STOCK (QUANTITY, PRODUCTID, STOREID)
VALUES (10, 0, 0);

INSERT INTO STOCK (QUANTITY, PRODUCTID, STOREID)
VALUES (5, 0, 1);

INSERT INTO STOCK (QUANTITY, PRODUCTID, STOREID)
VALUES (1, 0, 2);

INSERT INTO STOCK (QUANTITY, PRODUCTID, STOREID)
VALUES (5, 0, 3);

INSERT INTO STOCK (QUANTITY, PRODUCTID, STOREID)
VALUES (5, 1, 1);
