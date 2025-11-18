create table patient (
    id bigserial primary key,
    name varchar(255),
    city varchar(255),
    country varchar(255),
    phone varchar(50),
    email varchar(255),
    vk_id varchar(255),
    tg_id varchar(255),
    username varchar(255),
    insta_id varchar(255),
    type_of_payment varchar(100),
    operation_date date
);

create table campaign (
    id bigserial primary key,
    name varchar(255),
    platform varchar(100),
    type varchar(100),
    start_date date,
    end_date date,
    budget_per_day double precision,
    total_spent double precision,
    ctr double precision,
    total_leads integer
);

create table bot_user (
    telegram_id bigint primary key,
    role varchar(100),
    username varchar(255)
);

create table post (
    id bigserial primary key,
    title varchar(255),
    platform varchar(100),
    post_date date,
    reach integer,
    likes integer,
    comments integer,
    shares integer,
    visitors integer,
    views integer,
    is_weak boolean,
    stories_likes integer,
    stories_shares integer,
    stories_messages integer,
    clips_shares integer,
    clips_likes integer,
    clips_comments integer
);

create table application (
    id bigserial primary key,
    patient_id bigint references patient(id),
    campaign_id bigint references campaign(id),
    text text,
    status varchar(100),
    payment_status varchar(100),
    source varchar(100),
    channel varchar(100),
    target_or_spam boolean,
    created_at date,
    ad_type varchar(100),
    creative varchar(255),
    answered_by_human boolean,
    answered_by_ai boolean
);

create table application_photo_file_ids (
    application_id bigint not null references application(id),
    photo_file_ids varchar(255)
);