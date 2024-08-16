insert into location (id, city, address) values ('00000000-0000-0000-0000-000000000000', 'City', 'Address');

insert into office (id, location_id, start_time, end_time) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', '08:00:00', '18:00:00');

insert into workplace (id, office_id, floor, type, computer_present, available) values ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 1, 'DESK', true, true);
insert into workplace (id, office_id, floor, type, computer_present, available) values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', 2, 'DESK', false, true);
insert into workplace (id, office_id, floor, type, computer_present, available) values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 3, 'DESK', true, false);
insert into workplace (id, office_id, floor, type, computer_present, available) values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000000', 3, 'ROOM', false, false);