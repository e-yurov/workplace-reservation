create schema workplace_reservation;

create table workplace_reservation.location(
    id uuid primary key default gen_random_uuid(),
    city varchar(50) not null ,
    address varchar(150) not null
);

create table workplace_reservation.office(
    id uuid primary key default gen_random_uuid(),
    location_id uuid references workplace_reservation.location(id) not null,
    start_time time not null,
    end_time time not null
);

create table workplace_reservation.user(
    id uuid primary key default gen_random_uuid(),
    name varchar(70),
    email varchar(70),
    role varchar(30)
);

create table workplace_reservation.workplace(
    id uuid primary key default gen_random_uuid(),
    office_id uuid references workplace_reservation.office(id),
    floor int,
    type varchar(20) not null,
    computer_present boolean not null,
    available boolean not null
);

create table workplace_reservation.reservation(
    id uuid primary key default gen_random_uuid(),
    user_id uuid references workplace_reservation.user(id) not null,
    workplace_id uuid references workplace_reservation.workplace(id) not null,
    start_date_time timestamp not null,
    end_date_time timestamp not null
);