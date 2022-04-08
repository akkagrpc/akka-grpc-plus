CREATE TABLE IF NOT EXISTS public.movie (
    movieid VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	rating VARCHAR(255) NOT NULL,
	gnere VARCHAR(255) NOT NULL,
	createdby VARCHAR(255) NOT NULL,
	lastmodifiedby VARCHAR(255) NULL,
	creationdatetime VARCHAR(255) NOT NULL,
	lastmodifieddatetime VARCHAR(255) NULL,
	smstatus VARCHAR(255) NOT NULL,
    PRIMARY KEY (movieid));
