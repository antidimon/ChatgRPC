INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (1, 'User1GroupChat', 'Test user1 chat', 'GROUP', 1, null, null);
INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (2, 'User2GroupChat', 'Test user2 chat', 'GROUP', 2, null, null);
INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (3, 'User4GroupChat', 'Test user4 chat', 'GROUP', 4, null, null);

INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (4, null, null, 'PRIVATE', null, 1, 4);
INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (5, null, null, 'PRIVATE', null, 1, 3);
INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (6, null, null, 'PRIVATE', null, 4, 2);
INSERT INTO chats (id, name, description, type, owner_id, user1_id, user2_id) VALUES (7, null, null, 'PRIVATE', null, 4, 3);


INSERT INTO chat_participants (chat_id, user_id) VALUES (1, 3);
INSERT INTO chat_participants (chat_id, user_id) VALUES (1, 4);
INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 1);
INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 2);
INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 3);
INSERT INTO chat_participants (chat_id, user_id) VALUES (2, 4);


INSERT INTO messages (chat_id, sender_id, text) VALUES (4, 1, 'Hello User4');
INSERT INTO messages (chat_id, sender_id, text) VALUES (4, 4, 'Hello User1');
INSERT INTO messages (chat_id, sender_id, text) VALUES (4, 4, 'How are you?');

INSERT INTO messages (chat_id, sender_id, text) VALUES (5, 3, 'What do you want?');
INSERT INTO messages (chat_id, sender_id, text) VALUES (5, 1, 'All i want for x-mas is u');

INSERT INTO messages (chat_id, sender_id, text) VALUES (6, 4, 'Hey wassup');
INSERT INTO messages (chat_id, sender_id, text) VALUES (6, 2, 'Good n u?');
INSERT INTO messages (chat_id, sender_id, text) VALUES (6, 4, 'Could be better');
INSERT INTO messages (chat_id, sender_id, text) VALUES (6, 4, 'I broke my heart');

INSERT INTO messages (chat_id, sender_id, text) VALUES (7, 4, 'I will beat u');
INSERT INTO messages (chat_id, sender_id, text) VALUES (7, 4, 'Watch ur back');
INSERT INTO messages (chat_id, sender_id, text) VALUES (7, 3, 'U r so funny');



INSERT INTO messages (chat_id, sender_id, text) VALUES (1, 1, 'Hello everybody');
INSERT INTO messages (chat_id, sender_id, text) VALUES (1, 1, 'I need ur help');
INSERT INTO messages (chat_id, sender_id, text) VALUES (1, 4, 'Whats wrong?');

INSERT INTO messages (chat_id, sender_id, text) VALUES (2, 4, 'Hey bro');
INSERT INTO messages (chat_id, sender_id, text) VALUES (2, 2, 'Hey, lets do some stuff');

INSERT INTO messages (chat_id, sender_id, text) VALUES (3, 4, 'Hi');
INSERT INTO messages (chat_id, sender_id, text) VALUES (3, 3, 'Hey');
INSERT INTO messages (chat_id, sender_id, text) VALUES (3, 1, 'Wassup');
INSERT INTO messages (chat_id, sender_id, text) VALUES (3, 2, 'Salam');
