/* I. CREATE TABLES */

-- Tài xế
create table driver (
	id number primary key,
	name nvarchar2(30) not null,
    address nvarchar2(100) not null,
    phone nvarchar2(10) not null,
    level_driver nvarchar2(30) not null
);

-- Tuyến bus
create table bus_driver (
	id number primary key,
	distance number not null,
    stop_station_number number not null
);

-- Danh sách phân công
create table driver (
	driver_id number not null,
	busline_id number not null,
    round number not null
);