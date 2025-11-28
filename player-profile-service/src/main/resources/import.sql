INSERT INTO player (id, username, email) VALUES (1, 'alice', 'alice@example.com');

INSERT INTO ownedgame (id, gameid, player_id) VALUES (1, 1, 1);

ALTER SEQUENCE player_id_seq RESTART WITH 2;
ALTER SEQUENCE ownedgame_id_seq RESTART WITH 2;