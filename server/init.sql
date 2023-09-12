CREATE TABLE Users (
	id INT GENERATED ALWAYS AS IDENTITY,
	email VARCHAR(255) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	type CHAR(15) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE Venues (
	id INT GENERATED ALWAYS AS IDENTITY,
	name VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
	capacity INT NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE Events (
	id INT GENERATED ALWAYS AS IDENTITY,
	title VARCHAR(255) NOT NULL,
	artist VARCHAR(255) NOT NULL,
	venue_id INT NOT NULL,
	start_time TIMESTAMP WITH TIME ZONE NOT NULL,
	end_time TIMESTAMP WITH TIME ZONE NOT NULL,
	status CHAR(15),
	PRIMARY KEY (id),
	FOREIGN KEY (venue_id)
		REFERENCES Venues(id)
);

CREATE TABLE Planner_Events (
	event_id INT,
	planner_id INT,
	PRIMARY KEY (event_id, planner_id),
	FOREIGN KEY (event_id)
		REFERENCES Events(id),
	FOREIGN KEY (planner_id)
		REFERENCES Users(id)
);

CREATE TABLE Sections (
	id INT GENERATED ALWAYS AS IDENTITY,
	event_id INT NOT NULL,
	name VARCHAR(255) NOT NULL,
	unit_price NUMERIC(10, 2) NOT NULL,
	currency CHAR(15) NOT NULL,
	capacity INT NOT NULL,
	remaining_tickets INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (event_id)
		REFERENCES Events(id)
);

CREATE TABLE Orders (
	id INT GENERATED ALWAYS AS IDENTITY,
	customer_id INT NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL,
	status CHAR(15) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (customer_id)
		REFERENCES Users(id)
);

CREATE TABLE Order_Sections (
	order_id INT NOT NULL,
	section_id INT NOT NULL,
	quantity INT NOT NULL,
	unit_price NUMERIC(10, 2) NOT NULL,
	currency CHAR(15) NOT NULL,
	PRIMARY KEY (order_id, section_id),
	FOREIGN KEY (order_id)
		REFERENCES Orders(id),
	FOREIGN KEY (section_id)
		REFERENCES Sections(id)
);
