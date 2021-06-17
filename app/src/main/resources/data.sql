INSERT INTO user (user_id, created_at, updated_at, user_email, user_password, user_pseudo)
VALUES (1, '2021-03-14 16:33:13.000000', null, 'noe@yahoo.fr',
        '$2a$10$L3g//frHMeJ.k7BJ2RhvROkwkoVwksxwwU3L5.mRgnuYjwR6ZmDZm', 'Nouuu');

INSERT INTO user (user_id, created_at, updated_at, user_email, user_password, user_pseudo)
VALUES (2, '2020-02-14 05:15:15.000000', null, 'swann@mail.fr', 'swann', 'gerard mentor');
INSERT INTO user (user_id, created_at, updated_at, user_email, user_password, user_pseudo)
VALUES (3, '2021-03-06 16:33:15.000000', null, 'admin@admin.fr', 'admin', 'admin');

INSERT INTO board (board_id, created_at, updated_at, project_name, owner_user_id, card_id_prefix, project_description)
VALUES (1, '2021-03-14 16:39:41.000000', null, 'vecolo', 1, 'prefix', 'description of this project board');
INSERT INTO board (board_id, created_at, updated_at, project_name, owner_user_id, card_id_prefix)
VALUES (2, '2021-03-14 16:39:43.000000', null, 'vekanban', 1, 'prefix');
INSERT INTO board (board_id, created_at, updated_at, project_name, owner_user_id, card_id_prefix)
VALUES (3, '2021-03-14 16:39:44.000000', null, 'reboot', 2, 'prefix');

INSERT INTO card_status(card_status_id, status)
VALUES (1, 'TODO');

INSERT INTO card_status(card_status_id, status)
VALUES (2, 'IN PROGRESS');

INSERT INTO card_status(card_status_id, status)
VALUES (3, 'DONE');

INSERT INTO card (card_id, status_card_status_id, title, content, assigned_board_board_id, created_at, updated_at,
                  assigned_user_user_id, due_date)
VALUES (1, 1, 'Card à faire', 'content zdza', 1, '2021-03-14 16:39:43.000000', null, 1, '2021-12-12');

INSERT INTO card (card_id, status_card_status_id, title, content, assigned_board_board_id, created_at, updated_at)
VALUES (2, 2, 'Card en cours', 'content zdza', 1, '2021-03-14 16:39:43.000000', null);

INSERT INTO card (card_id, status_card_status_id, title, content, assigned_board_board_id, created_at, updated_at)
VALUES (3, 2, 'Card en cours 2', 'content zdza', 1, '2021-03-14 16:39:43.000000', null);

INSERT INTO card (card_id, status_card_status_id, title, content, assigned_board_board_id, created_at, updated_at,
                  assigned_user_user_id)
VALUES (4, 3, 'Card terminée', 'content zdza', 1, '2021-03-14 16:39:43.000000', null, 2);

INSERT INTO card (card_id, status_card_status_id, title, content, assigned_board_board_id, created_at, updated_at,
                  assigned_user_user_id)
VALUES (5, 3, 'Card terminée 2', 'content zdza', 1, '2021-03-14 16:39:43.000000', null, 3);

INSERT INTO board_members(boards_member_board_id, members_user_id)
VALUES (3, 1);
INSERT INTO board_members(boards_member_board_id, members_user_id)
VALUES (1, 2);

INSERT INTO card_label(card_label_id, created_at, updated_at, name, board_board_id)
VALUES (1, '2021-03-14 16:39:43.000000', null, 'Label 1', 1);
INSERT INTO card_label(card_label_id, created_at, updated_at, name, board_board_id)
VALUES (2, '2021-03-14 16:39:43.000000', null, 'Label 2', 1);

insert into card_labels(associated_cards_card_id, labels_card_label_id)
VALUES (1, 1);

COMMIT;