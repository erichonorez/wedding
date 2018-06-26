# Keep in touch request schema

# --- !Ups

CREATE TABLE KEEP_IN_TOUCH_REQUESTS (
    id          INTEGER     PRIMARY KEY AUTOINCREMENT,
    email       CHAR(128)   NOT NULL,
    request_on  DATETIME    DEFAULT CURRENT_TIMESTAMP
);

# --- !Downs

DROP TABLE KEEP_IN_TOUCH_REQUESTS;