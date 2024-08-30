insert into location (id, city, address) values ('00000000-0000-0000-0000-000000000000', 'City', 'Address');

insert into office (id, location_id, start_time, end_time) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '08:00:00', '18:00:00');

insert into workplace (id, office_id, floor, type, computer_present, available) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 1, 'DESK', true, true);

insert into reservation (id, user_id, workplace_id, start_date_time, end_date_time) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '2024-07-18T12:00:00', '2024-07-18T18:00:00');
insert into reservation (id, user_id, workplace_id, start_date_time, end_date_time) values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '2024-07-18T11:00:00', '2024-07-18T18:00:00');
insert into reservation (id, user_id, workplace_id, start_date_time, end_date_time) values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '2024-07-18T12:00:00', '2024-07-18T18:30:00');