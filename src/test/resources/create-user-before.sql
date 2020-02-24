delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$OPS5nUUXxFr7r39q4XX0BuGTmPKN/NGjxnlJTTDf6j/0CS5Y3f7j6', '999'),
(2, true, '$2a$08$53mnHz1EaCsKNrawBUCO9OmcsnTxdjxX2lJ0rBepx45hlXKt4AIpq', '333');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');