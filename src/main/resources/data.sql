delete from users;
delete from tasks;
delete from projects;
delete from units;
insert into users (username, password, first_name, last_name, email, birth_date, employment_date, roles)
values ('andrik', '$2a$12$R7FB4tgyqgyJyKGd/jsvfuf83ijkxwjEcyAtYraJL6OAqemp/ar6W', 'Andrii', 'Kostetskyi',
        'kostecki.andrij@gmail.com', '1991-09-18', '2025-06-16', 'ADMIN');



