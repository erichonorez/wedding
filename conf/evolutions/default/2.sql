# Product and orders schema

# --- !Ups

CREATE TABLE PRODUCTS (
    ID            VARCHAR(36)    PRIMARY KEY,
    NAME          VARCHAR(128)   NOT NULL,
    PRICE         DOUBLE         NOT NULL,
    DESCRIPTION   TEXT           NOT NULL,
    IMAGE         VARCHAR(255),
    INITIAL_STOCK INT            NOT NULL
);

CREATE TABLE ORDERS (
    ID          VARCHAR(36)    PRIMARY KEY,
    REF         VARCHAR(128)   NOT NULL,
    CONFIRMED   BOOLEAN
);

CREATE TABLE ORDER_ITEMS (
    ORDER_ID    VARCHAR(36)    NOT NULL,
    PRODUCT_ID  VARCHAR(36)    NOT NULL,
    QUANTITY    INT            NOT NULL
);

# --- !Downs

DROP TABLE ORDERS;