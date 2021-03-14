INSERT INTO user (user_id, created_at, updated_at, user_email, user_password, user_pseudo)
VALUES (1, '2021-03-14 16:33:13.000000', null, 'noe@yahoo.fr', 'noe', 'nospy');
INSERT INTO user (user_id, created_at, updated_at, user_email, user_password, user_pseudo)
VALUES (2, '2021-03-06 16:33:15.000000', null, 'admin@admin.fr', 'admin', 'admin');
INSERT INTO user (user_id, created_at, updated_at, user_email, user_password, user_pseudo)
VALUES (3, '2020-02-14 05:15:15.000000', null, 'swann@mail.fr', 'swann', 'gerard mentor');

INSERT INTO board (board_id, created_at, updated_at, project_name, owner_user_id)
VALUES (1, '2021-03-14 16:39:41.000000', null, 'vecolo', 1);
INSERT INTO board (board_id, created_at, updated_at, project_name, owner_user_id)
VALUES (2, '2021-03-14 16:39:43.000000', null, 'vekanban', 1);
INSERT INTO board (board_id, created_at, updated_at, project_name, owner_user_id)
VALUES (3, '2021-03-14 16:39:44.000000', null, 'reboot', 2);

COMMIT;