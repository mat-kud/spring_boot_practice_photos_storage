CREATE TABLE IF NOT EXISTS PHOTOS (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    file_name varchar(255),
    content_type varchar(255),
    category varchar(255),
    data blob
);