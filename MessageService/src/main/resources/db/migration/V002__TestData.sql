INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES ('User1GroupChat', 'Test user1 chat', 'GROUP', 1, null, null, CURRENT_TIMESTAMP);
INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES ('User2GroupChat', 'Test user2 chat', 'GROUP', 2, null, null, CURRENT_TIMESTAMP);
INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES ('User4GroupChat', 'Test user4 chat', 'GROUP', 4, null, null, CURRENT_TIMESTAMP);

INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES (null, null, 'PRIVATE', null, 1, 4, CURRENT_TIMESTAMP);
INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES (null, null, 'PRIVATE', null, 1, 3, CURRENT_TIMESTAMP);
INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES (null, null, 'PRIVATE', null, 4, 2, CURRENT_TIMESTAMP);
INSERT INTO chats (name, description, type, owner_id, user1_id, user2_id, created_at) VALUES (null, null, 'PRIVATE', null, 4, 3, CURRENT_TIMESTAMP);


INSERT INTO chat_participants (chat_id, user_id) VALUES (1, 1);
INSERT INTO chat_participants (chat_id, user_id) VALUES (1, 3);
INSERT INTO chat_participants (chat_id, user_id) VALUES (1, 4);

INSERT INTO chat_participants (chat_id, user_id) VALUES (2, 4);
INSERT INTO chat_participants (chat_id, user_id) VALUES (2, 2);

INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 1);
INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 2);
INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 3);
INSERT INTO chat_participants (chat_id, user_id) VALUES (3, 4);



INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (4, 1, 'Hello User4', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (4, 4, 'Hello User1', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (4, 4, 'How are you?', CURRENT_TIMESTAMP);

INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (5, 3, 'What do you want?', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (5, 1, 'All i want for x-mas is u', CURRENT_TIMESTAMP);

INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (6, 4, 'Hey wassup', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (6, 2, 'Good n u?', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (6, 4, 'Could be better', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (6, 4, 'I broke my heart', CURRENT_TIMESTAMP);

INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (7, 4, 'I will beat u', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (7, 4, 'Watch ur back', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (7, 3, 'U r so funny', CURRENT_TIMESTAMP);



INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (1, 1, 'Hello everybody', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (1, 1, 'I need ur help', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (1, 4, 'Whats wrong?', CURRENT_TIMESTAMP);

INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (2, 4, 'Hey bro', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (2, 2, 'Hey, lets do some stuff', CURRENT_TIMESTAMP);

INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (3, 4, 'Hi', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (3, 3, 'Hey', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (3, 1, 'Wassup', CURRENT_TIMESTAMP);
INSERT INTO messages (chat_id, sender_id, text, created_at) VALUES (3, 2, 'Salam', CURRENT_TIMESTAMP);
