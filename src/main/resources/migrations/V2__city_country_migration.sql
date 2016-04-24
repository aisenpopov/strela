INSERT INTO country (id, name) VALUES (nextval('country_id_seq'), 'Россия');

INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Другое', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Москва', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Санкт-Петербург', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Московская область', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Смоленск', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Элиста', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Иркутск', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Красноярск', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Дербент', 1);
INSERT INTO city (id, name, country_id) VALUES (nextval('city_id_seq'), 'Казань', 1);