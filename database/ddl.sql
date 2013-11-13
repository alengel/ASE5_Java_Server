create table t5_users(
        
        id int(11) auto_increment,
        email varchar(200) unique,
        passwd varchar(200),
        first_name varchar(200),
        last_name varchar(200),
        login_key varchar(200) unique,
        login_timestamp varchar(20),
        last_login varchar(200),
        last_request varchar(20),
        logout_session_time int(10),
        geo_push_interval int(10),
        min_distance int(10),
        max_login_interval int(10),

        dated varchar(20),
        primary key(id)

)engine=innodb;

create table t5_locations(
        
        id int(11) auto_increment,
        
        foursquare_venue_id varchar(200) unique,
        
        dated varchar(20),
        primary key(id)

)engine=innodb;


create table t5_reviews(
        
        users_id int(11),
        locations_id int(11),
        
        rating int(2),
        review_title text,
        review_description text,
        review_picture text,
        
        dated varchar(20),
        primary key(users_id, locations_id),
        foreign key (users_id) references t5_users(id) on update cascade on delete cascade,
        foreign key (locations_id) references t5_locations(id) on update cascade on delete cascade

)engine=innodb;


create table t5_checkins(
        
        users_id int(11),
        locations_id int(11),
        checkin_timestamp varchar(20),
        
        primary key(users_id, locations_id),
        foreign key (users_id) references t5_users(id) on update cascade on delete cascade,
        foreign key (locations_id) references t5_locations(id) on update cascade on delete cascade

)engine=innodb;
